package com.example.headlines.ui.adapters.viewholders

import androidx.recyclerview.widget.RecyclerView
import com.example.headlines.databinding.ItemDetailTextBinding

class TextDetailViewHolder(private val binding: ItemDetailTextBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(content: String) {
        binding.textContent.text = content
    }
}