package com.and2t2.secondhand.ui.uiseller.uidaftarjual.terjual

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.and2t2.secondhand.R
import com.and2t2.secondhand.common.toRp
import com.and2t2.secondhand.databinding.ItemDaftarjualDiminatiBinding
import com.and2t2.secondhand.databinding.ItemDaftarjualTerjualBinding
import com.and2t2.secondhand.domain.model.SellerOrder
import com.and2t2.secondhand.ui.uiseller.uidaftarjual.diminati.DiminatiAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class TerjualAdapter(private val onClick: (id: Int) -> Unit): RecyclerView.Adapter<TerjualAdapter.TerjualViewHolder>() {

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TerjualAdapter.TerjualViewHolder {
        val binding = ItemDaftarjualTerjualBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TerjualViewHolder(binding)

    }

    override fun onBindViewHolder(holder: TerjualAdapter.TerjualViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int = differ.currentList.size

    inner class TerjualViewHolder(private val binding: ItemDaftarjualTerjualBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SellerOrder) {
            binding.apply {
                Glide.with(itemView.context)
                    .load(item.imageProduct)
                    .apply(RequestOptions().placeholder(R.drawable.placeholder_image))
                    .into(binding.ivProductImage)

                tvNamaProduk.text = item.productName
                tvWaktuTerjual.text = item.updatedAt
                tvHargaBarang.text = item.basePrice?.toRp()
                tvHargaTerjual.text = "Terjual "+item.price?.toRp()

                itemDaftarjualTerjual.setOnClickListener {
                    onClick.invoke(item.id)
                }
            }
        }
    }

}