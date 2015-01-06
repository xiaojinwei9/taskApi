package controllers;

import java.net.URLDecoder;
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
			PrivateKey privateKey=PrivateKey.find("sysId=?",sysId).first();//取得privateKey
			//if(StrUtils.isEmpty(privateKey)||StrUtils.isEmpty(paramsMap.get(Cons.token_key))){
				//status=2;
			//}
			paramsMap.put("privateKey", privateKey.privateKey);//privateKey加入加密运算
			String[] paramsKeys = (String[])paramsMap.keySet().toArray(new String[0]);//参数字符串数组
			Arrays.sort(paramsKeys,String.CASE_INSENSITIVE_ORDER);//忽略大小写,升序
			String tokenPre = "";
			for (int i = 0; i < paramsKeys.length; i++) {
				if(!("token".equals(paramsKeys[i])||"action".equals(paramsKeys[i])||"body".equals(paramsKeys[i])||"controller".equals(paramsKeys[i]))){
					Logger.info("checkToken-paramsKeys>" + paramsKeys[i] + ">"
							+ paramsMap.get(paramsKeys[i]));
					 String value=paramsMap.get(paramsKeys[i]);
					 tokenPre +=paramsKeys[i]+"="+value+",";
					 if(!"privateKey".equals(paramsKeys[i])){
						 params.data.put(paramsKeys[i]+"",new String[]{URLDecoder.decode(value,"utf-8")});//decode参数值
					 }
				}
			}
			tokenPre=tokenPre.substring(0, tokenPre.length()-1);
			Logger.info("checkToken-tokenPre:" + tokenPre);
			String md5Token=StrUtils.md5(tokenPre);
			String tokenParam=paramsMap.get(Cons.token_key)+"";
			if(StrUtils.isEmpty(paramsMap.get(Cons.token_key))){
				status=1;
			}else{
				Logger.info("checkToken---md5Token:" + md5Token);
				Logger.info("checkToken-tokenParam:" + tokenParam);
				if(!md5Token.equals(tokenParam)){
					status=2;
				}
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
