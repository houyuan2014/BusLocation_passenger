package cn.cas.sict.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;

public class SaleUtil {

	/**
	 * @param args
	 */
	/** 
	* �жϿͻ��ֻ������Ƿ���Ϲ��� 
	* 
	* @param userPhone �ͻ��ֻ����� 
	* @return true | false 
	*/ 
	public static boolean isValidPhoneNumber(String userPhone) { 
	if (null == userPhone || "".equals(userPhone)) 
	return false; 
	Pattern p = Pattern.compile("^0?1[0-9]{10}"); 
	Matcher m = p.matcher(userPhone); 
	return m.matches(); 
	} 

	public static void createDialog(Context ctx, int dialogTitleTip,
			String string) {
		// TODO Auto-generated method stub
		
	}
}
