package com.sakuramomoko.repluginbridge

import android.content.Context
import android.util.Log
import com.qihoo360.replugin.RePluginApplication
import com.sakuramomoko.repluginbridge.api.TestAction
import com.sakuramomoko.repluginbridge.api.TestCallback
import com.sakuramomoko.repluginbridge.api.TestController
import com.sakuramomoko.repluginbridge.api.TestInfo
import com.wali.live.replugin.RepluginBridge
import com.wali.live.replugin.TestAction
import com.wali.live.replugin.TestCallback
import com.wali.live.replugin.TestController
import com.wali.live.replugin.TestInfo

/**
 * Created by wangfeihang on 2019/4/15.
 */
class MainApplication : RePluginApplication() {

    override fun onCreate() {
        super.onCreate()
        Log.i("MainApplication", "onCreate")
        RepluginBridge.register(TestAction::class.java, {
            object : TestAction {
                override fun show(context: Context, testInfo: TestInfo, callback: TestCallback): TestController {
                    Log.i("TestAction", "showTestAction, testInfo:" + testInfo)
                    callback.onSuc(testInfo.testObj)
                    return object : TestController {
                        override fun onControl(controller: String): String {
                            Log.i("TestAction", "onController:$controller")
                            return "onControl suc"
                        }
                    }
                }
            }
        })
    }

    override fun attachBaseContext(base: Context?) {
        Log.i("MainApplication", "attachBaseContext")
        super.attachBaseContext(base)
    }
}

