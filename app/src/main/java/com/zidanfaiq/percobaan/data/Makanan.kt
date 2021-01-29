package com.zidanfaiq.percobaan.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Makanan(
    var nama_makanan: String,
    var harga_makanan: String,
    var foto_makanan: String
) : Parcelable