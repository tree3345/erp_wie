package com.wie.permissions.tree;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.wie.permissions.model.Resources;
import com.wie.tree.Node;
import com.wie.tree.TreeDirector;
import com.wie.tree.TreeModel;
import com.wie.tree.UserDataUncoder;
import com.wie.tree.easyui.EasyuiTreeBuilder;
import com.wie.tree.easyui.WieTreeBuilder;
import com.wie.tree.support.AbstractWebTreeBuilder;
import com.wie.tree.support.AbstractWebTreeModelCreator;
import com.wie.tree.support.DefaultNodeComparator;
import com.wie.tree.support.DefaultTreeDirector;
import com.wie.tree.support.DefaultTreeModel;
import com.wie.tree.support.ReverseComparator;
import com.wie.tree.support.WebTreeNode;

public class Utils {
	public static String showeasyuiTree(UserDataUncoder orgUncoder,AbstractWebTreeModelCreator treeModelCreator,List list,HttpServletRequest qRequest){
		
		treeModelCreator.init(qRequest);
		TreeModel tempModel = treeModelCreator.create(list, orgUncoder);
		WebTreeNode virtualRootNode = new WebTreeNode("根节点", "Virtual");
		java.util.Iterator rootNodes = tempModel.getRootNodes();
		while (rootNodes.hasNext()) {
			Node rootNode = (Node) rootNodes.next();
			rootNode.setParent(virtualRootNode);
		}
		DefaultTreeModel treeModel = new DefaultTreeModel();
		treeModel.addRootNode(virtualRootNode);
		TreeDirector director = new DefaultTreeDirector();// 构造树导向器
		//director.setComparator(new ReverseComparator(new DefaultNodeComparator()));
		AbstractWebTreeBuilder treeBuilder = new EasyuiTreeBuilder();// 构造树Builder
		treeBuilder.init(qRequest);
		director.build(treeModel, treeBuilder);// 执行构造
		return treeBuilder.getTreeScript();
	}
	
	public static String getTree(UserDataUncoder orgUncoder,AbstractWebTreeModelCreator treeModelCreator,List list,HttpServletRequest qRequest){
		treeModelCreator.init(qRequest);
		TreeModel tempModel = treeModelCreator.create(list, orgUncoder);
		
		WebTreeNode virtualRootNode = new WebTreeNode("根节点", "Virtual");
		java.util.Iterator rootNodes = tempModel.getRootNodes();
		while (rootNodes.hasNext()) {
			Node rootNode = (Node) rootNodes.next();
			rootNode.setParent(virtualRootNode);
		}
		DefaultTreeModel treeModel = new DefaultTreeModel();
		treeModel.addRootNode(virtualRootNode);
		TreeDirector director = new DefaultTreeDirector();// 构造树导向器
		//director.setComparator(new ReverseComparator(new DefaultNodeComparator()));
		AbstractWebTreeBuilder treeBuilder = new WieTreeBuilder();// 构造树Builder
		treeBuilder.init(qRequest);
		director.build(treeModel, treeBuilder);// 执行构造
		System.out.println(treeModel);
		return treeBuilder.getTreeScript();
	}
}
