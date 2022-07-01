package com.and2t2.secondhand.ui.uihome

import android.content.Context
import android.util.Log
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


    var selectedPosition : Int = -1
    var selectedPositionBefore = -1
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
        holder.bind(differ.currentList[position],position)
//        val items = differ.currentList[position]
//        holder.tvNama.text = items.name
//        holder.itemView.setOnClickListener {
//            onclick.invoke(items.id)
//            selectedPosition = position
//        }
//        if(selectedPosition == position){
//            holder.cardIcon.setColorFilter(ContextCompat.getColor(mcontext,R.color.neutral01), android.graphics.PorterDuff.Mode.SRC_IN)
//            holder.cardClick.setBackgroundColor(ContextCompat.getColor(mcontext,R.color.white))
//            holder.tvNama.setTextColor(ContextCompat.getColor(mcontext,R.color.neutral01))
//        }else{
//            if(position==0 && selectedPosition==-1){
//                holder.cardIcon.setColorFilter(ContextCompat.getColor(mcontext,R.color.black), android.graphics.PorterDuff.Mode.SRC_IN)
//                holder.cardClick.setBackgroundColor(ContextCompat.getColor(mcontext,R.color.darkblue01))
//                holder.tvNama.setTextColor(ContextCompat.getColor(mcontext,R.color.black))
//            }
//        }
    }

    override fun getItemCount(): Int = differ.currentList.size

    inner class KategoriViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val tvNama = view.findViewById<MaterialTextView>(R.id.tv_categoryHomeProduct)
        val cardClick = view.findViewById<ConstraintLayout>(R.id.constraintHomeProductCategory)
        val cardIcon = view.findViewById<ShapeableImageView>(R.id.iv_itemSearchHomeProductCategory)
        fun bind(data : SellerCategory,position: Int){
            tvNama.text = data.name
            cardClick.setOnClickListener {
                onclick.invoke(data.id)
                selectedPosition = position
//                Log.e("Selected",selectedPosition.toString())
//                Log.e("Position",position.toString())
//                if(selectedPosition == position){
//                    cardIcon.setColorFilter(ContextCompat.getColor(itemView.context,R.color.neutral01), android.graphics.PorterDuff.Mode.SRC_IN)
//                    cardClick.setBackgroundColor(ContextCompat.getColor(itemView.context,R.color.darkblue04))
//                    tvNama.setTextColor(ContextCompat.getColor(itemView.context,R.color.neutral01))
//                }else{
//                    if(position==0 && selectedPosition==-1){
//                    }else{
//                        cardIcon.setColorFilter(ContextCompat.getColor(itemView.context,R.color.black), android.graphics.PorterDuff.Mode.SRC_IN)
//                        cardClick.setBackgroundColor(ContextCompat.getColor(itemView.context,R.color.darkblue01))
//                        tvNama.setTextColor(ContextCompat.getColor(itemView.context,R.color.black))
//
//                    }

                when(selectedPosition){
                    position ->{
                        cardIcon.setColorFilter(ContextCompat.getColor(itemView.context,R.color.neutral01), android.graphics.PorterDuff.Mode.SRC_IN)
                        cardClick.setBackgroundColor(ContextCompat.getColor(itemView.context,R.color.darkblue04))
                        tvNama.setTextColor(ContextCompat.getColor(itemView.context,R.color.neutral01))
                    }
                    else ->{
                        cardIcon.setColorFilter(ContextCompat.getColor(itemView.context,R.color.black), android.graphics.PorterDuff.Mode.SRC_IN)
                        cardClick.setBackgroundColor(ContextCompat.getColor(itemView.context,R.color.darkblue01))
                        tvNama.setTextColor(ContextCompat.getColor(itemView.context,R.color.black))
                    }

                }
                }





        }





    }

}