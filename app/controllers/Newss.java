package controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.News;
import models.NewsToUser;
import models.Task;
import models.User;
import play.Logger;
import play.mvc.With;
import utils.StrUtils;
import utils.SysTools;

@With(ApiSec.class)
public class Newss extends BasicController {
	public static void add(String title,String newsGroupId,String con,String userId){
		Map<String,Object> result=new HashMap<String,Object>();
		if(!(StrUtils.isNotEmpty(userId)&&StrUtils.isNumeric(userId)&&StrUtils.isNotEmpty(title)&&StrUtils.isNotEmpty(con)&&StrUtils.isNotEmpty(newsGroupId)&&StrUtils.isNumeric(newsGroupId))){
			SysTools.setResultParamsErr(result);
			renderJSON(result);
		}
		News news=new News(title,new Date(),new Date(),Integer.valueOf(newsGroupId),con,Integer.valueOf(userId),1);
		news.save();
		SysTools.setResultOpSec(result);
		result.put("news", news);
		renderJSON(result);
	}
	
	public static void update(String id,String title,String newsGroupId,String con,String userId){
		Map<String,Object> result=new HashMap<String,Object>();
		if(!(StrUtils.isNotEmpty(userId)&&StrUtils.isNumeric(userId)&&StrUtils.isNotEmpty(id)&&StrUtils.isNumeric(id))){
			SysTools.setResultParamsErr(result);
			renderJSON(result);
		}
		News news=News.findById(Long.valueOf(id));
		if(news!=null){
			news.userId=Integer.valueOf(userId);
			if(StrUtils.isNotEmpty(title)){
				news.title=title;
			}
			if(StrUtils.isNotEmpty(newsGroupId)){
				news.newsGroupId=Integer.valueOf(newsGroupId);
			}
			if(StrUtils.isNotEmpty(con)){
				news.con=con;
			}
			news.modifyTime=new Date();
			news.save();
		}
		SysTools.setResultOpSec(result);
		result.put("news", news);
		renderJSON(result);
	}
	
	public static void get(String id,String userId){
		Map<String,Object> result=new HashMap<String,Object>();
		if(!(StrUtils.isNotEmpty(id)&&StrUtils.isNumeric(id)&&StrUtils.isNotEmpty(userId)&&StrUtils.isNumeric(userId))){
			SysTools.setResultParamsErr(result);
			renderJSON(result);
		}
		News news=News.findById(Long.valueOf(id));
		List<NewsToUser> nuList=NewsToUser.find("newsId=? and userId=?",Integer.valueOf(id),Integer.valueOf(userId)).fetch();
		if(!(nuList!=null&&nuList.size()>0)){
			NewsToUser nu=new NewsToUser(news.newsGroupId,Integer.valueOf(id),Integer.valueOf(userId));
			nu.save();
		}
		SysTools.setResultOpSec(result);
		result.put("news", news);
		renderJSON(result);
	}
	
	public static void list(String userId,String groupId,String page,String length){
		Map<String,Object> result=new HashMap<String,Object>();
		if(!(StrUtils.isNotEmpty(page)&&StrUtils.isNumeric(page)&&StrUtils.isNotEmpty(length)&&StrUtils.isNumeric(length))){
			SysTools.setResultParamsErr(result);
			renderJSON(result);
		}
		Long total=0L;
		List<News> list=new ArrayList<News>();
		if(StrUtils.isEmpty(groupId)){
			list=News.find("available=1 and newsGroupId=?  order by id desc",Integer.valueOf(groupId)).fetch(Integer.valueOf(page), Integer.valueOf(length));
			if(list!=null&&list.size()>0){
				total=News.count("available=1 and newsGroupId=? ",Integer.valueOf(groupId));
			}
		}else{
			list=News.find("available=1 order by id desc").fetch(Integer.valueOf(page), Integer.valueOf(length));
			if(list!=null&&list.size()>0){
				total=News.count("available=1");
			}
		}
		for(News news:list){
			List<NewsToUser> nuList=NewsToUser.find("newsId=? and userId=?",Integer.valueOf(news.getId()+""),Integer.valueOf(userId)).fetch();
			if(nuList!=null&&nuList.size()>0){
				news.status=2;
			}else{
				news.status=1;
			}
		}
		result.put("list", list);
		result.put("page", page);
		result.put("length", length);
		result.put("total", total);
		result.put("groupId", groupId);
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
		News news=News.findById(Long.valueOf(id));
		news.available=2;
		news.save();
		SysTools.setResultOpSec(result);
		renderJSON(result);
	}
	
	
	public static void status(String userId){
		Map<String,Object> result=new HashMap<String,Object>();
		if(!(StrUtils.isNotEmpty(userId)&&StrUtils.isNumeric(userId))){
			SysTools.setResultParamsErr(result);
			renderJSON(result);
		}
		Long status1=News.count("available=1 and newsGroupId=1");
		Long status1a=NewsToUser.count("userId="+userId+" and newsGroupId=1");
		Long status2=News.count("available=1 and newsGroupId=2");
		Long status2a=NewsToUser.count("userId="+userId+" and newsGroupId=2");
		Long status3=News.count("available=1 and newsGroupId=3");
		Long status3a=NewsToUser.count("userId="+userId+"  and newsGroupId=3");
		Long status4=News.count("available=1 and newsGroupId=4");
		Long status4a=NewsToUser.count("userId="+userId+" and newsGroupId=4");
		Long status5=News.count("available=1 and newsGroupId=5");
		Long status5a=NewsToUser.count("userId="+userId+"  and newsGroupId=5");
		result.put("status1", status1-status1a);
		result.put("status2", status2-status2a);
		result.put("status3", status3-status3a);
		result.put("status4", status4-status4a);
		result.put("status5", status5-status5a);
		SysTools.setResultOpSec(result);
		renderJSON(result);
	}
}
