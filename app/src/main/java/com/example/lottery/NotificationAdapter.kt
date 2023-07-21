package com.example.lottery

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.lottery.databinding.NotificationItemLayoutBinding

class NotificationAdapter() : RecyclerView.Adapter<NotificationAdapter.CardViewHolder>() {

    private val dummyData = arrayListOf(
        "Notification 1",
        "Notification 2",
        "Notification 3","Notification 4","Notification 5")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.notification_item_layout, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val item = dummyData[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return dummyData.size
    }

    inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = NotificationItemLayoutBinding.bind(itemView)

        fun bind(item: String) {
            binding.tvNotificationTitle.text = item

            binding.notificationCardview.setOnClickListener {
                val intent = Intent(binding.root.context, NotificationDetailsActivity::class.java)
                binding.root.context.startActivity(intent)
            }

        }
    }
}