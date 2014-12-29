package utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.lang.StringUtils;

import play.Logger;

public class StrUtils extends StringUtils{

	final static char[] digits = {
		'0' , '1' , '2' , '3' , '4' , '5' ,	'6' , '7' ,
		'8' , '9' , 'a' , 'b' , 'c' , 'd' , 'e' , 'f' 
	};
	/**
	 * 计算字符串的md5的摘要信息
	 * @param s 源字符串
	 * @return 32字节的16进制的字符串
	 */
	public static String md5(String s) {
		return md5(s, null );
	}
	
	/**
	 * 计算字符串的md5的摘要信息
	 * @param data 源字符串
	 * @param key salt字符串，
	 * @return 32字节的16进制的字符串
	 */
	public static String md5(String data, String key) {
		return doHash( data, key, "MD5" );
    }
	
	/**
	 * 计算字符串的摘要信息
	 * @param data 源字符串
	 * @param key salt字符串，
	 * @param digestName 摘要算法名称，可以是MD5，SHA1等
	 * @return 32字节的16进制的字符串
	 */
	public static String doHash( String data, String key, String digestName ) {
		String ret = "";
		if( isEmpty(data) ) return ret;
		
		try {
			MessageDigest mgd = MessageDigest.getInstance( digestName );
			mgd.update(data.getBytes());
			byte[] bytes = null;
			if( isEmpty(key) ){
				bytes = mgd.digest();
			}
			else{
				bytes = mgd.digest(key.getBytes());
			}
			mgd.reset();
			ret = toHex( bytes,0, bytes.length );
		} catch (NoSuchAlgorithmException e) {
			Logger.error("hash error:" + e);
		}
		return ret;
	}
	
	
	/**
	 * byte数组的部分字节转化为16进制的String
	 * @param byteData 待转换的byte数组
	 * @param offset 开始位置
	 * @param len 字节数
	 * @return 16进制的String
	 */
	public static  String toHex(byte byteData[], int offset, int len) {
		char buf[] = new char[len*2];
		int k=0;
		for (int i= offset; i < len; i++ ) {
		    buf[k++] = digits[((int) byteData[i] & 0xff)>>4 ];
		    buf[k++] = digits[((int) byteData[i] & 0xff)%16];
		}
		return new String(buf);
	}
	
	/**
	 * 判断一个字符串是否为null或是空字符串<p>
	 *
	 * @param str The string for checking
	 * @return true if the string is neither null nor empty string
	 */
	public static boolean isEmpty(String str) {
		return str == null || (str.trim().length() == 0);
	}
	
	public static boolean isEmpty(Object obj) {
		return obj == null || isEmpty(obj.toString());
	}
	
	public static void main(String[] args){
		//System.out.println(md5("aaaaaaa"));
	}
}
