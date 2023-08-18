package com.example.lottery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.lottery.databinding.ItemBrandNameBinding

class OtherBrandAdapter(
    private val buttonDataList: List<OtherResultsData>,
    private val callback: OtherBrandAdapterCallback
) : RecyclerView.Adapter<OtherBrandAdapter.ButtonViewHolder>() {

    private var selectedBrandIndex: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ButtonViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemBrandNameBinding.inflate(inflater, parent, false)
        return ButtonViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ButtonViewHolder, position: Int) {
        val buttonData = buttonDataList[position]
        holder.bind(buttonData,position)
    }

    override fun getItemCount(): Int = buttonDataList.size

    inner class ButtonViewHolder(private val binding: ItemBrandNameBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(buttonData: OtherResultsData, position: Int) {
            binding.apply {
                tvName1.text = buttonData.brand_name
                tvName1.setTextColor(
                    ContextCompat.getColor(
                        itemView.context,
                        if (position == selectedBrandIndex) R.color.red else R.color.purple
                    )
                )

                Glide.with(binding.ivBrandLogo.context)
                    .load(buttonData.brand_logo)
                    .apply(RequestOptions.placeholderOf(R.drawable.prize))
                    .into(binding.ivBrandLogo)
                root.setOnClickListener {
                    selectedBrandIndex = position // Update the selectedBrandIndex
                    notifyDataSetChanged() // Notify the adapter about the data change
                    callback.onBrandItemClick(buttonData, position)
                }
            }
        }
    }
}

