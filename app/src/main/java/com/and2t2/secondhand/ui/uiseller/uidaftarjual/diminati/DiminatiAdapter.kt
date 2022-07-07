package com.and2t2.secondhand.ui.uiseller.uidaftarjual.diminati

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.and2t2.secondhand.R
import com.and2t2.secondhand.common.toRp
import com.and2t2.secondhand.databinding.ItemDaftarjualDiminatiBinding
import com.and2t2.secondhand.databinding.ItemDaftarjualProdukAddBinding
import com.and2t2.secondhand.databinding.ItemDaftarjualProdukBinding
import com.and2t2.secondhand.domain.model.SellerProduct
import com.and2t2.secondhand.ui.uiseller.uidaftarjual.produk.ProdukAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

//class DiminatiAdapter(private val onClick: (id: Int) -> Unit): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
//
//    val diffCallback = object : DiffUtil.ItemCallback<SellerProduct>() {
//        override fun areItemsTheSame(oldItem: SellerProduct, newItem: SellerProduct): Boolean {
//            return oldItem.id == newItem.id
//        }
//
//        override fun areContentsTheSame(oldItem: SellerProduct, newItem: SellerProduct): Boolean {
//            return oldItem.hashCode() == newItem.hashCode()
//        }
//    }
//
//    private var differ = AsyncListDiffer(this, diffCallback)
//
//    fun updateDataRecycler(product: List<SellerProduct>) = differ.submitList(product)
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        return when (viewType) {
//            R.layout.item_daftarjual_diminati -> {
//                val binding = ItemDaftarjualProdukBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//                ProdukViewHolder(binding)
//            }
//            else -> throw IllegalArgumentException("unknown view type $viewType")
//        }
//    }
//
//    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        when (getItemViewType(position)) {
//            R.layout.item_daftarjual_diminati -> (holder as ProdukAdapter.ProdukViewHolder).bind(differ.currentList[position - 0])
//        }
//    }
//
//    override fun getItemCount(): Int = differ.currentList.size + 1
//
//    override fun getItemViewType(position: Int): Int {
//        return when (position) {
//            0 -> R.layout.item_daftarjual_produk
//            else -> R.layout.item_daftarjual_produk
//        }
//    }
//
//    inner class ProdukViewHolder(private val binding: ItemDaftarjualDiminatiBinding): RecyclerView.ViewHolder(binding.root) {
//        fun bind(item: SellerProduct) {
//            binding.apply {
//                Glide.with(itemView.context)
//                    .load(item.imageUrl)
//                    .apply(RequestOptions().placeholder(R.drawable.placeholder_image))
//                    .into(binding.ivProductImage)
//
//                tvNamaProduk.text = item.productName
//                tvKategori.text = item.categories
//                tvHarga.text = item.basePrice?.toRp()
//
//                itemDaftarjualProduk.setOnClickListener {
//                    onClick.invoke(item.id)
//                }
//            }
//        }
//    }
//
//}