package com.zidanfaiq.percobaan.ui.home.tablayout

import android.os.Bundle
import android.view.*
import android.widget.EditText
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.zidanfaiq.percobaan.R
import com.zidanfaiq.percobaan.adapter.MakananAdapter
import com.zidanfaiq.percobaan.data.Makanan
import com.zidanfaiq.percobaan.databinding.FragmentMenuBinding
import java.util.*
import kotlin.collections.ArrayList

class MenuFragment : Fragment() {

    private val list = ArrayList<Makanan>()
    private var _binding: FragmentMenuBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentMenuBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)
        list.addAll(getListDataMakanan())
        binding.rvData.setHasFixedSize(true)
        showDataMakanan()
    }

    fun getListDataMakanan(): ArrayList<Makanan> {
        val dataNamaMakanan = resources.getStringArray(R.array.data_nama_makanan)
        val dataDeskripsiMakanan = resources.getStringArray(R.array.data_deskripsi_makanan)
        val dataFotoMakanan = resources.getStringArray(R.array.data_foto_makanan)
        val listDataMakanan = ArrayList<Makanan>()
        for (position in dataNamaMakanan.indices) {
            val DataMakanan = Makanan(
                dataNamaMakanan[position],
                dataDeskripsiMakanan[position],
                dataFotoMakanan[position]
            )
            listDataMakanan.add(DataMakanan)
        }
        return listDataMakanan
    }

    private fun showDataMakanan() {
        binding.rvData.layoutManager = LinearLayoutManager(activity)
        val DataFoodAdapter = MakananAdapter(list, requireActivity())
        binding.rvData.adapter = DataFoodAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search, menu)
        val menuItem = menu!!.findItem(R.id.search)

        if (menuItem != null) {
            val searchView = menuItem.actionView as SearchView
            val editText = searchView.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
            editText.hint = "Cari"

            searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText!!.isNotEmpty()) {
                        list.clear()
                        val search = newText.toLowerCase(Locale.getDefault())
                        getListDataMakanan().forEach {
                            if (it.nama_makanan.toLowerCase(Locale.getDefault()).contains(search)) {
                                list.add(it)
                            }
                            binding.rvData.adapter!!.notifyDataSetChanged()
                        }
                    } else {
                        list.clear()
                        list.addAll(getListDataMakanan())
                        binding.rvData.adapter!!.notifyDataSetChanged()
                    }

                    return true
                }

            })
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }
}