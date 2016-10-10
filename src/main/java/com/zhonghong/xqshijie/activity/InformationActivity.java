package com.zhonghong.xqshijie.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.zhonghong.xqshijie.R;
import com.zhonghong.xqshijie.app.SharedPreferencesTag;
import com.zhonghong.xqshijie.base.BaseActivity;
import com.zhonghong.xqshijie.controller.ImageController;
import com.zhonghong.xqshijie.controller.InformationController;
import com.zhonghong.xqshijie.data.response.AccessTokenResponse;
import com.zhonghong.xqshijie.data.response.ImageResponse;
import com.zhonghong.xqshijie.data.response.LoginResponse;
import com.zhonghong.xqshijie.dialog.InfoToast;
import com.zhonghong.xqshijie.interfaces.NetInterface;
import com.zhonghong.xqshijie.util.FileUtils;
import com.zhonghong.xqshijie.util.IdNumberUtils;
import com.zhonghong.xqshijie.util.ImageLoaderUtil;
import com.zhonghong.xqshijie.util.PermissionsChecker;
import com.zhonghong.xqshijie.util.PublicUtils;
import com.zhonghong.xqshijie.util.SharedPreferencesUtil;
import com.zhonghong.xqshijie.util.StringUtils;
import com.zhonghong.xqshijie.widget.CleanableEditText;
import com.zhonghong.xqshijie.widget.RoundAngleImageView;
import com.zhonghong.xqshijie.widget.SelectPicPopupWindow;
import com.zhonghong.xqshijie.widget.TitleView;
import com.zhonghong.xqshijie.widget.dialog.NormalDialog;
import com.zhonghong.xqshijie.widget.dialog.OnBtnClickL;

import java.io.File;

/**
 * Created by jh on 2016/6/28.
 */
public class InformationActivity extends BaseActivity{

