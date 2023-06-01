package com.app.testkotlin.ui.food

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.app.testkotlin.R
import com.app.testkotlin.databinding.ItemFoodBinding
import com.app.testkotlin.model.Food
import com.bumptech.glide.Glide

class FoodAdapter(
    var mFoodList: List<Food>,
) : RecyclerView.Adapter<FoodAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemFoodBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemFoodBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            with(mFoodList[position]){
                binding.tvFoodName.text = this.name
                binding.tvFoodCalories.text = this.energyPerServing.toString() + " kcals"
                Glide.with(binding.ivFoodImage.context)
                    .load(this.image)
                    .into(binding.ivFoodImage)
                binding.layoutItemFood.setOnClickListener { v ->
                    val controller = Navigation.findNavController(
                        (v.getContext() as Activity)!!,
                        R.id.nav_host_fragment_activity_main
                    )
                    val bundle = Bundle()
                    bundle.putInt("foodId", this.id)
                    controller.navigate(R.id.food_fragment_to_food_detail, bundle)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return mFoodList.size
    }
}