package com.app.testkotlin.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.testkotlin.databinding.ItemHistoryBottomSheetBinding
import com.app.testkotlin.dto.FoodTrackingHistory

class FoodTrackingHistoryAdapter(
    private var mFoodList: List<FoodTrackingHistory>
) : RecyclerView.Adapter<FoodTrackingHistoryAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemHistoryBottomSheetBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemHistoryBottomSheetBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            with(mFoodList[position]){
                var imgURL: String? = this.food?.image
                if (imgURL.isNullOrEmpty()) "https://upload.wikimedia.org/wikipedia/commons/6/6d/Good_Food_Display_-_NCI_Visuals_Online.jpg" else imgURL
                com.bumptech.glide.Glide.with(binding.foodImageView.context)
                    .load(this.food?.image)
                    .into(binding.foodImageView)
                binding.txtConsumedDateTime.text = this.consumedDatetime
                binding.txtFoodName.text = this.food!!.name
                binding.txtConsumedGram.text = this.consumedGram.toString() + " gram"
                binding.txtConsumedCalories.text = this.consumedCalories.toString() + " kcal"
            }
        }
    }

    override fun getItemCount(): Int {
        return mFoodList.size
    }
}