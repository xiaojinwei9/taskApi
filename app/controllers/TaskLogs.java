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
	
	public static void list(String comments,String taskId,String page,String length){
		Map<String,Object> result=new HashMap<String,Object>();
		if(!(StrUtils.isNotEmpty(page)&&StrUtils.isNumeric(page)&&StrUtils.isNotEmpty(length)&&StrUtils.isNumeric(length)&&StrUtils.isNotEmpty(taskId)&&StrUtils.isNumeric(taskId))){
			SysTools.setResultParamsErr(result);
			renderJSON(result);
		}
		Long total=0L;
		String commentUserIds="0";
		String needComments="N";
		if(StrUtils.isEmpty(comments)||"0".equals(comments)){
			needComments="Y";
		}
		if("Y".equals(needComments)){
			List userList=new ArrayList<User>();
			userList=User.find("available=1 and type=3").fetch(1, 10000);
			if(userList!=null&&userList.size()>0){
				for(int i=0;i<userList.size();i++){
					User uTemp=(User)userList.get(i);
					commentUserIds+=","+uTemp.getId();
				}
			}
		}
		List<TaskLog> list=TaskLog.find("available=1 and taskId=? and userId not in ("+commentUserIds+") order by id desc", Integer.valueOf(taskId)).fetch(Integer.valueOf(page), Integer.valueOf(length));
		if(list!=null&&list.size()>0){
			total=TaskLog.count("available=1 and taskId=? and userId not in ("+commentUserIds+")",Integer.valueOf(taskId));
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
	
	
	public static void listFiles(String taskId){
		Map<String,Object> result=new HashMap<String,Object>();
		if(!(StrUtils.isNotEmpty(taskId)&&StrUtils.isNumeric(taskId))){
			SysTools.setResultParamsErr(result);
			renderJSON(result);
		}
		List<HashMap<String,Object>> list=new ArrayList<HashMap<String,Object>>();
		List<TaskLog> taskLogs=TaskLog.find("available=1 and taskId=? order by id desc", Integer.valueOf(taskId)).fetch();
		if(taskLogs!=null&&taskLogs.size()>0){
			Logger.info("taskLogs.size:"+taskLogs.size());
			for(TaskLog tl:taskLogs){
				if(StrUtils.isNotEmpty(tl.files)){
					Logger.info("tl.files:"+tl.files);
					String[] fileArr=tl.files.split("\\|");
					for(int i=0;i<fileArr.length;i++){
						String file=fileArr[i];
						Logger.info("tl.file:"+file);
						if(StrUtils.isNotEmpty(file)&&file.indexOf("-")!=-1){
							String[] fileOneArr=file.split("-");
							//Logger.info("fileOneArr:"+fileOneArr);
							if(fileOneArr.length==2){
								HashMap<String,Object> fileMap=new HashMap<String,Object>();
								fileMap.put("name", fileOneArr[0]);
								fileMap.put("url", fileOneArr[1]);
								fileMap.put("time", tl.time);
								fileMap.put("userId", tl.userId);
								list.add(fileMap);
							}
						}
					}
				}
			}
		}
		result.put("list", list);
		SysTools.setResultOpSec(result);
		renderJSON(result);
	}
}
