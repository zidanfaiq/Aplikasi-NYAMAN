package com.zidanfaiq.percobaan

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.zidanfaiq.percobaan.data.SettingModel
import com.zidanfaiq.percobaan.databinding.ActivitySettingBinding
import com.zidanfaiq.percobaan.preferences.SettingPreference


class SettingActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var mSettingPreference: SettingPreference
    private lateinit var settingModel: SettingModel
    private lateinit var binding: ActivitySettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        binding.tvSignOut.setOnClickListener(this)
        binding.tvEmailVerify.setOnClickListener(this)

        mSettingPreference = SettingPreference(this)
        settingModel = mSettingPreference.getSetting()
        showPreferenceInForm()

        supportActionBar?.title = "Setting"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun showPreferenceInForm() {
        if (settingModel.isDarkTheme) {
            binding.rbYes.isChecked = true
        } else {
            binding.rbNo.isChecked = true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_save, menu)
        return super.onCreateOptionsMenu(menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_save -> {
                val isLoveMU = binding.rgLoveMu.checkedRadioButtonId == R.id.rb_yes
                saveSetting(isLoveMU)
            }
        }
        return super.onOptionsItemSelected(item)
        return true
    }

    private fun saveSetting(isDark: Boolean) {
        val settingPreference = SettingPreference(this)
        settingModel.isDarkTheme = isDark
        settingPreference.setSetting(settingModel)
        if (settingModel.isDarkTheme) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            this.setTheme(R.style.AppTheme)
        }
        else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            this.setTheme(R.style.AppTheme)
        }
        Toast.makeText(this, "Data Tersimpan", Toast.LENGTH_SHORT).show()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.tvSignOut -> {
                signOut()
            }
            R.id.tvEmailVerify -> {
                sendEmailVerification()
            }
        }
    }

    private fun signOut() {
        auth.signOut()
        val currentUser = auth.currentUser
        if (currentUser == null) {
            val intent = Intent(this@SettingActivity, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }
        googleSignInClient.signOut().addOnCompleteListener(this) {
        }
    }

    private fun sendEmailVerification() {
        binding.tvEmailVerify.isEnabled = false
        val user = auth.currentUser!!
        user.sendEmailVerification()
            .addOnCompleteListener(this) { task ->
                binding.tvEmailVerify.isEnabled = true
                if (task.isSuccessful) {
                    Toast.makeText(baseContext, "Verification email sent to ${user.email} ", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(baseContext, "Failed to send verification email.", Toast.LENGTH_SHORT).show()
                }
            }
    }
}