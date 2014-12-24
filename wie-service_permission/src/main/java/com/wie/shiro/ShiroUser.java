package com.wie.shiro;

import java.io.Serializable;

public  class ShiroUser implements Serializable
{
	private static final long serialVersionUID = -1748602382963711884L;
	private String userId;
	private String account;
	
	public ShiroUser(String userId, String account)
	{
		super();
		this.userId = userId;
		this.account = account;
	}

	/**
	 * 本函数输出将作为默认的<shiro:principal/>输出.
	 */
	public String toString()
	{
		return account;
	}

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId )
	{
		this.userId = userId;
	}

	public String getAccount()
	{
		return account;
	}

	public void setAccount(String account )
	{
		this.account = account;
	}
	    
}
