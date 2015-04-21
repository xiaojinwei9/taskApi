package controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.User;
import play.Logger;
import play.mvc.With;
import utils.StrUtils;
import utils.SysTools;

@With(ApiSec.class)
public class Users extends BasicController {
	
	public static void login(String mobile,String password){
		Map<String,Object> result=new HashMap<String,Object>();
		if(!(StrUtils.isNotEmpty(mobile)&&StrUtils.isNotEmpty(password))){
			SysTools.setResultParamsErr(result);
			renderJSON(result);
		}
		password=StrUtils.md5(password);
		User user=User.find("mobile=? and password=? and available=1", mobile,password).first();
		if(user!=null){
			SysTools.setResultOpSec(result);
			result.put("user", user);
		}else{
			SysTools.setLoginErr(result);
		}
		renderJSON(result);
	}

	public static void add(String mobile, String password, String name, String email, String phone, String image, String type, String taskGroupIds, String des,String isComment){
		Map<String,Object> result=new HashMap<String,Object>();
		if(!(StrUtils.isNotEmpty(mobile)&&StrUtils.isNotEmpty(password)&&StrUtils.isNotEmpty(type))){
			SysTools.setResultParamsErr(result);
			renderJSON(result);
		}
		User userTemp=User.find("mobile=? and available=1", mobile).first();
		if(userTemp!=null){
			SysTools.setUserAddErr(result);
			renderJSON(result);
		}
		password=StrUtils.md5(password);
		User user=new User(mobile, password, name, email, phone, image, Integer.valueOf(type), taskGroupIds, des, 0, new Date(),Integer.valueOf(isComment),1);
		user.save();
		SysTools.setResultOpSec(result);
		result.put("user", user);
		renderJSON(result);
	}
	public static void update(String mobile, String password, String name, String email, String phone, String image, String type, String taskGroupIds, String des,String isComment){
		Map<String,Object> result=new HashMap<String,Object>();
		if(!(StrUtils.isNotEmpty(mobile))){
			SysTools.setResultParamsErr(result);
			renderJSON(result);
		}
		User user=User.find("mobile=? and available=1", mobile).first();
		if(user!=null){
			if(StrUtils.isNotEmpty(password)){
				password=StrUtils.md5(password);
				user.password=password;
			}
			if(StrUtils.isNotEmpty(name)){
				user.name=name;
			}
			if(StrUtils.isNotEmpty(email)){
				user.email=email;
			}
			if(StrUtils.isNotEmpty(phone)){
				user.phone=phone;
			}
			if(StrUtils.isNotEmpty(image)){
				user.image=image;
			}
			if(StrUtils.isNotEmpty(type)){
				user.type=Integer.valueOf(type);
			}
			if(StrUtils.isNotEmpty(taskGroupIds)){
				user.taskGroupIds=taskGroupIds;
			}
			if(StrUtils.isNotEmpty(des)){
				user.des=des;
			}
			if(StrUtils.isNotEmpty(isComment)){
				user.isComment=Integer.valueOf(isComment);
			}
			user.save();
			result.put("user", user);
		}
		SysTools.setResultOpSec(result);
		renderJSON(result);
	}
	
	public static void get(String id){
		Map<String,Object> result=new HashMap<String,Object>();
		if(!(StrUtils.isNotEmpty(id)&&StrUtils.isNumeric(id))){
			SysTools.setResultParamsErr(result);
			renderJSON(result);
		}
		User u=User.findById(Long.valueOf(id));
		SysTools.setResultOpSec(result);
		result.put("user", u);
		renderJSON(result);
	}
	
	public static void list(String userId,String groupId,String page,String length){
		Map<String,Object> result=new HashMap<String,Object>();
		if(!(StrUtils.isNotEmpty(page)&&StrUtils.isNumeric(page)&&StrUtils.isNotEmpty(length)&&StrUtils.isNumeric(length))){
			SysTools.setResultParamsErr(result);
			renderJSON(result);
		}
		Long total=0L;
		List list=new ArrayList<User>();
		if(StrUtils.isNotEmpty(userId)){
			User u=User.findById(Long.valueOf(userId));
			String groupIds=u.taskGroupIds;
			if(StrUtils.isNotEmpty(groupIds)&groupIds.indexOf(",")!=-1){
				String sqlStr="";
				String[] groupIdsArr=groupIds.split(",");
				boolean strAnd=true;
				if(groupIdsArr.length>0){
					for(int i=0;i<groupIdsArr.length;i++){
						if(StrUtils.isNotEmpty(groupIdsArr[i])&&StrUtils.isNumeric(groupIdsArr[i])){
							if(strAnd){
								sqlStr+=" and taskGroupIds like '%,"+groupIdsArr[i]+",%' ";
								strAnd=false;
							}else{
								sqlStr+=" or taskGroupIds like '%,"+groupIdsArr[i]+",%' ";
							}
						}
					}
					String sql="available=1 "+sqlStr+"order by id asc";
					String sql2="available=1 "+sqlStr;
					Logger.info("sql:"+sql);
					Logger.info("sql2:"+sql2);
					list=User.find(sql).fetch(Integer.valueOf(page), Integer.valueOf(length));
					if(list!=null&&list.size()>0){
						total=User.count(sql2);
					}
				}
				
			}
		}else if(StrUtils.isEmpty(groupId)){
			list=User.find("available=1 order by id asc").fetch(Integer.valueOf(page), Integer.valueOf(length));
			if(list!=null&&list.size()>0){
				total=User.count("available=1");
			}
		}else{
			groupId=","+groupId+",";
			list=User.find("available=1 and taskGroupIds like ? order by id asc","%"+groupId+"%").fetch(Integer.valueOf(page), Integer.valueOf(length));
			if(list!=null&&list.size()>0){
				total=User.count("available=1 and taskGroupIds like ?","%"+groupId+"%");
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
		User u=User.findById(Long.valueOf(id));
		u.available=2;
		u.save();
		SysTools.setResultOpSec(result);
		renderJSON(result);
	}
}
