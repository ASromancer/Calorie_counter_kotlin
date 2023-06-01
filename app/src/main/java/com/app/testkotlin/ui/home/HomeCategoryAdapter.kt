package com.app.testkotlin.ui.home

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.app.testkotlin.R
import com.app.testkotlin.databinding.ItemHomeCategoryBinding
import com.app.testkotlin.model.Category
import com.bumptech.glide.Glide


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
                Glide.with(binding.ivHomeCategory.context)
                    .load(this.image)
                    .into(binding.ivHomeCategory)
                binding.ivHomeCategory.setOnClickListener { v ->
                    val controller = findNavController(
                        (v.getContext() as Activity)!!,
                        R.id.nav_host_fragment_activity_main
                    )
                    val bundle = Bundle()
                    bundle.putInt("cateId", this.id)
                    controller.navigate(R.id.fragment_food, bundle)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return mHomeFoodList.size
    }


}