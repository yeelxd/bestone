package com.yglbs.util;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * ZIP解压缩工具类
 * @author yeelxd
 * @date 2018-03-02
 */
public class ZipCompressorUtil {

	private static Logger log = LogManager.getLogger(ZipCompressorUtil.class);
	
	public static final int BUFFER = 8192;
	private File zipFile;

	public ZipCompressorUtil(String pathName) {
		zipFile = new File(pathName);
	}

	public void compress(String... pathName) {
		ZipOutputStream out;
		try{
			FileOutputStream fileOutputStream = new FileOutputStream(zipFile);
			CheckedOutputStream cos = new CheckedOutputStream(fileOutputStream, new CRC32());
			out = new ZipOutputStream(cos);
			String basedir = "";
			for(String path : pathName){
				compress(new File(path), out, basedir);
			}
			out.close();
		}catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void compress(String srcPathName) {
		File file = new File(srcPathName);
		if(file.exists()){
			try{
				FileOutputStream fileOutputStream = new FileOutputStream(zipFile);
				CheckedOutputStream cos = new CheckedOutputStream(fileOutputStream, new CRC32());
				ZipOutputStream out = new ZipOutputStream(cos);
				String basedir = "";
				compress(file, out, basedir);
				out.close();
			}catch (Exception e){
				throw new RuntimeException(e);
			}
		}
	}

	private void compress(File file, ZipOutputStream out, String basedir) {
		/* 判断是目录还是文件 */
		if (file.isDirectory()) {
			log.info("压缩：" + basedir + file.getName());
			this.compressDirectory(file, out, basedir);
		} else {
			log.info("压缩：" + basedir + file.getName());
			this.compressFile(file, out, basedir);
		}
	}

	/** 压缩一个目录 */
	private void compressDirectory(File dir, ZipOutputStream out, String basedir) {
		if(dir.exists()){
            File[] files = dir.listFiles();
            for(File file : files) {
                /* 递归 */
                compress(file, out, basedir + dir.getName() + "/");
            }
        }

	}

	/** 压缩一个文件 */
	private void compressFile(File file, ZipOutputStream out, String basedir) {
		if(file.exists()){
			try {
				BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
				ZipEntry entry = new ZipEntry(basedir + file.getName());
				out.putNextEntry(entry);
				int count;
				byte[] data = new byte[BUFFER];
				while((count = bis.read(data, 0, BUFFER)) != -1) {
					out.write(data, 0, count);
				}
				bis.close();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

    /**
     * 测试方法
     */
	public static void main(String[] args) {
		ZipCompressorUtil zc = new ZipCompressorUtil("C:/WorkCenter/_Temp/oval.zip");
		zc.compress("C:/WorkCenter/_Temp/oval");
	}
}