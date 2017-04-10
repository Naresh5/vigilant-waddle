package com.example.naresh.demoproject_1.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.Html;
import android.text.Spanned;
import android.text.format.DateUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.example.naresh.demoproject_1.HtmlImageGetter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by naresh on 2/3/17.
 */

public class Utility {

        public static Spanned convertTextToHTML(String body) {
        return convertTextToHTML(body, null);
    }

    public static Spanned convertTextToHTML(String body, HtmlImageGetter imageGetter) {
        Spanned spanned;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            spanned = Html.fromHtml(body, Html.FROM_HTML_MODE_LEGACY, imageGetter, null);
        } else {
            spanned = Html.fromHtml(body, imageGetter, null);
        }
        return spanned;
    }



    public static boolean networkIsAvailable(Context context) {
        ConnectivityManager manager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    public static String getQuestionScore(int rep) {
        String repString;
        String r = String.valueOf(rep);

        if (rep < 1000) {
            repString = r;
        } else {
            repString = (Math.round((rep / 1000f) * 10) / 10f) + "k";
        }
        return repString;
    }

    public static String arrayToStringConvert(ArrayList<String> listTag) {
        StringBuilder sb = new StringBuilder();
        for (int listTagPointer = 0; listTagPointer < listTag.size(); listTagPointer++) {
            if (listTagPointer == (listTag.size() - 1)) {
                sb.append(listTag.get(listTagPointer));
            } else {
                sb.append(listTag.get(listTagPointer));
                sb.append(", ");
            }
        }
        return sb.toString();
    }


    public static boolean isYesterday(Calendar calendar) {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.DATE, -1);

        return now.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)
                && now.get(Calendar.MONTH) == calendar.get(Calendar.MONTH)
                && now.get(Calendar.DATE) == calendar.get(Calendar.DATE);
    }

    public static String getDate(long seconds, String dateFormat) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(seconds * 1000);

        if (DateUtils.isToday(calendar.getTimeInMillis())) {
            return (String) DateUtils.getRelativeTimeSpanString(calendar.getTimeInMillis(),
                    System.currentTimeMillis(),
                    DateUtils.SECOND_IN_MILLIS,
                    DateUtils.FORMAT_ABBREV_RELATIVE);
        } else if (isYesterday(calendar)) {
            return "yesterday";
        } else {
            SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
            return (formatter.format(calendar.getTime()));
        }
    }

    public static String getReputation(long reputation) {
        String repString;
        String rep = String.valueOf(reputation);

        if (reputation < 1000) {
            repString = rep;
        } else if (reputation < 10000) {
            repString = rep.charAt(0) + "," + rep.substring(1);
        } else {
            repString = (Math.round((reputation / 1000f) * 10) / 10f) + "k";
        }
        return repString;
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

}