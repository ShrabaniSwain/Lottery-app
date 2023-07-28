package com.example.lottery

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import com.example.lottery.databinding.ItemBannerBinding
import com.squareup.picasso.Picasso

class HomeBannerAdapter(private val banners: List<HomeBannerModel>) : PagerAdapter() {

    override fun getCount(): Int {
        return banners.size
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view == obj
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val binding = ItemBannerBinding.inflate(LayoutInflater.from(container.context), container, false)
        val view = binding.root

        // Load the banner image for the current position from the URL using Picasso
        Picasso.get().load(banners[position].banner_image).into(binding.bannerImageView)
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        container.removeView(obj as View)
    }
}