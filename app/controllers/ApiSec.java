package controllers;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import models.ApiLog;
import models.PrivateKey;
import play.Logger;
import play.mvc.Before;
import utils.Cons;
import utils.StrUtils;

public class ApiSec extends BasicController {

	@Before
	static void checkToken(){
		Logger.info("checkToken-start");
		Integer status=1;
		String errMsg="";
		Map<String, String> paramsMap = params.allSimple();
		Logger.info("checkToken-params1:" + paramsMap);
		try{
			String sysId=paramsMap.get(Cons.sys_key)+"";
			PrivateKey privateKey=PrivateKey.find("sysId=?",sysId).first();
			if(StrUtils.isEmpty(privateKey)||StrUtils.isEmpty(paramsMap.get(Cons.token_key))){
				status=2;
			}
			paramsMap.put("privateKey", privateKey.privateKey);//privateKey加入加密参数
			Object[] paramsKeys = paramsMap.keySet().toArray();
			Arrays.sort(paramsKeys);//升序
			String tokenPre = "";
			for (int i = 0; i < paramsKeys.length; i++) {
				if(!("token".equals(paramsKeys[i])||"action".equals(paramsKeys[i])||"body".equals(paramsKeys[i])||"controller".equals(paramsKeys[i]))){
					Logger.info("checkToken-paramsKeys>" + paramsKeys[i] + ">"
							+ paramsMap.get(paramsKeys[i]));
					 String value=paramsMap.get(paramsKeys[i]);
					 tokenPre +=value+",";
					 params.data.put(paramsKeys[i]+"",new String[]{StrUtils.unescape(value)});//unescape参数值
				}
			}
			tokenPre=tokenPre.substring(0, tokenPre.length()-1);
			Logger.info("checkToken-tokenPre:" + tokenPre);
			String md5Token=StrUtils.md5(tokenPre);
			String tokenParam=paramsMap.get(Cons.token_key)+"";
			Logger.info("checkToken---md5Token:" + md5Token);
			Logger.info("checkToken-tokenParam:" + tokenParam);
			if(!md5Token.equals(tokenParam)){
				status=2;
			}
		}catch(Exception e){
			status=2;
			errMsg="func-checkToken-error:"+e.getMessage()+"-"+Arrays.deepToString(e.getStackTrace());
			Logger.error(errMsg);
		}
		Map<String, String> paramsMap2 = params.allSimple();
		Logger.info("checkToken-params2:" + paramsMap2);
		ApiLog apiLog=new ApiLog(paramsMap.get(Cons.sys_key)+"",status,new Date(),paramsMap.get(Cons.token_key)+"",paramsMap2.toString());
		apiLog.save();
		if(status!=1){
			tokenErr();
		}
	}
}
