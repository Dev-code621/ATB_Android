package com.atb.app.commons;

import com.atb.app.R;

import java.util.Locale;

public class Constants {
    public static int MY_PEQUEST_CODE = 123;
    public static final String DEFAULT_GROUPNAME = "Group";
    public static final int  GROUPCHATTING = 1;
    public static int AUTOCOMPLETE_REQUEST_CODE = 1;
    public enum BOOKING_TYPE {delay, instant};
    public static final int IMAGE_MAX_SIZE = 1024;
    public static final int PROFILE_IMAGE_SIZE = 256;
    public static int[] postType = {0,R.drawable.advice_icon,R.drawable.icon_sales,R.drawable.icon_serviceoffer,R.drawable.icon_poll};
    public static String[] category_word = {"Beauty","Ladieswear","Menswear","Hair","Kids","Home","Events","Health & Well-Being","Celebrations","Miscellaneous"};
    public static int[] category_selected = {R.drawable.icon_beauti_select,R.drawable.icon_ladies_select,R.drawable.icon_mens_select,R.drawable.icon_hair_select,
            R.drawable.icon_kids_selct,R.drawable.icon_home_select,R.drawable.icon_parties_select,R.drawable.icon_health_select,R.drawable.icon_garden_select,R.drawable.icon_seasonal_selct};
    public static int[] category_unselected = {R.drawable.icon_beauti_unselectpng,R.drawable.icon_ladies_unselect,R.drawable.icon_mens_unselct,R.drawable.icon_hair_unselect,
            R.drawable.icon_kids_unselct,R.drawable.icon_home_unselct,R.drawable.icon_parties_unselect,R.drawable.icon_health_unselect,R.drawable.icon_garden_unselect,R.drawable.icon_seasonal_unselct};
    public static int[] slideImage = {R.drawable.step1,R.drawable.step2,R.drawable.step3,R.drawable.step4,R.drawable.step5,R.drawable.step9};
    public static String[] slideTitle = {"Insurance & Qualification\nVerification."
            ,"Deposit Scheme",
            "Featured Posts",
            "Upload Unlimited Products & Services to your Store",
            "Multi-Post Services and Items",
           "Priority Admin\nSupport"};
    public static String[] slideDescription = {"(Gaining ATB approved business status)",
            "Covers your business against cancelled or no-show appointments",
            "All your posts will be highlighted and show the ATB approved logo",
            "Post your entire list of services or products and really promote your business",
            "Post all your services and items at the same time making it easier for users to shop",
            "Dedicated helpline and email support"};

    public static String terms = "https://app.termly.io/document/terms-of-use-for-online-marketplace/cbadd502-052f-40a2-8eae-30b1bb3ae9b1";
    public static String Policy = "https://app.termly.io/document/privacy-policy/a5b8733a-4988-42d7-8771-e23e311ab486";
    public static String EULA = "https://app.termly.io/document/eula/c8f66d8d-c546-452f-bcf4-1c28815043dd";
    public static String User_policy = "https://app.termly.io/document/acceptable-use-policy/6572cb8e-3250-4d62-88ba-a1bb2a403063";

    public static String Cookie = "https://app.termly.io/document/cookie-policy/de313fa7-ef48-4619-86d2-0daad3679b40";
    public static String Disclaimer = "https://app.termly.io/document/disclaimer/c3c5eb9f-6576-4402-b294-11a7ac0704c1";
    public static String App_version = "";

}
