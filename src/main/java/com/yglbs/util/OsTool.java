package com.yglbs.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yglbs.common.OsCommon;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.Blob;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 通用工具类
 * @author yeelxd
 * @date 2018-03-02
 */
public class OsTool {

	private static final Logger log = LogManager.getLogger(OsTool.class);

    /**
     * 连续多个空格的正则预编译
     */
   private static final Pattern SS_PATTERN=Pattern.compile("\\s+");

	public static boolean isNull(List<?> list) {
		boolean flag = true;
		if (null != list && list.size() > 0) {
			flag = false;
		}
		return flag;
	}
	
	public static boolean isNull(Object[] array) {
		boolean flag = true;
		if (null != array && array.length > 0) {
			flag = false;
		}
		return flag;
	}

	public static boolean isNull(Map<?, ?> map) {
		boolean flag = true;
		if (null != map && !map.isEmpty()) {
			flag = false;
		}
		return flag;
	}

	public static boolean isNull(String str) {
		return str == null || str.trim().isEmpty();
	}
	
	public static int getLength(Map<?, ?> map){
		int len=0;
		if(!isNull(map)){
			len=map.size();
		}
		return len;
	}
	
	public static int getLength(List<?> lis){
		int len=0;
		if(!isNull(lis)){
			len=lis.size();
		}
		return len;
	}
	
	public static int getLength(String str){
		int len=0;
		if(!isNull(str)){
			len=str.length();
		}
		return len;
	}
	
	public static int getInt(Object obj){
		int ret=0;
		try{
			if(null!=obj){
				if(obj instanceof String){
					ret=Integer.parseInt((String)obj);
				}
				if(obj instanceof Integer){
					ret=(int)obj;
				}
				if(obj instanceof Long){
					ret=((Long)obj).intValue();
				}
				if(obj instanceof BigDecimal){
					ret=((BigDecimal)obj).intValue();
				}
			}
		}catch(Exception e){
			log.error("INT转换失败：[" + obj+"]", e);
		}
		return ret;
	}
	
	public static String getStr(Object obj){
		String rot="";
		try{
			if(null!=obj) {
				if(obj instanceof Blob) {
					Blob blob=(Blob)obj;
					ByteArrayInputStream byteIs=(ByteArrayInputStream)blob.getBinaryStream();  
					byte[] byteData=new byte[byteIs.available()];
					byteIs.read(byteData, 0, byteData.length);
					rot=new String(byteData);
				}else {
					rot=obj.toString();
				}
			}
		}catch(Exception e){
			log.error("String转换失败：[" + obj+"]", e);
		}
		return rot;
	}
	
	public static String getParameter(HttpServletRequest request, String param, String defaultValue){
		String value=defaultValue;
		if(!isNull(param)){
			value=request.getParameter(param);
			if(!isNull(value)){
				value=value.trim();
			}
		}
		return value;
	}

	public static boolean equals(String str1, String str2){
		return !isNull(str1)&&!isNull(str2)&&str1.equals(str2);
	}

	/**
	 * 日期转换
	 */
	public static Date parseDate(String source, String format) {
		Date date = null;
		if (null != source) {
			SimpleDateFormat sf = new SimpleDateFormat(format);
			try {
				date = sf.parse(source);
			} catch (ParseException e) {
				log.error("日期转换失败：" + source+ "["+format+"]", e);
			}
		}

		return date;
	}

	/**
	 * 日期格式转换
	 */
	public static String formatDate(Date source, String format) {
		String str = null;
		if (null != source) {
			SimpleDateFormat sf = new SimpleDateFormat(format);
			str = sf.format(source);
		}

		return str;
	}
	
	/**
	 * 指定日期格式转换
	 */
	public static String formatGmtDate(Date source, String dateFormatSign) {
		String str = null;
		if (null != source) {
			if(OsTool.equals(OsCommon.Status.D, dateFormatSign)){
				str = formatDate(source, "yyyyMMdd");
			}else if(OsTool.equals(OsCommon.Status.GMDT, dateFormatSign)){
				str = formatDate(source, "yyyy-MM-dd HH:mm:ss");
				str=str.replace(' ', 'T');
				str=str+"Z";
			}else{
				str = formatDate(source, "yyyyMMddHHmmss");
			}
		}

		return str;
	}
	
