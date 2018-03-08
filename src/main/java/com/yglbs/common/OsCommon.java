package com.yglbs.common;

import com.yglbs.util.OsTool;

/**
 * 常量配置信息
 * @author yeelxd
 * @date 2018-03-02
 */
public class OsCommon {

	/**
	 * 系统配置
	 */
	public interface Config {
		/**
		 * SFTP
		 */
		String SFTP_IP = "sftp.ip";
		String SFTP_PORT = "sftp.port";
		String SFTP_USERNAME = "sftp.username";
		String SFTP_PASSWORD = "sftp.password";
		String SFTP_SERVERFOLDER = "sftp.serverFolder";
		String SFTP_LOCALBACKUP = "sftp.localBackup";
		String SFTP_DDIWSURL = "sftp.ddiWsUrl";
		/**
		 * Scheduled
		 */
		String TASK_DEALING_PAGECOUNT = "task.dealing.pageCount";
		/**
		 * Monitoring Email
		 */
		String TASK_MEMIAL_SERVER_HOST = "task.monitor.email.serverHost";
		String TASK_MEMIAL_SERVER_PORT = "task.monitor.email.serverPort";
		String TASK_MEMIAL_SERVER_ADDR = "task.monitor.email.serverAddr";
		String TASK_MEMIAL_SERVER_PWD = "task.monitor.email.serverPwd";
		String TASK_MEMIAL_SPECIAL_ADDR = "task.monitor.email.specialAddr";
		String TASK_MEMIAL_SPECIAL_ADDR_CC = "task.monitor.email.specialAddrCC";

	}

	/**
	 * 状态
	 */
	public interface Status{
		int SUCCESS = 1;
		int PROCESS = 0;
		int FAIL = -1;

		/**
		 * 2015-04-29T17:04:39Z
		 */
		String GMDT = "GMDT";
		/**
		 * 20121024154823
		 */
		String DT = "DT";
		/**
		 * 20121024
		 */
		String D = "D";

		/**
		 * 排序-升序
		 */
		String ASC = "ASC";
		/**
		 * 排序-降序
		 */
		String DESC = "DESC";
	}

	/**
	 * 文档类型
	 */
	public interface MineType {
		String XML = "text/xml";
	}

	/**
	 * Exception Code
	 */
	public interface ErrCode {
		/**
		 * 通用Exception Code
		 */
		int CODE_999=999;

	}

	/**
	 * 字段ENUM
	 */
	public enum DB_TABLES {
		/**
		 * Code及对应的名称值域
		 */
		T_BASE_PHARM("基础药厂表"), T_BD_API("API定义表"), T_API_LOG("API调用日志表"), 
		T_BD_DISTRIBUTOR("经销商表"), T_BD_PHARM("经销商药厂配置表"), T_BD_USER("用户表"), 
		T_MAP_SQLMAPPING("字段Mapping表"), T_BD_DISTMAPPING("总分经销商关系表"), 
		T_BD_PHARM1("经销商药厂配置备份表"), T_SQLMAPPING_TEMPLET("字段Mapping模板表"), 
		T_BD_MENU("菜单配置表"), T_BD_ROLE("角色配置表"), T_ROLE_MENU_OPERATION("角色菜单关系表"), 
		T_ORIGINALINVDATA("库存数据表"), T_ORIGINALPURDATA("采购数据表"), T_ORIGINALSALESDATA("销售数据表"),
		T_TEMPINVDATA("比对临时库存表"), T_TEMPPURDATA("比对临时采购表"), T_TEMPSALESDATA("比对临时销售表"),
		T_TEMPDDISTLOG("压力测试日志表"), T_TEMPDDIDBSTAT("表记录统计信息表"), T_BD_DICTIONARY("字典信息表"),
		T_BD_DATALIMIT("监控临界值设置表"), T_FILEANALYSIS_STATISTICS("文件解析工具记录表"),
		T_OP_LOG("用户操作记录表"), T_ORIGINALINVDATA_BAK("库存数据备份表"), T_ORIGINALPURDATA_BAK("采购数据备份表"), 
		T_ORIGINALSALESDATA_BAK("销售数据备份表"), T_TEMPSFTPSYNCCONFIG("文件直连同步工具配置表"),
		T_TEMPSFTPSYNCLOG("文件直连同步工具日志表");
		
		private String value;

		/**
		 * ENUM的构造函数
		 */
		DB_TABLES(String value) {
			this.value = value;
		}
		public static String getValue(String enName){
			String cnName="";
			for(DB_TABLES tab : DB_TABLES.values()){
				if(OsTool.equals(tab.name(), enName.toUpperCase())){
					cnName=tab.value;
					break;
				}
			}
			return cnName;
		}
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
	}
	
}
