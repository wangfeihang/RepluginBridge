package com.wali.live.replugin

import com.google.gson.Gson
import java.lang.reflect.Type

/**
 * Created by wangfeihang on 2019/5/17.
 */
object CodeUtils {

    private val gson = Gson()

    fun encode(`object`: Any?): String? {
        if (`object` == null) {
            return null
        } else {
            try {
                return gson.toJson(`object`)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return null
    }

    fun decode(data: String, clazz: Type): Any? {
        try {
            return gson.fromJson(data, clazz)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}