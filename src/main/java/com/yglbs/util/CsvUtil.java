package com.yglbs.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.*;

/**
 * CSV操作(导出和导入)
 * @author yeelxd
 * @date 2018-03-02
 */
public class CsvUtil {

    private static Logger log = LogManager.getLogger(CsvUtil.class);
	
	/**
	 * 导出CSV文件
	 */
	public static void exportCsv(String headerFields, JSONArray dataArray,
				List<Map<String, Object>> smList, String fileName) {
		FileOutputStream out = null;
		OutputStreamWriter osw = null;
		BufferedWriter bw = null;
		try {
			File file = new File(fileName);
            if (!file.getParentFile().exists()) {
                boolean isMkdir=file.getParentFile().mkdirs();
                log.info("文件所在目录创建，{1}", isMkdir);
            }
            if(file.isFile() && file.exists()){
				//先删除已存在的文件
                boolean isDelete=file.delete();
                log.info("对已存在文件删除，{1}", isDelete);
            }
            boolean isCreate=file.createNewFile();
            log.info("创建新文件，{1}", isCreate);
			out = new FileOutputStream(file);
			osw = new OutputStreamWriter(out);
			bw = new BufferedWriter(osw);
			
			//添加标题行
			String[] headerArray=headerFields.split(",");
			List<String> titleList=new ArrayList<>();
			for(String header : headerArray){
				for(Map<String, Object> smMap : smList){
					if(OsTool.equals(header, (String)smMap.get("DBFIELD"))){
						titleList.add((String)smMap.get("ORIGINALFIELD"));
						break;
					}
				}
			}
			StringBuffer headerTitleSB=new StringBuffer();
			for(Iterator<String> iter=titleList.iterator(); iter.hasNext(); ){
				headerTitleSB.append(iter.next());
				if(iter.hasNext()){
					headerTitleSB.append(",");
				}
			}
			bw.append(headerTitleSB);
			bw.newLine();
					
			//添加数据行
		    for(int i=0; i<dataArray.size(); i++){
		    	StringBuffer dataSB=new StringBuffer();
		    	JSONObject jsonObj=(JSONObject)dataArray.get(i);
				for(int j=0 ;j<headerArray.length; j++){
					String header=headerArray[j];
					//替换掉所有空格
					String dataValue=jsonObj.getString(header);
					dataSB.append("\"").append(dataValue).append("\"");
					if(j<headerArray.length-1){
						dataSB.append(",");
					}
				}
				bw.append(dataSB);
				if(i<dataArray.size()-1){
					bw.newLine();
				}
			}
		} catch (Exception e) {
			log.error("Export Csv Err.", e);
		} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException e) {
					log.error("BufferedWriter Close Err.", e);
				}
			}
			if (osw != null) {
				try {
					osw.close();
				} catch (IOException e) {
					log.error("OutputStreamWriter Close Err.", e);
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					log.error("FileOutputStream Close Err.", e);
				}
			}
		}
	}

	/**
	 * 读取CSV文件
	 */
	public static List<String> csvDataToList(File file) {
		List<String> dataList = new ArrayList<>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
            String line;
			while ((line = br.readLine()) != null) {
				dataList.add(line);
			}
		} catch (Exception e) {
			log.error("CSV DataToList Err.", e);
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					log.error("BufferedReader Close Err.", e);
				}
			}
		}

		return dataList;
	}
	
	/**
	 * 读取CSV文件的Title
	 */
	public static List<String> readCsvTitle(File file, boolean isTrim){
		List<String> titleList =null;
		try{
			List<String> dataList=csvDataToList(file);
			if(!OsTool.isNull(dataList)){
				String titles=dataList.get(0);
				String[] titleArray=titles.split(",");
				titleList=new ArrayList<>();
				for(String title : titleArray){
					if(isTrim){
						title=OsTool.trimAll(title);
					}
					titleList.add(clearPrfSub(title));
				}
			}
		}catch (Exception e) {
			log.error("Read Csv Title Err.", e);
		} 
		
		return titleList;
	}
	
	/**
	 * 转换CSV文件为List<Map>
	 */
	public static List<Map<String,String>> csvDataToListMap(File file, boolean isTrim) {
		List<Map<String,String>> titleMapList=null;
		try{
			List<String> dataList=csvDataToList(file);
			if(!OsTool.isNull(dataList) && dataList.size()>1){
				titleMapList=new ArrayList<>();
				String[] titleArray=null;
				for(int i=0; i<dataList.size(); i++){
					String[] dataArray=dataList.get(i).split(",");
					if(i==0){
						titleArray=dataArray;
						continue;
					}
					//开始封装数据
					Map<String,String> dataMap=new HashMap<>(16);
					for(int index=0; index<titleArray.length; index++){
						String key=titleArray[index];
						String value=dataArray[index];
						if(isTrim){
							key=OsTool.trimAll(key);
							value=OsTool.trimAll(value);
						}
						dataMap.put(clearPrfSub(key), clearPrfSub(value));
					}
					titleMapList.add(dataMap);
				}
			}
		}catch (Exception e) {
			log.error("CSV DataToListMap Err.", e);
		} 
		
		return titleMapList;
	}
	
	/**
	 * 去除字符串的前后"
	 */
	private static String clearPrfSub(String str){
	    String pref="\"";
		if(null!=str && str.startsWith(pref) && str.endsWith(pref)){
			str=str.substring(1, str.length()-1);
		}
		return str;
	}
	
}
