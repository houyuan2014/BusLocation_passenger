package cn.cas.sict.BusLocation_passenger;

public class Route {
	int[] id, phone;
	String[] routeName;

	void setRouteInfo(int id, int phone, String routeName) {
		this.phone[id] = phone;
		this.routeName[id] = routeName;
	}

	String getRouteName(int id) {
		return routeName[id];
	}

	int getPhone(int id) {
		return phone[id];
	}
}
