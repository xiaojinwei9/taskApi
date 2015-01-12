package controllers;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.activation.MimetypesFileTypeMap;

import play.Logger;
import play.mvc.Controller;
import utils.StrUtils;

public class BasicClientController extends Controller {
	
	public static Map<String,String> getUserInfo(){
		Map<String,String> userInfo=new HashMap<String,String>();
		if(session.get("userId")!=null){
			userInfo.put("userId", session.get("userId")+"");
		}
		if(session.get("userType")!=null){
			userInfo.put("userType", session.get("userType")+"");
		}
		if(session.get("userName")!=null){
			userInfo.put("userName", session.get("userName")+"");
		}
		if(session.get("userTaskGroupIds")!=null){
			userInfo.put("userTaskGroupIds", session.get("userTaskGroupIds")+"");
		}
		return userInfo;
	}
	
	public static String paramsConstruction(String method,Map<String, String> paramsMap){
		Logger.info("paramsMap"+paramsMap);
		paramsMap.put("sysId", "web");
		String privateKey="829c70fe3772013f22faa6620a411f3b";//需保密码,与sysId是一对值,一个sysId分配唯一privateKey
		paramsMap.put("privateKey", privateKey);//privateKey加入加密运算
		String[] paramsKeys = (String[])paramsMap.keySet().toArray(new String[0]);//参数字符串数组
		Arrays.sort(paramsKeys,String.CASE_INSENSITIVE_ORDER);//忽略大小写,升序
		String tokenPre = "";
		for (int i = 0; i < paramsKeys.length; i++) {
			    String value="";
				try {
					value = URLEncoder.encode(paramsMap.get(paramsKeys[i]),"utf-8");//encode
					paramsMap.put(paramsKeys[i]+"",value);
					tokenPre += paramsKeys[i]+"="+value+",";
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
		}
		tokenPre=tokenPre.substring(0, tokenPre.length()-1);
		Logger.info("checkToken-tokenPre:" + tokenPre);
		String md5Token=StrUtils.md5(tokenPre);
		//Logger.info("md5Token:"+md5Token);
		paramsMap.put("token", md5Token);
		paramsMap.remove("privateKey");//privateKey不参与参数传输
		Logger.info("paramsMapEnd:"+paramsMap);//最终post提交参数
		
		Map<String, String> fileMap = new HashMap<String, String>();
		
		//fileMap.put("userfile", filepath);
		String url="http://www.recb.com.cn"+method;
		//String url="http://localhost"+method;
		String ret = formUpload(url, paramsMap, fileMap);
		Logger.info("ret:"+ret);
		//JsonObject retObj=GsonUtils.parseJson(ret);
		//Logger.info("retObj:"+retObj);
		return ret;
	}
	
	/**
	 * txt,上传图片
	 * 
	 * @param urlStr
	 * @param textMap
	 * @param fileMap
	 * @return
	 */
	public static String formUpload(String urlStr, Map<String, String> textMap,
			Map<String, String> fileMap) {
		String res = "";
		HttpURLConnection conn = null;
		String BOUNDARY = "---------------------------123821742118716"; //boundary就是request头和上传文件内容的分隔符
		try {
			Logger.info("newUrl:"+urlStr);
			URL url = new URL(urlStr);
			Logger.info("newUrlEnd:"+urlStr);
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(10000);
			conn.setReadTimeout(30000);
			conn.setDoOutput(true);//向服务器写入数据
			conn.setDoInput(true);//从服务器获取数据
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("User-Agent",
							"Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
			conn.setRequestProperty("Content-Type",
					"multipart/form-data; boundary=" + BOUNDARY);
			Logger.info("DataOutputStreamConn");
			OutputStream out = new DataOutputStream(conn.getOutputStream());
			Logger.info("DataOutputStreamConnEnd");
			// text
			if (textMap != null) {
				StringBuffer strBuf = new StringBuffer();
				Iterator iter = textMap.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry entry = (Map.Entry) iter.next();
					String inputName = (String) entry.getKey();
					String inputValue = (String) entry.getValue();
					if (inputValue == null) {
						continue;
					}
					strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
					strBuf.append("Content-Disposition: form-data; name=\""
							+ inputName + "\"\r\n\r\n");
					strBuf.append(inputValue);
				}
				out.write(strBuf.toString().getBytes("utf-8"));
			}
			Logger.info("writeTxtDataEnd");
			// file
			if (fileMap != null) {
				Iterator iter = fileMap.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry entry = (Map.Entry) iter.next();
					String inputName = (String) entry.getKey();
					String inputValue = (String) entry.getValue();
					if (inputValue == null) {
						continue;
					}
					File file = new File(inputValue);
					String filename = file.getName();
					String contentType = new MimetypesFileTypeMap()
							.getContentType(file);
					if (filename.endsWith(".png")) {
						contentType = "image/png";
					}
					if (contentType == null || contentType.equals("")) {
						contentType = "application/octet-stream";
					}

					StringBuffer strBuf = new StringBuffer();
					strBuf.append("\r\n").append("--").append(BOUNDARY).append(
							"\r\n");
					strBuf.append("Content-Disposition: form-data; name=\""
							+ inputName + "\"; filename=\"" + filename
							+ "\"\r\n");
					strBuf.append("Content-Type:" + contentType + "\r\n\r\n");

					out.write(strBuf.toString().getBytes());

					DataInputStream in = new DataInputStream(
							new FileInputStream(file));
					int bytes = 0;
					byte[] bufferOut = new byte[1024];
					while ((bytes = in.read(bufferOut)) != -1) {
						out.write(bufferOut, 0, bytes);
					}
					in.close();
				}
			}
			Logger.info("writeFileDataEnd");
			byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
			out.write(endData);
			out.flush();
			out.close();

			// 读取返回数据
			StringBuffer strBuf = new StringBuffer();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				strBuf.append(line).append("\n");
			}
			res = strBuf.toString();
			reader.close();
			reader = null;
			Logger.info("returnDataEnd");
		} catch (Exception e) {
			System.out.println("发送POST请求出错。" + urlStr);
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.disconnect();
				conn = null;
			}
		}
		return res;
	}
}
