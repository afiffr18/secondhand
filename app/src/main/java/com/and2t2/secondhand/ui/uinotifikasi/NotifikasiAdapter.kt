package com.and2t2.secondhand.ui.uinotifikasi


import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
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

class NotifikasiAdapter(val listener : (id:Int) -> Unit) : RecyclerView.Adapter<NotifikasiAdapter.NotifikasiViewHolder>() {

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
        private val tvHargaBarang = itemView.findViewById<TextView>(R.id.tv_harga)
        private val tvUpdateDate = itemView.findViewById<TextView>(R.id.tv_tanggal)
        private val tvNotif = itemView.findViewById<TextView>(R.id.tv_notifikasi)
        private val tvProductName = itemView.findViewById<TextView>(R.id.tv_nama_barang)
        private val ivImage = itemView.findViewById<ShapeableImageView>(R.id.iv_barang)
        private val ivRead = itemView.findViewById<ImageView>(R.id.iv_cirlce)
        private val cardClick = itemView.findViewById<ConstraintLayout>(R.id.constraint)
        private val tvTitle = itemView.findViewById<TextView>(R.id.tv_title)
        private val bgOptions = RequestOptions().placeholder(R.drawable.ic_baseline_image_24)
        fun bind(notifikasi : Notifikasi){
            when (notifikasi.status) {
                "accepted" -> {
                    tvHargaBarang.paintFlags = tvHargaBarang.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    tvHargaBarang.text = notifikasi.basePrice?.toRp()
                    tvNotif.visibility = View.VISIBLE
                    tvPenawaran.text = "Berhasil ditawar " + notifikasi.bidPrice?.toRp()
                }
                "declined" -> {
                    tvPenawaran.paintFlags = tvPenawaran.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    tvHargaBarang.text = notifikasi.basePrice?.toRp()
                    tvPenawaran.text = "Ditawar ${ notifikasi.bidPrice?.toRp()}"
                }
                else -> {
                    tvHargaBarang.text = notifikasi.basePrice?.toRp()
                    tvPenawaran.text = "Ditawar ${ notifikasi.bidPrice?.toRp()}"
                }
            }
            if(notifikasi.read == true){
                ivRead.setColorFilter(ContextCompat.getColor(itemView.context,R.color.neutral03), android.graphics.PorterDuff.Mode.SRC_IN)
            }

            if(notifikasi.status == "create"){
                tvPenawaran.visibility = View.GONE
                tvTitle.text = "Berhasil di terbitkan"
                tvPenawaran.text = notifikasi.basePrice?.toRp()
            }
            tvProductName.text = notifikasi.namaBarang
            tvUpdateDate.text = notifikasi.updatedAt
            Glide.with(ivImage).load(notifikasi.imageUrl).apply(bgOptions).into(ivImage)
            cardClick.setOnClickListener {
                listener.invoke(notifikasi.id)
            }
        }


    }


}