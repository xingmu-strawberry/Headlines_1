package com.example.headlines.ui.adapters.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.headlines.R
import com.example.headlines.databinding.ItemDetailLongImageBinding

class LongImageDetailViewHolder(private val binding: ItemDetailLongImageBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(images: List<String>) {
        // 加载三张横向排列的图片
        val imageViews = listOf(binding.image1, binding.image2, binding.image3)

        images.take(3).forEachIndexed { index, imageUrl ->
            if (index < imageViews.size) {
                Glide.with(binding.root.context)
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_image_placeholder)
                    .into(imageViews[index])
            }
        }

        // 如果图片少于3张，隐藏多余的ImageView
        for (i in images.size until 3) {
            if (i < imageViews.size) {
                imageViews[i].visibility = View.GONE
            }
        }
    }
}