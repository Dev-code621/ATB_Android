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
    public static int[] slideImage = {R.drawable.step1,R.drawable.step2,R.drawable.step3,R.drawable.step4,R.drawable.step5,R.drawable.step6,R.drawable.step7,R.drawable.step8,R.drawable.step9};
    public static String[] slideTitle = {"Insurance & Qualification\nVerification.","Deposit Scheme","Featured Posts","Up to 10 Item Uploads","Bulk Item Uploads","Video Uploads",
            "Preferential Delivery\nRates","Preferencial Delivery\nSlots using ATB Direct","Priority Admin\nSupport"};
    public static String[] slideDescription = {"(Gaining ATB approved business status).","Covers your business against\ncancelled or no-show appointments",
            "You post will highlight between the\nregular post, you will have more chances to being viewed.",
            "You post will highlight between the\nregular post, you will have more\nchances to being viewed.",
            "You post will highlight between the\nregular post, you will have more\nchances to being viewed.",
            "You post will highlight between the\nregular post, you will have more\nchances to being viewed.",
            "With a business account you won’t\nhave to worry about the Delivery",
            "Use our tool and get better promotions",
            "You post will highlight between the\nregular post, you will have more\nchances to being viewed."};


}
