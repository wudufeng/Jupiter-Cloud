package com.jupiterframework.mybatis;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.baomidou.mybatisplus.enums.DBType;
import com.baomidou.mybatisplus.enums.IdType;

import lombok.Data;


@Data
@ConfigurationProperties(prefix = "mybatis")
public class MybatisExtendProperties {
	private String dbType = DBType.MYSQL.name();
	private int idType = IdType.ID_WORKER.getKey();
	private boolean dbColumnUnderline = true;
	private ReloadPropertis reloadMapper = new ReloadPropertis();

	@Data
	public static class ReloadPropertis {
		private int delaySecond = 10;
		private int sleepSeconds = 20;
		private boolean enabled;
	}
}
