package com.zhonghong.xqshijie.appupdate;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.zhonghong.xqshijie.R;
import com.zhonghong.xqshijie.data.response.UpdateAppResponse;
import com.zhonghong.xqshijie.interfaces.NetInterface;
import com.zhonghong.xqshijie.util.AppUtils;
import com.zhonghong.xqshijie.util.FileUtils;
import com.zhonghong.xqshijie.util.LogUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

public class UpdateAppService extends Service implements NetInterface {
    private UpdateAppController updateAppController = null;
    private UpdateAppResponse mUpdateResponse;
    private Notification notify;
    private NotificationManager manager;
    //是否正在下载
    private boolean isDownApp = false;
    //初次下载网络异常，没有获取到文件的长度 true 表示是这种异常，false 表示是这种异常
    private boolean isException =false;
    // 下载应用存放全路径
    private String FILE_NAME = null;
    //apk文件的大小
    private int apkLength = 0;
    //apk下载到的地方
    private int downWhile = 0;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.e("开启更新服务");
        // 下载应用存放全路径
        if (FileUtils.isSDCardAvailable()) {
            FILE_NAME = Environment.getExternalStorageDirectory() + "/" + getResources().getString(R.string.app_place_folder) + "/" + getResources().getString(R.string.app_place_apk);
        }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            //网络连接上是接收到的广播
            String neReceiver = intent.getStringExtra(UpdateConstants.RECEIVER_FLAG);
            //弹出框提示更新的标志
            boolean isupdate = intent.getBooleanExtra(UpdateConstants.ACTIVITY_FLAG, false);
            //连上wifi，且在通知栏有下载状态时才启动断点下载
            if (UpdateConstants.RECEIVER_TEXT.equals(neReceiver) && isDownApp == true) {
                startDownApp(mUpdateResponse.mUpdate_url);
                LogUtils.d("断点下载");
            } else if (!isDownApp && !UpdateConstants.RECEIVER_TEXT.equals(neReceiver) && !isupdate) {
                //没有下载时才判断,去获取更新信息
                LogUtils.d("检查新版本");
                getUpdateMessage();
            } else if (isupdate||isException==true) {
                //下载软件更新包
                LogUtils.d("下载新版本");
                startDownApp(mUpdateResponse.mUpdate_url);
            }
            intent = null;
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        /**
         *移除通知
         */
        if (manager != null) {
            manager.cancel(100);
        }
        /**
         * 清空所有handler 消息
         */
        if (completeHandler != null) {
            completeHandler.removeCallbacksAndMessages(null);
        }
        LogUtils.e("服务被销毁");
    }

    // 获取更新详情
    private void getUpdateMessage() {
        if (updateAppController == null) {
            updateAppController = new UpdateAppController(this);
        }
        updateAppController.handleGetUpdateAppByNet(this, AppUtils.getVerName());
        LogUtils.e("去联网获取更新");
    }

    @Override
    public void onNetResult(String interfaceAction, Object result) {

        mUpdateResponse = (UpdateAppResponse) result;
        String mResult = mUpdateResponse.mResult;
        LogUtils.e(result.toString());
        //测试使用
        mResult = "01";
        mUpdateResponse.mUpdate_force = "0";
        mUpdateResponse.mUpdate_content = "解决bug";
        mUpdateResponse.mUpdate_version = "1.2";
//        mUpdateResponse.mUpdate_url="http://v.meituan.net/movie/videos/54b5ef29210142259b85810ceaa8a5a0.mp4";
        mUpdateResponse.mUpdate_url = "http://dldir1.qq.com/dlomg/weishi/weishi_guanwang.apk";
        //测试使用
        if ("02".equals(mResult)) {
            //没有更新
            EventBus.getDefault().post(new MessageBean(UpdateConstants.CANCEL_ISNEW));
        } else if ("00".equals(mResult)) {
            //请求跟新失败
            EventBus.getDefault().post(new MessageBean(UpdateConstants.CANCEL_NETERROR));
        } else if ("03".equals(mResult)) {
            //请求协议参数不完整
            EventBus.getDefault().post(new MessageBean(UpdateConstants.CANCEL_NETERROR));
        } else if ("04".equals(mResult)) {
            //请求错误
            EventBus.getDefault().post(new MessageBean(UpdateConstants.CANCEL_NETERROR));
        } else if ("05".equals(mResult)) {
            // 非法请求
            EventBus.getDefault().post(new MessageBean(UpdateConstants.CANCEL_NETERROR));
        } else if ("01".equals(mResult)) {
            EventBus.getDefault().post(new MessageBean(UpdateConstants.CANCEL_UPDATE));
            String content = this.getResources().getString(R.string.dialog_updte_tag) + mUpdateResponse.mUpdate_version +
                    "\n" +
                    this.getResources().getString(R.string.dialog_updte_content) + mUpdateResponse.mUpdate_content;
            //有跟新
            //0不强制更新
            if ("0".equals(mUpdateResponse.mUpdate_force)) {
                UpdateAppController.startUpdateActivityDialog(UpdateAppService.this, false, content);
            } else if ("1".equals(mUpdateResponse.mUpdate_force)) {
                //1强制更新
                UpdateAppController.startUpdateActivityDialog(UpdateAppService.this, true, content);
            }
        }
    }


    @Override
    public void onNetError(String interfaceAction, Throwable ex, boolean isOnCallback) {
        EventBus.getDefault().post(new MessageBean(UpdateConstants.CANCEL_NETERROR));
    }

    @Override
    public void onNetFinished(String interfaceAction) {
    }

    /**
     * 去下载app
     */
    public void startDownApp(String path) {

        //建立下载的apk文件
        if (FILE_NAME == null) {
            Toast.makeText(UpdateAppService.this, getResources().getString(R.string.sd_cannot_find), Toast.LENGTH_SHORT).show();

            return;
        }
        //发送通知栏
        if (manager == null && notify == null) {
            manager = (NotificationManager) this
                    .getSystemService((this.NOTIFICATION_SERVICE));
            notify = new Notification();
            notify.icon = R.drawable.ic_launcher;
            // 通知栏显示所用到的布局文件
            notify.contentView = new RemoteViews(this.getPackageName(),
                    R.layout.view_notify_item);
            notify.contentView.setTextViewText(
                    R.id.notify_updata_values_tv, UpdateAppService.this.getResources().getString(R.string.app_name) );
            manager.notify(100, notify);
        }
        //多少是的

        File fileInstall = new File(FILE_NAME);
        downLoadSchedule(path, completeHandler, this,
                fileInstall);

    }

    public void downLoadSchedule(final String uri,
                                 final Handler handler, Context context, final File file) {
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // 每次读取文件的长度
        final int perLength = 4096;
        //文件大小
        new Thread() {
            @Override
            public void run() {
                super.run();
                FileOutputStream out = null;
                InputStream in = null;
                RandomAccessFile access = null;
                HttpURLConnection conn = null;
                try {
                    URL url = new URL(uri);
                    // 设置断点下载开始下载的位置单位为字节
                    if (apkLength != 0) {
                        conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestProperty("Range", "bytes=" + downWhile + "-" + apkLength);
                        Thread.sleep(1000);
                        // 断点下载使用的文件对象RandomAccessFile
                        access = new RandomAccessFile(FILE_NAME, "rw");
                        // 移动指针到开始位置
                        access.seek(downWhile);
                    } else {
                        conn = (HttpURLConnection) url.openConnection();
                        //设置非断点下载流
                        out = new FileOutputStream(file);
                    }
                    conn.setDoInput(true);
                    conn.connect();
                    // 每次读取1k
                    byte[] buffer = new byte[perLength];
                    int len = -1;
                    int temp = 0;
                    //第一次下载
                    if (conn.getResponseCode() == 200) {
                        int length = apkLength = conn.getContentLength();
                        in = conn.getInputStream();
                        while ((len = in.read(buffer)) != -1) {
                            //记录下载到的地方
                            downWhile += len;
                            // 写入文件
                            out.write(buffer, 0, len);
                            // 当前进度
                            int schedule = (int) ((file.length() * 100) / length);
                            // 通知更新进度（10,7,4整除才通知，没必要每次都更新进度）
                            if (temp != schedule
                                    && (schedule % 10 == 0 || schedule % 4 == 0 || schedule % 7 == 0)) {
                                // 保证同一个数据只发了一次
                                temp = schedule;
                                UpdateAppService.this.isDownApp = true;
                                handler.sendEmptyMessage(schedule);
                            }
                        }
                        //断点续传
                    } else if (conn.getResponseCode() == 206) {
                        in = conn.getInputStream();
                        while ((len = in.read(buffer)) != -1) {
                            //记录下载到的地方
                            downWhile += len;
                            // 写入文件
                            access.write(buffer, 0, len);
                            // 当前进度
                            int schedule = (int) ((file.length() * 100) / apkLength);
                            // 通知更新进度（10,7,4整除才通知，没必要每次都更新进度）
                            if (temp != schedule
                                    && (schedule % 10 == 0 || schedule % 4 == 0 || schedule % 7 == 0)) {
                                // 保证同一个数据只发了一次
                                temp = schedule;
                                UpdateAppService.this.isDownApp = true;
                                handler.sendEmptyMessage(schedule);
                            }
                        }
                    }
                    isException=false;
                } catch (Exception e) {
                    isException=true;
                    handler.sendEmptyMessage(-1);
                    e.printStackTrace();
                } finally {
                    try {
                        out.flush();
                        out.close();
                        in.close();
                        conn.disconnect();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    /**
     * 更新通知栏
     */
    private Handler completeHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            // 更新通知栏
            if(msg.what==-1) {
                //下载失败时
                notify.contentView.setTextViewText(
                        R.id.notify_updata_values_tv, UpdateAppService.this.getResources().getString(R.string.app_name));
                notify.contentView.setTextViewText(
                        R.id.notify_updata_state, "下载失败");
                notify.contentView.setViewVisibility(R.id.notify_updata_progress,
                        View.GONE);
                notify.contentView.setViewVisibility(R.id.notify_updata_state,
                        View.VISIBLE);
                manager.notify(100, notify);
            }else if (msg.what < 100&&msg.what>=0) {
                notify.contentView.setTextViewText(
                        R.id.notify_updata_values_tv, UpdateAppService.this.getResources().getString(R.string.app_name) + msg.what + "%");
                notify.contentView.setProgressBar(R.id.notify_updata_progress,
                        100, msg.what, false);
                notify.contentView.setViewVisibility(R.id.notify_updata_progress,
                        View.VISIBLE);
                notify.contentView.setViewVisibility(R.id.notify_updata_state,
                        View.GONE);

                manager.notify(100, notify);
            } else {
                notify.contentView.setTextViewText(
                        R.id.notify_updata_values_tv, UpdateAppService.this.getResources().getString(R.string.updte_complete));
                notify.contentView.setProgressBar(R.id.notify_updata_progress,
                        100, msg.what, false);
                notify.contentView.setViewVisibility(R.id.notify_updata_progress,
                        View.VISIBLE);
                notify.contentView.setViewVisibility(R.id.notify_updata_state,
                        View.GONE);
                // 清除通知栏
                manager.cancel(100);
                //没有正在下载版本
                UpdateAppService.this.isDownApp = false;
                File file = new File(FILE_NAME);
                AppUtils.installApk(UpdateAppService.this, file);
                UpdateAppController.stopUpdateService(UpdateAppService.this);
            }
        }

        ;
    };
}
