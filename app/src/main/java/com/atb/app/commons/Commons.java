package com.atb.app.commons;

import android.util.DisplayMetrics;

import com.atb.app.activities.profile.ProfileUserNavigationActivity;
import com.atb.app.base.CommonActivity;
import com.atb.app.model.UserModel;
import com.atb.app.model.submodel.VotingModel;
import com.atb.app.preference.Preference;
import com.volokh.danylo.video_player_manager.manager.VideoPlayerManager;
import com.volokh.danylo.video_player_manager.meta.MetaData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Commons {
    public static boolean g_isAppRunning=false;
    public static CommonActivity g_commentActivity = null;
    public static ProfileUserNavigationActivity profileUserNavigationActivity = null;
    public static boolean traffic = true;
    public static float zoom = 0f;
    public static double lat =0f;
    public static double lng =0f;
    public static String location ="";
    public static UserModel g_user ;
    public static UserModel selected_user;
    public static VideoPlayerManager<MetaData> mVideoPlayerManager;
    public static String token = "";
    public static int location_code = 1100;
    public static String[] Months;
    public static String main_category ="MY ATB";
    public static  int selectUsertype; //-1:main page: 1: business: 0 : user
    public static int phone_height;
    public static int phone_width;
    public static int glide_radius = 500;
    public static int glide_magin = 2;
    public static int glide_boder = 10;
    public static int video_flag = -1;
    public static int subscription_code = -11111;

    public static String fileNameWithoutExtFromPath(String path) {

        String fullname = fileNameWithExtFromPath(path);

        if (fullname.lastIndexOf(".") == -1) {
            return fullname;
        } else {
            return fullname.substring(0, fullname.lastIndexOf("."));
        }
    }
    public static String fileNameWithExtFromPath(String path) {

        if (path.lastIndexOf("/") > -1)
            return path.substring(path.lastIndexOf("/") + 1);

        return path;
    }

    public static String listToString(List<String> list, String separator) {
        if(list == null)return "";
        StringBuilder sb = new StringBuilder();
        String mySeparator = "";

        for (String text: list) {
            sb.append(mySeparator);
            sb.append(text);
            mySeparator = separator;
        }

        return sb.toString();
    }
    public static String getWeekday(String date){
        String dayName = "";
        SimpleDateFormat inFormat = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date myDate = inFormat.parse(date);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE");
            dayName=simpleDateFormat.format(myDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  dayName;
    }

    public static boolean myVoting(VotingModel votingModel){
        for(int i =0;i<votingModel.getVotes().size();i++){
            if(votingModel.getVotes().get(i) == Commons.g_user.getId())return true;
        }
        return false;
    }

    public static boolean mediaVideoType(String str){
        if(str.contains(".mp4")) return  true;
        return false;
    }

    public static String getDatefromMilionSecond(long milionsecond){
        String date = "";
        Date d = new Date(milionsecond*1000l);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MMM/yyyy hh:mm a");
        date = formatter.format(d);
        return date;
    }

    public static String getMonths(long milionsecond){
        String date = "";
        Date d = new Date(milionsecond*1000l);
        SimpleDateFormat formatter = new SimpleDateFormat("MMM yyyy");
        date = formatter.format(d);
        return date;
    }
    public static String getDisplayDate4(long milionsecond){
        String date = "";
        Date d = new Date(milionsecond*1000l);
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MMM.yyyy");
        date = formatter.format(d);
        return date;
    }
    public static String  getDisplayDate1(String date){
        String dayName = "";
        SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date myDate = inFormat.parse(date);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy");
            dayName=simpleDateFormat.format(myDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  dayName;
    }

    public static String  getDisplayDate2(String date){
        String dayName = "";
        SimpleDateFormat inFormat = new SimpleDateFormat("yyyy/MM/dd");
        try {
            Date myDate = inFormat.parse(date);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dayName=simpleDateFormat.format(myDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  dayName;
    }
    public static String  getDisplayDate3(String date){
        String dayName = "";
        SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date myDate = inFormat.parse(date);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
            dayName=simpleDateFormat.format(myDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  dayName;
    }


}
