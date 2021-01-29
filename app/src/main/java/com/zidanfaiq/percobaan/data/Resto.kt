package com.zidanfaiq.percobaan.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Resto(
    var nama_resto: String,
    var deskripsi_resto: String,
    var foto_resto: String,
    val lat: Double,
    val lang: Double
) : Parcelable