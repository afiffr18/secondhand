package com.and2t2.secondhand.ui.uiseller.uidaftarjual.diminati

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.and2t2.secondhand.R
import com.and2t2.secondhand.common.toRp
import com.and2t2.secondhand.databinding.ItemDaftarjualDiminatiBinding
import com.and2t2.secondhand.databinding.ItemDaftarjualProdukBinding
import com.and2t2.secondhand.domain.model.SellerOrder
import com.and2t2.secondhand.domain.model.SellerProduct
import com.and2t2.secondhand.ui.uiseller.uidaftarjual.produk.ProdukAdapter
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
        fun bind(item: SellerOrder) {
            binding.apply {
                Glide.with(itemView.context)
                    .load(item.imageProduct)
                    .apply(RequestOptions().placeholder(R.drawable.placeholder_image))
                    .into(binding.ivProductImage)

                tvNamaProduk.text = item.productName
                tvWaktuDiminati.text = item.updatedAt
                tvHargaBarang.text = item.basePrice?.toRp()
                tvHargaPenawaran.text = "Ditawar "+item.price?.toRp()

                itemDaftarjualDiminati.setOnClickListener {
                    onClick.invoke(item.id)
                }
            }
        }
    }

}