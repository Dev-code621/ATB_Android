package com.atb.app.api;

public class API {
    public static  String DOMAIN_URL = "https://test.myatb.co.uk/";
    public static String API_BASE_URL = "https://test.myatb.co.uk/api/";
    public static final String USERSESSION = "userSession";
    public static final String LOGIN = "User/login/authenticate";
    public static final String INVITE_URL = DOMAIN_URL + "invite?code=";

//public static final String DOMAIN_URL = "http://localhost/ATB"
//public static final String API_BASE_URL = "http://localhost/ATB/api/"

    public static final String STAGE_ONE_REGISTER_API = API_BASE_URL + "auth/register_stage_one";
    public static final String LOGIN_API = API_BASE_URL + "auth/login";
    public static final String REGISTER_API = API_BASE_URL + "auth/register";
    public static final String UPDATE_FEED_API = API_BASE_URL + "auth/update_feed";

    public static final String SEND_PWDRESETEMAIL_API = API_BASE_URL + "auth/forgot_pass_email_verification";
    public static final String RESETCODE_VERIFY_API = API_BASE_URL + "auth/check_verification_code";
    public static final String PWDRESET_API = API_BASE_URL + "auth/update_pass";
    public static final String PWDCHANGE_API = API_BASE_URL + "auth/change_pass";

    public static final String GET_PROFILE_API = API_BASE_URL + "profile/getprofile";
    public static final String UPDATE_PROFILE_API = API_BASE_URL + "profile/updateprofile";
    public static final String UPDATE_BIO_API = API_BASE_URL + "profile/updatebio";
    public static final String SET_POST_RANGE_API = API_BASE_URL + "profile/update_search_region";
    public static final String GET_FOLLOWER_API = API_BASE_URL + "profile/getfollower";
    public static final String GETSERVICEFILES = API_BASE_URL + "profile/get_service_files";

    public static final String GENERATE_EPHEMERAL_KEY = API_BASE_URL + "profile/generate_ephemeral_key";
    public static final String ADD_CARD_API = API_BASE_URL + "profile/add_payment";
    public static final String LOAD_CARDS_API = API_BASE_URL + "profile/get_cards";
    public static final String SET_PRIMARYCARD_API = API_BASE_URL + "profile/set_primary_card";
    public static final String DELETE_CARD_API = API_BASE_URL + "profile/remove_card";
    public static final String ADD_SUB = API_BASE_URL + "profile/add_sub";

    public static final String GET_BRAINTREE_CLIENT_TOKEN = API_BASE_URL + "profile/get_braintree_client_token";
    public static final String ADD_PP_SUB = API_BASE_URL + "profile/add_pp_sub";
    public static final String GET_PP_ADDRESS = API_BASE_URL + "profile/get_pp_add";
    public static final String MAKE_PP_PAYMENT = API_BASE_URL + "profile/make_pp_pay";
    public static final String GET_PP_TRANSACTIONS = API_BASE_URL + "profile/get_pp_transactions";

    public static final String ADD_SERVICE_API = API_BASE_URL + "profile/add_service";
    public static final String REMOVE_SERVICE_API = API_BASE_URL + "profile/remove_service";
    public static final String UPDATE_SERVICE_API = API_BASE_URL + "profile/update_service";

    public static final String LOAD_BUSINESS_API = API_BASE_URL + "profile/read_business_account";
    public static final String LOAD_BUSINESS_API_FROM_ID = API_BASE_URL + "profile/read_business_account_from_id";
    public static final String CREATE_BUSINESS_API = API_BASE_URL + "profile/create_business_account";
    public static final String UPDATE_BUSINESS_API = API_BASE_URL + "profile/update_business_account";
    public static final String UPDATE_BUSINESS_BIO = API_BASE_URL + "profile/update_business_bio";

    public static final String CREATE_POST_API = API_BASE_URL + "post/publish";
    public static final String GET_SELECTED_FEED_API = API_BASE_URL + "post/get_feed";
    public static final String GET_ALL_FEED_API = API_BASE_URL + "post/get_home_feed";
    public static final String GET_POST_DETAIL_API = API_BASE_URL + "post/get_post_detail";
    public static final String GET_MULTI_GROUP_ID = API_BASE_URL + "post/get_multi_group_id";

    public static final String GET_PRODUCT_MULTI_GROUP_ID  =   API_BASE_URL + "profile/get_multi_group_id";

    public static final String ADD_VOTE = API_BASE_URL + "post/add_vote";
    public static final String GET_USER_VOTE = API_BASE_URL + "post/get_user_vote";

