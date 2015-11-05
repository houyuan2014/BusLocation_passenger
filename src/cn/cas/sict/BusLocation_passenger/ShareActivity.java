package cn.cas.sict.BusLocation_passenger;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
    private Button f,c,p,cancel;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_share);
		f=(Button)findViewById(R.id.w_share);
		c=(Button)findViewById(R.id.c_share);
		p=(Button)findViewById(R.id.p_share);
		//cancel=(Button)findViewById(R.id.cancel);
		wxApi = WXAPIFactory.createWXAPI(this, Constants._ID);  
		wxApi.registerApp(Constants._ID);  
		
		
		f.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				wechatShare(0);
			}
			
		});
		c.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				wechatShare(1);
			}
			
		});
		p.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Uri smsToUri = Uri.parse("smsto:");
				Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
				intent.putExtra(Intent.EXTRA_SUBJECT, "title");
				intent.putExtra(Intent.EXTRA_TEXT, "班车app下载的官方网址");
				startActivity(intent);
				
//				Intent in=new Intent();
//				in.setClass(ShareActivity.this, ContactsActivity.class);
//				startActivity(in);
			}
			
		});
//		cancel.setOnClickListener(new OnClickListener(){
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				finish();
//			}
//			
//		});
	}
	private void wechatShare(int flag){  
		   WXWebpageObject webpage = new WXWebpageObject();  
		   webpage.webpageUrl = "班车app官方下载地址";  
	       WXMediaMessage msg = new WXMediaMessage(webpage);  
		   msg.title = "班车";  
		   msg.description = "随时定位";  
		   Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.drawable.che1);  
		   msg.setThumbImage(thumb);  
		   SendMessageToWX.Req req = new SendMessageToWX.Req();  
		   req.transaction = String.valueOf(System.currentTimeMillis());  
		   req.message = msg;  
		   req.scene = flag==0?SendMessageToWX.Req.WXSceneSession:SendMessageToWX.Req.WXSceneTimeline;  
		   wxApi.sendReq(req);  
		}  

}