    private TitleView mTitle;//标题栏
    private RoundAngleImageView mIvInformationReplaceAvatar;//更换头像img
    private SelectPicPopupWindow mPopupWindow;//弹出框
    private CleanableEditText mEtInformationNickname;//昵称输入框
    private CleanableEditText mEtInformationFullName;//姓名输入框
    private CleanableEditText mEtInformationIdNumber;//身份证输入框
    private CleanableEditText mEtInformationPhone;//手机号输入框
    private RadioButton mRbInformationMr;//先生
    private RadioButton mRbInformationMs;//女士
    private Button mBtnInformationSave;//保存
    // 所需的拍照和存储权限
    private String[] PERMISSIONS = new String[]{Manifest.permission.CAMERA , Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private String mAvatar="";//头像地址
    private String mNickname="";//昵称
    private String mFullName="";//姓名
    private String mIdNumber="";//身份证
    private String mPhone="";//手机号
    private String mGender="";//性别
    private String mMemberId="";//用户Id
    private String mPassword;//用户密码
    private InformationController mInformationController;
    private static final int IMAGE_REQUEST_CODE = 0;//照片
    private static final int CAMERA_REQUEST_CODE = 1;//相机
    private static final int RESULT_REQUEST_CODE = 2;
    private static final int REQUEST_CODE = 3; // 权限请求码
    private static final String IMAGE_FILE_NAME = "faceImage.jpg";
    private File sdcardTempFile;//临时文件
    private ImageController imageController;

    @Override
    public View initContentView() {
        View contentView = LayoutInflater.from(this).inflate(R.layout.activity_information, null);
        mTitle = (TitleView)contentView.findViewById(R.id.title);
        mIvInformationReplaceAvatar = (RoundAngleImageView)contentView.findViewById(R.id.iv_information_replace_avatar);
        mEtInformationNickname = (CleanableEditText)contentView.findViewById(R.id.et_information_nickname);
        mEtInformationFullName = (CleanableEditText)contentView.findViewById(R.id.et_information_full_name);
        mEtInformationIdNumber = (CleanableEditText)contentView.findViewById(R.id.et_information_id_number);
        mEtInformationPhone = (CleanableEditText)contentView.findViewById(R.id.et_information_phone);
        mRbInformationMr = (RadioButton)contentView.findViewById(R.id.rb_information_mr);
        mRbInformationMs = (RadioButton)contentView.findViewById(R.id.rb_information_ms);
        mBtnInformationSave = (Button)contentView.findViewById(R.id.btn_information_save);

        mIvInformationReplaceAvatar.setOnClickListener(this);
        mBtnInformationSave.setOnClickListener(this);
        mTitle.setLeftImageOnClickListener(this);
        return contentView;
    }

    @Override
    public void handleCreate() {
        mMemberId= SharedPreferencesUtil.getInstance(this).getStringValue(SharedPreferencesTag.MEMBER_ID);
        mAvatar= SharedPreferencesUtil.getInstance(this).getStringValue(SharedPreferencesTag.MEMBER_AVATAR);
        mNickname= SharedPreferencesUtil.getInstance(this).getStringValue(SharedPreferencesTag.MEMBER_NICKNAME);
        mFullName= SharedPreferencesUtil.getInstance(this).getStringValue(SharedPreferencesTag.MEMBER_FULLNAME);
        mIdNumber= SharedPreferencesUtil.getInstance(this).getStringValue(SharedPreferencesTag.MEMBER_ID_NUMBER);
        mPhone= SharedPreferencesUtil.getInstance(this).getStringValue(SharedPreferencesTag.MEMBER_MOBILE);
        mGender= SharedPreferencesUtil.getInstance(this).getStringValue(SharedPreferencesTag.MEMBER_GENDER);
        mPassword= SharedPreferencesUtil.getInstance(this).getStringValue(SharedPreferencesTag.MEMBER_PASSWORD);

        if(!StringUtils.isNull(mAvatar)){
            ImageLoaderUtil.getInstance().loadImageByHeadPortrait(mAvatar,mIvInformationReplaceAvatar,R.drawable.ic_member_centre_head_portrait);
        }
        if(!StringUtils.isNull(mNickname)){
            mEtInformationNickname.setText(mNickname);
        }
        if(!StringUtils.isNull(mFullName)){
            mEtInformationFullName.setText(mFullName);
            mEtInformationFullName.setFocusable(false);
        }
        if(!StringUtils.isNull(mIdNumber)){
            try {
                mIdNumber=IdNumberUtils.decrypt(mIdNumber);
                mEtInformationIdNumber.setText(mIdNumber);
                mEtInformationIdNumber.setFocusable(false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(!StringUtils.isNull(mPhone)){
            mEtInformationPhone.setText(mPhone);
            mEtInformationPhone.setFocusable(false);
        }
        if(!StringUtils.isNull(mGender)){
            if(mGender.equals("1")){//男
                mRbInformationMr.setChecked(true);
            }else{//女
                mRbInformationMs.setChecked(true);
            }
        }
    }

    @Override
    protected void customOnClick(View v) {
        switch (v.getId()) {
            case R.id.common_title_TV_left:
                finish();
                break;
            case R.id.iv_information_replace_avatar:
//                    实例化SelectPicPopupWindow
                mPopupWindow = new SelectPicPopupWindow(this, this);
//                    设置PopupWindow在layout中显示的位置
                mPopupWindow.alertPopupwindow(this.findViewById(R.id.ll_information_layout));
                sdcardTempFile = new File("mnt/sdcard", "sfang_my_icon.jpg");
                break;
            case R.id.tv_ppwd_pick_photo://相册
                mPopupWindow.dismiss();
                //选择本地图片
                Intent intentFromGallery = new Intent();
                intentFromGallery.setType("image/*"); // 设置文件类型
                intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intentFromGallery, IMAGE_REQUEST_CODE);
                break;
            case R.id.tv_ppwd_take_photo://拍照
                // 缺少权限时, 进入权限配置页面
                if (PermissionsChecker.lacksPermissions(this,PERMISSIONS)) {
                    ActivityCompat.requestPermissions(this, PERMISSIONS, REQUEST_CODE);
                }else{
                    mPopupWindow.dismiss();
                    Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    // 判断存储卡是否可以用，可用进行存储
                    if (FileUtils.isSDCardAvailable()) {
                        intentFromCapture.putExtra( MediaStore.EXTRA_OUTPUT,Uri.fromFile(new File(Environment.getExternalStorageDirectory(),IMAGE_FILE_NAME)));
                    }
                    startActivityForResult(intentFromCapture,CAMERA_REQUEST_CODE);
                }
                break;
            case R.id.btn_information_save:
                mNickname=mEtInformationNickname.getText().toString();
                mFullName=mEtInformationFullName.getText().toString();
                mPhone=mEtInformationPhone.getText().toString();
                mIdNumber=mEtInformationIdNumber.getText().toString();
                if(mRbInformationMr.isChecked()){
                    mGender="1";
                }else if(mRbInformationMs.isChecked()){
                    mGender="2";
                }
                analyzingData();
                break;
        }
    }

    /**
     * 用户权限处理,
     * 如果全部获取, 则直接过.
     * 如果权限缺失, 则提示Dialog.
     * @param requestCode  请求码
     * @param permissions  权限
     * @param grantResults 结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE){
            if(PermissionsChecker.lacksPermissions(this,PERMISSIONS)){
                final NormalDialog normalDialog=new NormalDialog(InformationActivity.this);
                normalDialog.isTitleShow(false).bgColor(Color.parseColor("#FFFFFF"))
                        .cornerRadius(5)
                        .contentGravity(Gravity.CENTER)
                        .contentTextColor(Color.parseColor("#2A2A2A"))
                        .content("当前应用缺少必要的权限。\n"+"请点击'确定'-'权限'打开所需的权限。")
                        .btnTextColor(Color.parseColor("#2A2A2A"), Color.parseColor("#2A2A2A"))
                        .show();
                normalDialog.setOnBtnClickL(new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        normalDialog.dismiss();
                    }
                }, new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        normalDialog.dismiss();
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.setData(Uri.parse("package:" + getPackageName()));
                        startActivity(intent);
                    }
                });
            }else{
                mPopupWindow.dismiss();
                Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // 判断存储卡是否可以用，可用进行存储
                if (FileUtils.isSDCardAvailable()) {
                    intentFromCapture.putExtra( MediaStore.EXTRA_OUTPUT,Uri.fromFile(new File(Environment.getExternalStorageDirectory(),IMAGE_FILE_NAME)));
                }
                startActivityForResult(intentFromCapture,CAMERA_REQUEST_CODE);
            }
        }
    }

    //    资料判断
    private void analyzingData(){
        Log.i("aaa",mIdNumber);
        if(StringUtils.isNull(mNickname)){
            InfoToast.makeText(this, this.getString(R.string.please_enter_a_nickname), Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringUtils.isNull(mFullName)) {
            InfoToast.makeText(this, this.getString(R.string.please_type_in_your_name), Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringUtils.isNull(mGender)) {
            InfoToast.makeText(this, this.getString(R.string.please_select_a_gender), Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringUtils.isNull(mIdNumber)){
            InfoToast.makeText(this, this.getString(R.string.please_enter_the_id_number), Toast.LENGTH_SHORT).show();
            return;
        }
        if (!PublicUtils.iDCardValidate(mIdNumber)) {
            InfoToast.makeText(this, this.getString(R.string.Invalid_id_card_number), Toast.LENGTH_SHORT).show();
            return;
        }
        if(StringUtils.isNull(mPhone)){
            InfoToast.makeText(this, this.getString(R.string.the_phone_number_can_not_be_empty), Toast.LENGTH_SHORT).show();
            return;
        }
        if(!PublicUtils.isTelNum(mPhone)){
            InfoToast.makeText(this, this.getString(R.string.the_phone_number_is_not_legitimate), Toast.LENGTH_SHORT).show();
            return;
        }else{
            try {
                mIdNumber=IdNumberUtils.encrypt(mIdNumber);
            } catch (Exception e) {
                e.printStackTrace();
            }
            informationModification();
        }
    }

//    修改资料
    private void informationModification() {
        this.showProgressDialog(R.string.loading1,false);//显示进度框
        if(mInformationController ==null){
            mInformationController =new InformationController(this);
        }
        mInformationController.handleUpdateMemberByNet(new NetInterface() {
            @Override
            public void onNetResult(String interfaceAction, Object result) {
                InformationActivity.this.hideProgressDialog();//隐藏进度框
                LoginResponse loginResponse=(LoginResponse) result;
                if(loginResponse.mResult.equals("01")){
                    SharedPreferencesUtil.getInstance(InformationActivity.this).putStringValue(SharedPreferencesTag.MEMBER_AVATAR,mAvatar);
                    SharedPreferencesUtil.getInstance(InformationActivity.this).putStringValue(SharedPreferencesTag.MEMBER_NICKNAME,mNickname);
                    SharedPreferencesUtil.getInstance(InformationActivity.this).putStringValue(SharedPreferencesTag.MEMBER_FULLNAME,mFullName);
                    SharedPreferencesUtil.getInstance(InformationActivity.this).putStringValue(SharedPreferencesTag.MEMBER_ID_NUMBER,mIdNumber);
                    SharedPreferencesUtil.getInstance(InformationActivity.this).putStringValue(SharedPreferencesTag.MEMBER_GENDER,mGender);
                    InfoToast.makeText(InformationActivity.this,getResources().getString(R.string.successfully_modified), Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    InfoToast.makeText(InformationActivity.this,loginResponse.mMsg, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onNetError(String interfaceAction, Throwable ex, boolean isOnCallback) {
                InformationActivity.this.hideProgressDialog();//隐藏进度框
                InfoToast.makeText(InformationActivity.this,getResources().getString(R.string.network_requests_fail),Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNetFinished(String interfaceAction) {
            }
        },mMemberId,mAvatar,mNickname,mFullName,mGender,mIdNumber);
    }

    /**
     * 添加图片
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //结果码不等于取消时候
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case IMAGE_REQUEST_CODE://相册选择
                    startPhotoZoom(data.getData());
                    break;
                case CAMERA_REQUEST_CODE://camera
                    if (FileUtils.isSDCardAvailable()) {
                        File tempFile = new File(Environment.getExternalStorageDirectory(),IMAGE_FILE_NAME);
                        startPhotoZoom(Uri.fromFile(tempFile));
                    } else {
                        InfoToast.makeText(this, "未找到存储卡，无法存储照片！", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case RESULT_REQUEST_CODE:
                    if (data != null) {
                        getAccessToken();
                    }
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 裁剪图片方法实现
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");//裁剪图片的意图
        intent.setDataAndType(uri, "image/*");// 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 裁剪框的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("output", Uri.fromFile(sdcardTempFile));
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 320);
        intent.putExtra("outputY", 320);
        intent.putExtra("outputFormat","JPEG");//图片格式
        intent.putExtra("noFaceDetection", false);//人脸识别
        intent.putExtra("return-data", true);//true:不返回uri，false返回uri
        startActivityForResult(intent, RESULT_REQUEST_CODE);
    }

//    获取Token
    private void getAccessToken(){
        this.showProgressDialog(R.string.loading1,false);//显示进度框
        if(imageController==null){
            imageController=new ImageController(this);
        }
        imageController.handleAccessTokenByNet(new NetInterface() {
            @Override
            public void onNetResult(String interfaceAction, Object result) {
                InformationActivity.this.hideProgressDialog();//隐藏进度框
                AccessTokenResponse accessTokenResponse= (AccessTokenResponse) result;
                if(accessTokenResponse.mCode==1){
                    upLoadImg(accessTokenResponse.mData);
                }else{
                    Log.i("result",accessTokenResponse.mMsg);
                    InfoToast.makeText(InformationActivity.this,accessTokenResponse.mMsg,Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onNetError(String interfaceAction, Throwable ex, boolean isOnCallback) {
                InformationActivity.this.hideProgressDialog();//隐藏进度框
                InfoToast.makeText(InformationActivity.this,getResources().getString(R.string.network_requests_fail),Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNetFinished(String interfaceAction) {
                InformationActivity.this.hideProgressDialog();//隐藏进度框
            }
        },mPhone,mPassword);
    }


    /**
     * 上传图片
     * @param accessToken
     */
    private void upLoadImg(String accessToken){
        this.showProgressDialog(R.string.loading1,false);//显示进度框
        if(imageController==null){
            imageController=new ImageController(this);
        }
        imageController.handleUpLoadImgByNet(new NetInterface() {
            @Override
            public void onNetResult(String interfaceAction, Object result) {
                InformationActivity.this.hideProgressDialog();//隐藏进度框
                ImageResponse imageResponse= (ImageResponse) result;
                if(imageResponse.mCode==1){
                    if(imageResponse.mData.mImages.size()==0){
                        mAvatar=imageResponse.mData.mOriginal;
                    }else{
                        mAvatar=imageResponse.mData.mImages.get(0);
                    }
                    ImageLoaderUtil.getInstance().loadImageByHeadPortrait(mAvatar,mIvInformationReplaceAvatar,R.drawable.ic_member_centre_head_portrait);
                }else{
                    InfoToast.makeText(InformationActivity.this,getResources().getString(R.string.network_requests_fail),Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onNetError(String interfaceAction, Throwable ex, boolean isOnCallback) {
                InformationActivity.this.hideProgressDialog();//隐藏进度框
                InfoToast.makeText(InformationActivity.this,getResources().getString(R.string.network_requests_fail),Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNetFinished(String interfaceAction) {
                InformationActivity.this.hideProgressDialog();//隐藏进度框
            }
        },accessToken,sdcardTempFile);
    }
}
