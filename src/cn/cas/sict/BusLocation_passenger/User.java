package cn.cas.sict.BusLocation_passenger;

import java.io.Serializable;

public class User implements Serializable {
	/**
	 * 所有的数据成员必须都是实现了序列化接口的
	 */
	private static final long serialVersionUID = 1L;
	private static String phone;
	private static String passwd;
	private static String name;
	private static int routeNum;
	private static String routePhone;
	private static String routeName;
	private static boolean isRemind;
	private static float remindDistance;

	public User() {

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
		User.phone = phone;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		User.passwd = passwd;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		User.name = name;
	}

	public int getRouteNum() {
		return routeNum;
	}

	public void setRouteNum(int routeNum) {
		User.routeNum = routeNum;
	}

	public String getRoutePhone() {
		return routePhone;
	}

	public void setRoutePhone(String routePhone) {
		User.routePhone = routePhone;
	}

	public String getRouteName() {
		return routeName;
	}

	public void setRouteName(String routeName) {
		User.routeName = routeName;
	}

	public boolean getIsRemind() {
		return isRemind;
	}

	public void setRemind(boolean isRemind) {
		User.isRemind = isRemind;
	}

	public float getRemindDistance() {
		return remindDistance;
	}

	public void setRemindDistance(float remindDistance) {
		User.remindDistance = remindDistance;
	}

}
