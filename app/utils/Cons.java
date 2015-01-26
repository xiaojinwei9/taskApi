package utils;

import play.Play;

public class Cons {

	public static final String client_url=Play.configuration.getProperty("client.url");
	public static final String api_url=Play.configuration.getProperty("api.url");
	//public static final String www_url="http://localhost:9000";
	public static final String token_key="token";
	public static final String sys_key="sysId";
	public static final String op_code="code";
	public static final String op_msg="msg";
	
	public static final String token_err="000";
	public static final String token_err_msg="验证码错误";
	
	public static final String op_suc="100";
	public static final String op_suc_msg="操作成功";
	
	public static final String params_err="200";
	public static final String params_err_msg="参数错误";
	
	public static final String login_err="201";
	public static final String login_err_msg="帐号或密码有误码";
	
	public static final String user_add_err="202";
	public static final String user_add_err_msg="帐号已存在,添加失败";
}
