package controllers;

import java.util.Arrays;
import java.util.Map;

import org.h2.engine.Constants;

import play.Logger;
import play.mvc.Before;
import play.mvc.Controller;
import utils.Cons;
import utils.StrUtils;

public class ApiSec extends BasicController {

	@Before
	static void checkToken(){
		Logger.info("checkToken-start");
		Map<String, String> paramsMap = params.allSimple();
		Logger.info("checkToken-params:" + paramsMap);
		if(StrUtils.isEmpty(paramsMap.get(Cons.token_key))||StrUtils.isEmpty(paramsMap.get(Cons.sys_key))){
			tokenErr();
		}
		Object[] paramsKeys = paramsMap.keySet().toArray();
		Arrays.sort(paramsKeys);//升序
		String tokenPre = "taskApi";//预设token前缀,需保密
		for (int i = 0; i < paramsKeys.length; i++) {
			if(!("token".equals(paramsKeys[i])||"action".equals(paramsKeys[i])||"body".equals(paramsKeys[i])||"controller".equals(paramsKeys[i]))){
				Logger.info("checkToken-paramsKeys>" + paramsKeys[i] + ">"
						+ paramsMap.get(paramsKeys[i]));
				tokenPre += paramsMap.get(paramsKeys[i]);
			}
		}
		Logger.info("checkToken-tokenPre:" + tokenPre);
		String md5Token=StrUtils.md5(tokenPre);
		String tokenParam=paramsMap.get(Cons.token_key)+"";
		Logger.info("checkToken---md5Token:" + md5Token);
		Logger.info("checkToken-tokenParam:" + tokenParam);
		if(!md5Token.equals(tokenParam)){
			tokenErr();
		}
	}
}
