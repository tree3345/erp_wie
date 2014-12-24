package com.wie.Realm;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;

import com.wie.permissions.biz.IRoleAuthService;
import com.wie.permissions.biz.IUserService;
import com.wie.permissions.model.Groups;
import com.wie.permissions.model.Resources;
import com.wie.permissions.model.Role;
import com.wie.permissions.model.RoleAuth;
import com.wie.permissions.model.Users;




/** 
  * @ClassName: TgPermissionsRealm 
  * @Description: 实际执行认证的类
  *  
  */
public class TgPermissionsRealm extends AuthorizingRealm{
	@Resource(name="roleAuthService")
	private IRoleAuthService roleAuthService;
	Users user;
	Set<Role> userRoles;
	Set<Resources> resources;
	@Autowired
	private IUserService userService;
	public TgPermissionsRealm(){
		setName("TgPermissionsRealm");
	}

	/* (non-Javadoc)
	 * <p>Title: doGetAuthorizationInfo</p> 
	 * <p>Description: 授权</p> 
	 * @param principals
	 * @return 
	 * @see org.apache.shiro.realm.AuthorizingRealm#doGetAuthorizationInfo(org.apache.shiro.subject.PrincipalCollection) 
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		Set<Groups> userGroups = user.getGroups();
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		if(user!=null){
			for(Groups group: userGroups){
				userRoles = group.getRoles();
				for(Role role : userRoles){
					info.addRole(role.getName());
					resources = role.getResources();
					for(Resources resource : resources){
						RoleAuth roleAuth = this.roleAuthService.find("from RoleAuth where roleId ='"+role.getId()+"' and resourceId ='"+resource.getId()+"'");
						if(roleAuth!=null && roleAuth.getActions()!=null &&!"".equals(roleAuth.getActions())){
							String[] actionString = roleAuth.getActions().split(",");
							for(String action : actionString){
								//shiro权限字符串为：“当前资源英文名称:操作名英文名称”
								info.addStringPermission(action);
							}
						}
						
						//info.addStringPermission(roleAuth.getActions());
					}
				}
			}
			return info;
		}
		return null;
	}


	/* (non-Javadoc)
	 * <p>Title: doGetAuthenticationInfo</p> 
	 * <p>Description: 认证</p> 
	 * @param authtoken
	 * @return
	 * @throws AuthenticationException 
	 * @see org.apache.shiro.realm.AuthenticatingRealm#doGetAuthenticationInfo(org.apache.shiro.authc.AuthenticationToken) 
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken authtoken) throws AuthenticationException {
		List<Resources> resourceList = new ArrayList<Resources>();
		CaptchaUsernamePasswordToken token = (CaptchaUsernamePasswordToken) authtoken;
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		String userName = token.getUsername();
		if(userName != null && !"".equals(userName) && doCaptchaValidate(token)){
			user = this.userService.login(token.getUsername(), String.valueOf(token.getPassword()));
            
			if( user != null ){
				SecurityUtils.getSubject().getSession().setAttribute("userId",user.getId());
				//SecurityUtils.getSubject().getSession().setAttribute("storeId",user.getStoreId());
				Set<Groups> userGroups = user.getGroups();
				//SecurityUtils.getSubject().getSession().setAttribute("userGroup", userGroups);
				for(Groups group: userGroups){
					//System.out.println("组名："+group.getName()+"\tid:"+group.getId());
					userRoles = group.getRoles();
					//SecurityUtils.getSubject().getSession().setAttribute("userRole", userRoles);
					for(Role role : userRoles){
						//System.out.println("角色名："+role.getName()+"\tId"+role.getId());
						info.addRole(role.getName());
						resources = role.getResources();
						//SecurityUtils.getSubject().getSession().setAttribute("userResource", resources);
						for(Resources resource : resources){
							//System.out.println("资源:"+resource.getName()+"\tid"+resource.getId());
							resourceList.add(resource);
							//RoleAuth roleAuth = this.roleAuthService.find("from RoleAuth where roleId ="+role.getId()+" and resourceId ="+resource.getId());
							//info.addStringPermission(roleAuth.getActions());
						}
					}
				}

				SecurityUtils.getSubject().getSession().setAttribute("resourceList", resourceList);
//				return new SimpleAuthenticationInfo( user.getLogonid(),user.getPassword(), getName());
                SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                        user.getLogonid(), //用户名
                        user.getPassword(), //密码
                       // ByteSource.Util.bytes(user.getCredentialsSalt()),//salt=username+salt
                        getName()  //realm name
                );
                return authenticationInfo;
			}
		}
		return null;
	}
	//验证码校验
		protected boolean doCaptchaValidate(CaptchaUsernamePasswordToken token)
		{
			String captcha = (String) ServletActionContext.getRequest().getSession().getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
			if (captcha != null &&!captcha.equalsIgnoreCase(token.getCaptcha()))
			{
				throw new IncorrectCaptchaException("验证码错误！");
			}
			return true;
		}
	public IUserService getUserService() {
		return userService;
	}
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}
	public IRoleAuthService getRoleAuthService() {
		return roleAuthService;
	}
	public void setRoleAuthService(IRoleAuthService roleAuthService) {
		this.roleAuthService = roleAuthService;
	}

}
