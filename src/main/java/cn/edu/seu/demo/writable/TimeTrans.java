package cn.edu.seu.demo.writable;

import java.text.SimpleDateFormat;

public class TimeTrans {
    public static String timeTrans(String data){
        SimpleDateFormat time =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long date_long=Long.parseLong(data);
        String time_string = time.format(date_long);
        return time_string;
    }
}
