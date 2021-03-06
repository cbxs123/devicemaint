package com.maint.system.service;

import org.springframework.stereotype.Service;

import com.maint.system.mapper.DeptMapper;
import com.maint.system.mapper.UserMapper;
import com.maint.system.model.Dept;
import com.maint.system.model.PointDetail;
import com.maint.system.model.User;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.List;

@Service
public class DeptService {

	@Resource
	private DeptMapper deptMapper;
	
	@Resource
	private UserMapper userMapper;
	
	public Dept insert(Dept dept) {
		int maxOrderNum = deptMapper.selectMaxOrderNum();
		dept.setOrderNum(maxOrderNum + 1);
		deptMapper.insert(dept);
		
		if (dept.getDeptType() == 1 && dept.getUserId() != null) {
			bunding(dept.getDeptId(), dept.getUserId());
		}
		
		return dept;
	}
	
	public int deleteByPrimaryKey(Integer deptId) {
		return deptMapper.deleteByPrimaryKey(deptId);
	}
	
	public Dept updateByPrimaryKey(Dept dept) {
		if (dept.getDeptType() == 1 && dept.getUserId() != null) {	//负责人关联维修点
			Dept dept_old = deptMapper.selectByPrimaryKey(dept.getDeptId());	//更新前的维修点
			if (dept_old.getUserId() != null && dept_old.getUserId() != dept.getUserId()) {
				unbunding(dept_old.getUserId());
				bunding(dept.getDeptId(), dept.getUserId());
			} else if (dept_old.getUserId() == null) {
				bunding(dept.getDeptId(), dept.getUserId());
			}
		}
		
		deptMapper.updateByPrimaryKey(dept);
		return dept;
	}
	
	public Dept selectByPrimaryKey(Integer deptId) {
		return deptMapper.selectByPrimaryKey(deptId);
	}
	
	/**
	 * 维修点详情
	 * @param deptId
	 * @return
	 */
	public PointDetail selectDetailByDeptId(Integer deptId) {
		Dept dept = deptMapper.selectByPrimaryKey(deptId);
		Dept parentDept = deptMapper.selectByPrimaryKey(dept.getParentId());
		
		PointDetail pointDetail = new PointDetail();
		pointDetail.setDept(dept);
		pointDetail.setParentDept(parentDept.getDeptName());
		if (dept.getUserId() != null) {
			User user = userMapper.selectByPrimaryKey(dept.getUserId());
			pointDetail.setUser(user);
		}
		return pointDetail;
	}
	
	/**
	 * 删除当前区域及下级节点
	 */
	public void deleteCascadeByID(Integer deptId) {
		List<Integer> childIDList = deptMapper.selectChildrenIDByPrimaryKey(deptId);
		for (Integer childId : childIDList) {
			deleteCascadeByID(childId);
		}
		Dept dept = deptMapper.selectByPrimaryKey(deptId);
		if (dept.getUserId() != null) {
			unbunding(dept.getUserId());
		}
		deleteByPrimaryKey(deptId);
	}
	
//	/**
//	* 根据父 ID 查询部门
//	*/
//	public List<Dept> selectByParentId(Integer parentId) {
//		return deptMapper.selectByParentId(parentId);
//	}
	/**
	 * 根据父 ID 查询区域
	 */
	public List<Dept> selectAreaByParentId(Integer parentId) {
		return deptMapper.selectAreaByParentId(parentId);
	}
	/**
	 * 根据父 ID 查询维修点
	 */
	public List<Dept> selectPointByParentId(Integer parentId) {
		return deptMapper.selectPointByParentId(parentId);
	}
	
	/**
	 * 查找所有的区域及维修点的树形结构
	 */
	public List<Dept> selectAllDeptTree() {
		return deptMapper.selectAllTree();
	}
	
	/**
	 * 查找所有的区域的树形结构
	 */
	public List<Dept> selectAllAreaTree() {
		return deptMapper.selectAllArea();
	}
	
	/**
	 * 获取所有菜单并添加一个根节点 (树形结构)
	 */
	public List<Dept> selectAllDeptTreeAndRoot() {
		List<Dept> deptList = selectAllDeptTree();
		Dept root = new Dept();
		root.setDeptId(0);
		root.setDeptName("根区域");
		root.setDeptType(0);
		root.setUserId(null);
		root.setChildren(deptList);
		List<Dept> rootList = new ArrayList<>();
		rootList.add(root);
		return rootList;
	}
	
	/**
	 * 获取所有区域并添加一个根节点 (树形结构)
	 */
	public List<Dept> selectAllAreaTreeAndRoot() {
		List<Dept> deptList = selectAllAreaTree();
		Dept root = new Dept();
		root.setDeptId(0);
		root.setDeptName("根区域");
		root.setDeptType(0);
		root.setUserId(null);
		root.setChildren(deptList);
		List<Dept> rootList = new ArrayList<>();
		rootList.add(root);
		return rootList;
	}
	
	public void swapSort(Integer currentId, Integer swapId) {
		deptMapper.swapSort(currentId, swapId);
	}
	
	/**
	 * 区域绑定负责人
	 * @param deptId
	 */
	private void bunding(int deptId, int userId) {
		userMapper.bunding(deptId, userId);
	}
	
	/**
	 * 区域解绑负责人
	 * @param deptId
	 */
	private void unbunding(int userId) {
		userMapper.unbunding(userId);
	}
}
