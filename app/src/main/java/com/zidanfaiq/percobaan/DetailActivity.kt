package com.zidanfaiq.percobaan

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.zidanfaiq.percobaan.data.Resto
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_scrolling.*

class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_DATARESTO = "extra_dataresto"
    }
    inline fun <reified T : Parcelable> Activity.getParcelableExtra(key: String) = lazy {
        intent.getParcelableExtra<T>(key)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(findViewById(R.id.toolbar))
        val DataResto by getParcelableExtra<Resto>(DetailActivity.EXTRA_DATARESTO)
        supportActionBar?.title = DataResto?.nama_resto.toString()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        tv_deskripsi_resto.text = DataResto?.deskripsi_resto.toString()

        Glide.with(this)
            .load(DataResto?.foto_resto.toString())
            .apply(RequestOptions().override(700, 700))
            .into(img_foto_resto)

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            val moveWithObjectIntent = Intent(this, MapsActivity::class.java)
            moveWithObjectIntent.putExtra(MapsActivity.EXTRA_DATARESTO, DataResto)
            startActivity(moveWithObjectIntent)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}