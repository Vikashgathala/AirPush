package com.vikash.airpush.transfer

import android.content.Context
import android.net.Uri
import com.vikash.airpush.models.Transfer
import com.vikash.airpush.models.TransferStatus
import com.vikash.airpush.utils.FileUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.util.UUID

object TransferReceiver {
    fun copyFile(source: File, destination: File): Boolean {
        return try {
            FileInputStream(source).use { input ->
                FileOutputStream(destination).use { output ->
                    input.copyTo(output)
                }
            }
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }
    suspend fun receiveFile(context: Context, sourceUri: Uri, destinationDir: File): Transfer? {
        return withContext(Dispatchers.IO) {
            val fileName = UUID.randomUUID().toString()
            val destinationFile = File(destinationDir, fileName)

            return@withContext if (copyFile(sourceUri, destinationFile)) {
                Transfer(
                    id = UUID.randomUUID().toString(),
                    fileName = fileName,
                    fileSize = destinationFile.length(),
                    senderId = "unknown",
                    receiverId = "local",
                    status = TransferStatus.COMPLETED
                )
            } else {
                null
            }
        }
    }
}
