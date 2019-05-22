package com.sakuramomoko.repluginbridge.api

import android.content.Context

/**
 * Created by wangfeihang on 2019/5/22.
 */
interface TestActionDemo2 {
    fun onTestDemo2(context: Context, testInfo: TestInfo, callback: TestCallback): TestController
}