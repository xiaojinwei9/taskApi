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
public class Users extends BasicController {
	
	public static void login(String mobile,String password){
		Map<String,Object> result=new HashMap<String,Object>();
		if(!(StrUtils.isNotEmpty(mobile)&&StrUtils.isNotEmpty(password))){
			SysTools.setResultParamsErr(result);
			renderJSON(result);
		}
		password=StrUtils.md5(password);
		User user=User.find("mobile=? and password=? ", mobile,password).first();
		if(user!=null){
			SysTools.setResultOpSec(result);
			result.put("user", user);
		}else{
			SysTools.setLoginErr(result);
		}
		renderJSON(result);
	}

	public static void add(String mobile, String password, String name, String email, String phone, String image, String type, String taskGroupIds, String des){
		Map<String,Object> result=new HashMap<String,Object>();
		if(!(StrUtils.isNotEmpty(mobile)&&StrUtils.isNotEmpty(password)&&StrUtils.isNotEmpty(type))){
			SysTools.setResultParamsErr(result);
			renderJSON(result);
		}
		User userTemp=User.find("mobile=?", mobile).first();
		if(userTemp!=null){
			SysTools.setUserAddErr(result);
			renderJSON(result);
		}
		password=StrUtils.md5(password);
		User user=new User(mobile, password, name, email, phone, image, Integer.valueOf(type), taskGroupIds, des, 0, new Date(), 1);
		user.save();
		SysTools.setResultOpSec(result);
		result.put("user", user);
		renderJSON(result);
	}
	public static void update(String mobile, String password, String name, String email, String phone, String image, String type, String taskGroupIds, String des){
		Map<String,Object> result=new HashMap<String,Object>();
		if(!(StrUtils.isNotEmpty(mobile))){
			SysTools.setResultParamsErr(result);
			renderJSON(result);
		}
		User user=User.find("mobile=?", mobile).first();
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
		List<User> users=new ArrayList<User>();
		if("0".equals(id)){
			users=User.find("available=1 order by id desc").fetch();
		}else{
			User u=User.findById(Long.valueOf(id));
			users.add(u);
		}
		SysTools.setResultOpSec(result);
		result.put("users", users);
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
