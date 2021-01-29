package com.zidanfaiq.percobaan.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.zidanfaiq.percobaan.DetailActivity
import com.zidanfaiq.percobaan.R
import com.zidanfaiq.percobaan.data.Resto
import de.hdodenhof.circleimageview.CircleImageView

class RestoAdapter(val listDataResto: ArrayList<Resto>, val context: Context) : RecyclerView.Adapter<RestoAdapter.RestoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_resto, parent, false)
        return RestoViewHolder(view)
    }

    override fun getItemCount(): Int = listDataResto.size

    override fun onBindViewHolder(holder: RestoViewHolder, position: Int) {
        val DataResto = listDataResto[position]
        Glide.with(holder.itemView.context)
            .load(DataResto.foto_resto)
            .apply(RequestOptions().override(350, 550))
            .into(holder.imgFotoResto)
        holder.tvNamaResto.text = DataResto.nama_resto
        holder.tvDeskResto.text = DataResto.deskripsi_resto
        holder.itemView.setOnClickListener {
            val moveWithObjectIntent = Intent(context, DetailActivity::class.java)
            moveWithObjectIntent.putExtra(DetailActivity.EXTRA_DATARESTO, DataResto)
            context.startActivity(moveWithObjectIntent)
        }
    }

    inner class RestoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvNamaResto: TextView = itemView.findViewById(R.id.tv_nama)
        var tvDeskResto: TextView = itemView.findViewById(R.id.tv_deskripsi)
        var imgFotoResto: CircleImageView = itemView.findViewById(R.id.img_foto)
    }
}