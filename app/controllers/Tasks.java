package controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.Task;
import models.TaskGroup;
import models.User;
import play.Logger;
import play.mvc.With;
import utils.StrUtils;
import utils.SysTools;

@With(ApiSec.class)
public class Tasks extends BasicController {
	public static void add(String name,String image,String des,String taskGroupId,String status,String userId){
		Map<String,Object> result=new HashMap<String,Object>();
		if(!(StrUtils.isNotEmpty(userId)&&StrUtils.isNumeric(userId)&&StrUtils.isNotEmpty(name)&&StrUtils.isNotEmpty(taskGroupId)&&StrUtils.isNumeric(taskGroupId)&&StrUtils.isNotEmpty(status)&&StrUtils.isNumeric(status))){
			SysTools.setResultParamsErr(result);
			renderJSON(result);
		}
		Task task=new Task(name, image, new Date(), new Date(), Integer.valueOf(status),des, Integer.valueOf(taskGroupId),Integer.valueOf(userId),1);
		task.save();
		SysTools.setResultOpSec(result);
		result.put("task", task);
		renderJSON(result);
	}
	public static void update(String id,String name,String image,String des,String taskGroupId,String status,String userId){
		Map<String,Object> result=new HashMap<String,Object>();
		if(!(StrUtils.isNotEmpty(userId)&&StrUtils.isNumeric(userId)&&StrUtils.isNotEmpty(id)&&StrUtils.isNumeric(id))){
			SysTools.setResultParamsErr(result);
			renderJSON(result);
		}
		Task task=Task.findById(Long.valueOf(id));
		if(task!=null){
			task.userId=Integer.valueOf(userId);
			if(StrUtils.isNotEmpty(name)){
				task.name=name;
			}
			if(StrUtils.isNotEmpty(image)){
				task.image=image;
			}
			if(StrUtils.isNotEmpty(des)){
				task.des=des;
			}
			if(StrUtils.isNotEmpty(taskGroupId)&&StrUtils.isNumeric(taskGroupId)){
				task.taskGroupId=Integer.valueOf(taskGroupId);
			}
			if(StrUtils.isNotEmpty(status)&&StrUtils.isNumeric(status)){
				task.status=Integer.valueOf(status);
			}
			task.save();
		}
		SysTools.setResultOpSec(result);
		result.put("task", task);
		renderJSON(result);
	}
	
	
	public static void get(String id){
		Map<String,Object> result=new HashMap<String,Object>();
		if(!(StrUtils.isNotEmpty(id)&&StrUtils.isNumeric(id))){
			SysTools.setResultParamsErr(result);
			renderJSON(result);
		}
		Task task=Task.findById(Long.valueOf(id));
		SysTools.setResultOpSec(result);
		result.put("task", task);
		renderJSON(result);
	}
	public static void updateStatus(String id,String status){
		Map<String,Object> result=new HashMap<String,Object>();
		if(!(StrUtils.isNotEmpty(id)&&StrUtils.isNumeric(id)&&StrUtils.isNotEmpty(status)&&StrUtils.isNumeric(status))){
			SysTools.setResultParamsErr(result);
			renderJSON(result);
		}
		Task task=Task.findById(Long.valueOf(id));
		task.status=Integer.valueOf(status);
		SysTools.setResultOpSec(result);
		result.put("task", task);
		renderJSON(result);
	}
	
	public static void list(String taskGroupId,String userId,String status,String page,String length){
		Map<String,Object> result=new HashMap<String,Object>();
		if(!(StrUtils.isNotEmpty(page)&&StrUtils.isNumeric(page)&&StrUtils.isNotEmpty(length)&&StrUtils.isNumeric(length))){
			SysTools.setResultParamsErr(result);
			renderJSON(result);
		}
		Long total=0L;
		List<Task> list=new ArrayList<Task>();
		String groupIds="0";
		if(StrUtils.isNotEmpty(taskGroupId)){
			groupIds=taskGroupId;
		}
		if(StrUtils.isNotEmpty(userId)){
			User user=User.findById(Long.valueOf(userId));
			if(StrUtils.isNotEmpty(user.taskGroupIds)&&user.taskGroupIds.length()>2){
				groupIds=user.taskGroupIds.substring(1, user.taskGroupIds.length()-1);
			}
		}
		Logger.info("groupIds:"+groupIds);
		if((StrUtils.isNotEmpty(userId)||StrUtils.isNotEmpty(taskGroupId))&&StrUtils.isNotEmpty(status)){
			list=Task.find("available=1 and status=? and taskGroupId in ("+groupIds+") order by id desc",Integer.valueOf(status)).fetch(Integer.valueOf(page), Integer.valueOf(length));
			if(list!=null&&list.size()>0){
				total=Task.count("available=1 and status=? and taskGroupId in ("+groupIds+")",Integer.valueOf(status));
			}
		}else if((StrUtils.isNotEmpty(userId)||StrUtils.isNotEmpty(taskGroupId))&&StrUtils.isEmpty(status)){
			list=Task.find("available=1 and taskGroupId in ("+groupIds+") order by id desc").fetch(Integer.valueOf(page), Integer.valueOf(length));
			if(list!=null&&list.size()>0){
				total=Task.count("available=1 and taskGroupId in ("+groupIds+")");
			}
		}else if(StrUtils.isEmpty(userId)&&StrUtils.isEmpty(taskGroupId)&&StrUtils.isNotEmpty(status)){
			list=Task.find("available=1 and status=?  order by id desc",Integer.valueOf(status)).fetch(Integer.valueOf(page), Integer.valueOf(length));
			if(list!=null&&list.size()>0){
				total=Task.count("available=1 and status=? ",Integer.valueOf(status));
			}
		}else{
			list=Task.find("available=1 order by id desc").fetch(Integer.valueOf(page), Integer.valueOf(length));
			if(list!=null&&list.size()>0){
				total=Task.count("available=1");
			}
		}
		
		result.put("list", list);
		result.put("page", page);
		result.put("length", length);
		result.put("total", total);
		result.put("status", status);
		result.put("userId", userId);
		
		SysTools.setResultOpSec(result);
		renderJSON(result);
	}
	
	public static void del(String id){
		Map<String,Object> result=new HashMap<String,Object>();
		if(!(StrUtils.isNotEmpty(id)&&StrUtils.isNumeric(id))){
			SysTools.setResultParamsErr(result);
			renderJSON(result);
		}
		Task task=Task.findById(Long.valueOf(id));
		task.available=2;
		task.save();
		SysTools.setResultOpSec(result);
		renderJSON(result);
	}
}
