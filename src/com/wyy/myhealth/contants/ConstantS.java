package com.wyy.myhealth.contants;

import android.R.integer;

import com.wyy.myhealth.R;

public interface ConstantS {

	/************* ACTION **********/

	public static final String NEW_LOGIN_ACTION = "COM.BING.NEW_LOGIN";

	public static final String NEW_FOOD_COMMENT = "COM.BING.NEW_FOOD_COMMENT";

	public static final String NOTICE_MAP_MSG = "�𾴵��û�����,������Ϊ�������ߣ���ʳ�����������ڿ�������,�뱣�ֶ��"
			+ "ʹ�ú���и��õط������飬������ʳ����Ӵ˿̿�ʼ��";

	public static final String ADD_FOODS_COMMENT = "add_foods_comment";

	public static final String FIRST_LOGIN = "first_login";

	public static final String BAIDU_ONBIND = "COM.BING.BAIDU.ONBIND";
	/**
	 * �ϴ�����
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
	 * ָ��ͼƬ����
	 */
	public static final int[] LEVEL_POINT = { R.drawable.star1,
			R.drawable.star1, R.drawable.star2, R.drawable.star3,
			R.drawable.star4, R.drawable.star5 };

	public static final int[] levels = { R.drawable.shai_star0,
			R.drawable.shai_star1, R.drawable.shai_star2,
			R.drawable.shai_star3, R.drawable.shai_star4, R.drawable.shai_star5 };

	/*********** json��ʽ *********/
	public static final String RESULT = "result";

	public static final String ID = "id";

	public static final String WOMAN = "0";

	public static final String MAN = "1";

	/************** ��Ϣ���� *************/
	public static final int MSG2M = 0;

	public static final int MSG2W = 1;

	/************* Preferences *************/

	public static final String USER_DATA = "Login";

	public static final String USER_PREFERENCES = "Preferences";

	public static final String USER_BACK_UP = "back_up";

	/************ ��¼���� ************/

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

	public static final long DELAY_TIME_INPUT = 500;

	/**************** RESH *******************/

	public static final String FIRST = "0";

	public static final String LIMIT = "1";

	/******************** ɨ���׼ **********************/

	public static final int FOOD_FETURE_MIN = 15;

	public static final int FOOD_FETURE_MAX = 80;

	public static final double THRESHOLD_INDEX = 0.4;

	/*************** Sina_Weobo **************/

	/** ����΢�� APP_KEY�� */
	public static final String APP_KEY = "2045436852";

	/**
	 * Ӧ�õĻص�ҳ��������Ӧ�ÿ���ʹ���Լ��Ļص�ҳ��
	 * 
	 * <p>
	 * ע��������Ȩ�ص�ҳ���ƶ��ͻ���Ӧ����˵���û��ǲ��ɼ��ģ����Զ���Ϊ������ʽ������Ӱ�죬 ����û�ж��彫�޷�ʹ�� SDK ��֤��¼��
	 * ����ʹ��Ĭ�ϻص�ҳ��https://api.weibo.com/oauth2/default.html
	 * </p>
	 */
	public static final String REDIRECT_URL = "http://www.sina.com";

	/**
	 * Scope �� OAuth2.0 ��Ȩ������ authorize �ӿڵ�һ��������ͨ�� Scope��ƽ̨�����Ÿ����΢��
	 * ���Ĺ��ܸ������ߣ�ͬʱҲ��ǿ�û���˽�������������û����飬�û����� OAuth2.0 ��Ȩҳ����Ȩ�� ѡ����Ӧ�õĹ��ܡ�
	 * 
	 * ����ͨ������΢������ƽ̨-->��������-->�ҵ�Ӧ��-->�ӿڹ������ܿ�������Ŀǰ������Щ�ӿڵ� ʹ��Ȩ�ޣ��߼�Ȩ����Ҫ�������롣
	 * 
	 * Ŀǰ Scope ֧�ִ����� Scope Ȩ�ޣ��ö��ŷָ���
	 * 
	 * �й���Щ OpenAPI ��ҪȨ�����룬��鿴��http://open.weibo.com/wiki/%E5%BE%AE%E5%8D%9AAPI
	 * ���� Scope ���ע�������鿴��http://open.weibo.com/wiki/Scope
	 */
	public static final String SCOPE = "email,direct_messages_read,direct_messages_write,"
			+ "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
			+ "follow_app_official_microblog," + "invitation_write";

	/******************* QQ ********************/
	public static final String TENCENT_APP_ID = "1101327092";
	/******************** ����ͳ�� *******************/
	/**
	 * ������½
	 */
	public static final String UMNEG_LOGIN_NORMAL = "login_normal";
	/**
	 * ע��
	 */
	public static final String UMNEG_LOGIN_OUT = "login_out";
	/**
	 * ɨӪ��
	 */
	public static final String UMNEG_SCAN_HEALTH = "scan_health";
	/**
	 * ������ʳ
	 */
	public static final String UMNEG_SHARE_FOOD = "share_food";
	/**
	 * �ղ���ʳ
	 */
	public static final String UMNEG_COLLECT_FOOD = "collect_food";
	/**
	 * ����ʳ��
	 */
	public static final String UMNEG_SEARCH_FOOD = "search_food";
	/**
	 * ���������
	 */
	public static final String UMNEG_OPEN_LIGHT = "open_light";
	/**
	 * ������ʳ
	 */
	public static final String UMNEG_PUBLISH_FOOD = "publish_food";
	/**
	 * ��������
	 */
	public static final String UMNEG_PUBLISH_MOOD = "publish_mood";
	/**
	 * ���ñ�������
	 */
	public static final String UMNEG_USER_TYPE = "WYY";
	/**
	 * ��������
	 */
	public static final int MESSAGE_TYPE_COMMENT = 6;
	/**
	 * ��������
	 */
	public static final int MESSAGE_TYPE_LAUD = 7;
	/**
	 * ��ʳ��Ϣ����
	 */
	public static final int MESSAGE_OBJ_TYPE_FOOD=1;
	/**
	 * ������Ϣ
	 */
	public static final int MESSAGE_OBJ_TYPE_MOOD=2;
	

}
