package controllers;

import java.util.HashMap;
import java.util.Map;

import play.mvc.Controller;
import utils.Cons;

public class BasicController extends Controller {

	public static void tokenErr(){
		Map<String,Object> result=new HashMap<String,Object>();
		result.put(Cons.op_code, Cons.token_err);
		result.put(Cons.op_msg, Cons.token_err_msg);
		renderJSON(result);
	}
	
}
