package com.asos.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

import com.asos.R;
/**
 * @author Kalpesh Sevak
 * @version 2015.0606
 * @since 1.0
 */
public class SplashScreenActivity extends Activity {

	private Intent intent = null;
	private ProgressBar spinner = null;

	Handler handler = new Handler();
	int progressStatus = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		spinner = (ProgressBar) this.findViewById(R.id.progressBar1);

		new Thread(new Runnable() {

			int i = 0;

			public void run() {
				while (progressStatus < 2) {
					progressStatus += doWork();
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					handler.post(new Runnable() {
						public void run() {
							spinner.setProgress(progressStatus);
							i++;
						}
					});
				}

				intent = new Intent(SplashScreenActivity.this, MainActivity.class);
				SplashScreenActivity.this.startActivity(intent);
				SplashScreenActivity.this.finish();
			}

			private int doWork() {

				return i++;
			}

		}).start();

	}

}
