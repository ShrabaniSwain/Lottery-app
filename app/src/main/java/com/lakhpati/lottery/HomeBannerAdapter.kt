package com.lakhpati.lottery

import android.content.Intent
import android.net.Uri
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import com.lakhpati.lottery.databinding.ItemBannerBinding
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

        binding.bannerImageView.setOnClickListener {
            val bannerLink = banners[position].banner_link
            if (bannerLink.isNotEmpty() && Patterns.WEB_URL.matcher(bannerLink).matches()) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(bannerLink))
                container.context.startActivity(intent)
            } else {
                Toast.makeText(container.context, "Invalid URL", Toast.LENGTH_SHORT).show()
            }
        }

        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        container.removeView(obj as View)
    }
}