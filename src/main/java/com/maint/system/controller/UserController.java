package com.maint.system.controller;

import com.github.pagehelper.PageInfo;
import com.maint.common.annotation.OperationLog;
import com.maint.common.util.PageResultBean;
import com.maint.common.util.ResultBean;
import com.maint.common.validate.groups.Create;
import com.maint.system.model.User;
import com.maint.system.model.vo.PwdVO;
import com.maint.system.service.RoleService;
import com.maint.system.service.UserService;

import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

	@Resource
	private UserService userService;
	
	@Resource
	private RoleService roleService;
	
	@GetMapping("/index")
	public String index() {
		return "user/user-list";
	}
	
	@OperationLog("获取用户列表")
	@GetMapping("/list")
	@ResponseBody
	public PageResultBean<User> getList(@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "limit", defaultValue = "10") int limit, User userQuery) {
		List<User> users = userService.selectAllWithDept(page, limit, userQuery);
		PageInfo<User> userPageInfo = new PageInfo<>(users);
		return new PageResultBean<>(userPageInfo.getTotal(), userPageInfo.getList());
	}
	
	@GetMapping
	public String add(Model model) {
		model.addAttribute("roles", roleService.selectAllByRole());
		return "user/user-add";
	}
	
	@GetMapping("/{userId}")
	public String update(@PathVariable("userId") Integer userId, Model model) {
		model.addAttribute("roleIds", userService.selectRoleIdsById(userId));
		model.addAttribute("user", userService.selectOne(userId));
		model.addAttribute("roles", roleService.selectAllByRole());
		return "user/user-add";
	}
	
	@OperationLog("编辑用户")
	@PutMapping
	@ResponseBody
	public ResultBean update(@Valid User user, @RequestParam(value = "role[]", required = false) Integer[] roleIds) {
		userService.update(user, roleIds);
		return ResultBean.success();
	}
	
	@OperationLog("新增用户")
	@PostMapping
	@ResponseBody
	public ResultBean add(@Validated(Create.class) User user,
			@RequestParam(value = "role[]", required = false) Integer[] roleIds) {
		return ResultBean.success(userService.add(user, roleIds));
	}
	
	@OperationLog("禁用账号")
	@PostMapping("/{userId:\\d+}/disable")
	@ResponseBody
	public ResultBean disable(@PathVariable("userId") Integer userId) {
		return ResultBean.success(userService.disableUserByID(userId));
	}
	
	@OperationLog("激活账号")
	@PostMapping("/{userId}/enable")
	@ResponseBody
	public ResultBean enable(@PathVariable("userId") Integer userId) {
		return ResultBean.success(userService.enableUserByID(userId));
	}
	
	@OperationLog("删除账号")
	@DeleteMapping("/{userId}")
	@ResponseBody
	public ResultBean delete(@PathVariable("userId") Integer userId) {
		userService.delete(userId);
		return ResultBean.success();
	}
	
	@GetMapping("/{userId}/reset")
	public String resetPassword(@PathVariable("userId") Integer userId, Model model) {
		model.addAttribute("userId", userId);
		return "user/user-reset-pwd";
	}
	
	/**
	 * 重置密码
	 * @return
	 */
	@GetMapping("/reset")
	public String resetPassword2() {
		return "user/user-reset-pwd";
	}
	
	@OperationLog("重置密码")
	@PostMapping("/{userId}/reset")
	@ResponseBody
	public ResultBean resetPassword(@PathVariable("userId") Integer userId, String password) {
		userService.updatePasswordByUserId(userId, password);
		return ResultBean.success();
	}
	
	@GetMapping("/userInfo")
	public String userInfo(Model model) {
		//当前用户
    	User currentUser = (User) SecurityUtils.getSubject().getPrincipal();
    	model.addAttribute("user", currentUser);
		return "userInfo";
	}
	
	@GetMapping("/editPwd")
	public String editPwd() {
		return "editPwd";
	}
	
	@PutMapping("/editPwd")
	@ResponseBody
	public ResultBean editPassword(PwdVO pwdVO) {
		return ResultBean.success(userService.editPassword(pwdVO));
	}
}