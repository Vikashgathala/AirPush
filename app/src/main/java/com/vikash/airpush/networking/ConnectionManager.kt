package com.vikash.airpush.networking

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.ServerSocket
import java.net.Socket
import java.util.Collections

object ConnectionManager {
    private const val PORT = 8988

    suspend fun getLocalIpAddress(): String? = withContext(Dispatchers.IO) {
        try {
            val interfaces = Collections.list(NetworkInterface.getNetworkInterfaces())
            for (networkInterface in interfaces) {
                val addresses = Collections.list(networkInterface.inetAddresses)
                for (address in addresses) {
                    if (!address.isLoopbackAddress) {
                        return@withContext address.hostAddress
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("ConnectionManager", "Error getting local IP address", e)
        }
        return@withContext null
    }

    suspend fun startServer(onClientConnected: (Socket) -> Unit) = withContext(Dispatchers.IO) {
        try {
            ServerSocket(PORT).use { serverSocket ->
                Log.d("ConnectionManager", "Server started on port $PORT")
                while (true) {
                    val clientSocket = serverSocket.accept()
                    onClientConnected(clientSocket)
                }
            }
        } catch (e: Exception) {
            Log.e("ConnectionManager", "Server error", e)
        }
    }

    suspend fun isDeviceReachable(targetIp: String): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            val address = InetAddress.getByName(targetIp)
            address.isReachable(2000)
        } catch (e: Exception) {
            Log.e("ConnectionManager", "Error checking device reachability", e)
            false
        }
    }
}