package com.and2t2.secondhand.ui.uiseller.uiinfopenawar

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.and2t2.secondhand.R
import com.and2t2.secondhand.common.toFormatDate
import com.and2t2.secondhand.common.toRp
import com.and2t2.secondhand.domain.model.SellerOrder
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView

class InfoPenawarAdapter(private val onStatus : (status : String,id : Int) -> Unit) : RecyclerView.Adapter<InfoPenawarAdapter.InfoPenawarViewHolder>() {

    private var selectedItem : Int = -1
    private val difCallback = object : DiffUtil.ItemCallback<SellerOrder>() {
        override fun areItemsTheSame(oldItem: SellerOrder, newItem: SellerOrder): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: SellerOrder, newItem: SellerOrder): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private val differ = AsyncListDiffer(this, difCallback)

    fun updateDataOrder(sellerOrder: List<SellerOrder>) = differ.submitList(sellerOrder)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfoPenawarViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product,parent,false)
        return InfoPenawarViewHolder(view)
    }

    override fun onBindViewHolder(holder: InfoPenawarViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int = differ.currentList.size


    inner class InfoPenawarViewHolder(view : View) : RecyclerView.ViewHolder(view){
        private val gambar = view.findViewById<ShapeableImageView>(R.id.iv_picture)
        private val hargaBarang = view.findViewById<MaterialTextView>(R.id.tv_product_price)
        private val hargaTawar = view.findViewById<MaterialTextView>(R.id.tv_product_bid)
        private val namaBarang = view.findViewById<MaterialTextView>(R.id.tv_product_name)
        private val updateDate = view.findViewById<MaterialTextView>(R.id.tv_product_time)
        private val btnTolakTerima = view.findViewById<ConstraintLayout>(R.id.tolak_terima)
        private val btnStatusHubungi = view.findViewById<ConstraintLayout>(R.id.status_hubungi)
        private val constraintItem = view.findViewById<ConstraintLayout>(R.id.constarinItemProduct)
        private val btnTolak = view.findViewById<MaterialButton>(R.id.btn_tolak)
        private val btnTerima = view.findViewById<MaterialButton>(R.id.btn_terima)
        private val btnStatus = view.findViewById<MaterialButton>(R.id.btn_status)
        private val btnHubungi = view.findViewById<MaterialButton>(R.id.btn_hubungi)
        fun bind(sellerOrder: SellerOrder){
            Glide.with(itemView.context).load(sellerOrder.imageProduct).into(gambar)
            namaBarang.text = sellerOrder.namaBarang
            updateDate.text = sellerOrder.date?.toFormatDate()
            if(sellerOrder.status=="success"){
                hargaBarang.text = sellerOrder.basePrice?.toInt()?.toRp()
                hargaTawar.text = "Ditawar ${ sellerOrder.price?.toRp()}"
                btnStatusHubungi.isVisible = true
            }else if(sellerOrder.status == "declined"){
                hargaTawar.paintFlags = hargaTawar.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                hargaBarang.text = sellerOrder.basePrice?.toInt()?.toRp()
                hargaTawar.text = "Ditawar ${ sellerOrder.price?.toRp()}"
            }else{
                hargaBarang.text = sellerOrder.basePrice?.toInt()?.toRp()
                hargaTawar.text = "Ditawar ${ sellerOrder.price?.toRp()}"
            }
            if(sellerOrder.status=="pending" && selectedItem == adapterPosition){
                btnTolakTerima.isVisible = true
                btnTolak.setOnClickListener {
                    onStatus.invoke("declined",sellerOrder.id!!)
                }
                btnTerima.setOnClickListener {
                    onStatus.invoke("accepted",sellerOrder.id!!)
                }
            }else if(sellerOrder.status=="accepted"){
                btnStatusHubungi.isVisible = true
                btnTolakTerima.isGone = true
                btnStatus.setOnClickListener {
                    onStatus.invoke("status",sellerOrder.id!!)
                }
                btnHubungi.setOnClickListener {
                    onStatus.invoke("hubungi",sellerOrder.id!!)
                }
            }else{
                btnTolakTerima.isGone = true
            }

            constraintItem.setOnClickListener {
                if(sellerOrder.status == "pending"){
                    notifyItemChanged(selectedItem)
                    selectedItem = adapterPosition
                    notifyItemChanged(selectedItem)
                }
            }


        }
    }
}