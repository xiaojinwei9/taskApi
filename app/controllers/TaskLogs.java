package controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.Task;
import models.TaskLog;
import models.User;
import play.Logger;
import play.mvc.With;
import utils.StrUtils;
import utils.SysTools;

@With(ApiSec.class)
public class TaskLogs extends BasicController {
	public static void add(String taskId,String cont,String files,String userId){
		Map<String,Object> result=new HashMap<String,Object>();
		if(!(StrUtils.isNotEmpty(userId)&&StrUtils.isNumeric(userId)&&StrUtils.isNotEmpty(taskId)&&StrUtils.isNumeric(taskId)&&StrUtils.isNotEmpty(cont))){
			SysTools.setResultParamsErr(result);
			renderJSON(result);
		}
		TaskLog taskLog=new TaskLog(Integer.valueOf(taskId), cont, files, new Date(), Integer.valueOf(userId), 1);
		taskLog.save();
		SysTools.setResultOpSec(result);
		result.put("taskLog", taskLog);
		renderJSON(result);
	}
	public static void update(String id,String cont,String files,String userId){
		Map<String,Object> result=new HashMap<String,Object>();
		if(!(StrUtils.isNotEmpty(id)&&StrUtils.isNumeric(id)&&StrUtils.isNotEmpty(userId)&&StrUtils.isNumeric(userId))){
			SysTools.setResultParamsErr(result);
			renderJSON(result);
		}
		TaskLog taskLog=TaskLog.findById(Long.valueOf(id));
		if(StrUtils.isNotEmpty(cont)){
			taskLog.cont=cont;
		}
		if(StrUtils.isNotEmpty(files)){
			taskLog.files=files;
		}
		taskLog.save();
		SysTools.setResultOpSec(result);
		result.put("taskLog", taskLog);
		renderJSON(result);
	}
	
	
	public static void get(String id){
		Map<String,Object> result=new HashMap<String,Object>();
		if(!(StrUtils.isNotEmpty(id)&&StrUtils.isNumeric(id))){
			SysTools.setResultParamsErr(result);
			renderJSON(result);
		}
		TaskLog taskLog=TaskLog.findById(Long.valueOf(id));
		SysTools.setResultOpSec(result);
		result.put("taskLog", taskLog);
		renderJSON(result);
	}
	
	public static void list(String taskId,String page,String length){
		Map<String,Object> result=new HashMap<String,Object>();
		if(!(StrUtils.isNotEmpty(page)&&StrUtils.isNumeric(page)&&StrUtils.isNotEmpty(length)&&StrUtils.isNumeric(length)&&StrUtils.isNotEmpty(taskId)&&StrUtils.isNumeric(taskId))){
			SysTools.setResultParamsErr(result);
			renderJSON(result);
		}
		Long total=0L;
		List<TaskLog> list=TaskLog.find("available=1 and taskId=? order by id desc", Integer.valueOf(taskId)).fetch(Integer.valueOf(page), Integer.valueOf(length));
		if(list!=null&&list.size()>0){
			total=TaskLog.count("available=1 and taskId=?",Integer.valueOf(taskId));
		}
		
		result.put("list", list);
		result.put("page", page);
		result.put("length", length);
		result.put("total", total);
		
		SysTools.setResultOpSec(result);
		renderJSON(result);
	}
	
	public static void del(String id){
		Map<String,Object> result=new HashMap<String,Object>();
		if(!(StrUtils.isNotEmpty(id)&&StrUtils.isNumeric(id))){
			SysTools.setResultParamsErr(result);
			renderJSON(result);
		}
		TaskLog taskLog=TaskLog.findById(Long.valueOf(id));
		taskLog.available=2;
		taskLog.save();
		SysTools.setResultOpSec(result);
		renderJSON(result);
	}
	
}
