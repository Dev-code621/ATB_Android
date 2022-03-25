package com.atb.app.commons;

import android.util.DisplayMetrics;

import com.atb.app.activities.MainActivity;
import com.atb.app.activities.profile.ProfileUserNavigationActivity;
import com.atb.app.base.CommonActivity;
import com.atb.app.model.AutoCompleteModel;
import com.atb.app.model.UserModel;
import com.atb.app.model.submodel.VotingModel;
import com.atb.app.preference.Preference;
import com.atb.app.service.BackgroundService;
import com.google.android.gms.maps.model.LatLng;
import com.pubnub.api.PubNub;
import com.volokh.danylo.video_player_manager.manager.VideoPlayerManager;
import com.volokh.danylo.video_player_manager.meta.MetaData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class Commons {
    public static boolean g_isAppRunning=false;
    public static CommonActivity g_commentActivity = null;
    public static MainActivity g_mainActivity = null;
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
    public static String fbtoken = "";
    public static String fcmtoken = "";
    public static int location_code = 1100;
    public static String[] Months;
    public static String main_category ="My ATB";
    public static  int selectUsertype; //-1:main page: 1: business: 0 : user
    public static int phone_height;
    public static int phone_width;
    public static int glide_radius = 500;
    public static int glide_magin = 2;
    public static int glide_boder = 10;
    public static int video_flag = -1;
    public static int subscription_code = -11111;
    public static int REQUEST_PAYMENT_CODE =10034;
    public static String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    public static int feed_id =-1;
    public static HashMap<String, ArrayList<String>>region = new HashMap<>();
    public static ArrayList<String> county = new ArrayList<>();
    public static HashMap<String, LatLng>LatLang = new HashMap<>();
    public static HashMap<String,String> postalCode = new HashMap<>();
    public static ArrayList<AutoCompleteModel> AllUsers = new ArrayList<>();
    public static ArrayList<UserModel> Appusers = new ArrayList<>();

    public static BackgroundService backgroundService ;
    public static int notification_count = 0;
    public static PubNub mPubNub;
    public static String senderID;
    public static String senderImage;
    public static String senderName;
    public static  boolean profile_flag = false;
    public static  List<String> pubnub_channels = new ArrayList<>();
    public  static  int userType = 0;
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
   public static String getPostalCode(String str){
        String[] array = str.split("\\|");
        if(array.length>2){
            return array[2];
        }
        return "";
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

    public static String getLocaltime(String date){

        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date d = null;
        try {
            d = df.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String localtime= new SimpleDateFormat("hh:mm a").format(d);
        return localtime;
    }
    public static int getMilionSecond(String date){
        SimpleDateFormat df = new SimpleDateFormat("hh:mm a");
        Date d = null;
        int time =0;
        try {
            d = df.parse(date);
            time = (int)d.getTime()/1000;
            if(time<0)time+=24*3600;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }
    public static String gettimeFromMilionSecond(int time){
        Date d = new Date(time*1000l);
        SimpleDateFormat df = new SimpleDateFormat("hh:mm a");
        String localtime = df.format(d);

        return localtime;
    }
    public static String getUTCtimeFromMilionSecond(int time){
        Date d = new Date(time*1000l);
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        String localtime = df.format(d);

        return localtime;
    }


    public static int getMonthnumber(String str){
        for(int i =0;i<12;i++){
            if(monthNames[i].equals(str))return i;
        }
        return -1;
    }
}
