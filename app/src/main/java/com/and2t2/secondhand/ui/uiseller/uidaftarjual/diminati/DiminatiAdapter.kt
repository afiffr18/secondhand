package com.and2t2.secondhand.ui.uiseller.uidaftarjual.diminati

import android.annotation.SuppressLint
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.and2t2.secondhand.R
import com.and2t2.secondhand.common.toRp
import com.and2t2.secondhand.databinding.ItemDaftarjualDiminatiBinding
import com.and2t2.secondhand.domain.model.SellerOrder
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class DiminatiAdapter(private val onClick: (id: Int) -> Unit): RecyclerView.Adapter<DiminatiAdapter.OrderViewHolder>() {

    val diffCallback = object : DiffUtil.ItemCallback<SellerOrder>() {
        override fun areItemsTheSame(oldItem: SellerOrder, newItem: SellerOrder): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: SellerOrder, newItem: SellerOrder): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

    }

    private var differ = AsyncListDiffer(this, diffCallback)

    fun updateDataRecycler(product: List<SellerOrder>) = differ.submitList(product)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding = ItemDaftarjualDiminatiBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderViewHolder(binding)

    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int = differ.currentList.size


    inner class OrderViewHolder(private val binding: ItemDaftarjualDiminatiBinding): RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("NotifyDataSetChanged")
        fun bind(item: SellerOrder) {
            binding.apply {
                Glide.with(itemView.context)
                    .load(item.imageProduct)
                    .apply(RequestOptions().placeholder(R.drawable.placeholder_image))
                    .into(binding.ivProductImage)
                tvHargaPenawaran.text = "Ditawar "+item.price?.toRp()
                tvNamaProduk.text = item.productName
                tvWaktuDiminati.text = item.updatedAt
                tvHargaBarang.text = item.basePrice?.toRp()
                itemDaftarjualDiminati.setOnClickListener {
                    notifyDataSetChanged()
                    item.buyerId?.let { it1 -> onClick.invoke(it1) }
                }
            }
        }
    }

}