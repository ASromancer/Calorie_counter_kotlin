package com.app.testkotlin.ui.home

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.testkotlin.databinding.ItemHomeCategoryBinding
import com.app.testkotlin.model.Category
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target

class HomeCategoryAdapter(
    var mHomeFoodList: List<Category>,
) : RecyclerView.Adapter<HomeCategoryAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemHomeCategoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHomeCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            with(mHomeFoodList[position]){
                binding.tvHomeCategory.text = this.name
                Glide.with(holder.binding.ivHomeCategory.context)
                    .load(this.image)
                    .into(holder.binding.ivHomeCategory)
            }
        }
    }

    // return the size of languageList
    override fun getItemCount(): Int {
        return mHomeFoodList.size
    }
}