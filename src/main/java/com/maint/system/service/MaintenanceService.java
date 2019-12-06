package com.maint.system.service;

import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.maint.common.util.StringUtil;
import com.maint.system.enums.MaintenanceOrderStatusEnum;
import com.maint.system.mapper.MaintenanceOrderMapper;
import com.maint.system.mapper.MaintenanceTraceMapper;
import com.maint.system.model.MaintenanceOrder;
import com.maint.system.model.MaintenanceTrace;
import com.maint.system.model.User;

import java.util.List;

import javax.annotation.Resource;

@Service
public class MaintenanceService {

	@Resource
	private MaintenanceOrderMapper maintenanceMapper;
	
	@Resource
	private MaintenanceTraceMapper traceMapper;
	
	public List<MaintenanceOrder> selectAllWithQuery(int page, int rows, MaintenanceOrder maintenanceQuery) {
		PageHelper.startPage(page, rows);
		List<MaintenanceOrder> maintenances = maintenanceMapper.selectAllWithQuery(maintenanceQuery);
		for (MaintenanceOrder maintenance : maintenances) {
			maintenance.setStateDesc(MaintenanceOrderStatusEnum.getvalueOf(maintenance.getState()).getTxt());
		}
		return maintenances;
	}
	
	public MaintenanceOrder selectByMaintenanceId(String maintenanceId) {
		return maintenanceMapper.selectByPrimaryKey(maintenanceId);
	}
	
	@Transactional
	public boolean add(MaintenanceOrder maintenance) {
		String maintenanceId = generateCode(8, 0, "BY");
		//当前用户
		User currentUser = (User) SecurityUtils.getSubject().getPrincipal();
		MaintenanceTrace trace = new MaintenanceTrace();
		
		trace.setMaintenanceTraceId(generateCode(10, 1, ""));
		trace.setMaintenanceOrderId(maintenanceId);
		trace.setUserId(currentUser.getUserId());
		trace.setOrderStatus(MaintenanceOrderStatusEnum.BYDSC.getValue());
		
		traceMapper.insert(trace);
		
		maintenance.setState(MaintenanceOrderStatusEnum.BYDSC.getValue());
		maintenance.setMaintenanceOrderId(maintenanceId);
		maintenance.setUserId(currentUser.getUserId());
		
		return maintenanceMapper.insert(maintenance) == 1 ? true : false;
	}
	
	public List<MaintenanceTrace> getTracesByMaintenanceId(String maintenanceId) {
		List<MaintenanceTrace> traces = traceMapper.selectByMaintenanceId(maintenanceId);
		for (MaintenanceTrace trace : traces) {
			trace.setStatusDesc(MaintenanceOrderStatusEnum.getvalueOf(trace.getOrderStatus()).getTxt());
		}
		return traces;
	}
	
	@Transactional
	public boolean appoint(MaintenanceOrder maintenance) {
		//当前用户
		User currentUser = (User) SecurityUtils.getSubject().getPrincipal();
		
		MaintenanceTrace trace = new MaintenanceTrace();
		
		trace.setMaintenanceTraceId(generateCode(10, 1, ""));
		trace.setMaintenanceOrderId(maintenance.getMaintenanceOrderId());
		trace.setOrderStatus(maintenance.getState());
		trace.setUserId(currentUser.getUserId());
		
		traceMapper.insert(trace);
		
		return maintenanceMapper.updateByPrimaryKeySelective(maintenance) == 1 ? true : false;
	}
	
	@Transactional
	public void delete(String maintenanceId) {
		// 删除订单追踪表
		traceMapper.deleteByMaintenanceId(maintenanceId);
		
		maintenanceMapper.deleteByPrimaryKey(maintenanceId);
	}
	
	private String generateCode(int length, int flag, String prefix) {
		String code = StringUtil.generateCode(length, prefix);
		//校验是否重复
		if (flag == 0) {	//保养单
			MaintenanceOrder maintenance = maintenanceMapper.selectByPrimaryKey(code);
			if (maintenance != null) {
				code = generateCode(length, flag, prefix);
			}
		} else {	//跟踪
			MaintenanceTrace trace = traceMapper.selectByPrimaryKey(code);
			if (trace != null) {
				code = generateCode(length, flag, prefix);
			}
		}
		
		return code;
	}
}
