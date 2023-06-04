package com.app.testkotlin.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.testkotlin.databinding.HistoryBottomSheetBinding
import com.app.testkotlin.dto.FoodTrackingHistory
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class HistoryBottomSheet(private val trackingHistory: List<FoodTrackingHistory>?) : BottomSheetDialogFragment() {

    private var _binding: HistoryBottomSheetBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = HistoryBottomSheetBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rcvHistoryItem.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        val adapter = trackingHistory?.let { FoodTrackingHistoryAdapter(it) }
        binding.rcvHistoryItem!!.adapter = adapter
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}