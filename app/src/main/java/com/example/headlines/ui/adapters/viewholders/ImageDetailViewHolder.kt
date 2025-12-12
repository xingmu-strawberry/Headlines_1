package com.example.headlines.ui.adapters.viewholders

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.headlines.R
import com.example.headlines.databinding.ItemDetailImageBinding

class ImageDetailViewHolder(private val binding: ItemDetailImageBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(images: List<String>) {
        if (images.isNotEmpty()) {
            // 显示第一张图片，可以根据需要显示多张
            Glide.with(binding.root.context)
                .load(images.first())
                .placeholder(R.drawable.ic_image_placeholder)
                .into(binding.imageView)

            // 可以添加图片描述
            if (images.size > 1) {
                binding.textImageDescription.text = "共${images.size}张图片"
            }
        }
    }
}