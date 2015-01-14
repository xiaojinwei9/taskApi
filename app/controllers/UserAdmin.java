package controllers;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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
		render(userInfo);
	}
	
	public static void tasks(String status){
		Map<String,String> userInfo=getUserInfo();
		Logger.info("userInfo:"+userInfo);
		render(status,userInfo);
	}
	
	
	public static void tasksJson(String status,String page,String length){
		Map<String,Object> result=new HashMap<String,Object>();
		if(!(StrUtils.isNotEmpty(page)&&StrUtils.isNotEmpty(length))){
			SysTools.setResultParamsErr(result);
			renderJSON(GsonUtils.mapToString(result));
		}
		Map<String,String> userInfo=getUserInfo();
		String userId=userInfo.get("userId");
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("status", status);
		paramsMap.put("userId", userId);
		paramsMap.put("page", page);
		paramsMap.put("length", length);
		String jsonStr=paramsConstruction("/Tasks/list",paramsMap);
		renderJSON(jsonStr);
	}
	
	

	public static void task(String id){
		render(id);
	}
	
	public static void taskView(String id){
		render(id);
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
	
	
	public static void taskLogSaveJson(String taskId,String cont,File file){
		Map<String,Object> result=new HashMap<String,Object>();
		if(!(StrUtils.isNotEmpty(taskId))){
			SysTools.setResultParamsErr(result);
			renderJSON(GsonUtils.mapToString(result));
		}
		//文件处理
		String files="";
		if(file!=null){
			String fileNameA=file.getName();
			//生成随机文件名：当前年月日时分秒+五位随机数（为了在实际项目中防止文件同名而进行的处理） 
			Random r = new Random();
			int rannum = (int) (r.nextDouble() * (99999 - 10000 + 1)) + 10000; //获取随机数 
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss"); //时间格式化的格式 
			String nowTimeStr = dateFormat.format(new Date()); //当前时间 
			String fileType=fileNameA.substring(fileNameA.lastIndexOf("."), fileNameA.length());
			String fileNameB=nowTimeStr+"_"+rannum+fileType;
			
			fileNameB="/public/uploads/"+fileNameB;
			String url=Cons.www_url+fileNameB;
			String filePath=Play.applicationPath.getAbsoluteFile()+fileNameB;
			Logger.info("filePath:"+filePath);
			File uploadFile=new File(filePath);   
		    play.libs.Files.copy(file,uploadFile);
		    files=fileNameA+"-"+url;
		}
		//文件处理 end
		Map<String, String> paramsMap = new HashMap<String, String>();
		Map<String,String> userInfo=getUserInfo();
		paramsMap.put("userId", userInfo.get("userId")+"");
		paramsMap.put("taskId", taskId);
		paramsMap.put("cont", cont);
		paramsMap.put("files", files);
		String jsonStr="";
		jsonStr=paramsConstruction("/TaskLogs/add",paramsMap);
		taskView(taskId);
		//renderJSON(jsonStr);
	}
	
	public static void taskLogsJson(String taskId,String page,String length){
		Map<String,Object> result=new HashMap<String,Object>();
		if(!(StrUtils.isNotEmpty(page)&&StrUtils.isNotEmpty(length))){
			SysTools.setResultParamsErr(result);
			renderJSON(GsonUtils.mapToString(result));
		}
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("taskId", taskId);
		paramsMap.put("page", page);
		paramsMap.put("length", length);
		String jsonStr=paramsConstruction("/TaskLogs/list",paramsMap);
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
		Map<String,String> userInfo=getUserInfo();
		String userTaskGroupIds=userInfo.get("userTaskGroupIds")+"";
		Logger.info("userTaskGroupIds:"+userTaskGroupIds);
		userTaskGroupIds=userTaskGroupIds.substring(userTaskGroupIds.indexOf(",")+1, userTaskGroupIds.length());
		Logger.info("userTaskGroupIds2:"+userTaskGroupIds);
		String userTaskGroupId=userTaskGroupIds.substring(0, userTaskGroupIds.indexOf(","));
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("page", page);
		paramsMap.put("length", length);
		paramsMap.put("groupId", userTaskGroupId);
		String jsonStr=paramsConstruction("/Users/list",paramsMap);
		renderJSON(jsonStr);
	}
	
	
	public static void user(){
		Map<String,String> userInfo=getUserInfo();
		String id=userInfo.get("userId");
		render(id);
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
	
	public static void userSaveJson(String mobile,String password,String type,String name,String email,String phone,String image,String taskGroupIds,String des){
		Map<String,String> userInfo=getUserInfo();
		String id=userInfo.get("userId");
		Map<String,Object> result=new HashMap<String,Object>();
		if(!(StrUtils.isNotEmpty(id))){
			SysTools.setResultParamsErr(result);
			renderJSON(GsonUtils.mapToString(result));
		}
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("id", id);
		paramsMap.put("mobile", mobile);
		paramsMap.put("password", password);
		paramsMap.put("name", name);
		paramsMap.put("email", email);
		paramsMap.put("phone", phone);
		paramsMap.put("image", image);
		paramsMap.put("des", des);
		String jsonStr=paramsConstruction("/Users/update",paramsMap);
		renderJSON(jsonStr);
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
		renderJSON(jsonStr);
	}
	
}
