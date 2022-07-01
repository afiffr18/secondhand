package com.and2t2.secondhand.ui.uihome

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.and2t2.secondhand.R
import com.and2t2.secondhand.data.remote.dto.seller.SellerCategoryDtoItem
import com.google.android.material.card.MaterialCardView
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView


class KategoriAdapter(val onclick : (id: Int) -> Unit) : RecyclerView.Adapter<KategoriAdapter.KategoriViewHolder>() {

    private val difCallback = object : DiffUtil.ItemCallback<SellerCategoryDtoItem>() {
        override fun areItemsTheSame(oldItem: SellerCategoryDtoItem, newItem: SellerCategoryDtoItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: SellerCategoryDtoItem, newItem: SellerCategoryDtoItem): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private val differ = AsyncListDiffer(this, difCallback)

    fun updateDataKategori(kategori: List<SellerCategoryDtoItem>) = differ.submitList(kategori)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KategoriViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_home_category,parent,false)
        return KategoriViewHolder(view)
    }

    override fun onBindViewHolder(holder: KategoriViewHolder, position: Int) {
        holder.bind(differ.currentList[position],position)
    }

    override fun getItemCount(): Int = differ.currentList.size

    inner class KategoriViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val tvNama = view.findViewById<MaterialTextView>(R.id.tv_categoryHomeProduct)
        val cardClick = view.findViewById<ConstraintLayout>(R.id.constraintHomeProductCategory)
        val cardIcon = view.findViewById<ShapeableImageView>(R.id.iv_itemSearchHomeProductCategory)
        fun bind(data : SellerCategoryDtoItem,position: Int){
            tvNama.text = data.name
            cardClick.setOnClickListener {
                onclick.invoke(data.id)
                if(position == position){
                    cardIcon.setColorFilter(ContextCompat.getColor(itemView.context,R.color.neutral01), android.graphics.PorterDuff.Mode.SRC_IN)
                    cardClick.setBackgroundColor(ContextCompat.getColor(itemView.context,R.color.darkblue04))
                    tvNama.setTextColor(ContextCompat.getColor(itemView.context,R.color.neutral01))
                }
            }

        }





    }

}