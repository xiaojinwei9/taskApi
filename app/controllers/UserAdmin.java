package controllers;

import java.io.File;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import play.Logger;
import play.Play;
import play.mvc.With;
import utils.Cons;
import utils.GsonUtils;
import utils.StrUtils;
import utils.SysTools;

@With(ClientSec.class)
public class UserAdmin extends BasicClientController {

	public static void index(){
		Map<String,String> userInfo=getUserInfo();
		Logger.info("userInfo:"+userInfo);
		if(userInfo.get("userType").equals("3")){
    		if(StrUtils.isNotEmpty(userInfo.get("userTaskGroupIds")+"")){
    			tasks("1", userInfo.get("userTaskGroupIds")+"");
    		}
		}
		render(userInfo);
	}
	
	public static void taskGroups(){
		Map<String,String> userInfo=getUserInfo();
		Logger.info("userInfo:"+userInfo);
		render(userInfo);
	}
	
	public static void taskGroup(String id,String msg){
		Map<String,String> userInfo=getUserInfo();
		Logger.info("userInfo:"+userInfo);
		render(id,userInfo,msg);
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
	
	public static void tasks(String status,String taskGroupId){
		Map<String,String> userInfo=getUserInfo();
		Logger.info("userInfo:"+userInfo);
		render(status,taskGroupId,userInfo);
	}
	
	
	public static void tasksJson(String taskGroupId,String status,String page,String length){
		Map<String,Object> result=new HashMap<String,Object>();
		if(!(StrUtils.isNotEmpty(page)&&StrUtils.isNotEmpty(length))){
			SysTools.setResultParamsErr(result);
			renderJSON(GsonUtils.mapToString(result));
		}
		Map<String,String> userInfo=getUserInfo();
		String userId=userInfo.get("userId");
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("status", status);
		if(taskGroupId!=null&&"0".equals(taskGroupId)){
			paramsMap.put("userId", userId);
		}else{
			paramsMap.put("taskGroupId", taskGroupId);
		}
		paramsMap.put("page", page);
		paramsMap.put("length", length);
		String jsonStr=paramsConstruction("/Tasks/list",paramsMap);
		renderJSON(jsonStr);
	}
	
	

	public static void task(String taskGroupId,String status,String id){
		Map<String,String> userInfo=getUserInfo();
		Logger.info("userInfo:"+userInfo);
		render(id,taskGroupId,status,userInfo);
	}
	
	public static void taskView(String taskGroupId,String status,String id,String msg){
		Map<String,String> userInfo=getUserInfo();
		Logger.info("userInfo:"+userInfo);
		render(taskGroupId,status,id,msg,userInfo);
	}
	
	public static void taskJson(String id){
		Map<String,Object> result=new HashMap<String,Object>();
		if(!(StrUtils.isNotEmpty(id))){
			SysTools.setResultParamsErr(result);
			renderJSON(GsonUtils.mapToString(result));
		}
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("id", id);
		String jsonStr=paramsConstruction("/Tasks/get",paramsMap);
		renderJSON(jsonStr);
	}
	
	public static void taskSaveJson(String id,String name,String image,String taskGroupId,String des,String status){
		Map<String,Object> result=new HashMap<String,Object>();
		if(!(StrUtils.isNotEmpty(id))){
			SysTools.setResultParamsErr(result);
			renderJSON(GsonUtils.mapToString(result));
		}
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("id", id);
		Map<String,String> userInfo=getUserInfo();
		paramsMap.put("userId", userInfo.get("userId")+"");
		paramsMap.put("taskGroupId", taskGroupId);
		paramsMap.put("name", name);
		paramsMap.put("status", status);
		paramsMap.put("image", image);
		paramsMap.put("des", des);
		String jsonStr="";
		if("0".equals(id)){
			jsonStr=paramsConstruction("/Tasks/add",paramsMap);
		}else{
			jsonStr=paramsConstruction("/Tasks/update",paramsMap);
		}
		renderJSON(jsonStr);
	}
	
	
	public static void taskDelJson(String id){
		Map<String,Object> result=new HashMap<String,Object>();
		if(!(StrUtils.isNotEmpty(id))){
			SysTools.setResultParamsErr(result);
			renderJSON(GsonUtils.mapToString(result));
		}
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("id", id);
		String jsonStr=paramsConstruction("/Tasks/del",paramsMap);
		renderJSON(jsonStr);
	}
	
	
	public static void taskLogSaveJson(String taskId,String taskGroupId,String taskStatus,String cont,File[] files){
		Map<String,Object> result=new HashMap<String,Object>();
		if(!(StrUtils.isNotEmpty(taskId))){
			SysTools.setResultParamsErr(result);
			renderJSON(GsonUtils.mapToString(result));
		}
		//文件处理
		String filesStr="";
		Logger.info("files:"+files);
		if(files!=null&&files.length>0){
			Logger.info("files.length:"+files.length);
			for(File file:files){
				String fileStr=saveFile(file);
				if(StrUtils.isNotEmpty(fileStr)&&fileStr.indexOf("-")!=-1){
					filesStr+=fileStr+"|";
				}
			}
		}
		//文件处理 end
		Map<String, String> paramsMap = new HashMap<String, String>();
		Map<String,String> userInfo=getUserInfo();
		paramsMap.put("userId", userInfo.get("userId")+"");
		paramsMap.put("taskId", taskId);
		paramsMap.put("cont", cont);
		paramsMap.put("files", filesStr);
		String jsonStr="";
		jsonStr=paramsConstruction("/TaskLogs/add",paramsMap);
    	Map<String,Object> resultMap=GsonUtils.toMapObj(jsonStr);
    	String msg=resultMap.get("msg")+"";
		taskView(taskGroupId,taskStatus,taskId,msg);
		//renderJSON(jsonStr);
	}
	
	public static void taskLogsJson(String taskId,String page,String length){
		Map<String,Object> result=new HashMap<String,Object>();
		if(!(StrUtils.isNotEmpty(page)&&StrUtils.isNotEmpty(length))){
			SysTools.setResultParamsErr(result);
			renderJSON(GsonUtils.mapToString(result));
		}
		String comments="0";
		Map<String,String> userInfo=getUserInfo();
		if("1".equals(userInfo.get("userType")+"")){
			comments="1";
		}
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("taskId", taskId);
		paramsMap.put("comments", comments);
		paramsMap.put("page", page);
		paramsMap.put("length", length);
		String jsonStr=paramsConstruction("/TaskLogs/list",paramsMap);
		renderJSON(jsonStr);
	}
	
	public static void users(String taskGroupId){
		Map<String,String> userInfo=getUserInfo();
		Logger.info("userInfo:"+userInfo);
		render(taskGroupId,userInfo);
	}
	
	public static void usersJson(String page,String length,String groupId){
		Map<String,Object> result=new HashMap<String,Object>();
		if(!(StrUtils.isNotEmpty(page)&&StrUtils.isNotEmpty(length))){
			SysTools.setResultParamsErr(result);
			renderJSON(GsonUtils.mapToString(result));
		}
		Map<String,String> userInfo=getUserInfo();
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("page", page);
		paramsMap.put("length", length);
		if(StrUtils.isNotEmpty(groupId)&&!"0".equals(groupId)){
			paramsMap.put("groupId", groupId);
		}else{
			if("3".equals(userInfo.get("userType"))){
				String userTaskGroupIds=userInfo.get("userTaskGroupIds")+"";
				Logger.info("userTaskGroupIds:"+userTaskGroupIds);
				userTaskGroupIds=userTaskGroupIds.substring(userTaskGroupIds.indexOf(",")+1, userTaskGroupIds.length());
				Logger.info("userTaskGroupIds2:"+userTaskGroupIds);
				String userTaskGroupId=userTaskGroupIds.substring(0, userTaskGroupIds.indexOf(","));
				paramsMap.put("groupId", userTaskGroupId);
			}
		}
		String jsonStr=paramsConstruction("/Users/list",paramsMap);
		renderJSON(jsonStr);
	}
	
	
	public static void user(){
		Map<String,String> userInfo=getUserInfo();
		String id=userInfo.get("userId");
		if(userInfo.get("userType").equals("3")){
    		if(StrUtils.isNotEmpty(userInfo.get("userTaskGroupIds")+"")){
    			String taskGroupId=userInfo.get("userTaskGroupIds")+"";
    			render(id,userInfo,taskGroupId);
    		}
		}else{
			render(id,userInfo);
		}
	}
	
	public static void userJson(){
		Map<String,String> userInfo=getUserInfo();
		String id=userInfo.get("userId");
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
	
	public static void userSaveJson(String mobile,String password,String type,String name,String email,String phone,String image,String taskGroupIds,String des,File file){
		Map<String,String> userInfo=getUserInfo();
		String id=userInfo.get("userId");
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
		paramsMap.put("name", name);
		paramsMap.put("email", email);
		paramsMap.put("phone", phone);
		if(StrUtils.isNotEmpty(files)){
			paramsMap.put("image", files);
		}
		paramsMap.put("des", des);
		String jsonStr=paramsConstruction("/Users/update",paramsMap);
		//renderJSON(jsonStr);
		user();
	}
	
	public static void taskStatusSaveJson(String id,String status){
		Map<String,Object> result=new HashMap<String,Object>();
		if(!(StrUtils.isNotEmpty(id))){
			SysTools.setResultParamsErr(result);
			renderJSON(GsonUtils.mapToString(result));
		}
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("id", id);
		paramsMap.put("status", status);
		String jsonStr=paramsConstruction("/Tasks/updateStatus",paramsMap);
		String cont="更改任务状态为:"+getStatusNameById(status);
		//taskLogSaveJson(id,cont,null);
		//log
		Map<String, String> paramsMapLog = new HashMap<String, String>();
		Map<String,String> userInfo=getUserInfo();
		paramsMapLog.put("userId", userInfo.get("userId")+"");
		paramsMapLog.put("taskId", id);
		paramsMapLog.put("cont", cont);
		paramsMapLog.put("files", "");
		String jsonStrLog="";
		jsonStrLog=paramsConstruction("/TaskLogs/add",paramsMapLog);
		
		renderJSON(jsonStr);
	}
	
	public static void files(String taskGroupId,String status,String taskId){
		Map<String,String> userInfo=getUserInfo();
		Logger.info("userInfo:"+userInfo);
		render(taskGroupId,status,taskId,userInfo);
	}
	
	public static void filesJson(String taskId){
		Map<String,Object> result=new HashMap<String,Object>();
		if(!(StrUtils.isNotEmpty(taskId)&&StrUtils.isNotEmpty(taskId))){
			SysTools.setResultParamsErr(result);
			renderJSON(GsonUtils.mapToString(result));
		}
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("taskId", taskId);
		String jsonStr=paramsConstruction("/TaskLogs/listFiles",paramsMap);
		renderJSON(jsonStr);
	}
	
	public static void adminUsers(){
		Map<String,String> userInfo=getUserInfo();
		render(userInfo);
	}
	
	public static void adminUserSaveJson(String id,String mobile,String password,String type,String name,String email,String phone,String taskGroupIds,String des,File file,String isComment){
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
		String msg="";
		if("0".equals(id)){
			jsonStr=paramsConstruction("/Users/add",paramsMap);
	    	Map<String,Object> map=GsonUtils.toMapObj(jsonStr);
	    	if("100".equals(map.get("code"))){
	    		Map<String,Object> user=(Map<String,Object>)map.get("user");
	    		id=user.get("id")+"";
	    	}
	    	msg=map.get("msg")+"";
		}else{
			jsonStr=paramsConstruction("/Users/update",paramsMap);
			Map<String,Object> map=GsonUtils.toMapObj(jsonStr);
			msg=map.get("msg")+"";
		}
		//renderJSON(jsonStr);
		adminUser(id,msg);
	}
	
	public static void adminUser(String id,String msg){
		Map<String,String> userInfo=getUserInfo();
		render(id,userInfo,msg);
	}
}
