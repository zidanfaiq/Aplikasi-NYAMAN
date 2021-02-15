package com.zidanfaiq.percobaan

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.zidanfaiq.percobaan.data.Pesan
import com.zidanfaiq.percobaan.databinding.ActivityPesanAddUpdateBinding
import com.zidanfaiq.percobaan.helper.ALERT_DIALOG_CLOSE
import com.zidanfaiq.percobaan.helper.ALERT_DIALOG_DELETE
import com.zidanfaiq.percobaan.helper.EXTRA_PESAN
import com.zidanfaiq.percobaan.helper.EXTRA_POSITION
import com.zidanfaiq.percobaan.helper.RESULT_ADD
import com.zidanfaiq.percobaan.helper.RESULT_DELETE
import com.zidanfaiq.percobaan.helper.RESULT_UPDATE
import kotlinx.android.synthetic.main.activity_pesan_add_update.*

class PesanAddUpdateActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private var isEdit = false
    private var categoriesSpinnerArray = ArrayList<String>()
    private var pesan: Pesan? = null
    private var position: Int = 0
    private var categorySelection: Int = 0
    private var categoryName: String = "0"
    private lateinit var binding: ActivityPesanAddUpdateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPesanAddUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firestore = Firebase.firestore
        auth = Firebase.auth
        categoriesSpinnerArray = getCategories()
        pesan = intent.getParcelableExtra(EXTRA_PESAN)
        if (pesan != null) {
            position = intent.getIntExtra(EXTRA_POSITION, 0)
            isEdit = true
        } else {
            pesan = Pesan()
        }
        val actionBarTitle: String
        val btnTitle: String

        if (isEdit) {
            actionBarTitle = "Update"
            btnTitle = "Update"
            pesan?.let {
                binding.edtHarga.setText(it.hargapesan)
                binding.edtKeterangan.setText(it.keterangan)
            }!!
        } else {
            actionBarTitle = "Pesan"
            btnTitle = "Pesan"
        }

        supportActionBar?.title = actionBarTitle
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.btnSubmit.text = btnTitle
        binding.btnSubmit.setOnClickListener(this)
    }

    private fun getCategories(): ArrayList<String> {
        progressbar.visibility = View.VISIBLE
        firestore.collection("categories")
            .whereEqualTo("is_active", true)
            .get()
            .addOnSuccessListener { documents ->
                var selection = 0;
                for (document in documents) {
                    val name = document.get("name").toString()
                    pesan?.let {
                        if(name==it.menupesan){
                            categorySelection = selection
                        }
                    }
                    categoriesSpinnerArray.add(name)
                    selection++
                }
                setCategories(categoriesSpinnerArray)
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this@PesanAddUpdateActivity, "Categories cannot be retrieved ", Toast.LENGTH_SHORT).show()
            }
        return categoriesSpinnerArray
    }

    private fun setCategories(paymentMethodSpinnerAarray: ArrayList<String>) {
        var spinnerAdapter= ArrayAdapter(this, android.R.layout.simple_list_item_1,paymentMethodSpinnerAarray)
        binding.edtMenu.adapter=spinnerAdapter
        binding.edtMenu.setSelection(categorySelection)
        binding.edtMenu.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View, position: Int, id: Long
            ) {
                categoryName = binding.edtMenu.selectedItem.toString()
                progressbar.visibility = View.INVISIBLE
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    override fun onClick(view: View) {
        if (view.id == R.id.btn_submit) {
            val harga = binding.edtHarga.text.toString().trim()
            val keterangan = binding.edtKeterangan.text.toString().trim()

            if (harga.isEmpty()) {
                binding.edtHarga.error = "Field can not be blank"
                return
            }
            if (keterangan.isEmpty()) {
                binding.edtKeterangan.error = "Field can not be blank"
                return
            }
            if (isEdit) {
                val currentUser = auth.currentUser
                val user = hashMapOf(
                    "uid" to currentUser?.uid,
                    "menu" to categoryName,
                    "harga" to harga,
                    "keterangan" to keterangan,
                    "date" to FieldValue.serverTimestamp()
                )
                firestore.collection("pesan").document(pesan?.id.toString())
                    .set(user)
                    .addOnSuccessListener {
                        setResult(RESULT_UPDATE, intent)
                        finish()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this@PesanAddUpdateActivity, "Gagal mengupdate data", Toast.LENGTH_SHORT).show()
                    }
            } else {
                val currentUser = auth.currentUser
                val user = hashMapOf(
                    "uid" to currentUser?.uid,
                    "menu" to categoryName,
                    "harga" to harga,
                    "keterangan" to keterangan,
                    "date" to FieldValue.serverTimestamp()
                )
                firestore.collection("pesan")
                    .add(user)
                    .addOnSuccessListener { documentReference ->
                        setResult(RESULT_ADD, intent)
                        finish()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this@PesanAddUpdateActivity, "Error adding document", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (isEdit) {
            menuInflater.inflate(R.menu.menu_form, menu)
        }
        return super.onCreateOptionsMenu(menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_delete -> showAlertDialog(ALERT_DIALOG_DELETE)
            android.R.id.home -> showAlertDialog(ALERT_DIALOG_CLOSE)
        }
        return super.onOptionsItemSelected(item)
        return true
    }

    private fun showAlertDialog(type: Int) {
        val isDialogClose = type == ALERT_DIALOG_CLOSE
        val dialogTitle: String
        val dialogMessage: String
        if (isDialogClose) {
            dialogTitle = "Batal"
            dialogMessage = "Apakah anda ingin membatalkan perubahan pada form?"
        } else {
            dialogMessage = "Apakah anda yakin ingin menghapus item ini?"
            dialogTitle = "Hapus Pesan"
        }
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle(dialogTitle)
        alertDialogBuilder
            .setMessage(dialogMessage)
            .setCancelable(false)
            .setPositiveButton("Ya") { _, _ ->
                if (isDialogClose) {
                    finish()
                } else {
                    firestore.collection("pesan").document(pesan?.id.toString())
                        .delete()
                        .addOnSuccessListener {
                            Log.d("delete", "DocumentSnapshot successfully deleted!"+pesan?.id.toString())
                            val intent = Intent()
                            intent.putExtra(EXTRA_POSITION, position)
                            setResult(RESULT_DELETE, intent)
                            finish()
                        }
                        .addOnFailureListener { e ->
                            Log.w("a", "Error deleting document", e)
                            Toast.makeText(this@PesanAddUpdateActivity, "Gagal menghapus data", Toast.LENGTH_SHORT).show()
                        }
                }
            }
            .setNegativeButton("Tidak") { dialog, _ -> dialog.cancel() }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
}