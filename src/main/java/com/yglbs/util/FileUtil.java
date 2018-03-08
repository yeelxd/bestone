package com.yglbs.util;

import com.yglbs.common.MessageException;
import com.yglbs.common.OsCommon;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 文件工具类
 * @author yeelxd
 * @date 2018-03-02
 */
public class FileUtil {

	private static Logger log = LogManager.getLogger(FileUtil.class);
	
	/**
	  * 保存文件到磁盘
	 */
	public static String saveDataFile(String dataXml, String dataType, String root) throws MessageException {
		Path path = Paths.get(root, (dataType.matches("[^/\\\\<>*?|\".]+") ? dataType : "dataType"),
				DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMdd"), 
				DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMddHHmmssSSS") + ".xml");
		try {
			Path dir = path.getParent();
			if (Files.notExists(dir)) {
				Files.createDirectories(dir);
			}
			Files.write(path, dataXml.getBytes("UTF-8"));
		} catch (Exception e) {
			throw new MessageException("saveDataFile Error dataType:"+dataType+", dataXml:" + dataXml, e);
		} 
		return path.toString();
	}
	
	/**
	 * 获取资源的URL
	 */
	public static URL getResourceURL(String path) {
		return FileUtil.class.getClassLoader().getResource(path);
	}
	
	/**
	 * 写文件, 同时格式化文件
	 */
	public static void creatFileAndWrite(String fileName, String content) throws MessageException {
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

			//写入文件
            RandomAccessFile randomFile = new RandomAccessFile(fileName, "rw");
			randomFile.write(content.getBytes("UTF-8"));
			randomFile.close();

		} catch (IOException e) {
			throw new MessageException("文件["+fileName+"]写入失败", e);
		}
	}
	
	/**
	 * 读取文件内容
	 */
	public static String getFileContent(String filePath){
		
		StringBuilder content=new StringBuilder();
		try{
			File file=new File(filePath);
			if(!file.exists()){
				throw new MessageException(OsCommon.ErrCode.CODE_999, "file is not exists..["+filePath+"]");
			}
			//转为字符形式读取
			BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8")); 
			String str;
			while((str=br.readLine())!=null){
				content.append(str);
			}
			br.close();
		} catch (Exception e) {
			log.error("文件读取失败", e);
		}
		
		return content.toString();
	}
	
	/**
	 * 删除文件及文件夹（如果文件夹为空）
	 */
	public static void deleteFile(String filePath, int count) {  
		File file=new File(filePath);
		if(file.exists()){
			//文件
			if(file.isFile()){
				boolean flag=file.delete();
				if(flag){
					deleteFile(file.getParentFile().getAbsolutePath(), count);
				}
			}
			//文件夹
			if(file.isDirectory()){
				File[] files = file.listFiles();
				if(null==files || 0==files.length){
					boolean flag=file.delete();
					if(flag){
						count++;
						if(count<5){
							deleteFile(file.getParentFile().getAbsolutePath(), count);
						}
					}
				}
			}
		}
	}
	
	/**
	 * 强制删除文件或者路径文件
	 */
	public static boolean forceDeleteFile(String filePath) {
		File file=new File(filePath);
		if(file.exists() && file.isDirectory()){
		    try{
                String[] children = file.list();
                if(!OsTool.isNull(children)){
                    //递归删除目录中的子目录下
                    for(String child : children){
                        boolean flag=FileUtil.forceDeleteFile(filePath.concat(File.separator).concat(child));
                        if(!flag){
                            break;
                        }
                    }
                }
            }catch (Exception e){
                 log.error("forceDeleteFile Err.", e);
            }
		}
		
		// 目录此时为空，可以删除
        return file.delete();
	}
	
	/**
	 * 拷贝文件或者文件夹
	 */
	public static void copyFile(File oldFile, File newFile) {
		if (null!=oldFile && null!=newFile){
			FileInputStream fis = null;
			BufferedInputStream reader = null;
			FileOutputStream fos = null;
			BufferedOutputStream write = null;
			try {
				fis = new FileInputStream(oldFile);
				reader = new BufferedInputStream(fis);
				
				fos = new FileOutputStream(newFile);
				write = new BufferedOutputStream(fos);
				
				int d;
				while((d = reader.read()) != -1){
					write.write(d);
				}
			}catch (Exception e) {
				log.error("Copy File Err.", e);
			}finally{
				if(write!=null){
					try {
						write.flush();
						write.close();
					} catch (IOException e) {
						log.error("Copy File BufferedOutputStream Close Err.", e);
					}
				}
				if(fos!=null){
					try {
						fos.flush();
						fos.close();
					} catch (IOException e) {
						log.error("Copy File FileOutputStream Close Err.", e);
					}
				}
				if(reader!=null){
					try {
						reader.close();
					} catch (IOException e) {
						log.error("Copy File BufferedInputStream Close Err.", e);
					}
				}
				if(fis!=null){
					try {
						fis.close();
					} catch (IOException e) {
						log.error("Copy File FileInputStream Close Err.", e);
					}
				}
			}
		}
	}
	
	/**
	 * 核心测试方法
	 */
	public static void main(String[] args) {
		
		
	}
}
