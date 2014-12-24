package com.wie.erp.biz;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wie.erp.dao.ProductDao;

@Service
public class MsSQLErpService implements IMsSQLErpService {

	@Autowired
	private ProductDao productDao;
	// 以下为服务端所用方法
	
}
