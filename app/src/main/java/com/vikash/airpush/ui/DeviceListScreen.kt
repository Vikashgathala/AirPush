package com.vikash.airpush.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.p2p.*
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vikash.airpush.ui.theme.AirPushTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceListScreen(context: Context, onDeviceSelected: (WifiP2pDevice) -> Unit) {
    val wifiP2pManager = remember { context.getSystemService(Context.WIFI_P2P_SERVICE) as WifiP2pManager }
    val channel = remember { wifiP2pManager.initialize(context, context.mainLooper, null) }
    val devices = remember { mutableStateListOf<WifiP2pDevice>() }
    val permissionGranted = remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        permissionGranted.value = isGranted
    }

    // Request permissions on first launch
    LaunchedEffect(Unit) {
        permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    // Discover devices
    LaunchedEffect(permissionGranted.value) {
        if (permissionGranted.value) {
            discoverDevices(context, wifiP2pManager, channel, devices)
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Available Devices") }) }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            items(devices) { device ->
                DeviceItem(device = device, onClick = { onDeviceSelected(device) })
            }
        }
    }
}

@Composable
fun DeviceItem(device: WifiP2pDevice, onClick: (WifiP2pDevice) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick(device) },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = device.deviceName, style = MaterialTheme.typography.bodyLarge)
            Text(text = device.deviceAddress, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@SuppressLint("MissingPermission")
fun discoverDevices(
    context: Context,
    wifiP2pManager: WifiP2pManager,
    channel: WifiP2pManager.Channel,
    devices: MutableList<WifiP2pDevice>
) {
    val intentFilter = IntentFilter().apply {
        addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION)
        addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION)
        addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION)
        addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION)
    }

    val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION -> {
                    wifiP2pManager.requestPeers(channel) { peerList ->
                        devices.clear()
                        devices.addAll(peerList.deviceList)
                    }
                }
            }
        }
    }

    context.registerReceiver(receiver, intentFilter)

    wifiP2pManager.discoverPeers(channel, object : WifiP2pManager.ActionListener {
        override fun onSuccess() {
            Log.d("WiFiP2P", "Discovery started")
        }

        override fun onFailure(reason: Int) {
            Log.e("WiFiP2P", "Discovery failed: $reason")
        }
    })
}
