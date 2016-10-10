package com.zhonghong.xqshijie.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebBackForwardList;
import android.webkit.WebChromeClient;
import android.webkit.WebHistoryItem;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.zhonghong.xqshijie.R;
import com.zhonghong.xqshijie.app.Constants;
import com.zhonghong.xqshijie.base.BaseActivity;
import com.zhonghong.xqshijie.util.StringUtils;
import com.zhonghong.xqshijie.widget.TitleView;
import com.zhonghong.xqshijie.widget.WebViewUI;

import java.io.File;
import java.net.URL;
import java.util.HashMap;

public class WebViewActivity extends BaseActivity {
	private TitleView mTitle;
	private WebViewUI mWebView;
	private ProgressBar pBar;
	private String titleStr = "";
	private String url = "";//处理后的URL
	private ValueCallback<Uri> mUploadMessage;
	private String mCameraFilePath;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_webview);
		initView();
		setValues();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

	}

	private void initView(){
		mTitle = (TitleView) findViewById(R.id.title);
		mWebView = (WebViewUI) findViewById(R.id.webview);
		pBar = (ProgressBar) findViewById(R.id.pbar);

		mWebView.setWebChromeClient(new WebChromeClient() {
			@Override//加载进度
			@JavascriptInterface
			public void onProgressChanged(WebView view, int newProgress) {
				super.onProgressChanged(view, newProgress);
				pBar.setProgress(newProgress);
				if (!TextUtils.isEmpty(view.getUrl()) && view.getUrl().equals(mWebView.currUrl)) {
					pBar.setVisibility(View.GONE);
				} else {
					if (newProgress < 100) {
						pBar.setVisibility(View.VISIBLE);
					}
					if (newProgress == 100) {
						pBar.setVisibility(View.GONE);
					}
				}
			}

			@Override//JS异常
			public void onConsoleMessage(String message, int lineNumber, String sourceID) {
//				Log.e(Constants.LOG_TAG, "message:" + message + ";lineNumber:"
//						+ lineNumber + ";sourceID:" + sourceID);
			}

			@Override
			public void onReceivedTitle(WebView view, String title) {
				super.onReceivedTitle(view, title);
				if (TextUtils.isEmpty(title)) {
					mTitle.setTitle(titleStr);
				} else {
					mTitle.setTitle(title);
				}
			}

			//Android > 4.1.1 调用这个方法
			public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
				mUploadMessage = uploadMsg;
//				startActivityForResult(createDefaultOpenableIntent(acceptType), WebViewActivity.FILECHOOSER_RESULTCODE);
			}

			// 3.0 + 调用这个方法
			public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
				openFileChooser(uploadMsg, acceptType, null);
			}

			// Android < 3.0 调用这个方法
			public void openFileChooser(ValueCallback<Uri> uploadMsg) {
				openFileChooser(uploadMsg, null);
			}
		});
	}



	private void setValues(){
		try{
			titleStr = getIntent().hasExtra("title") ? getIntent().getStringExtra("title") : "";
			if (!StringUtils.isNull(titleStr)) {
				mTitle.setTitle(titleStr);
			}


			url = getIntent().hasExtra("url")? getIntent().getStringExtra("url") : "";
			mWebView.loadUrl(url);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	protected void customOnClick(View v) {
		switch(v.getId()){
			case R.id.ll_common_title_TV_left:
				goBack();
				break;
			case R.id.ll_common_title_TV_right:
				shareOnClick(mWebView.currUrl);
				break;
		}
	}

	private void shareOnClick(final String terminalUrl) {

	}


	private HashMap<String, String> getUrlParamMap() {
		HashMap<String, String> queryMap = null;
		try {
			queryMap = new HashMap<String, String>();
			URL urlObject = new URL(url);
			String query = urlObject.getQuery().trim();
			String[] queryList = query.split("&");
			if (queryList.length > 0) {
				for (int i = 0; i < queryList.length; i++) {
					String[] params = queryList[i].split("=");
					if (params.length == 2) {
						queryMap.put(params[0].trim(), params[1].trim());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return queryMap;
	}

	/**
     * 按键响应，在WebView中查看网页时，按返回键的时候按浏览历史退回,如果不做此项处理则整个WebView返回退出
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        // Check if the key event was the Back button and if there's history
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()){
        	goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

	private int isSamePage(int cidx, WebBackForwardList wbfl) {
		int result = cidx;
		for (int i = cidx; i >= 0; i--) {
			if (i >= 1&& wbfl.getItemAtIndex(i).getUrl().equals(wbfl.getItemAtIndex(i - 1).getUrl())) {
				result = (cidx-i);
			} else {
				result = (cidx-i);
				break;

			}
		}
		return result;
	}

	private void goBack() {
		if (mWebView.canGoBack()) {
			WebBackForwardList wbfl = mWebView.copyBackForwardList();
			int cidx = wbfl.getCurrentIndex();
			int bidx = 0;
			if (cidx >= 2) {
				if (wbfl.getCurrentItem().getUrl().equals(wbfl.getItemAtIndex(cidx - 1).getUrl())) {
					int index = isSamePage(cidx, wbfl);
					if (index == cidx) {
						finish();
					} else {
						mWebView.goBackOrForward(-(index + 1));
						bidx = cidx - index;
					}
				} else {
					mWebView.goBack();
					bidx = cidx - 1;
				}
			} else {
				mWebView.goBack();
				bidx = cidx - 1;
			}
			if (bidx >= 0) {
				WebHistoryItem whi = wbfl.getItemAtIndex(bidx);
				mTitle.setTitle(StringUtils.repNull(whi.getTitle()));
			}
		} else {
			finish();
		}
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public View initContentView() {
		return null;
	}

	@Override
	public void handleCreate() {

	}

	private Intent createDefaultOpenableIntent(String acceptType) {
        // Create and return a chooser with the default OPENABLE
        // actions including the camera, camcorder and sound
        // recorder where available.
		Intent i = new Intent(Intent.ACTION_GET_CONTENT);
		i.addCategory(Intent.CATEGORY_OPENABLE);
		if(TextUtils.isEmpty(acceptType)){
			i.setType("*/*");
		}else{
			i.setType(acceptType);
		}

		Intent chooser = createChooserIntent(createCameraIntent()
//				, createCamcorderIntent()
//				, createSoundRecorderIntent()
				);
		chooser.putExtra(Intent.EXTRA_INTENT, i);
		return chooser;
	}

	private Intent createChooserIntent(Intent... intents) {
		Intent chooser = new Intent(Intent.ACTION_CHOOSER);
		chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, intents);
//		chooser.putExtra(Intent.EXTRA_TITLE, "File Chooser");
		return chooser;
	}

	private Intent createCameraIntent() {
		Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		File cameraDataDir = new File(Environment.getExternalStorageDirectory() + "/" + Constants.PACKAGENAME + "/oncon/photos/");
		cameraDataDir.mkdirs();
		mCameraFilePath = cameraDataDir.getAbsolutePath() + File.separator + "camera_"+System.currentTimeMillis() + ".jpg";
		cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(mCameraFilePath)));
		return cameraIntent;
    }

	private Intent createCamcorderIntent() {
		return new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
    }

	private Intent createSoundRecorderIntent() {
		return new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
    }
}