package cn.cas.sict.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
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
	// ����HttpClient����
	public static HttpClient httpClient = new DefaultHttpClient();
	public static final String BASE_URL = "http://Todolist.f3322.net/JSON_Service/";
	static String result;
	static Double lat = 41.74411;
	static Double lng = 123.506;

	/**
	 * @param url
	 *            ���������URL
	 * @param params
	 *            �������
	 * @return ��������Ӧ�ַ���
	 * @throws Exception
	 */
	public static String postRequest(final String url,
			final Map<String, String> map) throws Exception {
		FutureTask<String> task = new FutureTask<String>(
				new Callable<String>() {
					@Override
					public String call() throws Exception {
						// ����HttpPost����
						HttpPost post = new HttpPost(url);
						post.setHeader("Content-Type",
								"application/x-www-form-urlencoded; charset=utf-8");
						JSONObject json = new JSONObject(map);
						post.getParams().setIntParameter(
								CoreConnectionPNames.CONNECTION_TIMEOUT, 2000);
						post.setEntity(new StringEntity(json.toString(),
								HTTP.UTF_8));

						Log.i("sendjsonstring", json.toString());

						// ����POST����
						HttpResponse httpResponse;
						httpResponse = httpClient.execute(post);
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
		Log.i("task.get() ", task.get() + "");
		return task.get();
	}

	public static String post(final String url,
			final Map<String, Object> userLocMap) throws Exception {
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
