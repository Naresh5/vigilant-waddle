package com.example.naresh.demoproject_1.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.Html;
import android.text.Spanned;

/**
 * Created by naresh on 2/3/17.
 */

public class Utility {

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

    public static Spanned convertTextToHTML(String body) {
        Spanned spanned;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            spanned = Html.fromHtml(body, Html.FROM_HTML_MODE_LEGACY);
        } else {
            spanned = Html.fromHtml(body, null, null);
        }
        return spanned;
    }

}
