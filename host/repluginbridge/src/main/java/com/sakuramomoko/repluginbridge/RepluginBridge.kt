package com.sakuramomoko.repluginbridge

import android.util.Log

/**
 * Created by wangfeihang on 2019/5/14.
 *
 * 给宿主用的
 */
object RepluginBridge {

    private val TAG = "RepluginBridge"

    /**
     * [T]接口被注册后，其他插件或者宿主都可以通过[getApi]方法来调用接口[T]
     * 前提条件是本插件加载成功
     * @param clazz [T]的class
     * @param lazyInitialize 懒加载[T]的实例的方法
     */
    fun <T> register(clazz: Class<T>, lazyInitialize: () -> T) {
        ImplUtil.addImpl(clazz, lazyInitialize)
    }

    /**
     * 获取[T]的实例，调用实例后会运行别的插件的代码逻辑
     * @param classLoader
     *  [T]接口的实现如果在插件A中被[register]，则[classLoader]的值为插件A的classloader，可通过[RePlugin.fetchClassLoader(pluginName)]获得
     *  [T]接口的实现如果在宿主中被[register]，则[classLoader]的值为宿主的classloader，可在插件中通过[RePlugin.getHostCLassLoader()]获得
     * @param clazz [T]的class
     * @return [T]的实例
     */
    fun <T> getApi(classLoader: ClassLoader, clazz: Class<T>): T? {
        Log.i(TAG, "getApi, clazz:$clazz")
        ApiInvoker(classLoader).getInstance(clazz)?.let {
            return it as T
        }
        return null
    }
}

