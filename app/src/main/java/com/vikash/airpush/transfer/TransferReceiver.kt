package com.vikash.airpush.transfer

import android.content.Context
import android.net.Uri
import com.vikash.airpush.models.Transfer
import com.vikash.airpush.models.TransferStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.util.UUID

object TransferReceiver {

    /**
     * Copies a file from the source to the destination.
     * @param source The source file.
     * @param destination The destination file.
     * @return `true` if the copy is successful, `false` otherwise.
     */
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

    /**
     * Converts a given Uri into a File.
     * @param context The application context.
     * @param uri The Uri to convert.
     * @return A File object, or null if conversion fails.
     */
    private fun getFileFromUri(context: Context, uri: Uri): File? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri) ?: return null
            val tempFile = File(context.cacheDir, "temp_${System.currentTimeMillis()}.tmp")

            inputStream.use { input ->
                FileOutputStream(tempFile).use { output ->
                    input.copyTo(output)
                }
            }
            tempFile
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Receives a file and copies it to the destination directory.
     * @param context The application context.
     * @param sourceUri The Uri of the source file.
     * @param destinationDir The directory where the file should be saved.
     * @return A Transfer object if successful, otherwise null.
     */
    suspend fun receiveFile(context: Context, sourceUri: Uri, destinationDir: File): Transfer? {
        return withContext(Dispatchers.IO) {
            val sourceFile = getFileFromUri(context, sourceUri) ?: return@withContext null
            val fileName = UUID.randomUUID().toString()
            val destinationFile = File(destinationDir, fileName)

            return@withContext if (copyFile(sourceFile, destinationFile)) {
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
