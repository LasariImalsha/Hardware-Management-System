package util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTimeUtil {

    public static String currentDate(int index){
        Date d=new Date();
        String date=null;
        SimpleDateFormat sdf=null;
        switch(index){
            case 1:
                sdf=new SimpleDateFormat("yyyy-MM-dd");
                date=sdf.format(d);
                break;
            case 2:
                sdf=new SimpleDateFormat("EEEE, dd MMMM yyyy");
                date=sdf.format(d);
                break;
        }
        return date;
    }
    public static String currentTime(int index){
        Date d=new Date();
        String time=null;
        SimpleDateFormat sdf=null;
        switch(index){
            case 1:
                sdf=new SimpleDateFormat("hh:mm:ss a");
                time=sdf.format(d);
                break;
            case 2:
                sdf=new SimpleDateFormat("HH:mm:ss");
                time=sdf.format(d);
                break;
        }
        return time;
    }

    public static String dateAheadCurrentDate(String period) {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        String oldDate=sdf.format(new Date());
        return addDays(oldDate,period);
    }
    public static String dateAheadGivenDate(String oldDate, String period) {
        return addDays(oldDate,period);
    }

    private static String addDays(String oldDate, String period){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        String[] st=period.split(" ");
        int count=Integer.parseInt(st[0]);
        String unit=st[1];
        Calendar c=Calendar.getInstance();
        try{
            c.setTime(sdf.parse(oldDate));
        }catch(ParseException e){
            e.printStackTrace();
        }
        switch(unit){
            case "weeks":
                c.add(Calendar.WEEK_OF_MONTH, count);break;
            case "months":
                c.add(Calendar.MONTH, count);break;
            case "years":
                c.add(Calendar.YEAR, count);break;
        }
        return sdf.format(c.getTime());
    }
}
