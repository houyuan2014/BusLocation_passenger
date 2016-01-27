package cn.cas.sict.domain;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class User {

	private String id;
	private String username;
	private String tel;
	private String gender;
	private String email;
	private int routeNum;
	private String routePhone;
	private String routeName;
	private String jsonRouteList;

	private boolean isRemind;
	private float remindDistance;

	private static User user = new User();

	private User() {
	}

	public static User getUser() {
		return user;
	}

	public static void initUser(SharedPreferences sP) {

		user.setUsername(sP.getString("username", ""));
		user.setTel(sP.getString("tel", ""));
		user.setGender(sP.getString("gender", ""));
		user.setEmail(sP.getString("email", ""));
		user.setRouteNum(sP.getInt("route", 1));
		user.setRoutePhone(sP.getString("routephone", ""));
		user.setRouteName(sP.getString("routename", ""));
		user.setJsonRouteList(sP.getString("jsonroutelist", ""));
		user.setRemind(sP.getBoolean("remind", true));
		user.setRemindDistance(sP.getFloat("reminddistance", 3000));
	}

	public void save(Editor editor) {
		editor.putString("username", username)
				.putString("tel", tel).putString("username", username)
				.putString("gender", gender).putString("email", email)
				.putString("email", email)
				.putInt("route", routeNum)
				.putString("routephone", routePhone)
				.putString("routename", routeName)
				.putString("jsonroutelist", jsonRouteList)
				.putBoolean("remind", isRemind)
				.putFloat("reminddistance", remindDistance).commit();
	}

	public void reset() {
		tel = "";
		username = "";
		gender = "";
		routeNum = 1;
		routePhone = "";
		routeName = "";
		isRemind = true;
		remindDistance = 3000;
	}

	public String toString() {
		return "phone =" + tel + ",username =" + username + ",gender =" + gender
				+ ",routeNum =" + routeNum + ",routePhone =" + routePhone
				+ ",routeName =" + routeName + ",isRemind =" + isRemind
				+ ",remindDistance =" + remindDistance + ",routelist ="+ jsonRouteList;

	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getRouteNum() {
		return routeNum;
	}

	public void setRouteNum(int routeNum) {
		this.routeNum = routeNum;
	}

	public String getRoutePhone() {
		return routePhone;
	}

	public void setRoutePhone(String routePhone) {
		this.routePhone = routePhone;
	}

	public String getRouteName() {
		return routeName;
	}

	public void setRouteName(String routeName) {
		this.routeName = routeName;
	}

	public boolean isRemind() {
		return isRemind;
	}

	public void setRemind(boolean isRemind) {
		this.isRemind = isRemind;
	}

	public float getRemindDistance() {
		return remindDistance;
	}

	public void setRemindDistance(float remindDistance) {
		this.remindDistance = remindDistance;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getJsonRouteList() {
		return jsonRouteList;
	}

	public void setJsonRouteList(String jsonRouteList) {
		this.jsonRouteList = jsonRouteList;
	}
}
