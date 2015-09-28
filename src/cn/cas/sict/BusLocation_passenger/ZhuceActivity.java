package cn.cas.sict.BusLocation_passenger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ZhuceActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_zhuce);
		Button zhuce = (Button) findViewById(R.id.button1);
		zhuce.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String tel = ((EditText) findViewById(R.id.tel)).getText()
						.toString();
				String psw = ((EditText) findViewById(R.id.psw)).getText()
						.toString();
				String psw1 = ((EditText) findViewById(R.id.psw1)).getText()
						.toString();
				if (!"".equals(tel) && !"".equals(psw) && !"".equals(psw1)) {
					if (tel.length() == 11) {
						if (!psw.equals(psw1)) {
							Toast.makeText(ZhuceActivity.this,
									"两次输入的密码不一致，请重新输入！", Toast.LENGTH_LONG)
									.show();
							((EditText) findViewById(R.id.psw)).setText("");
							((EditText) findViewById(R.id.psw1)).setText("");
							((EditText) findViewById(R.id.psw)).requestFocus();
						} else {

							Intent intent = new Intent(ZhuceActivity.this,
									MainActivity.class);
							intent.putExtra("com.xixiwork.PHONE", tel);
							intent.putExtra("com.xixiwork.PASSWORD", psw);
							intent.setClass(ZhuceActivity.this,
									LoginActivity.class);
							startActivity(intent);

						}
					} else {
						Toast.makeText(ZhuceActivity.this, "请输入11位手机号！！！",
								Toast.LENGTH_LONG).show();
						((EditText) findViewById(R.id.tel)).setText("");
						((EditText) findViewById(R.id.psw)).setText("");
						((EditText) findViewById(R.id.psw1)).setText("");
						((EditText) findViewById(R.id.tel)).requestFocus();

					}
				} else {
					Toast.makeText(ZhuceActivity.this, "请将注册信息输入完整！！！",
							Toast.LENGTH_LONG).show();
				}
			}
		});
	}



}
