package com.jupiterframework.model;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * <b>应用的基本信息</b>
 * <p>
 * 通过feignClient调用服务时，默认会将当期系统的基本信息设置到header里，<br/>
 * 服务提供方接受到请求后会将该信息设置到线程变量里，通过com.ueb.framework.context.ThreadLocalContext.getClientInfo()获取。<br/>
 * </p>
 * <p>
 * 需要获取当前应用的信息，需要在应用的yml文件里配置baseinfo.xxx，直接将BaseInfo注入到业务类里可以获取信息。
 * </p>
 * 
 * @author wudf
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "baseinfo")
public class BaseInfo {

	public static final String HEADER_KEY = "CLIENT_INFO";

	/** 系统ID */
	private String systemId = "";

	/** 应用ID */
	private String appId = "";

}
