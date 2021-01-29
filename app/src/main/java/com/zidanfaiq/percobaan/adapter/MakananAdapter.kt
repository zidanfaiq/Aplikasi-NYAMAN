package com.zidanfaiq.percobaan.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.zidanfaiq.percobaan.R
import com.zidanfaiq.percobaan.data.Makanan

class MakananAdapter(val listDataMakanan: ArrayList<Makanan>) : RecyclerView.Adapter<MakananAdapter.MakananViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MakananViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_makanan, parent, false)
        return MakananViewHolder(view)
    }

    override fun getItemCount(): Int = listDataMakanan.size

    override fun onBindViewHolder(holder: MakananViewHolder, position: Int) {
        val DataMakanan = listDataMakanan[position]
        Glide.with(holder.itemView.context)
            .load(DataMakanan.foto_makanan)
            .apply(RequestOptions().override(350, 550))
            .into(holder.imgFotoMakanan)
        holder.tvNamaMakanan.text = DataMakanan.nama_makanan
        holder.tvHargaMakanan.text = DataMakanan.harga_makanan
        holder.itemView.setOnClickListener {
            Toast.makeText(holder.itemView.context, listDataMakanan[holder.adapterPosition].nama_makanan
                    + " " + listDataMakanan[holder.adapterPosition].harga_makanan, Toast.LENGTH_SHORT).show()
        }
    }

    inner class MakananViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvNamaMakanan: TextView = itemView.findViewById(R.id.tv_nama_makanan)
        var tvHargaMakanan: TextView = itemView.findViewById(R.id.tv_harga)
        var imgFotoMakanan: ImageView = itemView.findViewById(R.id.img_makanan)
    }
}