package com.google.library.javase.model;

import com.google.library.javase.utils.RefUtils;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.logger.LocalLog;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.io.File;

public final class SQLModelHelper {
	public enum Level {
		TRACE("TRACE"), DEBUG("DEBUG"), INFO("INFO"), WARN("WARN"), ERROR("ERROR");
		
		private String value;
		
		Level(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return value;
		}
	}
	
	public static void setLog(Level logLevel, File logFile) {
		if (logLevel != null) {// Values are: TRACE, DEBUG, INFO, WARN, ERROR, FATAL
			System.setProperty(LocalLog.LOCAL_LOG_LEVEL_PROPERTY, logLevel.getValue().toLowerCase());
		}
		if (logFile != null) {
			System.setProperty(LocalLog.LOCAL_LOG_FILE_PROPERTY, logFile.getAbsolutePath());
		}
	}
	
	public static ConnectionSource getSource(String path) throws Exception {
		String url = String.format("jdbc:sqlite:%s", path.trim());
		return (ConnectionSource) RefUtils.newInstance("com.j256.ormlite.jdbc.JdbcConnectionSource",
				new RefUtils.Param(String.class, url));
	}
	
	public static void closeSource(ConnectionSource source) throws Exception {
		if (source != null) source.close();
	}
	
	public static <D extends Dao<T, ?>, T> D createDao(ConnectionSource source, Class<T> clazz) throws Exception {
		return DaoManager.createDao(source, clazz);
	}
	
	public static boolean createTable(Dao<?, ?> dao) throws Exception {
		return dao.isTableExists() || (TableUtils.createTable(dao) > 0);
	}
	
	public static boolean deleteTable(Dao<?, ?> dao) throws Exception {
		return (TableUtils.dropTable(dao, true) > 0);
	}
	
	public static boolean clearTable(Dao<?, ?> dao) throws Exception {
		return (TableUtils.createTable(dao) > 0);
	}
}
