package cn.cas.sict.BusLocation_passenger;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.SyncStateContract.Constants;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class ShareActivity extends Activity {
	private IWXAPI wxApi;
	private Button f, c, p;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_share);
		f = (Button) findViewById(R.id.w_share);
		c = (Button) findViewById(R.id.c_share);
		p = (Button) findViewById(R.id.p_share);
		wxApi = WXAPIFactory.createWXAPI(this, Constants._ID);
		wxApi.registerApp(Constants._ID);

		f.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				wechatShare(0);
				finish();
			}

		});
		c.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				wechatShare(1);
				finish();
			}

		});
		p.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				  Intent sendIntent = new Intent(Intent.ACTION_VIEW);
				    sendIntent.putExtra("sms_body", "班车APP下载地址");
				    sendIntent.setType("vnd.android-dir/mms-sms");
			    startActivity(sendIntent);

				// Intent in=new Intent();
				// in.setClass(ShareActivity.this, ContactsActivity.class);
				// startActivity(in);
				// finish();
			}

		});

	}

	private void wechatShare(int flag) {
		WXWebpageObject webpage = new WXWebpageObject();
		webpage.webpageUrl = "班车app官方下载地址";
		WXMediaMessage msg = new WXMediaMessage(webpage);
		msg.title = "班车";
		msg.description = "随时定位";
		Bitmap thumb = BitmapFactory.decodeResource(getResources(),
				R.drawable.che1);
		msg.setThumbImage(thumb);
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = String.valueOf(System.currentTimeMillis());
		req.message = msg;
		req.scene = flag == 0 ? SendMessageToWX.Req.WXSceneSession
				: SendMessageToWX.Req.WXSceneTimeline;
		wxApi.sendReq(req);
	}

}
