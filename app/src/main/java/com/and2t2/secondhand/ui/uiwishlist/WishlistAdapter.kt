package com.and2t2.secondhand.ui.uiwishlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.and2t2.secondhand.R
import com.and2t2.secondhand.common.toRp
import com.and2t2.secondhand.domain.model.Wishlist
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView

class WishlistAdapter(private val onClick : (id : Int) -> Unit) : RecyclerView.Adapter<WishlistAdapter.WishlistViewHolder>() {

    private val difCallback = object : DiffUtil.ItemCallback<Wishlist>() {
        override fun areItemsTheSame(oldItem: Wishlist, newItem: Wishlist): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Wishlist, newItem: Wishlist): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private val differ = AsyncListDiffer(this, difCallback)

    fun updateDataWishlist(wishlist: List<Wishlist>) = differ.submitList(wishlist)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WishlistViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_wishlist,parent,false)
        return WishlistViewHolder(view)
    }

    override fun onBindViewHolder(holder: WishlistViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int = differ.currentList.size

    inner class WishlistViewHolder(view : View) : RecyclerView.ViewHolder(view){
        private val gambarProduct = view.findViewById<ShapeableImageView>(R.id.iv_product_image)
        private val namaBarang = view.findViewById<MaterialTextView>(R.id.tv_product_name)
        private val hargaBarang = view.findViewById<MaterialTextView>(R.id.tv_product_price)
        private val cardWishlist = view.findViewById<MaterialCardView>(R.id.card_wishlist)
        private val kategori = view.findViewById<MaterialTextView>(R.id.tv_kategori)
        fun bind(wishlist: Wishlist){
            Glide.with(itemView.context).load(wishlist.product_image).into(gambarProduct)
            namaBarang.text = wishlist.product_name
            hargaBarang.text = wishlist.base_price?.toRp()
            kategori.text = wishlist.categories
            cardWishlist.setOnClickListener {
                wishlist.productId?.let { it1 -> onClick.invoke(it1) }
            }
        }

    }
}