	/**
	 * XML文档时间格式的转换
	 */
	public static Date parseXmlDate(String source) {
		Date date = null;
		if (!isNull(source)) {
			//默认14位
			String format="yyyyMMddHHmmss";
			if(getLength(source)==20){
				//GMT格式
				source = source.replace("T", " ").replace("Z", "");
				format="yyyy-MM-dd HH:mm:ss";
			}
			if(getLength(source)==12){
				format="yyyyMMddHHmm";
			}
			if(getLength(source)==8){
				format="yyyyMMdd";
			}
			
			date = parseDate(source, format);
		}

		return date;
	}

	/**
	 * byte[] --> InputStream
	 */
	public static InputStream byte2InputStream(byte[] buf) {
		return new ByteArrayInputStream(buf);
	}

	/**
	 * InputStream --> byte[]
	 */
	public static byte[] inputStream2Byte(InputStream inStream) {
		ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
		int rc, len=100;
		byte[] buff = new byte[len], in2b = null;
		try {
			while ((rc = inStream.read(buff, 0, len)) > 0) {
				swapStream.write(buff, 0, rc);
			}
			in2b = swapStream.toByteArray();
		} catch (IOException e) {
			log.error("InputStream转换失败", e);
		}

		return in2b;
	}

	/***
	 * EncodeBase64
	 */
	public static String encodeBase64(String input) {
		String result = "";
		if (!isNull(input)) {
			result = (new sun.misc.BASE64Encoder()).encode(input.getBytes(StandardCharsets.UTF_8));
		}

		return result;
	}

	/***
	 * DecodeBase64
	 */
	public static String decodeBase64(String input) {
		String result = "";
		if (!isNull(input)) {
			try {
				sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
				byte[] b = decoder.decodeBuffer(input);
				result = new String(b, StandardCharsets.UTF_8);
			} catch (IOException e) {
				log.error("DecodeBase64转换失败", e);
			}
		}

		return result;
	}

