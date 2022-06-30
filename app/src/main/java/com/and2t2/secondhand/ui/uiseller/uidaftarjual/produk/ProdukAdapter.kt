package com.and2t2.secondhand.ui.uiseller.uidaftarjual.produk

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.and2t2.secondhand.R
import com.and2t2.secondhand.common.toRp
import com.and2t2.secondhand.databinding.ItemDaftarjualProdukAddBinding
import com.and2t2.secondhand.databinding.ItemDaftarjualProdukBinding
import com.and2t2.secondhand.domain.model.SellerProduct
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class ProdukAdapter(private val onClick: (id: Int) -> Unit): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val diffCallback = object : DiffUtil.ItemCallback<SellerProduct>() {
        override fun areItemsTheSame(oldItem: SellerProduct, newItem: SellerProduct): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: SellerProduct, newItem: SellerProduct): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private var differ = AsyncListDiffer(this, diffCallback)

    fun updateDataRecycler(product: List<SellerProduct>) = differ.submitList(product)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.item_daftarjual_produk -> {
                val binding = ItemDaftarjualProdukBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ProdukViewHolder(binding)
            }
            R.layout.item_daftarjual_produk_add -> {
                val binding = ItemDaftarjualProdukAddBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ProdukAddViewHolder(binding)
            }
            else -> throw IllegalArgumentException("unknown view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.item_daftarjual_produk -> (holder as ProdukViewHolder).bind(differ.currentList[position - 1])
            R.layout.item_daftarjual_produk_add -> (holder as ProdukAddViewHolder).bind()
        }
    }

    override fun getItemCount(): Int = differ.currentList.size + 1

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> R.layout.item_daftarjual_produk_add
            else -> R.layout.item_daftarjual_produk
        }
    }

    inner class ProdukViewHolder(private val binding: ItemDaftarjualProdukBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SellerProduct) {
            binding.apply {
                Glide.with(itemView.context)
                    .load(item.imageUrl)
                    .apply(RequestOptions().placeholder(R.drawable.placeholder_image))
                    .into(binding.ivProductImage)

                tvNamaProduk.text = item.productName
                tvKategori.text = item.categories
                tvHarga.text = item.basePrice?.toRp()

                itemDaftarjualProduk.setOnClickListener {
                    onClick.invoke(item.id)
                }
            }
        }
    }

    inner class ProdukAddViewHolder(private val binding: ItemDaftarjualProdukAddBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            binding.apply {
                btnAddProduct.setOnClickListener {
                    onClick.invoke(0)
                }
            }
        }
    }
}