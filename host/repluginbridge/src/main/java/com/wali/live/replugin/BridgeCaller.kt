package com.wali.live.replugin

import android.util.Log
import java.lang.reflect.Method

/**
 * Created by wangfeihang on 2019/5/14.
 */
class BridgeCaller(private val classLoader: ClassLoader) {

    private val TAG = "BridgeCaller"

    fun callBridgeMethod(
        clazz: Class<*>,
        method: Method,
        passedArgs: Array<out Any>?
    ): Any? {
        Log.i(TAG, "callBridgeMethod, calledClassName:${clazz.name}, calledMethodName:${method.name}")
        val hostMsgClass = classLoader.loadClass("com.wali.live.replugin.RealBridge")
        val instance = hostMsgClass.getDeclaredField("INSTANCE").get(null)
        val bridgeMethod = hostMsgClass.getDeclaredMethod(
            "bridgeMethod",
            String::class.java,
            String::class.java,
            Array<out Any>::class.java)
        bridgeMethod.isAccessible = true
        try {
            var invokeresult: Any? = null
            if (passedArgs == null) {
                invokeresult = bridgeMethod.invoke(instance, clazz.name, method.name, null)
            } else {
                invokeresult = bridgeMethod.invoke(instance, clazz.name, method.name, passedArgs)
            }
            return invokeresult?.parseToAnotherClass(method.returnType)
        } catch (e: Exception) {
            Log.e(TAG, "exception:${e.printStackTrace()},msg:${e.message}")
        }
        return null
    }
}