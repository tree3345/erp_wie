/**   
  * @Title: RadioXLoadTreeBuilder.java 
  * @Package com.tgyt.tree.xtree 
  * @Description: 
  * @author zhangfeng 13940488705@163.com 
  * @date 2011-8-9 下午05:11:10 
  * @version V1.0   
  */

package com.wie.tree.xtree;

import com.wie.templateEngine.Context;
import com.wie.templateEngine.support.DefaultContext;
import com.wie.templateEngine.support.StrTemplateUtil;
import com.wie.tree.BuildTreeException;
import com.wie.tree.Node;
import com.wie.tree.support.WebTreeDynamicNode;
import com.wie.tree.support.WebTreeNode;

/** 
 * @ClassName: RadioXLoadTreeBuilder 
 * @Description: 单选树
 *  
 */
public class RadioXLoadTreeBuilder extends XLoadTreeBuilder{
	
	/** 
	 * 负责导入Tree所需要的js,css
	 */
	public void buildTreeStart() throws BuildTreeException {
		StringBuffer resouces = new StringBuffer();
		if ( this.importCss ){
		  resouces.append("<link type='text/css' rel='stylesheet' href='${xtreeStyle}' />").append(ENTER);
		}
		
		if ( this.importJs ){
			resouces.append("<script src='${resouceHome}/xtree.js'></script>").append(ENTER);
			resouces.append("<script src='${resouceHome}/xloadtree.js'></script>").append(ENTER);		
			resouces.append("<script src='${resouceHome}/xmlextras.js'></script>").append(ENTER);		
			resouces.append("<script src='${resouceHome}/map.js'></script>").append(ENTER);
			resouces.append("<script src='${resouceHome}/radioTreeItem.js'></script>").append(ENTER);
			resouces.append("<script src='${resouceHome}/radioXLoadTree.js'></script>").append(ENTER);
		}
		Context context = new DefaultContext();
		context.put("resouceHome", getResourceHome());
		context.put("xtreeStyle", this.getXtreeStyle());
		treeScript.append(StrTemplateUtil.merge(resouces.toString(), context));		
	}
	
	public void buildNodeStart(Node pNode, Node pParentNode, int pLevel, int pRow) throws BuildTreeException {
		if ( pNode instanceof WebTreeNode == false ){
			throw new IllegalArgumentException("node type is error, should be WebTreeNode!");
		}
		WebTreeNode webTreeNode = (WebTreeNode)pNode;
		if ( webTreeNode instanceof WebTreeDynamicNode == false ){
			super.buildNodeStart(pNode, pParentNode, pLevel, pRow);
			return;
		}
		WebTreeDynamicNode dynamicNode = (WebTreeDynamicNode)webTreeNode;
		StringBuffer nodeTemplate = new StringBuffer();
		Context context = new DefaultContext();		
			nodeTemplate.append("   var ${nodeScriptName}=new ${treeItem}(\"${name}\",").
		     append("\"${value}\",\"${action}\",\"${subTreeURL}\",${parent},\"${icon}\",\"${openIcon}\",${checked},${disabled});"); 			
	    nodeTemplate.append(ENTER);
	    
		context.put("nodeScriptName", getNodeScriptName(webTreeNode));		
		context.put("treeItem", "WebFXLoadRadioTreeItem");

		String parentNodeScriptName = getNodeScriptName((WebTreeNode)pParentNode);
		context.put("name", webTreeNode.getName());
		context.put("value", webTreeNode.getValue());
		context.put("subTreeURL", dynamicNode.getSubTreeURL());
		context.put("checked", new Boolean(webTreeNode.isSelected()) );
		context.put("disabled", new Boolean(webTreeNode.isDisabled()) );
		context.put("icon", webTreeNode.getIcon());
		context.put("action", webTreeNode.getAction());
		context.put("openIcon", webTreeNode.getOpenIcon() );
		context.put("parent", parentNodeScriptName);
		
		treeScript.append(StrTemplateUtil.merge(nodeTemplate.toString(), context));
		
	}		
	
}