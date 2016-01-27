package cn.cas.sict.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.util.Log;

public class HttpUtil {
	// 创建HttpClient对象
	public static HttpClient httpClient = new DefaultHttpClient();
	public static String BASE_URL = "http://192.168.43.254:8080/SICT_BUS/";
//	static Double lat = 41.74411; y
//	static Double lng = 123.5060; x

	public static String sendPost(String urlAppend, final JSONObject json)
			throws Exception {
		final String url = BASE_URL + urlAppend;

		FutureTask<String> task = new FutureTask<String>(
				new Callable<String>() {
					@Override
					public String call() throws Exception {
						// 指定Post参数
						List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
								1);
						nameValuePairs.add(new BasicNameValuePair("data", json
								.toString()));

						HttpPost post = new HttpPost(url);
						post.setHeader("Content-Type",
								"application/x-www-form-urlencoded; charset=utf-8");
						post.getParams().setIntParameter(
								CoreConnectionPNames.CONNECTION_TIMEOUT, 2000);
						post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

						//System.out.println(json.toString());

						HttpResponse httpResponse = httpClient.execute(post);
						if (httpResponse.getStatusLine().getStatusCode() == 200) {
							// 获取服务器响应字符串
							String result = EntityUtils.toString(httpResponse
									.getEntity());
							return result;
						}
						return null;
					}
				});
		new Thread(task).start();
		return task.get();
	}

	public static String getBusLoc(String urlAppend, final JSONObject json2)
			throws Exception {
		String url = BASE_URL+urlAppend;
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		nameValuePairs.add(new BasicNameValuePair("data", json2.toString()));

		HttpPost post = new HttpPost(url);
		post.setHeader("Content-Type",
				"application/x-www-form-urlencoded; charset=utf-8");
		post.getParams().setIntParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, 2000);
		//post.setEntity(new StringEntity(json2.toString(), HTTP.UTF_8));
		post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		HttpResponse httpResponse = httpClient.execute(post);
		if (httpResponse.getStatusLine().getStatusCode() == 200) {
			String result = EntityUtils.toString(httpResponse.getEntity());
			return result;
		}
		return null;

		// Map<String, String> m = new HashMap<String, String>();
		// m.put("lat", "" + lat);
		// m.put("lng", "" + lng);
		// lng = lng + 0.001;
		// m.put("desc", "在XXX附近");
		// m.put("flag", "true");
		// JSONObject js = new JSONObject(m);
		// Log.i("test", "receivejsonstring   " + js.toString());
		// return js.toString();
	}
}
