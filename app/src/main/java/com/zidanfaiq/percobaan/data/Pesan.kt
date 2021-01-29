package com.zidanfaiq.percobaan.data

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Pesan(
    var id: String? = null,
    var menupesan: String? = null,
    var hargapesan: String? = null,
    var keterangan: String? = null,
    var date: Timestamp? = null
) : Parcelable