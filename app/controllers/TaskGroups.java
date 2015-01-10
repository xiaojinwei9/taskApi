package controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.TaskGroup;
import models.User;
import play.mvc.With;
import utils.StrUtils;
import utils.SysTools;

@With(ApiSec.class)
public class TaskGroups extends BasicController {

	public static void add(String name,String image,String userId){
		Map<String,Object> result=new HashMap<String,Object>();
		if(!(StrUtils.isNotEmpty(userId)&&StrUtils.isNumeric(userId)&&StrUtils.isNotEmpty(name))){
			SysTools.setResultParamsErr(result);
			renderJSON(result);
		}
		TaskGroup tg=new TaskGroup(name,image,new Date(),Integer.valueOf(userId), 1);
    	tg.save();
		SysTools.setResultOpSec(result);
		result.put("TaskGroup", tg);
		renderJSON(result);
	}
	public static void update(String id,String name,String image,String userId){
		Map<String,Object> result=new HashMap<String,Object>();
		if(!(StrUtils.isNotEmpty(id)&&StrUtils.isNumeric(id)&&StrUtils.isNotEmpty(userId)&&StrUtils.isNumeric(userId))){
			SysTools.setResultParamsErr(result);
			renderJSON(result);
		}
		TaskGroup tg=TaskGroup.findById(Long.valueOf(id));
		if(StrUtils.isNotEmpty(name)){
			tg.name=name;
		}
		if(StrUtils.isNotEmpty(image)){
			tg.image=image;
		}
		tg.time=new Date();
		tg.userId=Integer.valueOf(userId);
		tg.save();
		SysTools.setResultOpSec(result);
		result.put("TaskGroup", tg);
		renderJSON(result);
	}
	public static void get(String id){
		Map<String,Object> result=new HashMap<String,Object>();
		if(!(StrUtils.isNotEmpty(id)&&StrUtils.isNumeric(id))){
			SysTools.setResultParamsErr(result);
			renderJSON(result);
		}
		TaskGroup tg=TaskGroup.findById(Long.valueOf(id));
		SysTools.setResultOpSec(result);
		result.put("taskGroup", tg);
		renderJSON(result);
	}
	
	public static void list(String userId,String page,String length){
		Map<String,Object> result=new HashMap<String,Object>();
		if(!(StrUtils.isNotEmpty(page)&&StrUtils.isNumeric(page)&&StrUtils.isNotEmpty(length)&&StrUtils.isNumeric(length))){
			SysTools.setResultParamsErr(result);
			renderJSON(result);
		}
		Long total=0L;
		List<TaskGroup> list=new ArrayList<TaskGroup>();
		if(StrUtils.isEmpty(userId)){
			list=TaskGroup.find("available=1 order by id desc").fetch(Integer.valueOf(page), Integer.valueOf(length));
				if(list!=null&&list.size()>0){
					total=TaskGroup.count("available=1");
				}
		}else{
			String groupIds="0";
			User user=User.findById(Long.valueOf(userId));
			if(StrUtils.isNotEmpty(user.taskGroupIds)&&user.taskGroupIds.length()>2){
				groupIds=user.taskGroupIds.substring(1, user.taskGroupIds.length()-1);
			}
			list=TaskGroup.find("available=1 and id in ("+groupIds+") order by id desc").fetch(Integer.valueOf(page), Integer.valueOf(length));
			if(list!=null&&list.size()>0){
				total=TaskGroup.count("available=1 and id in ("+groupIds+")");
			}
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
		TaskGroup tg=TaskGroup.findById(Long.valueOf(id));
		tg.available=2;
		tg.save();
		SysTools.setResultOpSec(result);
		renderJSON(result);
	}
	
	
}
