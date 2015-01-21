package controllers;

import java.io.File;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import play.Logger;
import play.mvc.With;
import utils.GsonUtils;
import utils.StrUtils;
import utils.SysTools;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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
	
	public static void taskGroup(String id,String msg){
		render(id,msg);
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
	
	public static void taskGroupSaveJson(String id,String name,File file){
		Map<String,Object> result=new HashMap<String,Object>();
		if(!(StrUtils.isNotEmpty(id))){
			SysTools.setResultParamsErr(result);
			renderJSON(GsonUtils.mapToString(result));
		}
		//文件处理
		String files="";
		if(file!=null){
			files=saveFile(file);
		}
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("id", id);
		Map<String,String> userInfo=getUserInfo();
		paramsMap.put("userId", userInfo.get("userId")+"");
		paramsMap.put("name", name);
		if(StrUtils.isNotEmpty(files)){
			paramsMap.put("image", files);
		}
		String jsonStr="";
		if("0".equals(id)){
			jsonStr=paramsConstruction("/TaskGroups/add",paramsMap);
			Gson gson = new Gson();
	    	Type type=(Type) new TypeToken<Map<String, Object>>() {}.getType();   
	    	Map<String,Object> map=gson.fromJson(jsonStr,type);
	    	Map<String,Object> taskGroup=(Map<String,Object>)map.get("TaskGroup");
	    	id=taskGroup.get("id")+"";
		}else{
			jsonStr=paramsConstruction("/TaskGroups/update",paramsMap);
		}
		Map<String,Object> resultMap=GsonUtils.toMapObj(jsonStr);
		taskGroup(id,resultMap.get("msg")+"");
		//renderJSON(jsonStr);
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
	
	public static void usersJson(String page,String length,String groupId){
		Map<String,Object> result=new HashMap<String,Object>();
		if(!(StrUtils.isNotEmpty(page)&&StrUtils.isNotEmpty(length))){
			SysTools.setResultParamsErr(result);
			renderJSON(GsonUtils.mapToString(result));
		}
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("page", page);
		paramsMap.put("length", length);
		if(StrUtils.isNotEmpty(groupId)&&!"0".equals(groupId)){
			paramsMap.put("groupId", groupId);
		}
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
	
	public static void userSaveJson(String id,String mobile,String password,String type,String name,String email,String phone,String taskGroupIds,String des,File file,String isComment){
		Map<String,Object> result=new HashMap<String,Object>();
		if(!(StrUtils.isNotEmpty(id))){
			SysTools.setResultParamsErr(result);
			renderJSON(GsonUtils.mapToString(result));
		}
		
		//文件处理
		String files="";
		if(file!=null){
			files=saveFile(file);
		}
				
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("id", id);
		paramsMap.put("mobile", mobile);
		paramsMap.put("password", password);
		paramsMap.put("type", type);
		paramsMap.put("name", name);
		paramsMap.put("email", email);
		paramsMap.put("phone", phone);
		if(StrUtils.isNotEmpty(files)){
			paramsMap.put("image", files);
		}
		paramsMap.put("taskGroupIds", taskGroupIds);
		paramsMap.put("isComment", isComment);
		paramsMap.put("des", des);
		String jsonStr="";
		if("0".equals(id)){
			jsonStr=paramsConstruction("/Users/add",paramsMap);
			Gson gson = new Gson();
	    	Type typeMap=(Type) new TypeToken<Map<String, Object>>() {}.getType();   
	    	Map<String,Object> map=gson.fromJson(jsonStr,typeMap);
	    	Map<String,Object> user=(Map<String,Object>)map.get("user");
	    	id=user.get("id")+"";
		}else{
			jsonStr=paramsConstruction("/Users/update",paramsMap);
		}
		//renderJSON(jsonStr);
		user(id);
	}
	
	public static void allTaskGroupsJson(String userId){
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("page", "1");
		paramsMap.put("length", "1000");
		if(StrUtils.isNotEmpty(userId)){
			paramsMap.put("userId", userId);
		}
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
