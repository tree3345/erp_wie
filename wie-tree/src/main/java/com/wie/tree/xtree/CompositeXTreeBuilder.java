/**   
  * @Title: CompositeXTreeBuilder.java 
  * @Package com.tgyt.tree.xtree 
  * @Description: 
  * @author zhangfeng 13940488705@163.com 
  * @date 2011-8-9 下午05:09:46 
  * @version V1.0   
  */

package com.wie.tree.xtree;

import com.wie.templateEngine.Context;
import com.wie.templateEngine.support.DefaultContext;
import com.wie.templateEngine.support.StrTemplateUtil;
import com.wie.tree.BuildTreeException;
import com.wie.tree.Node;
import com.wie.tree.support.WebTreeNode;

/** 
 * @ClassName: CompositeXTreeBuilder 
 * @Description: composite tree builder
 *  
 */
public class CompositeXTreeBuilder extends XTreeBuilder{
	protected boolean cascadeCheck = true;	
	public boolean isCascadeCheck() {
		return cascadeCheck;
	}

	public void setCascadeCheck(boolean cascadeCheck) {
		this.cascadeCheck = cascadeCheck;
	}

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
			resouces.append("<script src='${resouceHome}/map.js'></script>").append(ENTER);
			resouces.append("<script src='${resouceHome}/checkboxTreeItem.js'></script>").append(ENTER);
			resouces.append("<script src='${resouceHome}/radioTreeItem.js'></script>").append(ENTER);
		}
		resouces.append("<script>").append(ENTER);
		resouces.append("   webFXTreeConfig.cascadeCheck = ${cascadeCheck};").append(ENTER);
		resouces.append("</script>").append(ENTER);
		
		Context context = new DefaultContext();
		context.put("resouceHome", getResourceHome());
		context.put("cascadeCheck", new Boolean(cascadeCheck));
		context.put("xtreeStyle", this.getXtreeStyle());
		treeScript.append(StrTemplateUtil.merge(resouces.toString(), context));		
	}
	
	public void buildNodeStart(Node pNode, Node pParentNode, int pLevel, int pRow) throws BuildTreeException {
		if ( pNode instanceof WebTreeNode == false ){
			throw new IllegalArgumentException("node type is error, should be WebTreeNode!");
		}
		WebTreeNode webTreeNode = (WebTreeNode)pNode;
		String nodeProperty = webTreeNode.getNodeProperty();
		if ( WebTreeNode.NONE.equals(nodeProperty) ){
			super.buildNodeStart(pNode, pParentNode, pLevel, pRow);
			return;
		}		
		String parentNodeScriptName = getNodeScriptName((WebTreeNode)pParentNode);
		
		StringBuffer node = new StringBuffer();
		node.append("   var ${nodeScriptName}=new ${treeItem}(\"${name}\",").
		     append("\"${value}\",\"${action}\",${parent},\"${icon}\",\"${openIcon}\",${checked},${disabled}); ");
		node.append(ENTER);
		
		
		Context context = new DefaultContext(); 
		context.put("nodeScriptName", getNodeScriptName(webTreeNode));		
		if ( WebTreeNode.CHECKBOX.equals(nodeProperty)){
			context.put("treeItem", "WebFXCheckBoxTreeItem");	
		}else if ( WebTreeNode.RADIO.equals(nodeProperty)){
			context.put("treeItem", "WebFXRadioTreeItem");
		}		
		context.put("name", webTreeNode.getName());
		context.put("value", webTreeNode.getValue());
		context.put("action", webTreeNode.getAction());
		context.put("checked", new Boolean(webTreeNode.isSelected()) );
		context.put("disabled", new Boolean(webTreeNode.isDisabled()) );
		context.put("icon", webTreeNode.getIcon());
		context.put("openIcon", webTreeNode.getOpenIcon() );
		
		context.put("parent", parentNodeScriptName);
		
		treeScript.append(StrTemplateUtil.merge(node.toString(), context));
		
	}		
	
}
