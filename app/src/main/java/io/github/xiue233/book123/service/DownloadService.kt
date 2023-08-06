package io.github.xiue233.book123.service

import android.app.DownloadManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.os.IBinder

object Downloader {
    fun sendDownloadRequest(context: Context, name: String, url: String, fileType: String) {
        context.startService(
            Intent(context, DownloadService::class.java).apply {
                putExtra("name", name)
                putExtra("url", url)
                putExtra("fileType", fileType)
            }
        )
    }
}

class DownloadService : Service() {
    private lateinit var downloadManager: DownloadManager

    override fun onCreate() {
        super.onCreate()
        downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            download(
                it.getStringExtra("name"),
                it.getStringExtra("url"),
                it.getStringExtra("fileType")
            )
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun download(name: String?, url: String?, fileType: String?) {
        if (name.isNullOrEmpty() || url.isNullOrEmpty() || fileType.isNullOrEmpty()) {
            return
        }
        downloadManager.enqueue(
            DownloadManager.Request(Uri.parse(url))
                .apply {
                    setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI)
                    setTitle("正在下载")
                    setDescription("$name.$fileType")
                    setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    setDestinationInExternalPublicDir(
                        Environment.DIRECTORY_DOWNLOADS,
                        "$name.$fileType"
                    )
                }
        )
    }

    override fun onBind(p0: Intent?): IBinder? = null
}