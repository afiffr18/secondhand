package com.and2t2.secondhand.ui.uinotifikasi


import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.and2t2.secondhand.R
import com.and2t2.secondhand.common.toRp
import com.and2t2.secondhand.domain.model.Notifikasi
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.imageview.ShapeableImageView
import kotlinx.coroutines.withContext

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


    inner class NotifikasiViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        private val tvPenawaran = itemView.findViewById<TextView>(R.id.tv_penawaran)
        private val tvUpdateDate = itemView.findViewById<TextView>(R.id.tv_tanggal)
        private val tvNotif = itemView.findViewById<TextView>(R.id.tv_notifikasi)
        private val tvProductName = itemView.findViewById<TextView>(R.id.tv_nama_barang)
        private val ivImage = itemView.findViewById<ImageView>(R.id.iv_barang)
        private val ivRead = itemView.findViewById<ImageView>(R.id.iv_cirlce)

        private val bgOptions = RequestOptions().placeholder(R.drawable.ic_baseline_image_24)
        fun bind(notifikasi : Notifikasi){
            if(notifikasi.status == "success"){
//                tvHarga.paintFlags = tvHarga.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                tvNotif.visibility = View.VISIBLE
                tvPenawaran.text = "Berhasil ditawar " + notifikasi.bidPrice?.toRp()
            }else{
                tvPenawaran.text = "Ditawar ${ notifikasi.bidPrice?.toRp() }"
            }
            if(notifikasi.read == true){
                ivRead.setColorFilter(ContextCompat.getColor(itemView.context,R.color.neutral03), android.graphics.PorterDuff.Mode.SRC_IN)
            }
            tvProductName.text = notifikasi.productId.toString()
            tvUpdateDate.text = notifikasi.updatedAt
            Glide.with(itemView.context).load(notifikasi.imageUrl).apply(bgOptions).into(ivImage)
        }
    }


}