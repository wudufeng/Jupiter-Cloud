package com.jupiterframework.util;

import com.jupiterframework.model.BaseInfo;
import com.jupiterframework.model.UserInfo;


public class SessionUtils {

	private SessionUtils() {
	}

	/** 当前登录用户信息 */
	private static final ThreadLocal<UserInfo> USER_SESSION = ThreadLocal.withInitial(UserInfo::new);

	/** 当前请求方的信息 */
	private static final ThreadLocal<BaseInfo> CLIENT_BASE_INFO = ThreadLocal.withInitial(BaseInfo::new);

	public static String getSessionId() {
		UserInfo ui = USER_SESSION.get();
		if (ui == null)
			return null;
		return ui.getSessionId();

	}

	public static Long getUserId() {
		UserInfo ui = USER_SESSION.get();
		if (ui == null)
			return null;
		return ui.getUserId();
	}

	public static String getSystemId() {
		UserInfo ui = USER_SESSION.get();
		if (ui == null)
			return null;
		return ui.getSystemId();
	}

	public static String getCompanyId() {
		UserInfo ui = USER_SESSION.get();
		if (ui == null || StringUtils.isBlank(ui.getCompanyId()))
			return "-1";
		return ui.getCompanyId();
	}

	/** 获取当前登录的用户对象信息 */
	public static UserInfo currentUser() {
		return USER_SESSION.get();
	}

	/** 设置当前登录的用户对象 */
	public static void setUserInfo(UserInfo session) {
		USER_SESSION.set(session);
	}

	/** 获取请求来源方的基本信息数据 */
	public static BaseInfo getBaseInfo() {
		return CLIENT_BASE_INFO.get();
	}

	public static void setBaseInfo(BaseInfo baseInfo) {
		CLIENT_BASE_INFO.set(baseInfo);
	}

}
