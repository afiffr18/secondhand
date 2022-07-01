package com.and2t2.secondhand.ui.uihome

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.and2t2.secondhand.R
import com.and2t2.secondhand.data.remote.dto.seller.SellerCategoryDtoItem
import com.google.android.material.textview.MaterialTextView


class KategoriAdapter : RecyclerView.Adapter<KategoriAdapter.KategoriViewHolder>() {

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
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int = differ.currentList.size

    inner class KategoriViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val tvNama = view.findViewById<MaterialTextView>(R.id.tv_categoryHomeProduct)

        fun bind(data : SellerCategoryDtoItem){
            tvNama.text = data.name
        }



    }

}