package com.and2t2.secondhand.ui.uihome

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.and2t2.secondhand.R
import com.and2t2.secondhand.common.toRp
import com.and2t2.secondhand.domain.model.BuyerProduct
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.card.MaterialCardView
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView


class ProductAdapter(private val onClick : (id : Int) -> Unit) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    private val difCallback = object : DiffUtil.ItemCallback<BuyerProduct>() {
        override fun areItemsTheSame(oldItem: BuyerProduct, newItem: BuyerProduct): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: BuyerProduct, newItem: BuyerProduct): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private val differ = AsyncListDiffer(this, difCallback)

    fun updateDataProduct(product: List<BuyerProduct>) = differ.submitList(product)



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_home_list_product,parent,false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int = differ.currentList.size

    inner class ProductViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val image = view.findViewById<ShapeableImageView>(R.id.iv_product_image)
        val namaBarang = view.findViewById<MaterialTextView>(R.id.tv_product_name)
        val hargaBarang = view.findViewById<MaterialTextView>(R.id.tv_product_price)
        val kategori = view.findViewById<MaterialTextView>(R.id.tv_product_category)
        val itemclick = view.findViewById<MaterialCardView>(R.id.card_item_home_list_product)

        private val bgOptions = RequestOptions().placeholder(R.drawable.ic_baseline_image_24)
        fun bind(buyerProduct: BuyerProduct){
            Glide.with(image).load(buyerProduct.imageUrl).apply(bgOptions).into(image)
            namaBarang.text = buyerProduct.namaBarang
            hargaBarang.text = buyerProduct.hargaBarang.toRp()
            kategori.text = buyerProduct.kategori

            itemclick.setOnClickListener {
                onClick.invoke(buyerProduct.id)
            }
        }

    }
}