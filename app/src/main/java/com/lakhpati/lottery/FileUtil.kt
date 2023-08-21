package com.lakhpati.lottery

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

object FileUtil {

    fun getFileFromUri(context: Context, uri: Uri): File {
        val contentResolver: ContentResolver = context.contentResolver
        val file = File(context.cacheDir, getFileName(contentResolver, uri))

        try {
            val inputStream = contentResolver.openInputStream(uri)
            val outputStream = FileOutputStream(file)
            inputStream?.copyTo(outputStream, bufferSize = 4 * 1024)
            outputStream.close()
            inputStream?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return file
    }

    private fun getFileName(contentResolver: ContentResolver, uri: Uri): String {
        var name = ""
        val projection = arrayOf(MediaStore.MediaColumns.DISPLAY_NAME)
        val cursor = contentResolver.query(uri, projection, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val columnIndex = it.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME)
                name = it.getString(columnIndex)
            }
        }
        return name
    }
}