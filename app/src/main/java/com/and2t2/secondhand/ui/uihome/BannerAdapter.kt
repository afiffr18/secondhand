package com.and2t2.secondhand.ui.uihome

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.and2t2.secondhand.R
import com.and2t2.secondhand.data.remote.dto.seller.SellerBannerDtoItemX
import com.and2t2.secondhand.data.remote.dto.seller.SellerBannerDtoX
import com.and2t2.secondhand.domain.model.SellerCategory
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class BannerAdapter : RecyclerView.Adapter<BannerAdapter.BannerViewHolder>() {

    private val difCallback = object : DiffUtil.ItemCallback<SellerBannerDtoItemX>() {

        override fun areItemsTheSame(
            oldItem: SellerBannerDtoItemX,
            newItem: SellerBannerDtoItemX
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: SellerBannerDtoItemX,
            newItem: SellerBannerDtoItemX
        ): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private val differ = AsyncListDiffer(this, difCallback)

    fun updateDataBanner(banner: List<SellerBannerDtoItemX>) = differ.submitList(banner)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_banner_list,parent,false)
        return BannerViewHolder(view)
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int = differ.currentList.size

    inner class BannerViewHolder(view : View) : RecyclerView.ViewHolder(view){
        private val bannerImage = view.findViewById<ImageView>(R.id.iv_banner)
        fun bind(banner : SellerBannerDtoItemX){
            Glide.with(itemView.context).load(banner.imageUrl).into(bannerImage)
        }
    }
}