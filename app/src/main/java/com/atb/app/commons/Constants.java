package com.atb.app.commons;

import com.atb.app.R;

public class Constants {
    public static int MY_PEQUEST_CODE = 123;
    public static final String DEFAULT_GROUPNAME = "Group";
    public static final int  GROUPCHATTING = 1;
    public static int AUTOCOMPLETE_REQUEST_CODE = 1;
    public enum BOOKING_TYPE {delay, instant};
    public static final int IMAGE_MAX_SIZE = 1024;
    public static final int PROFILE_IMAGE_SIZE = 256;

    public static String[] category_word = {"Beauty","Ladieswear","Menswear","Hair","Kids","General","Home","Events","Health","Well-Being","Seasonal"};
    public static int[] category_selected = {R.drawable.icon_beauti_select,R.drawable.icon_ladies_select,R.drawable.icon_mens_select,R.drawable.icon_hair_select,
            R.drawable.icon_kids_selct,R.drawable.icon_garden_select,R.drawable.icon_home_select,R.drawable.icon_parties_select,R.drawable.icon_health_select,R.drawable.icon_seasonal_selct};
    public static int[] category_unselected = {R.drawable.icon_beauti_unselectpng,R.drawable.icon_ladies_unselect,R.drawable.icon_mens_unselct,R.drawable.icon_hair_unselect,
            R.drawable.icon_kids_unselct,R.drawable.icon_garden_unselect,R.drawable.icon_home_unselct,R.drawable.icon_parties_unselect,R.drawable.icon_health_unselect,R.drawable.icon_seasonal_unselct};


}
