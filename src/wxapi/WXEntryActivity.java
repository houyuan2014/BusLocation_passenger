package wxapi;

import android.app.Activity;
import android.os.Bundle;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/** ΢�ſͻ��˻ص�activityʾ�� */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
	// IWXAPI �ǵ�����app��΢��ͨ�ŵ�openapi�ӿ�
	private IWXAPI api;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		api = WXAPIFactory.createWXAPI(this, "wx2e362f75905313d4", false);
		api.handleIntent(getIntent(), this);
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onReq(BaseReq arg0) {
	}

	@Override
	public void onResp(BaseResp resp) {
		// Log.i("TAG", "resp.errCode:" + resp.errCode + ",resp.errStr:"
		// + resp.errStr);
		switch (resp.errCode) {
		case BaseResp.ErrCode.ERR_OK:
			// ����ɹ�
			break;
		case BaseResp.ErrCode.ERR_USER_CANCEL:
			// ����ȡ��
			break;
		case BaseResp.ErrCode.ERR_AUTH_DENIED:
			// ����ܾ�
			break;
		}
	}
}
