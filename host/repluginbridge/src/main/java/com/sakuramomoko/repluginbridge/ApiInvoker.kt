package com.sakuramomoko.repluginbridge

import android.util.Log
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy

/**
 * Created by wangfeihang on 2019/5/14.
 */

private const val TAG = "ApiInvokerFile"

class ApiInvoker(
    private val classLoader: ClassLoader
) {

    fun getInstance(clazz: Class<*>): Any? {
        Log.i(TAG, "getInstance, clazz:$clazz")
        try {
            val invocationHandler = MethodProxy(classLoader, clazz)
            return Proxy.newProxyInstance(
                clazz.classLoader,
                arrayOf(clazz),
                invocationHandler)
        } catch (e: Exception) {
            Log.e(TAG, "exception:$e")
        }
        return null
    }
}

class MethodProxy(
    private val classLoader: ClassLoader,
    private val clazz: Class<*>
) : InvocationHandler {

    override fun invoke(proxy: Any?, method: Method?, args: Array<out Any>?): Any? {
        Log.i(TAG, "method invoke, method:${method?.name}")
        method?.let { method ->
            return BridgeCaller(classLoader).callBridgeMethod(
                clazz = clazz,
                method = method,
                passedArgs = args
            )
        }
        return null
    }
}

