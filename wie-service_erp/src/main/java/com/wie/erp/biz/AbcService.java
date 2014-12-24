package com.wie.erp.biz;


import com.wie.common.tools.util.FreemarkerUtil;
import com.wie.erp.dao.ProductDao;
import com.wie.erp.model.Product;
import com.wie.permissions.dao.ActionsDAO;
import com.wie.permissions.model.Actions;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2014/11/14.
 */
@Service
public class AbcService  implements IAbcService {
    @Autowired
    private String outPath;
    @Autowired
    private String ftlPath;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private ActionsDAO actionsDAO;

    @Override
    public void generate(String path) {
        FreemarkerUtil util=FreemarkerUtil.getInstance(ftlPath);
        Map<String,Object> root = new HashMap<String,Object>();
        String outfile = path+outPath+"/index.jsp";
        List<Actions> list=actionsDAO.findList("select a from Actions a  left join  a.resource as ar where ar.enname ='product' ");
       for(Actions action:list)
       System.out.println(action.getEnname());
/*

        Class cl=Product.class;

        for (Field field : cl.getDeclaredFields()) {
            int n=field.toString().indexOf("Product.");
            System.out.println(field.toString().substring(n+8));
        }*/
        root.put("resourceName", "product");
        root.put("actionlist", list);
        util.fprint(root, "/index.ftl", outfile);
    }
    public  static void main(String[] args){
        Product product = new Product();
        JSONObject jsonEntity= JSONObject.fromObject(product);
        System.out.print(jsonEntity.toString());
    }
}
