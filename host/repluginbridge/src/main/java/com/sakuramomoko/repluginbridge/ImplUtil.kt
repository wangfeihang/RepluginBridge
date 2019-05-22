package com.sakuramomoko.repluginbridge

import android.util.Log
import com.sakuramomoko.repluginbridge.ParamWrapper.processParams

/**
 * Created by wangfeihang on 2019/5/14.
 */
object ImplUtil {

    private val TAG = "ImplUtil"

    private var implFactoryMap = mutableMapOf<Class<*>, ImplFactory<*>>()

    fun <T> addImpl(clazz: Class<T>, lazyInitialize: () -> T) {
        implFactoryMap[clazz] =
            ImplFactory(lazyInitialize)
    }

    fun <T> getImpl(clazz: Class<T>): T? {
        return implFactoryMap[clazz]?.getImplInstance() as T
    }

    fun getImplMethodResult(clazzName: String, methodName: String, params: Array<out Any>?): Any? {
        val apiClass = Class.forName(clazzName)
        if (implFactoryMap[apiClass] == null) {
            Log.e(
                TAG, "getImplMethodResult, implFactoryMap:${implFactoryMap}")
            Log.e(TAG, "getImplMethodRsult failed, cannot find $clazzName, please register first")
            return Unit
        }
        implFactoryMap[apiClass]?.let {
            val implInstance = it.getImplInstance()
            val realMethod = apiClass.methods.firstOrNull { it.name == methodName }
            if (realMethod == null) {
                Log.e(TAG, "cannot find method:$methodName")
            } else {
                return if (params == null) {
                    realMethod.invoke(implInstance)
                } else {
                    processParams(realMethod, params).let {
                        if (it == null) {
                            Log.e(TAG, "processParams error!")
                        } else {
                            realMethod.invoke(implInstance, *it)
                        }
                    }
                }
            }
        }
        return Unit
    }
}

class ImplFactory<T>(
    private val lazyInitialize: () -> T
) {

    private var implInstance: T? = null

    fun getImplInstance(): T {
        if (implInstance == null) {
            implInstance = lazyInitialize()
        }
        return implInstance!!
    }
}


