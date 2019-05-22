package com.sakuramomoko.repluginbridge

import android.app.ActivityManager
import android.content.Context
import android.os.Process
import android.text.TextUtils
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

/**
 * Created by wangfeihang on 2019/5/10.
 */
object FileUtil {
    public fun simulateInstallExternalPlugin(context: Context) {
        val isMainProcess = TextUtils.equals(context.packageName, getMyProcessName(context))
        if (!isMainProcess) {
            return
        }
        val demo3Apk = "demo8.apk"
        val demo3apkPath = "external" + File.separator + demo3Apk
        // 文件是否已经存在？直接删除重来
        val pluginFilePath = context.filesDir.absolutePath + File.separator + demo3Apk
        val pluginFile = File(pluginFilePath)
        if (!pluginFile.exists()) {
            copyAssetsFileToAppFiles(context, demo3apkPath, demo3Apk)
        }
        if (pluginFile.exists()) {
            PluginUtil.installPlugin(pluginFilePath)
        }
    }

    private fun getMyProcessName(context: Context): String? {
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningApps = am.runningAppProcesses
        return runningApps?.run {
            val myPid = Process.myPid()
            firstOrNull { it.pid == myPid }?.processName
        }
    }

    /**
     * 从assets目录中复制某文件内容
     * @param  assetFileName assets目录下的Apk源文件路径
     * @param  newFileName 复制到/data/data/package_name/files/目录下文件名
     */
    private fun copyAssetsFileToAppFiles(context: Context, assetFileName: String, newFileName: String) {
        var inputStream: InputStream? = null
        var fos: FileOutputStream? = null
        val buffsize = 1024

        try {
            inputStream = context.assets.open(assetFileName)
            fos = context.openFileOutput(newFileName, Context.MODE_PRIVATE)

            val buffer = ByteArray(buffsize)
            var byteCount = inputStream?.read(buffer) ?: 0
            while (byteCount != -1) {
                fos!!.write(buffer, 0, byteCount)
                byteCount = inputStream?.read(buffer) ?: 0
            }
            fos!!.flush()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                inputStream!!.close()
                fos!!.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}