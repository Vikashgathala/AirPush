package com.vikash.airpush.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id: String,
    val name: String,
    val deviceId: String
) : Parcelable
