package com.zidanfaiq.percobaan.ui.home

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.zidanfaiq.percobaan.R
import com.zidanfaiq.percobaan.adapter.MakananAdapter
import com.zidanfaiq.percobaan.adapter.RestoAdapter
import com.zidanfaiq.percobaan.data.Makanan
import com.zidanfaiq.percobaan.data.Resto
import com.zidanfaiq.percobaan.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private val list = ArrayList<Resto>()
    private val list2 = ArrayList<Makanan>()
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)
        list.addAll(getListDataResto())
        list2.addAll(getListDataMakanan())
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

    private fun showDataRestaurant() {
        (activity as AppCompatActivity).supportActionBar?.title = "Restoran"
        binding.rvData.layoutManager = LinearLayoutManager(activity)
        val DataRestoAdapter = RestoAdapter(list, requireActivity())
        binding.rvData.adapter = DataRestoAdapter
    }

    private fun showDataMakanan() {
        (activity as AppCompatActivity).supportActionBar?.title = "Makanan"
        binding.rvData.layoutManager = LinearLayoutManager(activity)
        val DataFoodAdapter = MakananAdapter(list2, requireActivity())
        binding.rvData.adapter = DataFoodAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        setMode(item.itemId)
        return super.onOptionsItemSelected(item)
    }

    private fun setMode(selectedMode: Int) {
        when (selectedMode) {
            R.id.action_resto -> {
                showDataRestaurant()
            }
            R.id.action_makanan -> {
                showDataMakanan()
            }
        }
    }
}