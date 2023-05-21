package com.app.testkotlin.ui.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.testkotlin.databinding.ItemFavoriteBinding
import com.app.testkotlin.databinding.ItemHomeCategoryBinding
import com.app.testkotlin.dto.Favorite
import com.app.testkotlin.model.Category
import com.bumptech.glide.Glide

class FavoriteAdapter(
    var mFavoriteList: List<Favorite>,
) : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemFavoriteBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            with(mFavoriteList[position]){
                binding.tvFavoriteName.text = this.getFood().name
                Glide.with(holder.binding.ivFavoriteImage.context)
                    .load(this.getFood().image)
                    .into(holder.binding.ivFavoriteImage)
            }
        }
    }

    // return the size of languageList
    override fun getItemCount(): Int {
        return mFavoriteList.size
    }
}