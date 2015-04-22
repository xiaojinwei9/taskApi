package controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.News;
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
	
	public static void get(String id){
		Map<String,Object> result=new HashMap<String,Object>();
		if(!(StrUtils.isNotEmpty(id)&&StrUtils.isNumeric(id))){
			SysTools.setResultParamsErr(result);
			renderJSON(result);
		}
		News news=News.findById(Long.valueOf(id));
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
		int i=0;
		for(News news:list){
			i++;
			if(i<3){
				news.status=1;
			}else{
				news.status=2;
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
	
}
