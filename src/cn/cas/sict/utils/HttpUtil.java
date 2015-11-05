package cn.cas.sict.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.util.Log;

public class HttpUtil {
	// ����HttpClient����
	public static HttpClient httpClient = new DefaultHttpClient();
	public static String BASE_URL = "http://Todolist.f3322.net/JSON_Service/";
	static String result;
	static Double lat = 41.74411;
	static Double lng = 123.5060;

	public static String sendPost(String urlAppend,
			final Map<String, String> map) throws Exception {
		final String url = BASE_URL + urlAppend;

		FutureTask<String> task = new FutureTask<String>(
				new Callable<String>() {
					@Override
					public String call() throws Exception {
						HttpPost post = new HttpPost(url);
						post.setHeader("Content-Type",
								"application/x-www-form-urlencoded; charset=utf-8");
						JSONObject json = new JSONObject(map);
						post.getParams().setIntParameter(
								CoreConnectionPNames.CONNECTION_TIMEOUT, 2000);
						post.setEntity(new StringEntity(json.toString(),
								HTTP.UTF_8));

						Log.i("sendjsonstring", json.toString());

						HttpResponse httpResponse = httpClient.execute(post);
						if (httpResponse.getStatusLine().getStatusCode() == 200) {
							// ��ȡ��������Ӧ�ַ���
							result = EntityUtils.toString(httpResponse
									.getEntity());
							return result;
						}
						return null;
					}
				});
		new Thread(task).start();
		return task.get();
	}

	public static String getBusLoc(String urlAppend,
			final Map<String, Object> userLocMap) throws Exception {
		String url = BASE_URL = urlAppend;
		HttpPost post = new HttpPost(url);
		post.setHeader("Content-Type",
				"application/x-www-form-urlencoded; charset=utf-8");
		JSONObject json = new JSONObject(userLocMap);
		post.getParams().setIntParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, 2000);
		post.setEntity(new StringEntity(json.toString(), HTTP.UTF_8));

		Log.i("test", "sendjsonstring   " + json.toString());

		// ����POST����
		// HttpResponse httpResponse;
		// httpResponse = httpClient.execute(post);
		// if (httpResponse.getStatusLine().getStatusCode() == 200) {
		// // ��ȡ��������Ӧ�ַ���
		// result = EntityUtils.toString(httpResponse.getEntity());
		// return result;
		// }
		// return null;

		Map<String, String> m = new HashMap<String, String>();
		m.put("lat", "" + lat);
		m.put("lng", "" + lng);
		lng = lng + 0.001;
		m.put("desc", "���йش帽��");
		m.put("flag", "true");
		JSONObject js = new JSONObject(m);
		return js.toString();
	}
}
