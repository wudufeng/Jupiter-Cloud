package com.jupiterframework.autoconfig.mybatis;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.baomidou.mybatisplus.enums.DBType;
import com.baomidou.mybatisplus.enums.IdType;


@ConfigurationProperties(prefix = "mybatis")
public class MybatisExtendProperties {
	private String dbType = DBType.MYSQL.name();
	private int idType = IdType.ID_WORKER.getKey();
	private boolean dbColumnUnderline = true;
	private ReloadPropertis reloadMapper = new ReloadPropertis();

	public String getDbType() {
		return this.dbType;
	}

	public void setDbType(String dbType) {
		this.dbType = dbType;
	}

	public int getIdType() {
		return this.idType;
	}

	public void setIdType(int idType) {
		this.idType = idType;
	}

	public boolean isDbColumnUnderline() {
		return this.dbColumnUnderline;
	}

	public void setDbColumnUnderline(boolean dbColumnUnderline) {
		this.dbColumnUnderline = dbColumnUnderline;
	}

	public ReloadPropertis getReloadMapper() {
		return this.reloadMapper;
	}

	public void setReloadMapper(ReloadPropertis reloadMapper) {
		this.reloadMapper = reloadMapper;
	}

	public static class ReloadPropertis {
		private int delaySecond = 10;
		private int sleepSeconds = 20;
		private boolean enabled;

		public int getDelaySecond() {
			return this.delaySecond;
		}

		public void setDelaySecond(int delaySecond) {
			this.delaySecond = delaySecond;
		}

		public int getSleepSeconds() {
			return this.sleepSeconds;
		}

		public void setSleepSeconds(int sleepSeconds) {
			this.sleepSeconds = sleepSeconds;
		}

		public boolean isEnabled() {
			return this.enabled;
		}

		public void setEnabled(boolean enabled) {
			this.enabled = enabled;
		}
	}
}
