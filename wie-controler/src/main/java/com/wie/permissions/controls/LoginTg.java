package com.wie.permissions.controls;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.sf.json.JSONArray;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.subject.Subject;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.wie.Realm.CaptchaUsernamePasswordToken;
import com.wie.Realm.IncorrectCaptchaException;
import com.wie.common.tools.json.Json;
import com.wie.erp.biz.IStoreService;
import com.wie.erp.model.Store;
import com.wie.framework.controls.struts2.BaseTg;
import com.wie.permissions.biz.IUserService;
import com.wie.permissions.common.HasParent;
import com.wie.permissions.model.CloneResources;
import com.wie.permissions.model.Resources;
import com.wie.permissions.model.Users;
import com.wie.permissions.tree.Utils;
import com.wie.shiro.Constants;
import com.wie.tree.Node;
import com.wie.tree.UncodeException;
import com.wie.tree.UserDataUncoder;
import com.wie.tree.support.AbstractWebTreeModelCreator;
import com.wie.tree.support.WebTreeNode;

/** 
  * @ClassName: LoginTg 
  * @Description: 用户登录
  *  
  */
@Scope("prototype")
@Controller(value="loginTgControl")
public class LoginTg extends BaseTg{
	private static final Logger logger = Logger.getLogger(LoginTg.class);
	private String agentname;
	private String username;
	private String password;
	private String captcha;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	private String rememberMe;
		public String getRememberMe() {
		return rememberMe;
	}
	public void setRememberMe(String rememberMe) {
		this.rememberMe = rememberMe;
	}

	public String test(){
        String path1=request.getRealPath("/");
        String path2=ServletActionContext.getServletContext().getRealPath("/");
        request.setAttribute("path1",path1);
        request.setAttribute("path2",path2);
        System.out.print("path1:"+path1);
        System.out.print("path2:"+path2);
        return "test";
	}
	/**
	 * @return the agentname
	 */
	public String getAgentname() {
		return agentname;
	}
	/**
	 * @param agentname the agentname to set
	 */
	public void setAgentname(String agentname) {
		this.agentname = agentname;
	}
	
