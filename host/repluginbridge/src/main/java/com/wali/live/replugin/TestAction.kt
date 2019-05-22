package com.wali.live.replugin

import android.content.Context

/**
 * Created by wangfeihang on 2019/5/17.
 */
interface TestAction {
    fun show(context: Context, testInfo: TestInfo, callback: TestCallback): TestController
}

interface TestCallback {
    fun onSuc(testObj: TestInfo1)
    fun onFail()
}

interface TestController {
    fun onControl(controller: String): String
}

data class TestReturn(
    val returnValue: Boolean
)

data class TestInfo(
    val text: String,
    val testObj: TestInfo1
)

data class TestInfo1(
    val text: String
)