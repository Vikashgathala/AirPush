# AirPush - A Cross-Platform File Sharing Solution

**AirPush** is an open-source alternative to Apple's AirDrop, designed for seamless peer-to-peer (P2P) file transfers between Android and iOS devices. The project leverages **Wi-Fi Direct (Wi-Fi P2P)** and **mDNS (Bonjour Protocol)** to establish a direct, high-speed file-sharing link without requiring an internet connection.

🚀 **Current Status:** The project is in its early development phase and is not yet functional. Check back later for updates.

## ✨ Features (Planned)

- **Cross-Platform Support**: Android-to-iOS file transfer via local wireless networking.
- **Wi-Fi Direct & Bonjour Service**: Uses Wi-Fi P2P for Android and mDNS for iOS device discovery.
- **Fast File Transfer**: Direct high-speed data transmission without the need for external servers.
- **End-to-End Encryption**: Ensuring secure file sharing using AES-256 and TLS.
- **Seamless UI**: Built with **Jetpack Compose** for an intuitive user experience.

---

## 📦 Technology Stack

### **Networking**

- **Wi-Fi Direct (Android)** – `android.net.wifi.p2p`
- **Multicast DNS (iOS)** – `NSNetService` & `NSNetServiceBrowser`
- **WebRTC** – Future implementation for enhanced NAT traversal

### **Android Development**

- **Jetpack Compose** – UI framework
- **Kotlin Coroutines** – Asynchronous operations
- **Ktor Client** – P2P data transmission
- **Parcelize** – Data serialization for transfers

### **Security & Encryption**

- **AES-256** – File encryption
- **TLS (Transport Layer Security)** – Secure key exchange
- **SHA-256** – File integrity verification

---

## 💜 Code Examples

### **Wi-Fi Direct Device Discovery (Android)**

```kotlin
@SuppressLint("MissingPermission")
fun discoverDevices(manager: WifiP2pManager, channel: WifiP2pManager.Channel, devices: MutableList<WifiP2pDevice>) {
    manager.discoverPeers(channel, object : WifiP2pManager.ActionListener {
        override fun onSuccess() {}
        override fun onFailure(reason: Int) {}
    })
    manager.requestPeers(channel) { peerList ->
        devices.clear()
        devices.addAll(peerList.deviceList)
    }
}
```

### **File Transfer via Sockets**

```kotlin
suspend fun sendFile(socket: Socket, file: File) {
    withContext(Dispatchers.IO) {
        socket.getOutputStream().use { output ->
            FileInputStream(file).use { input ->
                input.copyTo(output)
            }
        }
    }
}
```

### **Receive File Over Wi-Fi P2P**

```kotlin
suspend fun receiveFile(socket: Socket, destination: File) {
    withContext(Dispatchers.IO) {
        socket.getInputStream().use { input ->
            FileOutputStream(destination).use { output ->
                input.copyTo(output)
            }
        }
    }
}
```

### **mDNS Service for iOS Discovery (Planned)**

```swift
import Foundation

let service = NetService(domain: "local.", type: "_airpush._tcp.", name: "AirPushDevice", port: 8888)
service.publish()
```

---

## 👥 Contributing

New contributors are welcome! Follow these steps to get started:

1. **Fork the repository** and create a new branch.
2. Ensure you have the **Android SDK 35** and **Kotlin 1.8+** installed.
3. Implement features or fix bugs and submit a **pull request**.
4. Follow best practices in **Jetpack Compose**, **coroutines**, and **networking**.
5. Join discussions in issues and share your ideas!

### **Development Setup**

```sh
git clone https://github.com/vikashgathala/airpush.git
cd airpush
./gradlew assembleDebug
```

---

-

Stay tuned for updates! 🚀