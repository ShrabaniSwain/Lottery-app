package com.example.lottery

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.example.lottery.databinding.ItemBannerBinding

class HomeBannerAdapter(private val bannerImages: List<Int>) : PagerAdapter() {

    override fun getCount(): Int {
        return bannerImages.size
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view == obj
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val binding = ItemBannerBinding.inflate(LayoutInflater.from(container.context), container, false)
        val view = binding.root

        // Set the banner image for the current position
        binding.bannerImageView.setImageResource(bannerImages[position])

        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        container.removeView(obj as View)
    }
}