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


	class AuthDialogListener implements WeiboDialogListener {

		@Override
		public void onComplete(Bundle values) {
			String token = values.getString("access_token");
			String expires_in = values.getString("expires_in");
			
			AccessToken accessToken = new AccessToken(token, CONSUMER_SECRET);
			accessToken.setExpiresIn(expires_in);
			Weibo.getInstance().setAccessToken(accessToken);

		

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


	public void shareByWeibo() {
		Weibo weibo = Weibo.getInstance();
		weibo.setupConsumerConfig(CONSUMER_KEY, CONSUMER_SECRET);

		weibo.setRedirectUrl("http://www.popovivi.com/app/avene2/callback.php");
		weibo.authorize(StartActivity.this, new AuthDialogListener());
	}
}
