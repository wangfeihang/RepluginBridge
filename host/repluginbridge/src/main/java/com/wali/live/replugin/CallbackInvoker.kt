package com.wali.live.replugin

import android.util.Log
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy

/**
 * Created by wangfeihang on 2019/5/14.
 */

private const val TAG = "ApiInvokerFile"

class CallbackInvoker {

    /**
     * 获取[clazz]类的代理，
     * 代理realObject
     */
    fun getInstance(clazz: Class<*>, realObject: Any): Any? {
        Log.i(TAG, "getInstance, clazz:$clazz")
        try {
            val invocationHandler = CallbackMethodProxy(realObject)
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

class CallbackMethodProxy(
    private val passParam: Any
) : InvocationHandler {

    override fun invoke(proxy: Any?, method: Method?, args: Array<out Any>?): Any? {
        Log.i(TAG, "method invoke, method:${method?.name}")
        method?.let { method ->
            var methodResult: Any? = null
            val passMethod = passParam.javaClass.methods.first {
                it.name == method.name
            }
            if (args == null) {
                methodResult = passMethod.invoke(passParam)
            } else {
                /**
                 * [args]是当前的classloader的，拿得到
                 * [passMethod]是对面classloader的，拿不到
                 * 把[args]传给[passMethod]
                 *
                 * [passMethod] decdcs
                 */
                ParamWrapper.processParams(passMethod, args)?.let {
                    methodResult = passMethod.invoke(passParam, *it)
                }
            }
            return methodResult?.parseToAnotherClass(method.returnType)
        }
        return null
    }
}
