package com.yglbs.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * CSV操作(导出和导入)
 */
public class CsvUtil {

	private static Logger log = LoggerFactory.getLogger(CsvUtil.class);
	
	/**
	 * 导出CSV文件
	 * @param fileName CSV文件(路径+文件名)
	 * @param dataArray 数据
	 */
	public static void exportCsv(String headerFields, JSONArray dataArray,
				List<Map<String, Object>> smList, String fileName) {
		FileOutputStream out = null;
		OutputStreamWriter osw = null;
		BufferedWriter bw = null;
		try {
			File file = new File(fileName);
			if(!file.getParentFile().exists()){
				file.getParentFile().mkdirs();
			}
			if(file.exists()){
				//先删除已存在的文件
				file.delete();
			}
			file.createNewFile();
			out = new FileOutputStream(file);
			osw = new OutputStreamWriter(out);
			bw = new BufferedWriter(osw);
			
			//添加标题行
			String[] headerArray=headerFields.split(",");
			List<String> titleList=new ArrayList<String>();
			for(String header : headerArray){
                for (Map<String, Object> smMap : smList) {
                    if (OsTool.equals(header, (String) smMap.get("DBFIELD"))) {
                        titleList.add((String) smMap.get("ORIGINALFIELD"));
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
	 * @param file CSV文件(路径+文件)
	 */
	public static List<String> csvDataToList(File file) {
		List<String> dataList = new ArrayList<String>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
			String line = "";
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
	 * @param file
	 */
	public static List<String> readCsvTitle(File file, boolean isTrim){
		List<String> titleList =null;
		try{
			List<String> dataList=csvDataToList(file);
			if(!OsTool.isNull(dataList)){
				String titles=dataList.get(0);
				String[] titleArray=titles.split(",");
				titleList=new ArrayList<String>();
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
	 * @param file
	 */
	public static List<Map<String,String>> csvDataToListMap(File file, boolean isTrim) {
		List<Map<String,String>> titleMapList=null;
		try{
			List<String> dataList=csvDataToList(file);
			if(!OsTool.isNull(dataList) && dataList.size()>1){
				titleMapList=new ArrayList<Map<String,String>>();
				String[] titleArray=null;//标题行
				for(int i=0; i<dataList.size(); i++){
					String[] dataArray=dataList.get(i).split(",");
					if(i==0){
						titleArray=dataArray;
						continue;
					}
					//开始封装数据
					Map<String,String> dataMap=new HashMap<String,String>();
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
		if(null!=str && str.startsWith("\"") && str.endsWith("\"")){
			str=str.substring(1, str.length()-1);
		}
		return str;
	}
	
}
