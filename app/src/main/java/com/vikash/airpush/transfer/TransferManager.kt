package com.vikash.airpush.transfer

import com.vikash.airpush.models.Transfer
import com.vikash.airpush.models.TransferStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object TransferManager {
    private val activeTransfers = mutableListOf<Transfer>()

    suspend fun addTransfer(transfer: Transfer) {
        withContext(Dispatchers.IO) {
            activeTransfers.add(transfer.copy(status = TransferStatus.PENDING))
        }
    }

    fun getActiveTransfers(): List<Transfer> {
        return activeTransfers
    }

    suspend fun updateTransferStatus(transferId: String, status: TransferStatus) {
        withContext(Dispatchers.IO) {
            activeTransfers.find { it.id == transferId }?.let {
                val updatedTransfer = it.copy(status = status)
                activeTransfers[activeTransfers.indexOf(it)] = updatedTransfer
            }
        }
    }
}
