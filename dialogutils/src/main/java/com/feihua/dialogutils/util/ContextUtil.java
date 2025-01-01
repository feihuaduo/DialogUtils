package com.feihua.dialogutils.util;


import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;

import java.util.List;

public class ContextUtil
{
	
	public static boolean isServiceExisted(Context context, String className) {
		ActivityManager activityManager = (ActivityManager) context
            .getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> serviceList = activityManager
            .getRunningServices(Integer.MAX_VALUE);

		if (!(serviceList.size() > 0)) {
			return false;
		}

		for (int i = 0; i < serviceList.size(); i++) {
			ActivityManager.RunningServiceInfo serviceInfo = serviceList.get(i);
			ComponentName serviceName = serviceInfo.service;

			if (serviceName.getClassName().equals(className)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isContextExisted(Context context) {
		if (context != null) {
			if (context instanceof Activity) {
				if (!((Activity)context).isFinishing()) {
					return true;
				}
			} else if (context instanceof Service) {
				if (isServiceExisted(context, context.getClass().getName())) {
					return true;
				}
			} else if (context instanceof Application) {
				return true;
			}
		}
		return false;
	}
}
