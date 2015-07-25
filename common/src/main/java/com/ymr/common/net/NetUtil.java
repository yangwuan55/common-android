package com.ymr.common.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetUtil {
	
    public static boolean checkNet(Context context) {

        // 获取手机所以连接管理对象（包括wi-fi，net等连接的管理）

        ConnectivityManager conn = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conn != null) {
            // 网络管理连接对象
            NetworkInfo info = conn.getActiveNetworkInfo();
            
            if(info != null && info.isConnected()) {
                // 判断当前网络是否连接
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
            
        }

        return false;
    }
}
