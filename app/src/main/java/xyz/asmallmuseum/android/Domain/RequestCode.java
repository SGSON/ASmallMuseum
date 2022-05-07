package xyz.asmallmuseum.android.Domain;

public class RequestCode {
    public final static int REQUEST_SINGLE = 2;
    public final static int REQUEST_MULTIPLE = 8;

    public final static int REQUEST_USER = 2010;
    public final static int REQUEST_UPLOAD = 2011;
    public final static int REQUEST_RANDOM = 2012;

    public static final int REQUEST_CODE = 3020;
    
    public static final int RESULT_UPLOAD_OK = 2301;
    public static final int RESULT_UPLOAD_INFO_OK = 2302;
    public static final int RESULT_UPLOAD_FAIL = 2303;

    public static final int REQUEST_EXIST = 3301;
    public static final int REQUEST_USER_LIST = 3303;
    public static final int RESULT_POST_OK = 3304;
    public static final int RESULT_POST_FAIL = 3305;

    public static final int REQUEST_INIT = 5500;
    public static final int REQUEST_EMAIL_SIGN_UP = 5501;
    public static final int REQUEST_OTHERS = 5502;
    public static final int REQUEST_GET_EMAIL = 5503;
    public static final int REQUEST_END_SIGN_UP = 5504;
    public static final int REQUEST_MENU = 5505;
    public static final int REQUEST_EMAIL_VERIFY = 5506;

    public static final int REQUEST_OTHER_SIGN_IN = 5601;

    public static final int REQUEST_PASSWORD = 3601;
    public static final int REQUEST_INFO = 3602;
    public static final int REQUEST_PROFILE = 3604;

    public static final int REQUEST_END = 8701;

    public static final int RESULT_PATH_DELETE_OK = 12501;
    public static final int RESULT_ART_DELETE_OK = 12502;
    public static final int RESULT_DELETE_FAIL = 12503;
    public static final int RESULT_IMAGE_DELETE_OK = 12504;
}
