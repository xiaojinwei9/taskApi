package controllers;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import models.Task;
import models.TaskGroup;
import play.mvc.With;
import utils.StrUtils;
import utils.SysTools;

@With(ApiSec.class)
public class Tasks extends BasicController {
	public static void add(String name,String image,String taskGroupId,String status,String userId){
		Map<String,Object> result=new HashMap<String,Object>();
		if(!(StrUtils.isNotEmpty(userId)&&StrUtils.isNumeric(userId)&&StrUtils.isNotEmpty(name)&&StrUtils.isNotEmpty(taskGroupId)&&StrUtils.isNumeric(taskGroupId)&&StrUtils.isNotEmpty(status)&&StrUtils.isNumeric(status))){
			SysTools.setResultParamsErr(result);
			renderJSON(result);
		}
		Task task=new Task(name, image, new Date(), new Date(), Integer.valueOf(status), Integer.valueOf(taskGroupId),Integer.valueOf(userId),1);
		task.save();
		SysTools.setResultOpSec(result);
		result.put("task", task);
		renderJSON(result);
	}
	public static void update(String id,String name,String image,String taskGroupId,String status,String userId){
		Map<String,Object> result=new HashMap<String,Object>();
		if(!(StrUtils.isNotEmpty(userId)&&StrUtils.isNumeric(userId)&&StrUtils.isNotEmpty(id)&&StrUtils.isNumeric(id))){
			SysTools.setResultParamsErr(result);
			renderJSON(result);
		}
		Task task=Task.findById(Long.valueOf(id));
		if(task!=null){
			if(StrUtils.isNotEmpty(name)){
				task.name=name;
			}
			if(StrUtils.isNotEmpty(image)){
				task.image=image;
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
}
