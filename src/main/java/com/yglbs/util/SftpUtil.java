package com.yglbs.util;

import com.jcraft.jsch.*;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.yglbs.common.MessageException;
import com.yglbs.common.OsCommon;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;
import java.util.Vector;

/**
 *	SFTP工具类
 * @author yeelxd
 * @date 2018-03-02
 */
public class SftpUtil {

    private static Logger log = LogManager.getLogger(SftpUtil.class);
	
    /**
     * 创建SFTP连接Session
     */
    public static Session createSession(String host, int port, String username, String password)
    		throws MessageException {
    	Session session;
    	try {
    		JSch jsch = new JSch();
    		jsch.getSession(username, host, port);
    		session = jsch.getSession(username, host, port);
            log.info("Session created.");
            session.setPassword(password);
            Properties sshConfig = new Properties();
            //设置第一次登陆的时候提示，可选值：(ask | yes | no)
            sshConfig.put("StrictHostKeyChecking", "no");
            session.setConfig(sshConfig);
            session.connect();
            if(!session.isConnected()){
            	throw new Exception("Session connected Err.");
            }
            log.info("Session connected.");
        } catch (Exception e) {
            throw new MessageException("SFTP createSession Err.", e);
        }
    	return session;
    }
    
    /**
     * 创建SFTP连接ChannelSftp
     */
    public static ChannelSftp createChannelSftp(Session session)
    		throws MessageException{
    	ChannelSftp sftp;
        try {
            log.info("Opening Channel.");
            Channel channel = session.openChannel("sftp");
            //建立SFTP通道的连接超时
            channel.connect(300000);
            if(!channel.isConnected()){
            	throw new Exception("Channel connected Err.");
            }
            sftp = (ChannelSftp) channel;
            log.info("Channel Connected.");
        } catch (Exception e) {
            throw new MessageException("SFTP createChannelSftp Err.", e);
        }
        return sftp;
    }
    
    /**
     * Upload File
     * @param localPath 本地目录
     * @param remotePath 服务器目录
     * @param mode  0:OVERWRITE  1:RESUME  2:APPEND
     */
    public static void upload(ChannelSftp sftp, String localPath, String remotePath, int mode)
    		throws MessageException{
        try {
        	if(!isDirExist(sftp, remotePath)){
        		log.info("目录不存在，尝试创建目录{{}}", remotePath);
                //尝试创建目录
        		createServerDir(sftp, remotePath);
        	}
            //CD到目录
        	sftp.cd(remotePath);
        	File dir = new File(localPath);
        	if(dir.exists() && dir.isDirectory()){
        		File[] files = dir.listFiles();
                for(File file : files){
                    //暂时只上传文件
                    if(file.isFile()){
                        FileInputStream fis = new FileInputStream(file);
                        sftp.put(fis, file.getName(), mode);
                        fis.close();
                    }
                }
        	}
        } catch (Exception e) {
            throw new MessageException("SFTP upload Err.", e);
        }
    }
    
    /**
     * Download File from SFTP
     */
    public static File download(ChannelSftp sftp, String directory, String downloadFile, String saveFileName) 
    		throws MessageException{
    	FileOutputStream fos=null;
    	File saveFile=new File(saveFileName);  
    	try {
    		if(!saveFile.exists()){  
                File parentFile = saveFile.getParentFile();  
                if(!parentFile.exists()) {  
                    parentFile.mkdirs();  
                }  
                saveFile.createNewFile();  
            }  
    		fos = new FileOutputStream(saveFile);
    		//CD到具体目录
            sftp.cd(directory);
            sftp.get(downloadFile, fos);
        } catch (Exception e) {
        	throw new MessageException(OsCommon.ErrCode.CODE_999, "SFTP Download Err.", e);
        }finally{
        	if(null!=fos){
        		try {
					fos.close();
				} catch (Exception e) {
		        	log.error("SFTP Download fos.close Err.", e);
				}
        	}
        }
    	return saveFile;
    }
    
    /**
     * 移动或重命名文件
     * @param sftp SFTP服务
     * @param oldFilePath 原文件路径
     * @param newFilePath 新文件路径
     * @throws MessageException Exception Info
     */
    public static void renameFile(ChannelSftp sftp, String oldFilePath, String newFilePath)
    		throws MessageException{
    	try {  
            sftp.rename(oldFilePath, newFilePath);
        } catch (Exception e) {  
        	throw new MessageException("SFTP renameFile Err.", e);
        } 
    }
    
    /** 
     * 删除文件-sftp协议. 
     * @param fullFileName 要删除的文件 
     * @param sftp sftp连接 
     */  
    public static void rmFile(ChannelSftp sftp, String fullFileName)
    		throws MessageException{
        try {  
            sftp.rm(fullFileName);  
        } catch (Exception e) {  
        	throw new MessageException("SFTP rmFile Err.", e);
        }  
    }  
  
