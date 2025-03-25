package com.vikash.airpush.transfer

import android.content.Context
import android.net.Uri
import com.vikash.airpush.models.Transfer
import com.vikash.airpush.models.TransferStatus
import com.vikash.airpush.utils.FileUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.util.UUID

object TransferReceiver {
    suspend fun receiveFile(context: Context, sourceUri: Uri, destinationDir: File): Transfer? {
        return withContext(Dispatchers.IO) {
            val fileName = UUID.randomUUID().toString()
            val destinationFile = File(destinationDir, fileName)

            return@withContext if (FileUtils.copyFile(context, sourceUri, destinationFile)) {
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
