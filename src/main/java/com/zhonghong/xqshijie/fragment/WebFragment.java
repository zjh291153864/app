package com.zhonghong.xqshijie.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebBackForwardList;
import android.webkit.WebChromeClient;
import android.webkit.WebHistoryItem;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.zhonghong.xqshijie.R;
import com.zhonghong.xqshijie.activity.MainActivity;
import com.zhonghong.xqshijie.base.BaseFragment;
import com.zhonghong.xqshijie.util.StringUtils;
import com.zhonghong.xqshijie.widget.TitleView;
import com.zhonghong.xqshijie.widget.WebViewUI;

/**
 * Created by xiezl on 16/6/14.
 */
public class WebFragment extends BaseFragment {

    private TitleView mTitle;
    private WebViewUI mWebView;
    private ProgressBar pBar;
    private String titleStr = "";
    private String url = "https://m.xqshijie.com/";//处理后的URL
    private ValueCallback<Uri> mUploadMessage;
    private String mCameraFilePath;
    // 标志位，标志已经初始化完成。
    private boolean isPrepared;

    @Override
    public View initContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_web, null);
        initView(contentView);
        return contentView;
    }


    private void initView(View view) {
        mTitle = (TitleView) view.findViewById(R.id.title);
        mWebView = (WebViewUI) view.findViewById(R.id.webview);
        pBar = (ProgressBar) view.findViewById(R.id.pbar);
        //初始化view的各控件完成
        isPrepared = true;
        lazyLoad();
    }


    @Override
    protected void handleCreate() {
        if (getArguments() != null && getArguments().get("title") != null) {
            titleStr = (String) getArguments().get("title");
            mTitle.setTitle(titleStr);
        }
        if (getArguments() != null && getArguments().get("url") != null) {
            url = (String) getArguments().get("url");
        }
        try {
            mWebView.loadUrl(url);
        } catch (Exception e) {
            e.printStackTrace();
        }

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

    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible) {
            return;
        }
    }

    @Override
    protected void processMessage(Message msg) {

    }

    @Override
    protected void customOnClick(View v) {
        switch (v.getId()) {
            case R.id.ll_common_title_TV_left:
                onBackPressed();
                break;
            case R.id.ll_common_title_TV_right:
                shareOnClick(mWebView.currUrl);
                break;
        }
    }

    @Override
    public void onNetResult(String interfaceAction, Object result) {

    }

    @Override
    public void onNetError(String interfaceAction, Throwable ex, boolean isOnCallback) {

    }

    @Override
    public void onNetFinished(String interfaceAction) {

    }

    private void shareOnClick(final String terminalUrl) {

    }

    private int isSamePage(int cidx, WebBackForwardList wbfl) {
        int result = cidx;
        for (int i = cidx; i >= 0; i--) {
            if (i >= 1 && wbfl.getItemAtIndex(i).getUrl().equals(wbfl.getItemAtIndex(i - 1).getUrl())) {
                result = (cidx - i);
            } else {
                result = (cidx - i);
                break;

            }
        }
        return result;
    }

    /**
     * 返回
     */
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            WebBackForwardList wbfl = mWebView.copyBackForwardList();
            int cidx = wbfl.getCurrentIndex();
            int bidx = 0;
            if (cidx >= 2) {
                if (wbfl.getCurrentItem().getUrl().equals(wbfl.getItemAtIndex(cidx - 1).getUrl())) {
                    int index = isSamePage(cidx, wbfl);
                    if (index == cidx) {
                        MainActivity mainActivity= (MainActivity) getActivity();
                        mainActivity.mMenu.toggle();
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
            MainActivity mainActivity= (MainActivity) getActivity();
            mainActivity.mMenu.toggle();
        }
    }
}
