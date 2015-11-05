package cn.cas.sict.BusLocation_passenger;

public class Values {
	/*
	 *   类型		 	字段名				字段值					备注
 	 *	String		phone										手机号
 	 *	String  	passwd										密码
 	 *	int			route				1						路线号
 	 *	String		routename			一号线					路线名称
 	 *	String		routephone			18811112222				路线手机号
 	 *	String 		name				hy_sict					姓名
 	 *	boolean		remind				true					是否提醒
 	 *	floate		reminddistance		3000					提醒距离(米)
	 */
	public final static String SP = "userconfig";
	public final static String BROADCASTTOUI = "cn.cas.sict.aaa";
	public final static String BROADCASTTOSERVICE = "cn.cas.sict.bbb";
	public final static int USERFLAG = 0;
	public final static int BUSFLAG = 1;
	public final static int DISTANCEFLAG = 2;
	public final static int BUSDISABLE = 3;
	public final static int GETSERVICEINFO = 4;
}
