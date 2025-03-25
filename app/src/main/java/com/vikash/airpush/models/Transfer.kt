package com.vikash.airpush.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Transfer(
    val id: String,
    val fileName: String,
    val fileSize: Long,
    val senderId: String,
    val receiverId: String,
    val status: TransferStatus
) : Parcelable

enum class TransferStatus {
    PENDING, IN_PROGRESS, COMPLETED, FAILED
}