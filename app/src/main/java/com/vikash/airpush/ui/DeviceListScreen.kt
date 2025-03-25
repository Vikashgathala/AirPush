package com.vikash.airpush.ui

import android.net.wifi.p2p.WifiP2pDevice
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vikash.airpush.ui.theme.AirPushTheme

@Composable
fun DeviceListScreen(devices: List<WifiP2pDevice>, onDeviceSelected: (WifiP2pDevice) -> Unit) {
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
                DeviceItem(device, onClick = { onDeviceSelected(device) })
            }
        }
    }
}

@Composable
fun DeviceItem(device: WifiP2pDevice, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
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
fun DeviceListScreenPreview() {
    val sampleDevices = listOf(
        WifiP2pDevice().apply { deviceName = "Device 1" },
        WifiP2pDevice().apply { deviceName = "Device 2" }
    )
    AirPushTheme {
        DeviceListScreen(sampleDevices, onDeviceSelected = {})
    }
}
