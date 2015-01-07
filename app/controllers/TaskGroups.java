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

	public static void add(String name,String userId){
		Map<String,Object> result=new HashMap<String,Object>();
		if(!(StrUtils.isNotEmpty(userId)&&StrUtils.isNumeric(userId)&&StrUtils.isNotEmpty(name))){
			SysTools.setResultParamsErr(result);
			renderJSON(result);
		}
		TaskGroup tg=new TaskGroup(name,new Date(),Integer.valueOf(userId), 1);
    	tg.save();
		SysTools.setResultOpSec(result);
		result.put("TaskGroup", tg);
		renderJSON(result);
	}
	public static void update(String id,String name,String userId){
		Map<String,Object> result=new HashMap<String,Object>();
		if(!(StrUtils.isNotEmpty(id)&&StrUtils.isNumeric(id)&&StrUtils.isNotEmpty(userId)&&StrUtils.isNumeric(userId)&&StrUtils.isNotEmpty(name)&&StrUtils.isNotEmpty(name))){
			SysTools.setResultParamsErr(result);
			renderJSON(result);
		}
		TaskGroup tg=TaskGroup.findById(Long.valueOf(id));
		tg.name=name;
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
	
	public static void list(String page,String length){
		Map<String,Object> result=new HashMap<String,Object>();
		if(!(StrUtils.isNotEmpty(page)&&StrUtils.isNumeric(page)&&StrUtils.isNotEmpty(length)&&StrUtils.isNumeric(length))){
			SysTools.setResultParamsErr(result);
			renderJSON(result);
		}
		Long total=0L;
		List list=TaskGroup.find("available=1 order by id desc").fetch(Integer.valueOf(page), Integer.valueOf(length));
			if(list!=null&&list.size()>0){
				total=TaskGroup.count("available=1");
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
