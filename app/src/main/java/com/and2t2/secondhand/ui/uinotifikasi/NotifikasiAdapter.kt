package com.and2t2.secondhand.ui.uinotifikasi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.and2t2.secondhand.R
import com.and2t2.secondhand.domain.model.Notifikasi

class NotifikasiAdapter : RecyclerView.Adapter<NotifikasiAdapter.NotifikasiViewHolder>() {

    private val difCallback = object : DiffUtil.ItemCallback<Notifikasi>() {
        override fun areItemsTheSame(oldItem: Notifikasi, newItem: Notifikasi): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Notifikasi, newItem: Notifikasi): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private val differ = AsyncListDiffer(this, difCallback)

    fun updateDataNotif(notifikasi: List<Notifikasi>) = differ.submitList(notifikasi)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotifikasiViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.notifikasi_card,parent,false)
        return NotifikasiViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotifikasiViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int = differ.currentList.size


    inner class NotifikasiViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val tvPenawaran = view.findViewById<TextView>(R.id.tv_penawaran)

        fun bind(notifikasi : Notifikasi){
            tvPenawaran.text = notifikasi.bidPrice.toString()
        }
    }
}