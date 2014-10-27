package com.wyy.myhealth.contants;

import com.wyy.myhealth.R;

public interface ConstantS {

	/************* ACTION **********/

	public static final String NEW_LOGIN_ACTION = "COM.BING.NEW_LOGIN";

	public static final String NEW_FOOD_COMMENT = "COM.BING.NEW_FOOD_COMMENT";

	public static final String NOTICE_MAP_MSG = "尊敬的用户您好,本地区为初次上线，美食服务数据正在快速增加,请保持多次"
			+ "使用后会有更好地服务体验，健康饮食生活从此刻开始！";

	public static final String ADD_FOODS_COMMENT = "add_foods_comment";

	public static final String FIRST_LOGIN = "first_login";

	public static final String BAIDU_ONBIND = "COM.BING.BAIDU.ONBIND";
	/**
	 * 上传步伐
	 */
	public static final String POST_FOOT_ACTION = "COM.BING.POST.FOOTS";

	public static final String PAGE_INDEX_CHANG = "ACTION.WYY.PAGE.CHANGE";

	public static final String ACTION_BASE_INFO_CHANGE = "ACTION.BASEINFO.CHANGE";

	public static final String ACTION_SEARCH_FOOD = "ACTION.WYY.SEARCH.FOOD";

	public static final String ACTION_HIDE_UI_CHANGE = "ACTION.WYY.UI.CHANGE";

	public static final String ACTION_RESH_USER_DATA = "ACTION.WYY.RESH.USER.DATA";

	public static final String ACTION_LOGIN_FINISH = "ACTION.WYY.LOGIN.FINISH";

	public static final String ACTION_MAIN_FINSH = "ACTION.WYY.MAIN.FINSH";

	public static final String ACTION_SEND_DELETE_ICEFOOD = "ACTION.WYY.SEND_DELETE_ICEFOOD";

	public static final String ACTION_SEND_SHAI = "ACTION.WYY.SEND.SHAI";

	public static final String ACTION_SEND_CANEL_NOTICE = "ACTION.WYY.SEND.CANEL.NOTICE";

	public static final String ACTION_RECORED_NOTICE = "ACTION.WYY.RECORED.NOTICE";

	public static final String ACTION_CHANEG_PAGER_INDEX = "ACTION.WYY.CHANGE.PAGER";

	public static final String ACTION_SHARE_RECORDER = "ACTION.WYY.SHARE.RECORDER";

	public static final String ACTION_RECOOMEND_TODAY_FOOD = "ACTION.WYY.RECOMMEND.TODAYFOOD";

	/************** NOTICE_ID *************/
	public static final int NEW_LOGIN_ACTION_ID = 0;

	public static final int NEW_FOOD_COMMENT_ID = 1;

	public static final int PUBLISH_MOOD_ID = 2;

	public static final int PUBLISH_SHAI_ID = 3;

	public static final int PUBLISH_FOOD_ID = 4;

	public static final int PUBLISH_COMMENT = 5;
	/**
	 * 指数图片数组
	 */
	public static final int[] LEVEL_POINT = { R.drawable.star1,
			R.drawable.star1, R.drawable.star2, R.drawable.star3,
			R.drawable.star4, R.drawable.star5 };

	public static final int[] levels = { R.drawable.shai_star0,
			R.drawable.shai_star1, R.drawable.shai_star2,
			R.drawable.shai_star3, R.drawable.shai_star4, R.drawable.shai_star5 };

	/*********** json格式 *********/
	public static final String RESULT = "result";

	public static final String ID = "id";

	public static final String WOMAN = "0";

	public static final String MAN = "1";

	/************** 消息类型 *************/
	public static final int MSG2M = 0;

	public static final int MSG2W = 1;

	/************* Preferences *************/

	public static final String USER_DATA = "Login";

	public static final String USER_PREFERENCES = "Preferences";

	public static final String USER_BACK_UP = "back_up";

