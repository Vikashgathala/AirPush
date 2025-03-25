package com.vikash.airpush.ui

import android.annotation.SuppressLint
import android.content.Context
import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pManager
import android.os.Looper
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

@Composable
fun DeviceDiscoveryScreen(onDeviceSelected: (WifiP2pDevice) -> Unit) {
    val context = LocalContext.current
    val devices = remember { mutableStateListOf<WifiP2pDevice>() }
    val wifiP2pManager = remember { context.getSystemService(Context.WIFI_P2P_SERVICE) as WifiP2pManager }
    val channel = remember { wifiP2pManager.initialize(context, Looper.getMainLooper(), null) }

    LaunchedEffect(Unit) {
        discoverDevices(wifiP2pManager, channel, devices)
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Discover Devices") }) }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            items(devices) { device ->
                DeviceItem(device, onDeviceSelected)
            }
        }
    }
}

@SuppressLint("MissingPermission")
fun discoverDevices(
    manager: WifiP2pManager,
    channel: WifiP2pManager.Channel,
    devices: MutableList<WifiP2pDevice>
) {
    manager.discoverPeers(channel, object : WifiP2pManager.ActionListener {
        override fun onSuccess() {
            // Peers discovery started
        }

        override fun onFailure(reason: Int) {
            // Handle failure
        }
    })

    manager.requestPeers(channel) { peerList ->
        devices.clear()
        devices.addAll(peerList.deviceList)
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
            Text(device.deviceName, style = MaterialTheme.typography.bodyLarge)
            Text(device.deviceAddress, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DeviceDiscoveryScreenPreview() {
    AirPushTheme {
        DeviceDiscoveryScreen(onDeviceSelected = {})
    }
}