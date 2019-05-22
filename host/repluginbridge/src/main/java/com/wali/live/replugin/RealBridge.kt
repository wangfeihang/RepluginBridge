package com.wali.live.replugin

import android.util.Log

/**
 * Created by wangfeihang on 2019/5/14.
 */
object RealBridge {

    private const val TAG = "RealBridge"

    fun bridgeMethod(clazzName: String, methodName: String, params: Array<out Any>?): Any? {
        Log.i(TAG, "bridgeMethod, clazzName:$clazzName, methodName:$methodName")
        return ImplUtil.getImplMethodResult(clazzName, methodName, params)
    }
}