	/************ 记录类型 ************/

	public static final int YINSHI = 100;

	public static final int YUNDONG = 101;

	public static final int ZHIFANG = 102;

	public static final int TANGLEI = 103;

	public static final int DANGBAIZHI = 104;

	public static final int WEISHENGSU = 105;

	public static final int KUANGWUZHI = 106;

	public static final String RECEODER = "receoder_type";

	/************** TYPE SHAI *****************/

	public static final int TYPE_FOOD = 1;

	public static final int TYPE_MOOD = 2;

	/**************** TIME *******************/

	public static final long DELAY_TIME = 2;

	public static final long PERIOD_TIME = 5 * 60;

	public static final long PERIOD_TIME_ = 5;

	/**************** RESH *******************/

	public static final String FIRST = "0";

	public static final String LIMIT = "1";

	/******************** 扫描标准 **********************/

	public static final int FOOD_FETURE_MIN = 15;

	public static final int FOOD_FETURE_MAX = 80;

	public static final double THRESHOLD_INDEX = 0.4;

	/*************** Sina_Weobo **************/

	/** 新浪微博 APP_KEY， */
	public static final String APP_KEY = "2045436852";

	/**
	 * 应用的回调页，第三方应用可以使用自己的回调页。
	 * 
	 * <p>
	 * 注：关于授权回调页对移动客户端应用来说对用户是不可见的，所以定义为何种形式都将不影响， 但是没有定义将无法使用 SDK 认证登录。
	 * 建议使用默认回调页：https://api.weibo.com/oauth2/default.html
	 * </p>
	 */
	public static final String REDIRECT_URL = "http://www.sina.com";

	/**
	 * Scope 是 OAuth2.0 授权机制中 authorize 接口的一个参数。通过 Scope，平台将开放更多的微博
	 * 核心功能给开发者，同时也加强用户隐私保护，提升了用户体验，用户在新 OAuth2.0 授权页中有权利 选择赋予应用的功能。
	 * 
	 * 我们通过新浪微博开放平台-->管理中心-->我的应用-->接口管理处，能看到我们目前已有哪些接口的 使用权限，高级权限需要进行申请。
	 * 
	 * 目前 Scope 支持传入多个 Scope 权限，用逗号分隔。
	 * 
	 * 有关哪些 OpenAPI 需要权限申请，请查看：http://open.weibo.com/wiki/%E5%BE%AE%E5%8D%9AAPI
	 * 关于 Scope 概念及注意事项，请查看：http://open.weibo.com/wiki/Scope
	 */
	public static final String SCOPE = "email,direct_messages_read,direct_messages_write,"
			+ "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
			+ "follow_app_official_microblog," + "invitation_write";

	/******************* QQ ********************/
	public static final String TENCENT_APP_ID = "1102856689";
	/******************** 友盟统计 *******************/
	/**
	 * 正常登陆
	 */
	public static final String UMNEG_LOGIN_NORMAL = "login_normal";
	/**
	 * 注销
	 */
	public static final String UMNEG_LOGIN_OUT = "login_out";
	/**
	 * 扫营养
	 */
	public static final String UMNEG_SCAN_HEALTH = "scan_health";
	/**
	 * 分享美食
	 */
	public static final String UMNEG_SHARE_FOOD = "share_food";
	/**
	 * 收藏美食
	 */
	public static final String UMNEG_COLLECT_FOOD = "collect_food";
	/**
	 * 搜索食物
	 */
	public static final String UMNEG_SEARCH_FOOD = "search_food";
	/**
	 * 开启闪光灯
	 */
	public static final String UMNEG_OPEN_LIGHT = "open_light";
	/**
	 * 发布美食
	 */
	public static final String UMNEG_PUBLISH_FOOD = "publish_food";
	/**
	 * 发布心情
	 */
	public static final String UMNEG_PUBLISH_MOOD = "publish_mood";
}