    public static final String REPORT_POST_API = API_BASE_URL + "post/add_report_post";
    public static final String POST_LIKE_API = API_BASE_URL + "post/add_like_post";
    public static final String WRITE_COMMENT_API = API_BASE_URL + "post/add_comment_post";
    public static final String REPLY_COMMENT_API = API_BASE_URL + "post/add_comment_reply";
    public static final String LOAD_REPLIES_API = API_BASE_URL + "post/get_sub_comment";

    public static final String GET_USER_BOOKMARKS = API_BASE_URL + "profile/get_user_bookmarks";
    public static final String ADD_USER_BOOKMARK = API_BASE_URL + "profile/add_user_bookmark";

    public static final String GET_FOLLOWER = API_BASE_URL + "profile/getfollower";
    public static final String GET_FOLLOW = API_BASE_URL + "profile/getfollow";
    public static final String ADD_FOLLOW = API_BASE_URL + "profile/addfollow";
    public static final String DELETE_FOLLOWER = API_BASE_URL + "profile/deletefollower";
    public static final String GET_FOLLOWER_COUNT = API_BASE_URL + "profile/getfollowercount";
    public static final String GET_FOLLOW_COUNT = API_BASE_URL + "profile/getfollowcount";
    public static final String GET_POST_COUNT = API_BASE_URL + "profile/getpostcount";

    public static final String GET_NOTIFICATIONS = API_BASE_URL + "profile/get_notifications";

    public static final String ADD_BUSINESS_REVIEWS = API_BASE_URL + "profile/addbusinessreviews";
    public static final String GET_BUSINESS_REVIEWS = API_BASE_URL + "profile/getbusinessreview";

    public static final String ADD_CONNECT_ACCOUNT = API_BASE_URL + "profile/add_connect_account";
    public static final String MAKE_PAYMENT = API_BASE_URL + "profile/make_payment";

    public static final String IS_SOLD = API_BASE_URL + "post/is_sold";
    public static final String SET_SOLD = API_BASE_URL + "post/set_sold";
    public static final String RELIST = API_BASE_URL + "post/relist";

    public static final String GET_TRANSACTIONS = API_BASE_URL + "profile/get_transactions";

    public static final String COUNT_SERVICE_POST = API_BASE_URL + "post/count_service_posts";
    public static final String COUNT_SALE_POST = API_BASE_URL + "post/count_sales_posts";

    public static final String GET_USERS_POSTS = API_BASE_URL + "profile/get_users_posts";

    public static final String GET_USER_PRODUCTS   =   API_BASE_URL + "profile/get_user_products";
    public static final String GET_USER_SERVICES   =   API_BASE_URL + "profile/get_services";

    public static final String UPDATE_NOTIFCATION_TOKEN = API_BASE_URL + "profile/update_notification_token";
    public static final String LIKE_NOTIFICATIONS = API_BASE_URL + "profile/like_notifications";
    public static final String HAS_LIKE_NOTIFICATIONS = API_BASE_URL + "profile/has_like_notifications";

    public static final String ADD_PRODUCT  =  API_BASE_URL + "profile/add_product";

    public static final String ADD_SERVICE  =  API_BASE_URL + "profile/add_service";

    public static final String IS_USERNAME_USED = API_BASE_URL + "auth/is_username_used";

    public static final String DELETE_POST = API_BASE_URL + "post/delete_post";

    public static final String GET_CART_PRODUCTS = API_BASE_URL + "post/get_cart_products";

    public static final String GET_BUSINESS_ITEMS  =   API_BASE_URL + "profile/get_business_items";
    public static final String UPDTE_REGULAR_WEEK = API_BASE_URL + "business/update_week";
    public static final String ADD_HOLIDAY = API_BASE_URL + "business/add_holiday";
    public static final String DELETE_HOLIDAY = API_BASE_URL + "business/delete_holiday";
    public static final String DELETE_SERVICE_FILE = API_BASE_URL + "profile/delete_service_file";
    public static final String ADD_SERVICE_FILE = API_BASE_URL + "profile/add_service_file";
    public static final String UPDATE_SERVICE_FILE = API_BASE_URL + "profile/update_service_file";
    public static final String ADD_SOCIAL = API_BASE_URL + "profile/add_social";
    public static final String REMOVE_SOCIAL = API_BASE_URL + "profile/remove_social";
    public static final String  GET_ITEMS_SOLD =   API_BASE_URL + "transaction/get_items_sold";
    public static final String UPDATE_PRODUCT_VARIANT = API_BASE_URL + "profile/update_variant_product";
    public static final String GET_PURCHASES       =   API_BASE_URL + "transaction/get_purchases";
    public static final String GET_BOOKING       =   API_BASE_URL + "booking/get_bookings";

}
