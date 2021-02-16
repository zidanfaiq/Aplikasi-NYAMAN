package com.zidanfaiq.percobaan.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zidanfaiq.percobaan.PesanAddUpdateActivity
import com.zidanfaiq.percobaan.R
import com.zidanfaiq.percobaan.data.Pesan
import com.zidanfaiq.percobaan.databinding.ItemPesanBinding
import com.zidanfaiq.percobaan.helper.EXTRA_PESAN
import com.zidanfaiq.percobaan.helper.EXTRA_POSITION
import com.zidanfaiq.percobaan.helper.REQUEST_UPDATE
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class PesanAdapter(private val activity: Activity): RecyclerView.Adapter<PesanAdapter.PesanViewHolder>() {
    var listPesan = ArrayList<Pesan>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PesanViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pesan, parent, false)
        return PesanViewHolder(view)
    }

    override fun getItemCount(): Int = this.listPesan.size

    override fun onBindViewHolder(holder: PesanViewHolder, position: Int) {
        holder.bind(listPesan[position], position)
    }

    inner class PesanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemPesanBinding.bind(itemView)
        fun bind(pesan: Pesan, position: Int) {
            binding.tvItemHarga.text = pesan.hargapesan
            binding.tvItemMenu.text = pesan.menupesan
            val timestamp = pesan.date as com.google.firebase.Timestamp
            val milliseconds = timestamp.seconds * 1000 + timestamp.nanoseconds / 1000000
            val sdf = SimpleDateFormat("dd/MM/yy, HH:mm")
            val netDate = Date(milliseconds)
            val date = sdf.format(netDate).toString()
            binding.tvItemDate.text = date
            binding.tvItemKeterangan.text = pesan.keterangan

            binding.cvItemPesan.setOnClickListener{
                val intent = Intent(activity, PesanAddUpdateActivity::class.java)
                intent.putExtra(EXTRA_POSITION, position)
                intent.putExtra(EXTRA_PESAN, pesan)
                activity.startActivityForResult(intent, REQUEST_UPDATE)
            }
        }
    }
}