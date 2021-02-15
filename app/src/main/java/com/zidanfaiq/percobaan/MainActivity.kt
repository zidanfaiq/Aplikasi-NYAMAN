package com.zidanfaiq.percobaan

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.zidanfaiq.percobaan.data.SettingModel
import com.zidanfaiq.percobaan.preferences.SettingPreference
import com.zidanfaiq.percobaan.receiver.AirplaneModeChangedReceiver
import com.zidanfaiq.percobaan.receiver.PowerConnectionReceiver

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    lateinit var receiver: AirplaneModeChangedReceiver
    lateinit var receiver2: PowerConnectionReceiver
    private lateinit var mSettingPreference: SettingPreference
    private lateinit var settingModel: SettingModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

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