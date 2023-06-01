package com.app.testkotlin.ui.tracking

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.testkotlin.R
import com.app.testkotlin.databinding.FragmentTrackingBinding
import com.app.testkotlin.dto.FoodTrackingHistory
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar


class TrackingFragment : Fragment() {
    private var _binding: FragmentTrackingBinding? = null
    private val binding get() = _binding!!
    private val trackingViewModel: TrackingViewModel by viewModel()
    var mAdapter: TrackingListAdapter? = null

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
            enableSwipeToDelete(userId, token)
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
        trackingViewModel.report.observe(viewLifecycleOwner) { reportResponse ->
            if (reportResponse == null) {
                binding.tvTrackingProgreess.text = "0"
                binding.circularProgressIndicator.progress = 0
                binding.circularProgressIndicator.invalidate()
                mAdapter = null
                binding.rcvTrackingList.adapter = mAdapter
            }
            else{
                val foodTrackingHistories: MutableList<FoodTrackingHistory>? = reportResponse?.consumedHistory as MutableList<FoodTrackingHistory>?
                mAdapter = foodTrackingHistories?.let { TrackingListAdapter(it) }
                binding.rcvTrackingList.adapter = mAdapter
                var total: Double? = reportResponse!!.total
                binding.tvTrackingProgreess.text = total.toString()
                var percent: Int = ((total!! / 2100) * 100).toInt()
                binding.circularProgressIndicator.isIndeterminate = false
                binding.circularProgressIndicator.progress = percent
                binding.circularProgressIndicator.invalidate()
            }
        }
        trackingViewModel.fetchReport(userId, dateTime, "day", token)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun enableSwipeToDelete(userId: Int, token: String) {
        val swipeToDeleteCallback: SwipeToDeleteCallback = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, i: Int) {
                val position = viewHolder.adapterPosition
                var trackingId: Int = mAdapter!!.mTrackingList.get(position).id
                mAdapter!!.mTrackingList.removeAt(position)
                trackingViewModel.deleteTracking(trackingId, token)
                val calendar = Calendar.getInstance()
                val year = calendar[Calendar.YEAR]
                val month = calendar[Calendar.MONTH] + 1
                val dayOfMonth = calendar[Calendar.DATE]
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                val ldt = LocalDateTime.of(year, month, dayOfMonth, 0, 0, 0)
                val dateTime = ldt.format(formatter)
                val snackbar = Snackbar
                    .make(binding.layoutTracking,
                        "Item was removed.",
                        Snackbar.LENGTH_LONG
                    )
                snackbar.setActionTextColor(Color.YELLOW)
                snackbar.show()
                trackingViewModel.fetchReport(userId, dateTime, "day", token)
            }
        }
        val itemTouchhelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchhelper.attachToRecyclerView(binding.rcvTrackingList)
    }
}