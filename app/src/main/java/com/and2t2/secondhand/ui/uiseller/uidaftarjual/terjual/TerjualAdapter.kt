package com.and2t2.secondhand.ui.uiseller.uidaftarjual.terjual

import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.and2t2.secondhand.domain.model.SellerOrder
import com.and2t2.secondhand.ui.uiseller.uidaftarjual.diminati.DiminatiAdapter

//class TerjualAdapter(private val onClick: (id: Int) -> Unit): RecyclerView.Adapter<TerjualAdapter.OrderViewHolder>() {
//
//    val diffCallback = object : DiffUtil.ItemCallback<SellerOrder>() {
//        override fun areItemsTheSame(oldItem: SellerOrder, newItem: SellerOrder): Boolean {
//            return oldItem.id == newItem.id
//        }
//
//        override fun areContentsTheSame(oldItem: SellerOrder, newItem: SellerOrder): Boolean {
//            return oldItem.hashCode() == newItem.hashCode()
//        }
//
//    }
//
//    private var differ = AsyncListDiffer(this, diffCallback)
//
//    fun updateDataRecycler(product: List<SellerOrder>) = differ.submitList(product)
//
//} 