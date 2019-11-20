package com.maint.system.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Dept implements Serializable {

	private static final long serialVersionUID = -194076170058276436L;
	
	/**
	 * 部门ID
	 */
	@JsonProperty("id")
	private Integer deptId;
	
	/**
	 * 部门名称
	 */
	@JsonProperty("name")
	private String deptName;
	
	/**
	 * 上级部门 ID
	 */
	private Integer parentId;
	
	/**
	 * 排序
	 */
	private Integer orderNum;
	
	/**
	 * 类型
	 */
	@JsonProperty("basicData")
	private Integer deptType;
	
	/**
	 * 负责人
	 */
	private Integer userId;
	
	/**
	 * 维修点地址
	 */
	private String deptAddr;
	
	/**
	 * 创建时间
	 */
	@JsonIgnore
	private Date createTime;
	
	/**
	 * 修改时间
	 */
	@JsonIgnore
	private Date modifyTime;
	
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private List<Dept> children;
	
	public Integer getDeptId() {
		return deptId;
	}
	
	public void setDeptId(Integer deptId) {
		this.deptId = deptId;
	}
	
	public String getDeptName() {
		return deptName;
	}
	
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	
	public Integer getParentId() {
		return parentId;
	}
	
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
	
	public Integer getOrderNum() {
		return orderNum;
	}
	
	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}
	
	public Integer getDeptType() {
		return deptType;
	}
	
	public void setDeptType(Integer deptType) {
		this.deptType = deptType;
	}
	
	public Integer getUserId() {
		return userId;
	}
	
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	public String getDeptAddr() {
		return deptAddr;
	}
	
	public void setDeptAddr(String deptAddr) {
		this.deptAddr = deptAddr;
	}
	
	public Date getCreateTime() {
		return createTime;
	}
	
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	public Date getModifyTime() {
		return modifyTime;
	}
	
	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
	
	public List<Dept> getChildren() {
		return children;
	}
	
	public void setChildren(List<Dept> children) {
		this.children = children;
	}
	
	@Override
	public String toString() {
		return "Dept{" + "deptId=" + deptId + ", deptName='" + deptName + '\'' + ", parentId=" + parentId
				+ ", orderNum=" + orderNum + ", deptType=" + deptType + ", userId=" + userId + ", deptAddr=" + deptAddr + ", createTime=" + createTime + ", modifyTime=" + modifyTime + ", children="
				+ children + '}';
	}
}