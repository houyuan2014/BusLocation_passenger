package cn.cas.sict.utils;

import java.io.Serializable;

import android.content.SharedPreferences;

public class User {

	/*
	 * 类型 字段名 字段值 备注 String phone 手机号 String passwd 密码 int route 1 路线号 String
	 * routename 一号线 路线名称 String routephone 18811112222 路线手机号 String name
	 * hy_sict 姓名 boolean remind true 是否提醒 floate reminddistance 3000 提醒距离(米)
	 */

	/**
	 * 所有的数据成员必须都是实现了序列化接口的
	 */
	private String phone;
	private String passwd;
	private String name;
	private int routeNum;
	private String routePhone;
	private String routeName;
	private boolean isRemind;
	private float remindDistance;
	private static User user = new User();

	private User() {
	}

	public static void initUser(SharedPreferences sP) {
		String phone = sP.getString("phone", "");
		String passwd = sP.getString("passwd", "");
		String name = sP.getString("name", "");
		int routeNum = sP.getInt("route", 1);
		String routePhone = sP.getString("routephone", "13655665566");
		String routeName = sP.getString("routename", "一号线");
		boolean isRemind = sP.getBoolean("remind", true);
		float remindDistance = sP.getFloat("reminddistance", 3000);
		user.setPhone(phone);
		user.setPasswd(passwd);
		user.setName(name);
		user.setRouteNum(routeNum);
		user.setRoutePhone(routePhone);
		user.setRouteName(routeName);
		user.setRemind(isRemind);
		user.setRemindDistance(remindDistance);
		System.out.println("------> initUser");
	}

	public static User getUser() {
		return user;
	}

	public void reset() {
		phone = "";
		passwd = "";
		name = "";
		routeNum = 1;
		routePhone = "";
		routeName = "一号线";
		isRemind = true;
		remindDistance = 3000;
	}

	public String toString() {
		return "phone " + phone + ",passwd " + passwd + ",name " + name
				+ ",routeNum " + routeNum + ",routePhone " + routePhone
				+ ",routeName " + routeName + ",isRemind " + isRemind
				+ ",remindDistance " + remindDistance;

	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		user.phone = phone;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		user.passwd = passwd;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		user.name = name;
	}

	public int getRouteNum() {
		return routeNum;
	}

	public void setRouteNum(int routeNum) {
		user.routeNum = routeNum;
	}

	public String getRoutePhone() {
		return routePhone;
	}

	public void setRoutePhone(String routePhone) {
		user.routePhone = routePhone;
	}

	public String getRouteName() {
		return routeName;
	}

	public void setRouteName(String routeName) {
		user.routeName = routeName;
	}

	public boolean getIsRemind() {
		return isRemind;
	}

	public void setRemind(boolean isRemind) {
		user.isRemind = isRemind;
	}

	public float getRemindDistance() {
		return remindDistance;
	}

	public void setRemindDistance(float remindDistance) {
		user.remindDistance = remindDistance;
	}

}
