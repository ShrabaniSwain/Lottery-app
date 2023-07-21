package com.example.lottery

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.lottery.databinding.WinTicketCardBinding

class WInTicketAdapter(private val imageUrlList: List<String>) :
    RecyclerView.Adapter<WInTicketAdapter.ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = WinTicketCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imageUrl = imageUrlList[position]
        Glide.with(holder.itemView)
            .load(imageUrl)
            .into(holder.imageView)
    }


    override fun getItemCount(): Int {
        return imageUrlList.size
    }

    class ImageViewHolder(private val binding: WinTicketCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val imageView: ImageView = binding.ivWinTicket
    }
}
