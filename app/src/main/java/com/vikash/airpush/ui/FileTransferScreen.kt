package com.vikash.airpush.ui

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pManager
import android.os.Looper
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vikash.airpush.ui.theme.AirPushTheme
import java.io.*
import java.net.ServerSocket
import java.net.Socket
import java.util.concurrent.Executors

@Composable
fun FileTransferScreen(selectedDevice: WifiP2pDevice) {
    val context = LocalContext.current
    var selectedFile by remember { mutableStateOf<Uri?>(null) }
    val filePicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        selectedFile = uri
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("File Transfer") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = { filePicker.launch("*/*") }) {
                Text("Select File")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text("Sending to: ${selectedDevice.deviceName}")
            Spacer(modifier = Modifier.height(16.dp))
            selectedFile?.let {
                Text("Selected File: $it")
                Button(onClick = { sendFile(context, it, selectedDevice.deviceAddress) }) {
                    Text("Send File")
                }
            }
        }
    }
}

fun sendFile(context: Context, fileUri: Uri, deviceAddress: String) {
    val executor = Executors.newSingleThreadExecutor()
    executor.execute {
        try {
            val socket = Socket(deviceAddress, 8988)
            val outputStream = socket.getOutputStream()
            val inputStream = context.contentResolver.openInputStream(fileUri)
            inputStream?.copyTo(outputStream)
            outputStream.close()
            inputStream?.close()
            socket.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}

fun startFileReceiver() {
    val executor = Executors.newSingleThreadExecutor()
    executor.execute {
        try {
            val serverSocket = ServerSocket(8988)
            val client = serverSocket.accept()
            val inputStream = client.getInputStream()
            val file = File("/storage/emulated/0/Download/received_file")
            val outputStream = FileOutputStream(file)
            inputStream.copyTo(outputStream)
            outputStream.close()
            inputStream.close()
            client.close()
            serverSocket.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FileTransferScreenPreview() {
    AirPushTheme {
        FileTransferScreen(WifiP2pDevice().apply { deviceName = "Preview Device" })
    }
}