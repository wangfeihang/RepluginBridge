package com.sakuramomoko.repluginbridge

import android.util.Log
import java.lang.reflect.Method

/**
 * Created by wangfeihang on 2019/5/21.
 */
object ParamWrapper {

    const val TAG = "ParamWrapper"

    fun processParams(
        calledMethod: Method,
        params: Array<out Any>
    ): Array<out
    Any>? {
        val TAG = "processParams"

        Log.i(TAG, "calledMethod.parameters:${calledMethod.parameterTypes}")
        Log.i(TAG, "passParams:$params")
        val passCloner = mutableListOf<Any>()
        if (calledMethod.parameterTypes?.size != params.size) {
            Log.i(TAG,
                "size not match, callparamssize:${calledMethod.parameterTypes?.size}, params size:${params.size}")
            return null
        } else {
            calledMethod.parameterTypes?.let { calledParams ->
                for (index in calledParams.indices) {

                    val calledParam = calledParams[index]
                    val passParam = params[index]
                    Log.i(TAG, "calledParam:$calledParam, passParam:$passParam")
                    val passProxy = passParam.parseToAnotherClass(calledParam)
                    if (passProxy == null) {
                        Log.e(TAG, "passParam parse to calledParam failed")
                        return null
                    } else {
                        passCloner.add(passProxy)
                    }
                }
            }
        }
        return passCloner.toTypedArray()
    }
}

fun Any.parseToAnotherClass(classB: Class<*>): Any? {
    if (classB.isAssignableFrom(this.javaClass) || classB.isPrimitive) {
        return this
    } else if (this.isMatchType(classB)) {
        if (classB.isInterface) {
            return CallbackInvoker().getInstance(Class.forName(classB.name), this)
        } else {
            return this.getCloner(classB)
        }
    }
    return null
}

private fun Any.isMatchType(callParam: Class<*>): Boolean {
    Log.i(ParamWrapper.TAG, "matchType, passparams:${this.javaClass.name}, callParams:$callParam")
    val passClassLoader = this.javaClass.classLoader
    return passClassLoader.loadClass(callParam.name)
        .isAssignableFrom(passClassLoader.loadClass(this.javaClass.name))
}

/**
 * 把any转换为另一个classloader的class
 * [clazz]另一个classloader的class，被转换的class
 */
private fun Any.getCloner(clazz: Class<*>) = CodeUtils.encode(this)?.let {
    CodeUtils.decode(it, clazz)
}