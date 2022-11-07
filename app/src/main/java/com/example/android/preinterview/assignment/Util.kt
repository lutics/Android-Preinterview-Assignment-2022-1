package com.example.android.preinterview.assignment

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import org.apache.commons.io.FilenameUtils
import java.security.MessageDigest
import kotlin.text.Charsets.UTF_8

fun ByteArray.toHex() = joinToString(separator = "") { byte -> "%02x".format(byte) }

fun md5(str: String): String = MessageDigest.getInstance("MD5").digest(str.toByteArray(UTF_8)).toHex()

fun DownloadManager.download(context: Context, url: String) {
    val filename = FilenameUtils.getName(url)

    val request = DownloadManager.Request(Uri.parse(url))
        .setTitle("이미지 다운받기")
        .setDescription("다운받는중")
        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        .setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, filename)

    enqueue(request)
}