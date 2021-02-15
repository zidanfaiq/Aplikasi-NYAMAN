package com.zidanfaiq.percobaan

import android.app.Activity
import android.os.Bundle
import android.os.Parcelable
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.zidanfaiq.percobaan.data.Makanan
import kotlinx.android.synthetic.main.activity_detail_makanan.*
import kotlinx.android.synthetic.main.content_scrolling.*

class DetailMakananActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_DATA_MAKANAN = "extra_data_makanan"
    }
    inline fun <reified T : Parcelable> Activity.getParcelableExtra(key: String) = lazy {
        intent.getParcelableExtra<T>(key)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_makanan)
        setSupportActionBar(findViewById(R.id.toolbar))
        val DataMakanan by getParcelableExtra<Makanan>(DetailMakananActivity.EXTRA_DATA_MAKANAN)
        supportActionBar?.title = DataMakanan?.nama_makanan.toString()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        tv_deskripsi.text = DataMakanan?.deskripsi_makanan.toString()

        Glide.with(this)
            .load(DataMakanan?.foto_makanan.toString())
            .apply(RequestOptions().override(700, 700))
            .into(img_foto_makanan)

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}