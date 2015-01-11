package controllers;

import java.util.Map;

import play.Logger;
import play.mvc.With;

@With(ClientSec.class)
public class UserAdmin extends BasicClientController {

	public static void index(){
		Map<String,String> userInfo=getUserInfo();
		Logger.info("userInfo:"+userInfo);
		render(userInfo);
	}
	
	public static void tasks(String status){
		render(status);
	}
}
