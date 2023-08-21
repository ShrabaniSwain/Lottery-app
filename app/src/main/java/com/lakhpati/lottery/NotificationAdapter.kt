package com.lakhpati.lottery

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.lakhpati.lottery.databinding.NotificationItemLayoutBinding

class NotificationAdapter(private val notificationData: List<NotificationData>) : RecyclerView.Adapter<NotificationAdapter.CardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.notification_item_layout, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val item = notificationData[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return notificationData.size
    }

    inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = NotificationItemLayoutBinding.bind(itemView)

        fun bind(item: NotificationData) {
            binding.tvNotificationTitle.text = item.notification_name
            binding.tvNotification.text = item.notification_details

            binding.notificationCardview.setOnClickListener {
                val intent = Intent(binding.root.context, NotificationDetailsActivity::class.java).apply {
                    putExtra("notification_name", item.notification_name)
                    putExtra("notification_details", item.notification_details)
                    putExtra("notification_image", item.notification_image)
                }
                binding.root.context.startActivity(intent)
            }
        }
    }
}