	/**
	 * 生成UUID
	 */
	public static String getUuid() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString();
	}

	/**
	 * 字段格式转换
	 */
	public static String toInRowName(String voName) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < voName.length(); i++) {
			char cur = voName.charAt(i);
			if (Character.isUpperCase(cur)) {
				sb.append("_");
			}
			sb.append(cur);
		}

		return sb.toString().toUpperCase();
	}

	public static boolean isDate(String str) {
		return Pattern.matches("^\\d{8}(\\d{6})?$", str);
	}
	
	public static boolean isTime(String str) {
		return Pattern.matches("^(20|21|22|23|[0-1]\\d):[0-5]\\d:[0-5]\\d$", str);
	}

	public static boolean isNumber(String str) {
		return Pattern.matches("^(-?\\d+)(\\.\\d+)?$", str);
	}

	public static boolean isEmail(String str) {
		return Pattern.matches("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*", str);
	}
	
	public static boolean isDateTime(String str) {
		return Pattern.matches("^[1-9]\\d{3}[-/]\\d{1,2}[-/]\\d{1,2}(\\s+\\d{1,2}[:]\\d{1,2}([:]\\d{1,2})?)?$", str);
	}
	
	/**
	 * 替换连续多个空格为一个
	 */
	public static String leaveBlankOne(String str){
		Matcher matcher=SS_PATTERN.matcher(str);
		return matcher.replaceAll(" ");
	}
	
	/*** 
     * MD5加密 生成32位md5码
     */
    public static String md5Encode(String inStr) {
    	String result = "";
        try {
        	MessageDigest md5 = MessageDigest.getInstance("MD5");            
            byte[] byteArray = inStr.getBytes(StandardCharsets.UTF_8);
            byte[] md5Bytes = md5.digest(byteArray);
            StringBuilder hexValue = new StringBuilder();
            for (byte bt : md5Bytes) {
                int val = ((int) bt) & 0xff;
                if (val < 16) {
                    hexValue.append("0");
                }
                hexValue.append(Integer.toHexString(val));
            }
            result=hexValue.toString();
        } catch (Exception e) {
        	log.error("MD5加密失败..", e);
        }
       
        return result;
    }
    
    /**
     * 获取客户端的IP地址
     */
    public static String getIpAddr(HttpServletRequest request) {
		String ipAddress;
		ipAddress = request.getHeader("x-forwarded-for");
		if (ipAddress == null || ipAddress.length() == 0
				|| "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.length() == 0
				|| "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.length() == 0
				|| "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getRemoteAddr();
			String localIp="127.0.0.1";
			if (ipAddress.equals(localIp)) {
				// 根据网卡取本机配置的IP
				InetAddress inet;
				try {
					inet = InetAddress.getLocalHost();
                    ipAddress = inet.getHostAddress();
				} catch (UnknownHostException e) {
					log.error("InetAddress getLocalHost Err.", e);
				} catch (Exception e){
                    log.error("InetAddress getIpAddr Err.", e);
                }
			}
		}

        //对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
		//"***.***.***.***".length() = 15
        if(ipAddress!=null && ipAddress.length()>15){ 
            if(ipAddress.indexOf(",")>0){
                ipAddress = ipAddress.substring(0,ipAddress.indexOf(","));
            }
        }
        
        return ipAddress;
   }
    
    /**
     * List<Bean> to JSON String
     */
	public static String listBeanToJSON(List<Object> objs){
		JSONArray jsonArray=new JSONArray();
		if(!OsTool.isNull(objs)){
			for(Object obj : objs){	
				JSONObject jsonObj=(JSONObject) JSONObject.toJSON(obj);
				jsonArray.add(jsonObj);
			}
		}
			
		return jsonArray.toJSONString();
	}
	
	/**
	 *  保留两位小数，四舍五入
	 */
	public static double scaleDoubles(double de, int scale){
		BigDecimal b=new BigDecimal(de); 
		return b.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();  
	}

	/**
	 * 利用反射方法设置属性值
	 */
	@SuppressWarnings("rawtypes")
	public static Object reflectClassStrSet(Class clazz, Map<String, String> dataMap) {  
		Object object=null;
		try{  
			object = Class.forName(clazz.getName()).newInstance();  
			Class<?> obj = object.getClass();  
			Field[] fields = obj.getDeclaredFields();  
			for (Field field : fields) {
                field.setAccessible(true);
				for(String fieldName : dataMap.keySet()){
					//赋值
					if(OsTool.equals(fieldName.toLowerCase(), field.getName())) {
						String fieldType =field.getGenericType().toString();
						String tempV=dataMap.get(fieldName);
						Object fieldValue=null;
						if(OsTool.equals("class java.util.Date", fieldType) 
									|| OsTool.equals("java.util.Date", fieldType)){
							if(!OsTool.isNull(tempV)){
								tempV=tempV.replaceAll("/", "").replaceAll("-", "");
								tempV=tempV.replaceAll(" ", "").replaceAll(":", "");
								fieldValue=OsTool.parseXmlDate(tempV);
							}
		           		}else if(OsTool.equals("class java.math.BigDecimal", fieldType) 
		           				 	|| OsTool.equals("java.math.BigDecimal", fieldType)){
		           			if(!OsTool.isNull(tempV)){
		           				fieldValue=BigDecimal.valueOf(Double.parseDouble(tempV));
		           			}
		           		}else{
		           			fieldValue=tempV;
		           		}
                        field.set(object, fieldValue);
						break;  
					}  
				}
			}  
		}catch(Exception e) {  
			log.error("Reflect ClassStrSet Err.", e);
		} 
		return object;  
	}

    /**
     * 利用反射方法设置属性值
     */
	@SuppressWarnings("rawtypes")
	public static Object reflectClassObjectSet(Class clazz, Map<String, Object> dataMap) {  
		Object object=null;
		try{  
			object = Class.forName(clazz.getName()).newInstance();  
			Class<?> obj = object.getClass();  
			Field[] fields = obj.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                for(String fieldName : dataMap.keySet()){
					//赋值
					if(OsTool.equals(fieldName.toLowerCase(), field.getName())) {
						Object fieldValue=dataMap.get(fieldName);
						String fieldType =field.getGenericType().toString();
						if(OsTool.equals("class java.math.BigDecimal", fieldType) 
									|| OsTool.equals("java.math.BigDecimal", fieldType)){
							if(null != fieldValue){
								fieldValue=BigDecimal.valueOf(Double.parseDouble(fieldValue.toString()));
							}
						}else if(OsTool.equals("class java.lang.String", fieldType) 
								|| OsTool.equals("java.lang.String", fieldType)){
							if(null != fieldValue){
								fieldValue=OsTool.trimAll(fieldValue.toString());
							}
						}
                        field.set(object, fieldValue);
						break;  
					}  
				}
			}  
		}catch(Exception e) {  
			log.error("Reflect ClassObject Err.", e);
		} 
		return object;  
	} 
	
	/**
	 * 根据指定字段获取对象值的HashCode
	 */
	public static int reflectObjectValGetHash(Object obj, String compFields) {  
		StringBuilder fieldsValuSb=new StringBuilder();
		try{  
			if(null!=obj && !OsTool.isNull(compFields)){
				String[] compFieldSDArray=compFields.split(",");
		        Field[] fields = obj.getClass().getDeclaredFields();  
		        for (Field field : fields) {
                    field.setAccessible(true);
					for(String fieldName : compFieldSDArray){
						//取值
						if(OsTool.equals(fieldName.toLowerCase(), field.getName())){
							Object fieldValue=field.get(obj);
							String fieldType =field.getGenericType().toString();
							if(null==fieldValue){
                                fieldsValuSb.append("NULL");
							}else{
								if(OsTool.equals("class java.util.Date", fieldType) 
										|| OsTool.equals("java.util.Date", fieldType)){
                                    fieldsValuSb.append(OsTool.formatDate((Date)fieldValue, "yyyyMMdd"));
								}else if(OsTool.equals("class java.math.BigDecimal", fieldType) 
										|| OsTool.equals("java.math.BigDecimal", fieldType)){
                                    fieldsValuSb.append(((BigDecimal)fieldValue).doubleValue());
								}else{
                                    fieldsValuSb.append((String)fieldValue);
								}
							}
							break;  
						}  
					}
				}  
			}
		}catch(Exception e) {  
			log.error("Reflect Object Get Value Err.", e);
		} 
		
		//取HashCode
		return fieldsValuSb.toString().hashCode();
	}

    /**
     * 替换掉前后空格、制表符、回车、换行
     */
	public static String trimAll(String str){
        //水平制表符 \u0009
        //回车 \u000a
        //换行 \u000d
		return str.replaceAll("\t", "")
        			.replaceAll("\n", "")
        			.replaceAll("\r", "")
        			.trim();
	}
	
	/**
	 * 获取指定日期的推移
	 */
	public static Date getPointDay(Date date, int pass) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, pass);
		date = calendar.getTime();
		return date;
	}
	
	/**
	 * 自动前缀补0
	 * @param ret 数字
	 * @param length 位数
	 * @return Str
	 */
	public static String autoPrefixFillZero(int ret, int length){
		return String.format("%0" + length + "d", ret);
	}
	
	/**
	 *获取距离当前月的最后一天
	 *ret=-1 前一个月
	 *ret=0   当前月
	 *ret=1 下一个月 
	 */
	public static Date getPointMonthLastDay(int ret){
		Calendar calendar=Calendar.getInstance();    
		int month = calendar.get(Calendar.MONTH);
		calendar.set(Calendar.MONTH, month+ret);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));  
		return calendar.getTime();
	}
	
	/**
	 * List<Map>排序使用Lambada
	 * @param dataListMaps 待排序的集合
	 * @param sortName 根据排序的Map Key名字
	 * @param sortType 排序的类型ASC,DESC
	 */
    public static void sortListMaps(List<Map<String, Object>> dataListMaps, String sortName, String sortType){
        //是否升序
        boolean isAsc=OsTool.equals(OsCommon.Status.ASC, sortType);
        dataListMaps.sort((Map<String, Object> o1, Map<String, Object> o2)->isAsc?
                OsTool.getInt(o1.get(sortName))-OsTool.getInt(o2.get(sortName)):
                OsTool.getInt(o2.get(sortName))-OsTool.getInt(o1.get(sortName))
        );
    }

	/**
	 * 测试方法
	 */
	public static void main(String[] args) {
		
		System.out.println("MD5(123456):"+OsTool.md5Encode("123456"));
		System.out.println("MD5(ddi31231):"+OsTool.md5Encode("ddi31231"));
		
		Map<String, String> data=new HashMap<>(16);
		data.put("dddd", "dddd");
		System.out.println("Map Key: "+(data.get("qqqq")==null));

	}
}