    /** 
     * 删除文件夹-sftp协议.如果文件夹有内容，则会抛出异常. 
     * @param folderName 文件夹路径 
     * @param sftp sftp连接 
     * @param recursion 递归删除 
     */  
    public static void rmDir(ChannelSftp sftp, String folderName, boolean recursion)
    		throws MessageException{
        try {  
            if (recursion){
            	recursionRmRec(sftp, folderName);  
            }else{
            	sftp.rmdir(folderName);  
            } 
        } catch (Exception e) {  
        	throw new MessageException("SFTP rmDir Err.", e);
        }  
    }  
  
    /** 
     * 递归删除执行. 
     * @param folderName 文件路径 
     * @param sftp sftp连接 
     */  
    @SuppressWarnings("unchecked")  
    public static void recursionRmRec(ChannelSftp sftp, String folderName)
    		throws MessageException{
    	try{
    		Vector<LsEntry> vector = sftp.ls(folderName);
            if (vector.size() == 1) {
                // 文件，直接删除
                sftp.rm(folderName);
            } else if (vector.size() == 2) {
                // 空文件夹，直接删除
                sftp.rmdir(folderName);  
            } else {  
                String fileName;
                // 删除文件夹下所有文件  
                for(LsEntry en : vector) {  
                    fileName = en.getFilename();  
                    if (!OsTool.equals(".", fileName) && !OsTool.equals("..", fileName)){
                    	recursionRmRec(sftp, folderName + "/" + fileName);
                    }  
                }  
                // 删除文件夹  
                sftp.rmdir(folderName);  
            }  
    	} catch (Exception e) {  
        	throw new MessageException("SFTP recursionRmRec Err.", e);
        }  
    } 
    
    /**
	 * 判断目录是否存在
	 */
    public static boolean isDirExist(ChannelSftp sftp, String directory) {
		boolean isDirExistFlag = false;
		try {
			sftp.lstat(directory);
			isDirExistFlag = true;
		} catch (Exception e) {
			if ("no such file".equals(e.getMessage().toLowerCase())) {
				isDirExistFlag = false;
			}
		}
		return isDirExistFlag;
	}
    
    /**
     * create Directory
     */
    public static void createServerDir(ChannelSftp sftp, String remotePath)
    		throws MessageException{
    	try {
			if (!isDirExist(sftp, remotePath)) {
				String[] pathArry = remotePath.split("/");
				StringBuilder filePath = new StringBuilder("/");
				for(String path : pathArry) {
					if ("".equals(path)) {
						continue;
					}
					filePath.append(path).append("/");
					if(isDirExist(sftp, filePath.toString())) {
						sftp.cd(filePath.toString());
					}else{
						// 建立目录
						sftp.mkdir(filePath.toString());
						//进入并设置为当前目录
						sftp.cd(filePath.toString());
					}
				}
			}
			sftp.cd(remotePath);
		}catch(Exception e) {
	        throw new MessageException("SFTP CreateDir Err."+ remotePath, e);
		}
    }
    
    /**
     * 列出目录下的文件
     * @param directory：要列出的目录
     */
    @SuppressWarnings({"unchecked" })
    public static Vector<LsEntry> listFiles(ChannelSftp sftp, String directory)
    		throws MessageException{
    	Vector<LsEntry> lsFiles;
    	try{
    		lsFiles=sftp.ls(directory);
    	}catch(SftpException e) {
    		 throw new MessageException("SFTP.ls Err :" + directory, e);
		}
    	
		return lsFiles;
    }
    
    /**
     * Disconnect Server
     */
    public static void closeConnected(ChannelSftp sftp, Session session) {
        if(sftp != null){
            if(sftp.isConnected()){
                sftp.disconnect();
                log.info("sftp is closed already");
            }
        }
        if(session != null){
            if(session.isConnected()){
                session.disconnect();
                log.info("session is closed already");
            }
        }
    }
    
    /**
     * Main
     */
    public static void main(String[] args) throws Exception {
    	
        String host = "10.11.11.9";
        int port = 22;
        String username="sftp_test";
        String password="sftp_test";
        String remotePath = "/upload/test/";
    	String localPath = "C:\\WorkCenter\\_Temp\\oval\\SyncDir\\";
    	
    	//SFTP连接
    	Session session=SftpUtil.createSession(host, port, username, password);
    	ChannelSftp sftp=SftpUtil.createChannelSftp(session);
    	//SFTP上传文件
        SftpUtil.upload(sftp, localPath, remotePath, 2);
    	//SFTP关闭
        SftpUtil.closeConnected(sftp, session);
        System.exit(0);
    }
}
