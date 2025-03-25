package com.vikash.airpush.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Device(
    val id: String,
    val name: String,
    val ipAddress: String,
    val isAvailable: Boolean
) : Parcelable