	public String getCaptcha() {
		return captcha;
	}
	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}
	@Autowired
	private IUserService userService;
	@Autowired
	private IStoreService storeService;
	
	/** 
	  * @Title: login 
	  * @Description: 用户登录方法
	  * @param @return
	  * @return String
	  * @throws 
	  */
	@SuppressWarnings("unchecked")
	public String login1() {
		UsernamePasswordToken token=null;
		List<Users> userList = null;
		if(agentname == null||"".equals(agentname)){
			token = new UsernamePasswordToken(username, password, Boolean.valueOf(rememberMe));
			userList = userService.findList("from Users where logonid ='"+username+"'");
			for (int i = 0; i < userList.size(); i++) {
				//System.out.println(userList.get(i).getId());
				request.getSession().setAttribute("userId", userList.get(i).getId());
			}
		}else{
			userList = userService.findList("from Users where logonid ='"+agentname+"'");
			for (int i = 0; i < userList.size(); i++) {
				System.out.println(userList.get(i).getLogonid());
				token = new UsernamePasswordToken(userList.get(i).getLogonid(), userList.get(i).getPassword(), Boolean.valueOf(rememberMe));
			}
		}
		Subject user = SecurityUtils.getSubject();
		try {
			user.login(token);
			HasParent.isHasParent();
			List<Resources> list = (List<Resources>) SecurityUtils.getSubject().getSession().getAttribute("resourceList");
			Collections.sort(list, new Comparator<Resources>() {
				public int compare(Resources arg0, Resources arg1) {
					return arg0.getOrderid().compareTo(arg1.getOrderid());
				}
			});
			for (Resources u : list) {
				System.out.println(u.getOrderid());
			}
			HasParent.returnCurrentObj(null,request);
//			List<Resources> xxx = HasParent.returnCurrentObj(null,request);
//			System.out.println(request.getAttribute("jsonOne"));
//			System.out.println(request.getAttribute("jsonTwo"));
			
			
			List<CloneResources> resource = new ArrayList<CloneResources>();
			
			for(int i=0;i<list.size();i++){
				Resources temp = (Resources)list.get(i);
				CloneResources cloe = new CloneResources();
				cloe.setId(temp.getId());
				cloe.setParentId(null == temp.getParent() ? null:temp.getParent().getId()+"");
				cloe.setName(temp.getName());
				cloe.setIcon(temp.getIcon());
				cloe.setLink(temp.getLink());
				cloe.setOrderid(temp.getOrderid());
				resource.add(cloe);
			}
			
			// 业务数据解码器，从业务数据中分解出id和parentid
			UserDataUncoder orgUncoder = new UserDataUncoder() {
				public Object getID(Object pUserData) throws UncodeException {
					CloneResources org = (CloneResources) pUserData;
					return org.getId();
				}

				public Object getParentID(Object pUserData) throws UncodeException {
					CloneResources org = (CloneResources) pUserData;
					return null == org.getParentId()?null:org.getParentId();
				}
			};

			// Tree模型构造器，用于生成树模型
			AbstractWebTreeModelCreator treeModelCreator = new AbstractWebTreeModelCreator() {
				// 该方法负责将业务数据映射到树型节点
				protected Node createNode(Object pUserData, UserDataUncoder pUncoder) {
					CloneResources org = (CloneResources) pUserData;
					WebTreeNode result = new WebTreeNode(org.getName(), "node"+org.getId());
					result.setIcon(org.getIcon());
					result.setAction(org.getLink());
					return result;
				}
			};
			JSONArray jsonArray = JSONArray.fromObject( resource );  
	        System.out.println( jsonArray );  
			String temp = Utils.getTree(orgUncoder, treeModelCreator, resource, request);
			String storeId=(String) request.getSession().getAttribute("storeId");
			Store store=storeService.findById(storeId);
			request.setAttribute("store", store);
			request.setAttribute("treescript", temp);
			request.setAttribute("jsontree", jsonArray);
			//JSONArray 
			
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("验证出错！");
			return "error";
		}
	}
	@SuppressWarnings("unchecked")
	public String login() throws Exception{
		CaptchaUsernamePasswordToken token=null;
		Json json=new Json();
		String returnStr=null;
		List<Users> userList = null;
		if(agentname == null||"".equals(agentname)){
			  token = new CaptchaUsernamePasswordToken(username, password, Boolean.valueOf(rememberMe),captcha);
		      json.setTitle("登录提示");
		}else{
			userList = userService.findList("from Users where logonid ='"+agentname+"'");
			for (int i = 0; i < userList.size(); i++) {
//				System.out.println(userList.get(i).getLogonid());
				token = new CaptchaUsernamePasswordToken(userList.get(i).getLogonid(), userList.get(i).getPassword(), Boolean.valueOf(rememberMe),captcha);
		        json.setTitle("登录提示");
			}
		}
		Subject user = SecurityUtils.getSubject();
		try {
			user.login(token);
			HasParent.isHasParent();
			List<Resources> list = (List<Resources>) SecurityUtils.getSubject().getSession().getAttribute("resourceList");
			//排序
			Collections.sort(list, new Comparator<Resources>() {
				public int compare(Resources arg0, Resources arg1) {
					return arg0.getOrderid().compareTo(arg1.getOrderid());
				}
			});
			HasParent.returnCurrentObj(null,request);
//			List<Resources> xxx = HasParent.returnCurrentObj(null,request);
//			System.out.println(request.getAttribute("jsonOne"));
//			System.out.println(request.getAttribute("jsonTwo"));
			
			
			List<CloneResources> resource = new ArrayList<CloneResources>();
			
			for(int i=0;i<list.size();i++){
				Resources temp = (Resources)list.get(i);
				CloneResources cloe = new CloneResources();
				cloe.setId(temp.getId());
				cloe.setParentId(null == temp.getParent() ? null:temp.getParent().getId()+"");
				cloe.setName(temp.getName());
				cloe.setIcon(temp.getIcon());
				cloe.setLink(temp.getLink());
				resource.add(cloe);
			}
			
			// 业务数据解码器，从业务数据中分解出id和parentid
			UserDataUncoder orgUncoder = new UserDataUncoder() {
				
				public Object getID(Object pUserData) throws UncodeException {
					CloneResources org = (CloneResources) pUserData;
					return org.getId();
				}

				public Object getParentID(Object pUserData) throws UncodeException {
					CloneResources org = (CloneResources) pUserData;
					return null == org.getParentId()?null:org.getParentId();
				}
			};

			// Tree模型构造器，用于生成树模型
			AbstractWebTreeModelCreator treeModelCreator = new AbstractWebTreeModelCreator() {
				// 该方法负责将业务数据映射到树型节点
				protected Node createNode(Object pUserData, UserDataUncoder pUncoder) {
					CloneResources org = (CloneResources) pUserData;
					WebTreeNode result = new WebTreeNode(org.getName(), "node"+org.getId());
					result.setIcon(org.getIcon());
					result.setAction(org.getLink());
					return result;
				}
			};
			
			String temp = Utils.getTree(orgUncoder, treeModelCreator, resource, request);
			//String storeId=(String) request.getSession().getAttribute("storeId");
			//Store store=storeService.findById(storeId);
			//request.setAttribute("store", store);
			request.setAttribute("treescript", temp);
			returnStr="success";
//			return "success";
			
		}
		catch (UnknownSessionException use) {
            user = new Subject.Builder().buildSubject();
            user.login(token);
            logger.error(Constants.UNKNOWN_SESSION_EXCEPTION);
            json.setMessage(Constants.UNKNOWN_SESSION_EXCEPTION);
            returnStr= "error";
        }
        catch(UnknownAccountException ex){
			logger.error(Constants.UNKNOWN_ACCOUNT_EXCEPTION);
			json.setMessage(Constants.UNKNOWN_ACCOUNT_EXCEPTION);
			returnStr= "error";
		}
        catch (IncorrectCredentialsException ice) {
            json.setMessage(Constants.INCORRECT_CREDENTIALS_EXCEPTION);
            returnStr= "error";
        } 
        catch (LockedAccountException lae) {
            json.setMessage(Constants.LOCKED_ACCOUNT_EXCEPTION);
            returnStr= "error";
        }catch (IncorrectCaptchaException e) {
        	 json.setMessage(Constants.INCORRECT_CAPTCHA_EXCEPTION);
        	 returnStr= "error";
		}
        catch (AuthenticationException ae) {
            json.setMessage(Constants.AUTHENTICATION_EXCEPTION);
            returnStr= "error";
        } 
        catch(Exception e){
            json.setMessage(Constants.UNKNOWN_EXCEPTION);
            returnStr= "error";
        }
		request.setAttribute("message", json.getMessage());
        return returnStr;
	}
	public void getcaptcha()
	{
		String captcha=request.getParameter("captcha");
		Json json=new Json();
		String captchasev = (String) ServletActionContext.getRequest().getSession().getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
		if(captcha.equals(captchasev))
			json.setStatus(true);
		else{json.setMessage(Constants.INCORRECT_CAPTCHA_EXCEPTION);}
		OutputJson(json,Constants.TEXT_TYPE_PLAIN);
	}
	public String loginca(){
		return "loginca";
	}
	public String load1() throws Exception
	{	
		Subject subject=SecurityUtils.getSubject();
		CaptchaUsernamePasswordToken token=new CaptchaUsernamePasswordToken();
        token.setUsername(username);
        token.setPassword(password.toCharArray());
        token.setCaptcha(captcha);
        token.setRememberMe(true);
        Json json=new Json();
        json.setTitle("登录提示");
        try{
            subject.login(token);
            System.out.println("sessionTimeout===>"+subject.getSession().getTimeout());
            json.setStatus(true);	
        }
        catch (UnknownSessionException use) {
            subject = new Subject.Builder().buildSubject();
            subject.login(token);
            logger.error(Constants.UNKNOWN_SESSION_EXCEPTION);
            json.setMessage(Constants.UNKNOWN_SESSION_EXCEPTION);
        }
        catch(UnknownAccountException ex){
			logger.error(Constants.UNKNOWN_ACCOUNT_EXCEPTION);
			json.setMessage(Constants.UNKNOWN_ACCOUNT_EXCEPTION);
		}
        catch (IncorrectCredentialsException ice) {
            json.setMessage(Constants.INCORRECT_CREDENTIALS_EXCEPTION);
        } 
        catch (LockedAccountException lae) {
            json.setMessage(Constants.LOCKED_ACCOUNT_EXCEPTION);
        }catch (IncorrectCaptchaException e) {
        	 json.setMessage(Constants.INCORRECT_CAPTCHA_EXCEPTION);
		}
        catch (AuthenticationException ae) {
            json.setMessage(Constants.AUTHENTICATION_EXCEPTION);
        } 
        catch(Exception e){
            json.setMessage(Constants.UNKNOWN_EXCEPTION);
        }
        OutputJson(json,Constants.TEXT_TYPE_PLAIN);
        //token.clear();
		return null;
	}
	public void isexist(){
		Json json=new Json();
		String userId=(String)request.getSession().getAttribute("userId");
		if(userId==null)
			json.setStatus(false);
		else{
			json.setStatus(true);
		}
		OutputJson(json,Constants.TEXT_TYPE_PLAIN);
	}
	public String logout(){
			SecurityUtils.getSubject().logout();
			return "error";
	}
	}
