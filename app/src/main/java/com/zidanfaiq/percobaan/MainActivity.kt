package com.zidanfaiq.percobaan

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.zidanfaiq.percobaan.data.SettingModel
import com.zidanfaiq.percobaan.fragment.HomeFragment
import com.zidanfaiq.percobaan.fragment.PesanFragment
import com.zidanfaiq.percobaan.fragment.ProfilFragment
import com.zidanfaiq.percobaan.preferences.SettingPreference
import com.zidanfaiq.percobaan.receiver.AirplaneModeChangedReceiver
import com.zidanfaiq.percobaan.receiver.PowerConnectionReceiver
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    lateinit var receiver: AirplaneModeChangedReceiver
    lateinit var receiver2: PowerConnectionReceiver
    private lateinit var mSettingPreference: SettingPreference
    private lateinit var settingModel: SettingModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = Firebase.auth

        mSettingPreference = SettingPreference(this)
        showExistingPreference()
        receiver = AirplaneModeChangedReceiver()
        receiver2 = PowerConnectionReceiver()

        val ifilter = IntentFilter()
        ifilter.addAction(Intent.ACTION_POWER_CONNECTED)
        ifilter.addAction(Intent.ACTION_POWER_DISCONNECTED)
        registerReceiver(receiver2, ifilter)

        IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED).also {
            registerReceiver(receiver, it)
        }

        bottomView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        var fragment = HomeFragment()
        addFragment(fragment)

        val currentUser = auth.currentUser
        if (currentUser == null) {
            finish()
        }
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser == null) {
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
        unregisterReceiver(receiver2)
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.homeMenu -> {
                val fragment = HomeFragment()
                addFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.pesanMenu -> {
                val fragment = PesanFragment()
                addFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.profilMenu -> {
                val fragment = ProfilFragment()
                addFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun addFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(R.anim.design_bottom_sheet_slide_in, R.anim.design_bottom_sheet_slide_out)
            .replace(R.id.rootFragment, fragment, fragment.javaClass.simpleName)
            .commit()
    }

    private fun showExistingPreference() {
        settingModel = mSettingPreference.getSetting()
        populateView(settingModel)
    }

    private fun populateView(settingModel: SettingModel) {
        if (settingModel.isDarkTheme) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            this.setTheme(R.style.AppTheme)
        }
        else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            this.setTheme(R.style.AppTheme)
        }
    }

    override fun onBackPressed() {
        finishAffinity()
    }
}