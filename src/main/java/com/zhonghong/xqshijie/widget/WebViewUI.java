package com.zhonghong.xqshijie.widget;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.URLUtil;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.zhonghong.xqshijie.R;
import com.zhonghong.xqshijie.base.BaseActivity;
import com.zhonghong.xqshijie.dialog.InfoToast;
import com.zhonghong.xqshijie.util.FileUtils;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 */
public class WebViewUI extends WebView {

    private String yxjs;
    private Context mContext;
    private ProgressDialog mDialog;
    private String directory;
    public OnReceivedErrorListener onReceivedErrorListener;
    private boolean isExist;
    public boolean isBacking = false;
    public String currUrl = "";


    public WebViewUI(Context context) {
        super(context);
        mContext = context;
        directory = Environment.getExternalStorageDirectory() + File.separator
                + mContext.getPackageName() + File.separator + "xqshijie"
                + File.separator + "Downloads";
        init();
    }

    public WebViewUI(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        directory = Environment.getExternalStorageDirectory() + File.separator
                + mContext.getPackageName() + File.separator + "xqshijie"
                + File.separator + "Downloads";
        init();
    }

    public WebViewUI(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        directory = Environment.getExternalStorageDirectory() + File.separator
                + mContext.getPackageName() + File.separator + "xqshijie"
                + File.separator + "Downloads";
        init();
    }

    @SuppressLint("SetJavaScriptEnabled")
    @JavascriptInterface
    private void init() {
        setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);// 不加上，会显示白边
        WebSettings webSettings = getSettings();
        // 设置支持JavaScript脚本
        webSettings.setJavaScriptEnabled(true);
        // 设置可以访问文件
        webSettings.setAllowFileAccess(true);
        // 设置支持缩放
        webSettings.setSupportZoom(true);
        // 提高渲染的优先级
        webSettings.setRenderPriority(RenderPriority.HIGH);
        webSettings.setDatabaseEnabled(true);
        String dbDir = mContext.getDir("database", Context.MODE_PRIVATE)
                .getPath();
        // 设置数据库路径
        webSettings.setDatabasePath(dbDir);
        // 使用localStorage则必须打开
        webSettings.setDomStorageEnabled(false);

        // 开启应用程序缓存
        webSettings.setAppCacheEnabled(false);
//		String cacheDir = mContext.getDir("cache", Context.MODE_PRIVATE)
//				.getPath();
        // 无缓存
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
//		// 设置应用缓存的路径
//		webSettings.setAppCachePath(cacheDir);
        // 设置应用缓存的最大尺寸
//		webSettings.setAppCacheMaxSize(1024 * 1024 * 8);

