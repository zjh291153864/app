package com.zhonghong.xqshijie.net;

/**
 * Created by xiezl on 16/6/29.
 */
public class NetTag {
    /**
     * 基本域名
     */
    public static final String BASEURL = "https://103.10.86.28/xqsj/app";

    /**
     * 以下接口常量
     */
    public static final String GETINDEXCONFIG = "/page/getIndexConfig";//获取首页面接口

    public static final String CHECKMOBILE = "/member/checkMobile";//判断用户是否注册

    public static final String SENDSMSONREGISTER = "/validateCode/sendSmsOnRegister";//注册发送验证码接口
    public static final String REGISTER = "/member/register";//用户注册接口

    public static final String LOGIN = "/member/login";//登陆接口

    public static final String SENDSMSONFORGITPWD = "/validateCode/sendSmsOnForgetPwd";//忘记密码发送验证码接口
    public static final String CHANGEPWDBYFOTGIT = "/member/changePwdByForget";//忘记密码用户修改密码

    public static final String UPDATEMEMBER = "/member/updateMember";//用户个人资料更新修改
    public static final String ACCESSTOKENN = "http://test.auth.xqshijie.com/accessToken";//取得access_Token
    //    public static final String ACCESSTOKENN = "http://auth.xqshijie.com/accessToken";//取得access_Token(线上接口)
    public static final String UPLOADIMAGE = "http://test.upload.xqshijie.com/upload/";//根据access_token 上传图片
//    public static final String UPLOADIMAGE = "http://upload.xqshijie.com/upload/";//根据access_token 上传图片(线上接口)

    public static final String CHECKOLDPWD = "/member/checkOldPwd";//检测用户原密码是否正确
    public static final String CHANGEPWDBYSET = "/member/changePwdBySet";//设置中心-用户修改密码

    public static final String ADDFEEDBACK = "/sys/addFeedback";//意见反馈

    public static final String GETSCENICPRODUCT = "/projects/getAllPro";//新奇景区首页接口
    public static final String GETSCENICBANNER = "/page/getXQJQConfig";//新奇景区首页顶部图片的接口
    public static final String GETPROBYREGION = "/projects/getProByRegion";//目的地分页查询项目

    public static final String GETADDRESSLIST = "/member/getAddressList";//获取个人地址列表
    public static final String ADDADDRESS = "/member/addAddress";//新增个人地址
    public static final String UPDATEADDRESS = "/member/updateAddress";//修改个人地址
    public static final String DELADDRESS = "/member/delAddress";//删除个人地址

    public static final String GITBYPROJECTID = "/projects/getByProjectId";//逸乐通规格弹层选择页
    public static final String SAVEORIDER = "/order/saveOrder";//确认定单页面
    public static final String PAYCHANNELLIST = "/paychannel/paychannellist.do";//获取支付渠道列表
    public static final String CONTRACTLIST = "/order/contractlist";//确认订单合同展示
    public static final String ORDERALIPAY = "/order/orderAlipay"; //支付记录插入（支付单号取得）


    public static final String GETYLTHOMEPAGE = "/projects/getConfig.do";//逸乐通首页接口
    public static final String GETYLTPRODUCTS = "/projects/allPageProjects.do";//逸乐通产品列表接口
    public static final String GETMYORDER = "/order/myorderlistpage.do";//我的订单
    public static final String GETMYORDERDETAIL = "/order/mypaymentlist.do";//我的订单详情
    public static final String GETMYORDERPAYMENTLIST = "/order/mypaymentlist.do";//支付列表
    public static final String ORDERCANCEL = "/order/ordercancel";//取消订单
    public static final String ORDERCLOSE = "/order/orderclose";//关闭订单


    /**
     * 以下需要缓冲接口的常量
     */

    public static final String GETINDEXCONFIG_CACHE = "getIndexConfig";//获取首页面接口缓存KEY
    public static final String GETSCENICPRODUCT_CACHE = "getAllPro";//新奇景区首页接口缓存KEY
    public static final String GETPROBYREGION_CACHE = "getProByRegion";//目的地分页查询项目
    public static final String GETSCENICBANNER_CACHE = "getXQJQConfig";//新奇景区首页顶部图片的接口缓存KEY
    public static final String GETPROPERTYATTR_CACHE = "projectsgetProById";//项目属性信息
    public static final String GETYLTHOMEPAGE_CACHE = "getYLTConfig";//逸乐通首页接口缓存KEY
    public static final String GETYLTPRODUCTS_CACHE = "getByProjectId";//逸乐通产品列表接口缓存KEY

    /**
     * 项目详情，包含相册
     */
    public static final String GET_PROJECT_DETAIL = "/projects/getProById";//项目详情接口
    public static final String GET_PROPERTY_ATTR = "/projects/getProById";//项目属性信息
    public static final String GET_MY_WALLET = "/cash/mycashlist.do";//我的钱包
    public static final String GET_PROJECT_PHOTO = "/projects/getProPhotoById";//项目详情相册接口

    /**
     * 软件更新，
     */
    public static final String GET_APP_NEW_VERSION = "/sys/updateApp";

}
