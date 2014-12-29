package utils;

import java.util.Map;

public class SysTools {
	
	public static void setResultParamsErr(Map<String,Object> result){
		result.put(Cons.op_code, Cons.params_err);
		result.put(Cons.op_msg, Cons.params_err_msg);
	}
	
	public static void setResultOpSec(Map<String,Object> result){
		result.put(Cons.op_code, Cons.op_suc);
		result.put(Cons.op_msg, Cons.op_suc_msg);
	}
}
