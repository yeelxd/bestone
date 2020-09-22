package com.yglbs.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.client.methods.HttpHead;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.System.out;

/**
 *  企查查接口调用 https://openapi.qichacha.com/
 *  企业工商数据查询 Full
 *  Token		String	httpHeader头		验证加密值（key+Timespan+SecretKey组成的32位md5加密的大写字符串）
*   Timespan	String	httpHeader头		精确到秒的Unix时间戳
*   SecretKey	String	不传递			密钥，请妥善保管好
 * @author liuxd 20190809
 */
public class QccMainApp {

	private static final String API_URI="http://api.qichacha.com/ECIV4/GetFullDetailsByName";
	private static final String API_KEY="e91e6837fb3b40b5a7b0143a92b44afb";
	private static final String API_SECRET_KEY ="FE54E4F96D6DFE1750193D50290103FF";

	public static void main(String[] args) {
		// 请登录http://yjapi.com/DataCenter/MyData
		// 查看我的Key和秘钥 
		try {
			String paramStr = "keyword=江西南华(上药)医药有限公司";
			// auth header setting
			HttpHead reqHeader = new HttpHead();
			String[] autherHeader = randomAuthentHeader(API_KEY, API_SECRET_KEY );
			reqHeader.setHeader("Token", autherHeader[0]);
			reqHeader.setHeader("Timespan", autherHeader[1]);
			final String reqUri = API_URI.concat("?key=").concat(API_KEY).concat("&").concat(paramStr);
			String tokenJson = HttpHelper.httpGet(reqUri, reqHeader.getAllHeaders());
			out.printf("==========>this is response:{%s}%n", tokenJson);

			// parse status from json
			String status = formartJson(tokenJson, "Status");
			out.printf("==========>Status:{%s}%n", status);
			if (!HttpCodeRegex.isAbnormalRequest(status)) {
				prettyPrintJson(tokenJson);
				//转Map
				 JSONObject jsonObject = JSON.parseObject(tokenJson);
				 Map<?, ?> resultMap= QccMainApp.parseJson2Map(jsonObject);
				 out.println(resultMap.toString());
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * 获取返回码 Res Code
	 */
	static class HttpCodeRegex {
		private static final String ABNORMAL_REGIX = "(101)|(102)";

		public static boolean isAbnormalRequest(final String status) {
			return Pattern.compile(ABNORMAL_REGIX).matcher(status).matches();
		}
	}

	/**
	 * 获取Auth Code
	 */
	public static String[] randomAuthentHeader(String apikey, String secretkey) {
		String timeSpan = String.valueOf(System.currentTimeMillis() / 1000);
		return new String[] { DigestUtils.md5Hex(apikey.concat(timeSpan).concat(secretkey)).toUpperCase(), timeSpan };
	}

	public static String formartJson(String jsonString, String key) throws JSONException {
        JSONObject jsonObject = JSON.parseObject(jsonString);
		return (String) jsonObject.get(key);
	}

	public static void prettyPrintJson(String jsonString) throws JSONException {
		try {
			if(jsonString!=null && !"".equals(jsonString.trim())){
		        JSONObject jsonObject = JSONObject.parseObject(jsonString);
		        jsonString=JSONObject.toJSONString(jsonObject,true);
		    }
			out.println(jsonString);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 将json对象转换为HashMap
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Map<String, Object> parseJson2Map(JSONObject jsonObject) {
        Map<String, Object> map = new HashMap<>(16);
        // 最外层解析
        for (Object k : jsonObject.keySet()) {
            Object v = jsonObject.get(k.toString());
            String key=k.toString().toLowerCase();
            if("date".equals(key)) {
            	key="datee";
            }
            if("level".equals(key)) {
            	key="levell";
            }
            // 如果内层还是json数组的话，继续解析
            if (v instanceof JSONArray) {
            	Vector vector = new Vector();
                JSONArray jsonArray=(JSONArray)v;
				for (Object o : jsonArray) {
					JSONObject json2 = (JSONObject) o;
					vector.add(parseJson2Map(json2));
				}
                map.put(key, vector);
            } else if (v instanceof JSONObject) {
                // 如果内层是json对象的话，继续解析
                map.put(key, parseJson2Map((JSONObject) v));
            } else {
            	// 如果内层是普通对象的话，直接放入map中
            	String val=(String) v;
            	if(val !=null && !val.isEmpty() && getChineseSize(val) > 1300) {
            		val=val.substring(0, 1300)+"..";
            		map.put(key, val);
            	}else {
            		map.put(key, v);
            	}
            }
        }
        return map;
    }
	
	public static boolean isDateTime(String str) {
		return Pattern.matches("^[1-9]\\d{3}[-/]\\d{1,2}[-/]\\d{1,2}(\\s+\\d{1,2}[:]\\d{1,2}([:]\\d{1,2})?)?$", str);
	}
	
	public static Date parseDate(String source) {
		Date date = null;
		if (source!=null && !source.isEmpty()) {
			source=source.trim().toUpperCase().replaceAll("/", "-");
			//默认14位
			String format="yyyy-MM-dd HH:mm:ss";
			if(source.length()==20){
				//GMT格式
				source = source.replace("T", " ").replace("Z", "");
			}
			if(source.length()==10){
				format="yyyy-MM-dd";
			}
			SimpleDateFormat sf = new SimpleDateFormat(format);
			try {
				date = sf.parse(source);
			} catch (ParseException e) {
				out.println("日期转换失败：" + source+ "["+format+"]"+e);
			}
		}
		return date;
	}

	/**
	 * 获取内容中-汉字个数
	 */
	public static int getChineseSize(String content) {
		int count = 0;
		String regEx = "[\\u4e00-\\u9fa5]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(content);
		int len = m.groupCount();
		while (m.find()) {
			for (int i = 0; i <= len; i++) {  
		    	 count = count + 1;  
		     }  
		}
		return count;
	}
	
}
