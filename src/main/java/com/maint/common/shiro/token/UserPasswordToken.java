package com.maint.common.shiro.token;

import org.apache.shiro.authc.UsernamePasswordToken;

public class UserPasswordToken extends UsernamePasswordToken {

private static final long serialVersionUID = -1L;
	
	private String loginType;
 
	public UserPasswordToken(String username, char[] password,String loginType) {
		super(username, password);
		this.loginType = loginType;
	}
	
	public String getLoginType() {
		return loginType;
	}
 
	public void setLoginType(String loginType) {
		this.loginType = loginType;
	}
}
