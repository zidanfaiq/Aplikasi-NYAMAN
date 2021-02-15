package com.zidanfaiq.percobaan.ui.pesan

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.zidanfaiq.percobaan.PesanAddUpdateActivity
import com.zidanfaiq.percobaan.R
import com.zidanfaiq.percobaan.adapter.PesanAdapter
import com.zidanfaiq.percobaan.data.Pesan
import com.zidanfaiq.percobaan.databinding.FragmentPesanBinding
import com.zidanfaiq.percobaan.helper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PesanFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private var _binding: FragmentPesanBinding? = null
    private val binding get() = _binding!!
    private lateinit var firestore: FirebaseFirestore
    private lateinit var adapter: PesanAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentPesanBinding.inflate(inflater, container, false)
        val view = binding.root
        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = "Pesan"
        setHasOptionsMenu(true)

        firestore = Firebase.firestore
        auth = Firebase.auth
        binding.rvPesan.layoutManager = LinearLayoutManager(activity)
        binding.rvPesan.setHasFixedSize(true)
        adapter = PesanAdapter(requireActivity())

        loadPesan()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_add, menu)
        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_refresh -> {
                val mFragmentManager = fragmentManager as FragmentManager
                mFragmentManager
                    .beginTransaction()
                    .detach(this)
                    .attach(this)
                    .commit()
            }
            R.id.action_add -> {
                val intent = Intent(activity, PesanAddUpdateActivity::class.java)
                startActivityForResult(intent, helper.REQUEST_ADD)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun loadPesan() {
        GlobalScope.launch(Dispatchers.Main) {
            val pesanList = ArrayList<Pesan>()

            val currentUser = auth.currentUser
            firestore.collection("pesan")
                .whereEqualTo("uid", currentUser?.uid)
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        val id = document.id
                        val menupesan = document.get("menu").toString()
                        val hargapesan = document.get("harga").toString()
                        val keterangan = document.get("keterangan").toString()
                        val date = document.get("date") as com.google.firebase.Timestamp
                        pesanList.add(Pesan(id, menupesan, hargapesan, keterangan, date))
                    }
                    if (pesanList.size > 0) {
                        binding.rvPesan.adapter = adapter
                        adapter.listPesan = pesanList
                    } else {
                        adapter.listPesan.clear()
                        binding.rvPesan?.adapter?.notifyDataSetChanged()
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(activity, "Error adding document", Toast.LENGTH_SHORT).show()
                }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            when (requestCode) {
                helper.REQUEST_ADD -> if (resultCode == helper.RESULT_ADD) {
                    loadPesan()
                    Toast.makeText(activity, "Satu menu berhasil di pesan", Toast.LENGTH_SHORT).show()
                }
                helper.REQUEST_UPDATE ->
                    when (resultCode) {
                        helper.RESULT_UPDATE -> {
                            loadPesan()
                            Toast.makeText(activity, "Satu pesan berhasil di ubah", Toast.LENGTH_SHORT).show()
                        }
                        helper.RESULT_DELETE -> {
                            loadPesan()
                            Toast.makeText(activity, "Satu pesan berhasil di hapus", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }
}