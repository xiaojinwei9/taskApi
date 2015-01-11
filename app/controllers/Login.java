package controllers;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonObject;

import play.Logger;
import utils.GsonUtils;
import utils.StrUtils;
import utils.SysTools;

public class Login extends BasicClientController {
	
	public static void loginpage(){
		render();
	}

	public static void login(String mobile,String password){
		Map<String,Object> result=new HashMap<String,Object>();
		if(!(StrUtils.isNotEmpty(mobile)&&StrUtils.isNotEmpty(password))){
			SysTools.setResultParamsErr(result);
			renderJSON(GsonUtils.mapToString(result));
		}
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("mobile", mobile);
		paramsMap.put("password", password);
		String jsonStr=paramsConstruction("/Users/login",paramsMap);
		 JsonObject jsonObj=GsonUtils.parseJson(jsonStr);
		if("\"100\"".equals(jsonObj.get("code")+"")){
			Logger.info("login sucess");
			JsonObject userObj=(JsonObject)jsonObj.get("user");
			session.put("userId", userObj.get("id")+"");
    		session.put("userType", userObj.get("type")+"");
    		session.put("userName", userObj.get("name")+"");
		}
		renderJSON(jsonStr);
	}
	
	public static void logout(){
		session.clear();
		loginpage();
	}
}
