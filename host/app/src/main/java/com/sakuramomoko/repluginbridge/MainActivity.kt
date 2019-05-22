package com.sakuramomoko.repluginbridge

import android.app.ProgressDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.qihoo360.replugin.RePlugin
import com.wali.live.replugin.RepluginBridge
import com.wali.live.replugin.TestActionDemo2
import com.wali.live.replugin.TestCallback
import com.wali.live.replugin.TestInfo
import com.wali.live.replugin.TestInfo1
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initLoadBuiltinPluginBtn()
        initbtnGetPluginInterfaceBtn()
    }

    //加载内置插件
    private fun initLoadBuiltinPluginBtn() {
        btnLoadBuiltinPlugin.setOnClickListener {
            val dialog = ProgressDialog.show(this, "",
                "插件加载中...", false, false)
            Thread({
                FileUtil.simulateInstallExternalPlugin(this)
                RePlugin.fetchContext(StaticInfo.pluginName)
                Handler(Looper.getMainLooper()).post {
                    dialog.dismiss()
                    Toast.makeText(this, "插件加载完成", Toast.LENGTH_SHORT).show()
                }
            }).start()
        }
    }

    //获取yy插件接口
    private fun initbtnGetPluginInterfaceBtn() {
        btnGetPluginInterface.setOnClickListener {
            if (RePlugin.isPluginInstalled(StaticInfo.pluginName)) {
                val testController =
                    RepluginBridge.getApi(RePlugin.fetchClassLoader(StaticInfo.pluginName), TestActionDemo2::class.java)
                        ?.onTestDemo2(this, TestInfo("测试demo2", TestInfo1("ceshidemo2")), object : TestCallback {
                            override fun onSuc(testObj: TestInfo1) {
                                toast("onsuc, testinfo:$testObj")
                            }

                            override fun onFail() {
                            }
                        })
                val controllerReturnType = testController?.onControl("wangwangwangdemo2")
                android.util.Log.e("TestActionDemo2", "controllerReturnType:$controllerReturnType")
            } else {
                toast("请先加载插件")
            }
        }
    }

    private fun toast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}
