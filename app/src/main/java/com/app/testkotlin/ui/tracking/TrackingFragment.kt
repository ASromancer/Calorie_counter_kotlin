package com.app.testkotlin.ui.tracking

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.testkotlin.R
import com.app.testkotlin.databinding.FragmentTrackingBinding
import com.app.testkotlin.dto.FoodTrackingHistory
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar

class TrackingFragment : Fragment() {
    private var _binding: FragmentTrackingBinding? = null
    private val binding get() = _binding!!
    private val trackingViewModel: TrackingViewModel by viewModel()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTrackingBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rcvTrackingList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        val sharedPref: SharedPreferences = requireContext().getSharedPreferences("mySharedPreferences", Context.MODE_PRIVATE)
        val userId = sharedPref.getInt("userId", 0)
        val token = sharedPref.getString("token", null)

        if (token != null) {
            showTrackingList(token, userId)
        } else {
            Toast.makeText(activity, "ERROR", Toast.LENGTH_LONG).show()
        }

        binding.btnAddTracking.setOnClickListener { v ->
            val controller = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main)
            controller.navigate(R.id.navigation_category)
        }

    }

    private fun showTrackingList(token: String, userId: Int) {
        val calendar = Calendar.getInstance()
        val year = calendar[Calendar.YEAR]
        val month = calendar[Calendar.MONTH] + 1
        val dayOfMonth = calendar[Calendar.DATE]
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val ldt = LocalDateTime.of(year, month, dayOfMonth, 0, 0, 0)
        val dateTime = ldt.format(formatter)
        trackingViewModel.fetchReport(userId, dateTime, "day", token )
        trackingViewModel.report.observe(viewLifecycleOwner) { reportResponse ->
            val foodTrackingHistories: List<FoodTrackingHistory>? = reportResponse?.consumedHistory
            if (foodTrackingHistories != null) {
                val trackingListAdapter = TrackingListAdapter(foodTrackingHistories)
                binding.rcvTrackingList.adapter = trackingListAdapter
                var total: Double? = reportResponse.total
                binding.tvTrackingProgreess.text = total.toString()
                var percent: Int = ((total!! /2100)*100).toInt()
                binding.circularProgressIndicator.isIndeterminate = false
                binding.circularProgressIndicator.progress = percent
                binding.circularProgressIndicator.invalidate()

                //delete
                ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
                    override fun onMove(
                        recyclerView: RecyclerView,
                        viewHolder: RecyclerView.ViewHolder,
                        target: RecyclerView.ViewHolder
                    ): Boolean {
                        return false
                    }


                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                        val position = viewHolder.adapterPosition
                        //courseList.removeAt(viewHolder.adapterPosition)
                        trackingListAdapter.notifyItemRemoved(viewHolder.adapterPosition)
                    }
                }).attachToRecyclerView(binding.rcvTrackingList)
            }
            else{
                binding.circularProgressIndicator.progress = 0
                binding.circularProgressIndicator.invalidate()
                binding.tvTrackingProgreess.text = "0"
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}