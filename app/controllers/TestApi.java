package controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.TestTb;
import play.mvc.With;
import utils.StrUtils;
import utils.SysTools;

@With(ApiSec.class)
public class TestApi extends BasicController {

	public static void add(String userId,String userName){
		Map<String,Object> result=new HashMap<String,Object>();
		if(!(StrUtils.isNotEmpty(userId)&&StrUtils.isNumeric(userId)&&StrUtils.isNotEmpty(userName))){
			SysTools.setResultParamsErr(result);
			renderJSON(result);
		}
		TestTb testTb=new TestTb(Long.valueOf(userId),userName);
		testTb.save();
		SysTools.setResultOpSec(result);
		result.put("testTb", testTb);
		renderJSON(result);
	}
	
	public static void get(String userId){
		Map<String,Object> result=new HashMap<String,Object>();
		if(!(StrUtils.isNotEmpty(userId)&&StrUtils.isNumeric(userId))){
			SysTools.setResultParamsErr(result);
			renderJSON(result);
		}
		List<TestTb> testTbs=TestTb.find("userId=? order by id desc", Long.valueOf(userId)).fetch();
		SysTools.setResultOpSec(result);
		result.put("testTbs", testTbs);
		renderJSON(result);
	}
	
	public static void del(String userId){
		Map<String,Object> result=new HashMap<String,Object>();
		if(!(StrUtils.isNotEmpty(userId)&&StrUtils.isNumeric(userId))){
			SysTools.setResultParamsErr(result);
			renderJSON(result);
		}
		List<TestTb> testTbs=TestTb.find("userId=? order by id desc", Long.valueOf(userId)).fetch();
		for(TestTb testTb:testTbs){
			testTb.delete();
		}
		SysTools.setResultOpSec(result);
		renderJSON(result);
	}
	
	
}
