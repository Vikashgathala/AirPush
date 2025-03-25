package com.vikash.airpush.transfer

import android.content.Context
import android.net.Uri
import com.vikash.airpush.models.Transfer
import com.vikash.airpush.models.TransferStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.OutputStream
import java.util.UUID

object TransferSender {
    suspend fun sendFile(context: Context, fileUri: Uri, outputStream: OutputStream): Transfer? {
        return withContext(Dispatchers.IO) {
            try {
                context.contentResolver.openInputStream(fileUri)?.use { inputStream ->
                    inputStream.copyTo(outputStream)
                }
                outputStream.flush()

                Transfer(
                    id = UUID.randomUUID().toString(),
                    fileName = fileUri.lastPathSegment ?: "unknown",
                    fileSize = File(fileUri.path ?: "").length(),
                    senderId = "local",
                    receiverId = "unknown",
                    status = TransferStatus.COMPLETED
                )
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
}
