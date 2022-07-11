package com.and2t2.secondhand.ui.uihome

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.and2t2.secondhand.R
import com.and2t2.secondhand.domain.model.SellerCategory
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView


class KategoriAdapter(val onclick: (id: Int) -> Unit) : RecyclerView.Adapter<KategoriAdapter.KategoriViewHolder>() {


    var selectedPosition : Int = 0

    private val difCallback = object : DiffUtil.ItemCallback<SellerCategory>() {
        override fun areItemsTheSame(oldItem: SellerCategory, newItem: SellerCategory): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: SellerCategory, newItem: SellerCategory): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private val differ = AsyncListDiffer(this, difCallback)

    fun updateDataKategori(kategori: List<SellerCategory>) = differ.submitList(kategori)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KategoriViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_home_category,parent,false)
        return KategoriViewHolder(view)
    }

    override fun onBindViewHolder(holder: KategoriViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int =differ.currentList.size


    inner class KategoriViewHolder(view: View) : RecyclerView.ViewHolder(view){
        private val tvNama = view.findViewById<MaterialTextView>(R.id.tv_categoryHomeProduct)
        private val cardClick = view.findViewById<ConstraintLayout>(R.id.constraintHomeProductCategory)
        private val cardIcon = view.findViewById<ShapeableImageView>(R.id.iv_itemSearchHomeProductCategory)
        fun bind(data : SellerCategory){
            tvNama.text = data.name
            if(selectedPosition == adapterPosition){
                cardIcon.setColorFilter(ContextCompat.getColor(itemView.context,R.color.neutral01), android.graphics.PorterDuff.Mode.SRC_IN)
                cardClick.setBackgroundColor(ContextCompat.getColor(itemView.context,R.color.darkblue04))
                tvNama.setTextColor(ContextCompat.getColor(itemView.context,R.color.neutral01))
            }else {
                cardIcon.setColorFilter(ContextCompat.getColor(itemView.context, R.color.black), android.graphics.PorterDuff.Mode.SRC_IN)
                cardClick.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.darkblue01))
                tvNama.setTextColor(ContextCompat.getColor(itemView.context, R.color.black))
            }
            cardClick.setOnClickListener {
                onclick.invoke(data.id)
                notifyItemChanged(selectedPosition)
                selectedPosition = adapterPosition
                notifyItemChanged(selectedPosition)

            }
        }// bind

    }
}