package com.app.testkotlin.ui.tracking

import android.R.attr.data
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.testkotlin.databinding.ItemTrackingBinding
import com.app.testkotlin.dto.FoodTrackingHistory
import com.bumptech.glide.Glide


class TrackingListAdapter(
    var mTrackingList: MutableList<FoodTrackingHistory>,
) : RecyclerView.Adapter<TrackingListAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemTrackingBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTrackingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    fun removeItem(position: Int) {
        mTrackingList.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun getItemCount(): Int {
        return mTrackingList.size    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            with(mTrackingList[position]){
                binding.tvTrackingName.text = this.food?.name
                binding.tvTrackingConsumed.text = this.consumedGram.toString() + " gram"
                Glide.with(binding.ivTrackingImage.context)
                    .load(this.food?.image)
                    .into(binding.ivTrackingImage)
                binding.tvTrackingKcal.text = this.consumedCalories.toString() + " kcal"
                }
            }
        }
    }



