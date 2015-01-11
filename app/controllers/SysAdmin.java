package controllers;

import java.util.HashMap;
import java.util.Map;

import play.Logger;
import play.mvc.With;
import utils.GsonUtils;
import utils.StrUtils;
import utils.SysTools;

@With(ClientSec.class)
public class SysAdmin extends BasicClientController {

	public static void index(){
		Map<String,String> userInfo=getUserInfo();
		Logger.info("userInfo:"+userInfo);
		render(userInfo);
	}
	
	public static void taskGroups(){
		render();
	}
	
	public static void taskGroupsJson(String page,String length){
		Map<String,Object> result=new HashMap<String,Object>();
		if(!(StrUtils.isNotEmpty(page)&&StrUtils.isNotEmpty(length))){
			SysTools.setResultParamsErr(result);
			renderJSON(GsonUtils.mapToString(result));
		}
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("page", page);
		paramsMap.put("length", length);
		String jsonStr=paramsConstruction("/TaskGroups/list",paramsMap);
		renderJSON(jsonStr);
	}
	
	public static void taskGroup(String id){
		render(id);
	}
	
	public static void taskGroupJson(String id){
		Map<String,Object> result=new HashMap<String,Object>();
		if(!(StrUtils.isNotEmpty(id))){
			SysTools.setResultParamsErr(result);
			renderJSON(GsonUtils.mapToString(result));
		}
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("id", id);
		String jsonStr=paramsConstruction("/TaskGroups/get",paramsMap);
		renderJSON(jsonStr);
	}
	
	public static void taskGroupSaveJson(String id,String name,String image){
		Map<String,Object> result=new HashMap<String,Object>();
		if(!(StrUtils.isNotEmpty(id))){
			SysTools.setResultParamsErr(result);
			renderJSON(GsonUtils.mapToString(result));
		}
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("id", id);
		Map<String,String> userInfo=getUserInfo();
		paramsMap.put("userId", userInfo.get("userId")+"");
		paramsMap.put("name", name);
		paramsMap.put("image", image);
		String jsonStr="";
		if("0".equals(id)){
			jsonStr=paramsConstruction("/TaskGroups/add",paramsMap);
		}else{
			jsonStr=paramsConstruction("/TaskGroups/update",paramsMap);
		}
		renderJSON(jsonStr);
	}
	
	public static void taskGroupDelJson(String id){
		Map<String,Object> result=new HashMap<String,Object>();
		if(!(StrUtils.isNotEmpty(id))){
			SysTools.setResultParamsErr(result);
			renderJSON(GsonUtils.mapToString(result));
		}
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("id", id);
		String jsonStr=paramsConstruction("/TaskGroups/del",paramsMap);
		renderJSON(jsonStr);
	}
	
	public static void users(){
		render();
	}
	
	public static void usersJson(String page,String length){
		Map<String,Object> result=new HashMap<String,Object>();
		if(!(StrUtils.isNotEmpty(page)&&StrUtils.isNotEmpty(length))){
			SysTools.setResultParamsErr(result);
			renderJSON(GsonUtils.mapToString(result));
		}
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("page", page);
		paramsMap.put("length", length);
		String jsonStr=paramsConstruction("/Users/list",paramsMap);
		renderJSON(jsonStr);
	}
	
	public static void taskGroupsByUserIdJson(String userId){
		Map<String,Object> result=new HashMap<String,Object>();
		if(!(StrUtils.isNotEmpty(userId))){
			SysTools.setResultParamsErr(result);
			renderJSON(GsonUtils.mapToString(result));
		}
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("userId", userId);
		paramsMap.put("page", "1");
		paramsMap.put("length", "100");
		String jsonStr=paramsConstruction("/TaskGroups/list",paramsMap);
		renderJSON(jsonStr);
	}
	
	public static void user(String id){
		render(id);
	}
	
	public static void userJson(String id){
		Map<String,Object> result=new HashMap<String,Object>();
		if(!(StrUtils.isNotEmpty(id))){
			SysTools.setResultParamsErr(result);
			renderJSON(GsonUtils.mapToString(result));
		}
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("id", id);
		String jsonStr=paramsConstruction("/Users/get",paramsMap);
		renderJSON(jsonStr);
	}
	
	public static void userSaveJson(String id,String mobile,String password,String type,String name,String email,String phone,String image,String taskGroupIds,String des){
		Map<String,Object> result=new HashMap<String,Object>();
		if(!(StrUtils.isNotEmpty(id))){
			SysTools.setResultParamsErr(result);
			renderJSON(GsonUtils.mapToString(result));
		}
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("id", id);
		paramsMap.put("mobile", mobile);
		paramsMap.put("password", password);
		paramsMap.put("type", type);
		paramsMap.put("name", name);
		paramsMap.put("email", email);
		paramsMap.put("phone", phone);
		paramsMap.put("image", image);
		paramsMap.put("taskGroupIds", taskGroupIds);
		paramsMap.put("des", des);
		String jsonStr="";
		if("0".equals(id)){
			jsonStr=paramsConstruction("/Users/add",paramsMap);
		}else{
			jsonStr=paramsConstruction("/Users/update",paramsMap);
		}
		renderJSON(jsonStr);
	}
	
	public static void allTaskGroupsJson(){
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("page", "1");
		paramsMap.put("length", "1000");
		String jsonStr=paramsConstruction("/TaskGroups/list",paramsMap);
		renderJSON(jsonStr);
	}
	
	public static void userDelJson(String id){
		Map<String,Object> result=new HashMap<String,Object>();
		if(!(StrUtils.isNotEmpty(id))){
			SysTools.setResultParamsErr(result);
			renderJSON(GsonUtils.mapToString(result));
		}
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("id", id);
		String jsonStr=paramsConstruction("/Users/del",paramsMap);
		renderJSON(jsonStr);
	}
	
}
