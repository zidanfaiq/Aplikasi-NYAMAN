package com.zidanfaiq.percobaan.ui.home.tablayout

import android.os.Bundle
import android.view.*
import android.widget.EditText
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.zidanfaiq.percobaan.R
import com.zidanfaiq.percobaan.adapter.RestoAdapter
import com.zidanfaiq.percobaan.data.Resto
import com.zidanfaiq.percobaan.databinding.FragmentRestoBinding
import java.util.*
import kotlin.collections.ArrayList

class RestoFragment : Fragment() {

    private val list = ArrayList<Resto>()
    private var _binding: FragmentRestoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentRestoBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)
        list.addAll(getListDataResto())
        binding.rvData.setHasFixedSize(true)
        showDataRestaurant()
    }

    fun getListDataResto(): ArrayList<Resto> {
        val dataNamaResto = resources.getStringArray(R.array.data_nama_resto)
        val dataDeskripsiResto = resources.getStringArray(R.array.data_deskripsi_resto)
        val dataFotoResto = resources.getStringArray(R.array.data_foto_resto)
        val dataLat = resources.getStringArray(R.array.data_lat)
        val dataLang = resources.getStringArray(R.array.data_lang)
        val listDataResto = ArrayList<Resto>()
        for (position in dataNamaResto.indices) {
            val DataResto = Resto(
                dataNamaResto[position],
                dataDeskripsiResto[position],
                dataFotoResto[position],
                dataLat[position].toDouble(),
                dataLang[position].toDouble()
            )
            listDataResto.add(DataResto)
        }
        return listDataResto
    }

    private fun showDataRestaurant() {
        binding.rvData.layoutManager = LinearLayoutManager(activity)
        val DataRestoAdapter = RestoAdapter(list, requireActivity())
        binding.rvData.adapter = DataRestoAdapter
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
                        getListDataResto().forEach {
                            if (it.nama_resto.toLowerCase(Locale.getDefault()).contains(search)) {
                                list.add(it)
                            }
                            binding.rvData.adapter!!.notifyDataSetChanged()
                        }
                    } else {
                        list.clear()
                        list.addAll(getListDataResto())
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