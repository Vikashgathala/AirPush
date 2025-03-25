package com.vikash.airpush.networking

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.InputStream
import java.io.OutputStream
import java.net.ServerSocket
import java.net.Socket

class TransferService : Service() {
    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        CoroutineScope(Dispatchers.IO).launch {
            startServer()
        }
        return START_STICKY
    }

    private fun startServer() {
        try {
            ServerSocket(8988).use { serverSocket ->
                Log.d("TransferService", "Server started on port 8988")
                while (true) {
                    val clientSocket: Socket = serverSocket.accept()
                    handleClient(clientSocket)
                }
            }
        } catch (e: Exception) {
            Log.e("TransferService", "Server error", e)
        }
    }

    private fun handleClient(socket: Socket) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val inputStream: InputStream = socket.getInputStream()
                val outputStream: OutputStream = socket.getOutputStream()
                val buffer = ByteArray(4096)
                var bytesRead: Int

                while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                    outputStream.write(buffer, 0, bytesRead)
                }

                inputStream.close()
                outputStream.close()
                socket.close()
                Log.d("TransferService", "File received successfully")
            } catch (e: Exception) {
                Log.e("TransferService", "Error handling client connection", e)
            }
        }
    }
}