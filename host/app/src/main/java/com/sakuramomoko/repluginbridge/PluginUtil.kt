package com.sakuramomoko.repluginbridge

import com.qihoo360.replugin.RePlugin
import com.qihoo360.replugin.model.PluginInfo
import java.io.File

/**
 * Created by wangfeihang on 2019/5/10.
 */
object PluginUtil {

    fun installPlugin(pluginFilePath: String): Boolean {
        val pluginFile = File(pluginFilePath)
        var pluginInfo: PluginInfo? = null
        if (pluginFile.exists()) {
            pluginInfo = RePlugin.install(pluginFilePath)
        }
        return pluginInfo != null
    }
}