        // 将图片下载阻塞
        webSettings.setBlockNetworkImage(true);
        this.setSaveEnabled(false);
        setDownloadListener(new DownloadListener() {

            @Override
            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimeType,
                                        long contentLength) {
                if (!Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED)) {
                    Toast.makeText(
                            mContext,
                            mContext.getResources().getString(
                                    R.string.moreapp_sdcard_not_exist),
                            Toast.LENGTH_SHORT).show();
                    return;
                }
//				CookieSyncManager cookieSyncManager = CookieSyncManager
//						.createInstance(mContext);
//				cookieSyncManager.sync();
//				CookieManager cookieManager = CookieManager.getInstance();
//				sessionID = cookieManager.getCookie(url);
                String fileName = URLUtil.guessFileName(url,
                        contentDisposition, mimeType);
                new DownloaderTask().execute(url, fileName);
            }
        });
        setWebViewClient(new WebViewClient() {
            @Override
            // 加载错误提示
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                if (failingUrl.startsWith("yixin://")) {// 拦截消息
                    return;
                }
                super.onReceivedError(view, errorCode, description, failingUrl);
                InfoToast.makeText(
                        mContext,
                        mContext.getString(R.string.open_web_fail, errorCode
                                + " " + description), Gravity.CENTER, 0, 0,
                        Toast.LENGTH_SHORT).show();
                if (onReceivedErrorListener != null)
                    onReceivedErrorListener.onReceivedError(view, errorCode,
                            description, failingUrl);
            }

            @Override
            public void onFormResubmission(WebView view, Message dontResend,
                                           Message resend) {
                resend.sendToTarget();
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

            }

            @Override
            @JavascriptInterface
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                getSettings().setBlockNetworkImage(false);//
                if (!TextUtils.isEmpty(url) && !url.startsWith("yixin://")
                        && !url.equals(currUrl)) {
                    currUrl = url;
                }
                isBacking = false;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("mailto:")) {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse(url));
                        getContext().startActivity(intent);
                    } catch (Exception e) {
                        ((BaseActivity) getContext())
                                .toastToMessage(R.string.no_right_mail);
                    }
                    return true;
                }
                if (url.startsWith("tel:")) {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse(url));
                        getContext().startActivity(intent);
                    } catch (Exception e) {
                        ((BaseActivity) getContext())
                                .toastToMessage(R.string.no_right_tel);
                    }
                    return true;
                }
                if (url.startsWith("sms:")) {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse(url));
                        getContext().startActivity(intent);
                    } catch (Exception e) {
                        ((BaseActivity) getContext())
                                .toastToMessage(R.string.no_right_sendsms);
                    }
                    return true;
                }
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
    }

    private void showProgressDialog() {
        if (mDialog == null) {
            mDialog = new ProgressDialog(mContext);
            mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置风格为圆形进度条
            mDialog.setMessage(getResources().getString(
                    R.string.moreapp_downloading));
            mDialog.setIndeterminate(false);// 设置进度条是否为不明确
            mDialog.setCancelable(true);// 设置进度条是否可以按退回键取消
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.setOnDismissListener(new OnDismissListener() {

                @Override
                public void onDismiss(DialogInterface dialog) {
                    mDialog = null;
                }
            });
            mDialog.show();

        }
    }

    private void closeProgressDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
    }


    private class DownloaderTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String url = params[0];
            String fileName = params[1];
            HttpURLConnection connection = null;
            InputStream inputStream = null;
            try {
                URL URL = new URL(url);
                connection = (HttpURLConnection) URL.openConnection();
                connection.setConnectTimeout(30 * 1000);
                connection.setReadTimeout(30 * 1000);
                connection.setRequestProperty("Content-Type", "text/plain;charset=UTF-8");
//				connection.setRequestProperty("Cookie", sessionID);
                String contentType = connection.getContentType();
                String contentDisposition = connection.getHeaderField("Content-Disposition");
                fileName = FileUtils.getFileName(contentDisposition, contentType);
                inputStream = connection.getInputStream();
                Log.i("tag", "fileName=" + fileName);

                File dir = new File(directory);
                if (!dir.exists() && !dir.isDirectory()) {
                    dir.mkdir();
                }
                File file = new File(directory, fileName);
                if (file.exists()) {
                    Log.i("tag", "The file has already exists.");
                    isExist = true;
                    return fileName;
                }

                if (HttpURLConnection.HTTP_OK == connection.getResponseCode()) {
                    FileUtils.writeToSDCard(directory, fileName, inputStream);
                    // entity.consumeContent();
                    return fileName;
                } else {
                    return null;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            } finally {
                try {
                    if (inputStream != null) inputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    if (connection != null) connection.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            closeProgressDialog();
            if (result == null) {
                Toast.makeText(
                        mContext,
                        mContext.getResources().getString(
                                R.string.update_networktimeout),
                        Toast.LENGTH_SHORT).show();
                return;
            }

            File file = new File(directory, result);
            Log.i("tag", "Path=" + file.getAbsolutePath());
            if (!isExist) {
                Toast.makeText(
                        mContext,
                        mContext.getResources().getString(
                                R.string.download_success)
                                + file.getAbsolutePath(), Toast.LENGTH_SHORT)
                        .show();
            }
            try {
                mContext.startActivity(FileUtils.openFile(file.getAbsolutePath()));
            } catch (Exception e) {
                Toast.makeText(
                        mContext,
                        mContext.getResources().getString(
                                R.string.cant_open_suffix_file),
                        Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }

    public interface OnReceivedErrorListener {
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl);
    }
}