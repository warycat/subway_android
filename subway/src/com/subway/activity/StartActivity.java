package com.subway.activity;


import com.subway.R;
import com.subway.R.layout;
import com.weibo.net.AccessToken;
import com.weibo.net.DialogError;
import com.weibo.net.Weibo;
import com.weibo.net.WeiboDialogListener;
import com.weibo.net.WeiboException;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class StartActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
		
		ImageButton btn_Login=(ImageButton)findViewById(R.id.imageButtonLogin);
		btn_Login.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				shareByWeibo();
			}
			
			
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		return true;
	}

	
	private static final String CONSUMER_KEY = "1449511190";
	private static final String CONSUMER_SECRET = "3999eadeb03d55377b2f68aa73f7b5fe";

	// private String username = "";
	// private String password = "";

	// 产品分享微博的监听
	class AuthDialogListener implements WeiboDialogListener {

		@Override
		public void onComplete(Bundle values) {
			String token = values.getString("access_token");
			String expires_in = values.getString("expires_in");
			// mToken.setText("access_token : " + token + "  expires_in: " +
			// expires_in);
			AccessToken accessToken = new AccessToken(token, CONSUMER_SECRET);
			accessToken.setExpiresIn(expires_in);
			Weibo.getInstance().setAccessToken(accessToken);

		
			// String packageName =
			// ProductDetailsActivity.this.getPackageName();
			// String link = "http://play.google.com/store/apps/details?id="+
			// packageName;
			// String link =
			// "http://www.eau-thermale-avene.cn/AveneAppDownload/pc.html";

			String link = "http://www.eau-thermale-avene.cn/AveneAppDownload/index.html";

			String content = "@"
					 + link;
			try {
				Weibo weibo = Weibo.getInstance();
				weibo.share2weibo(StartActivity.this, weibo
						.getAccessToken().getToken(), weibo.getAccessToken()
						.getSecret(), content, "");

			} catch (WeiboException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {

			}
			// Intent intent = new Intent();
			// intent.setClass(AuthorizeActivity.this, TestActivity.class);
			// startActivity(intent);
		}

		@Override
		public void onError(DialogError e) {
			Toast.makeText(getApplicationContext(),
					"Auth error : " + e.getMessage(), Toast.LENGTH_LONG).show();
		}

		@Override
		public void onCancel() {
			Toast.makeText(getApplicationContext(), "Auth cancel",
					Toast.LENGTH_LONG).show();
		}

		@Override
		public void onWeiboException(WeiboException e) {
			Toast.makeText(getApplicationContext(),
					"Auth exception : " + e.getMessage(), Toast.LENGTH_LONG)
					.show();
		}

	}

	// 产品分享微博
	public void shareByWeibo() {
		Weibo weibo = Weibo.getInstance();
		weibo.setupConsumerConfig(CONSUMER_KEY, CONSUMER_SECRET);

		// Oauth2.0 隐式授权认证方式
		weibo.setRedirectUrl("http://www.popovivi.com/app/avene2/callback.php");
		weibo.authorize(StartActivity.this, new AuthDialogListener());
	}
}
