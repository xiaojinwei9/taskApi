package controllers;

import java.util.HashMap;
import java.util.Map;

import play.mvc.Before;
import utils.Cons;
import utils.StrUtils;

public class ClientSec extends BasicClientController {

	@Before
	static void checkLogin(){
		Map<String,String> userInfo=getUserInfo();
		if(StrUtils.isEmpty(userInfo.get("userId"))){
			Map<String,Object> result=new HashMap<String,Object>();
			result.put(Cons.op_code, "-1");
			result.put(Cons.op_msg, "请先登录");
			renderJSON(result);
		}
	}
}
