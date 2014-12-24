package com.wie.Realm;

import org.apache.shiro.authc.UsernamePasswordToken;

public class CaptchaUsernamePasswordToken extends UsernamePasswordToken
{
	private static final long serialVersionUID = -3217596468830869181L;
	private String captcha;

	public String getCaptcha()
	{
		return captcha;
	}

	public void setCaptcha(String captcha )
	{
		this.captcha = captcha;
	}

	public CaptchaUsernamePasswordToken(String username, String password, boolean rememberMe,String captcha)
	{
		super(username, password, rememberMe);
		this.captcha = captcha; 
	}

	public CaptchaUsernamePasswordToken()
	{
		super();
	}
}
