package com.zidanfaiq.percobaan

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.zidanfaiq.percobaan.data.Resto

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    companion object {
        const val EXTRA_DATARESTO = "extra_dataresto"
    }
    inline fun <reified T : Parcelable> Activity.getParcelableExtra(key: String) = lazy {
        intent.getParcelableExtra<T>(key)
    }

    val DataResto by getParcelableExtra<Resto>(DetailActivity.EXTRA_DATARESTO)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        supportActionBar?.title = DataResto?.nama_resto.toString()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        var lat:Double = DataResto!!.lat
        var lng:Double = DataResto!!.lang
        val location = LatLng(lat, lng)
        mMap.addMarker(MarkerOptions().position(location).title(DataResto!!.nama_resto.toString()))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 16.0f))
    }
}