package cn.exrick.manager.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class datetool {
	/**
     * 判断当前时间距离第二天凌晨的秒数
     *
     * @return 返回值单位为[s:秒]
     */
    public static Long getSecondsNextEarlyMorning() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return (cal.getTimeInMillis() - System.currentTimeMillis()) / 1000;
    }
    
    
    
    
    
    
    
    
    
    private static final int invalidAge = -1;//非法的年龄，用于处理异常。
    /**
        *根据身份证号码计算年龄
         * idNumber 考虑到了15位身份证，但不一定存在
         */
     
        public static int getAgeByIDNumber(String idNumber) {
        	//后台暂时不校验身份证号对错
            String dateStr;
            if (idNumber.length() == 15) {
                dateStr = "19" + idNumber.substring(6, 12);
            } else if (idNumber.length() == 18) {
                dateStr = idNumber.substring(6, 14);
            } else {//默认是合法身份证号，但不排除有意外发生
                return invalidAge;
            }
     
     
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
            try {
                Date birthday = simpleDateFormat.parse(dateStr);
                return getAgeByDate(birthday);
            } catch (ParseException e) {
                return invalidAge;
            }
     
     
        }
     
        /**
         *根据生日计算年龄
         *  dateStr 这样格式的生日 1990-01-01
         */
     
        public static int getAgeByDateString(String dateStr) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date birthday = simpleDateFormat.parse(dateStr);
                return getAgeByDate(birthday);
            } catch (ParseException e) {
                return -1;
            }
        }
     
     
        public static int getAgeByDate(Date birthday) {
            Calendar calendar = Calendar.getInstance();
     
            //calendar.before()有的点bug
            if (calendar.getTimeInMillis() - birthday.getTime() < 0L) {
                return invalidAge;
            }
     
     
            int yearNow = calendar.get(Calendar.YEAR);
            int monthNow = calendar.get(Calendar.MONTH);
            int dayOfMonthNow = calendar.get(Calendar.DAY_OF_MONTH);
     
            calendar.setTime(birthday);
     
     
            int yearBirthday = calendar.get(Calendar.YEAR);
            int monthBirthday = calendar.get(Calendar.MONTH);
            int dayOfMonthBirthday = calendar.get(Calendar.DAY_OF_MONTH);
     
            int age = yearNow - yearBirthday;
     
     
            if (monthNow <= monthBirthday && monthNow == monthBirthday && dayOfMonthNow < dayOfMonthBirthday || monthNow < monthBirthday) {
                age--;
            }
     
            return age;
        }
   
    
    
    
    
    
    
    
    
    
    

}
