package com.oguzdev.circularfloatingactionmenu.library;


import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Handler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HomeScreenDetector {
    public interface onHomeScreenListener {
        public void onPause();
        public void onResume();
    }

    private List<String> mLauncherPackageList;
    private static List<onHomeScreenListener> mListener = new ArrayList<>();
    private Context mContext;
    private HomeScreenChecker mChecker;
    private boolean mPrevState;

    public static void addListener(onHomeScreenListener listener) {
        mListener.add(listener);
    }

    public static void removeListener(onHomeScreenListener listener) {
        mListener.remove(listener);
    }

    public void startDetecting(Context context) {
        mContext = context;
        mChecker = new HomeScreenChecker();
        mChecker.start();
    }

    public void stopDetecting() {
        mContext = null;
        if(mChecker != null) {
            mChecker.interrupt();
            mChecker = null;
        }
    }

    private boolean isLauncherAppForeground(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        Iterator<ActivityManager.RunningAppProcessInfo> iterator = am.getRunningAppProcesses().iterator();

        if(mLauncherPackageList == null) {
            createLauncherPackageList(context);
        }

        while(iterator.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = iterator.next();

            if(info.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND)
                continue;

            String[] pkgList = info.pkgList;
            for(String pkgName : pkgList) {
                if(isLauncherPackage(pkgName)) {
                    return true;
                }
            }
        }

        return false;
    }

    private void createLauncherPackageList(Context context) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        List<ResolveInfo> info = context.getPackageManager().queryIntentActivities(intent, 0);
        mLauncherPackageList = new ArrayList<>(info.size());
        for(int i=0; i<info.size(); i++){
            mLauncherPackageList.add(info.get(i).activityInfo.packageName);
        }
    }

    private boolean isLauncherPackage(String pkgName) {
        return mLauncherPackageList.contains(pkgName);
    }


    private class HomeScreenChecker extends Thread {
        public void run() {
            while (true) {
                boolean curState = isLauncherAppForeground(mContext);
                if(mPrevState != curState) {
                    mPrevState = curState;
                    if(curState) {
                        for(onHomeScreenListener listener : mListener) {
                            listener.onResume();
                        }
                    }
                    else {
                        for(onHomeScreenListener listener : mListener) {
                            listener.onPause();
                        }
                    }
                }

                try {
                    Thread.sleep(700);
                } catch (InterruptedException ignored) {
                    return;
                }
            }
        }
    }
}
