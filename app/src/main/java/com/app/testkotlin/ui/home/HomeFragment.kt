package com.app.testkotlin.ui.home

import android.app.DatePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.testkotlin.databinding.FragmentHomeBinding
import com.app.testkotlin.dto.FoodTrackingHistory
import com.app.testkotlin.dto.ReportResponse
import com.app.testkotlin.ui.category.CategoryViewModel
import com.app.testkotlin.ui.tracking.TrackingViewModel
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val categoryViewModel: CategoryViewModel by viewModel()
    private val trackingViewModel: TrackingViewModel by viewModel()
    private val homeViewModel: HomeViewModel by viewModel()
    var token: String = ""
    var userId: Int = 0
    private var calendar: Calendar? = null
    private var dayOfMonth: Int = 0
    private var month: Int = 0
    private var year: Int = 0
    val items = arrayOf("DAY", "WEEK", "MONTH", "YEAR")
    private var trackingHistory: List<FoodTrackingHistory>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rcvHomeCategory.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        setInitial()
        setControl()

    }

    override fun onResume() {
        super.onResume()
        requireActivity().onBackPressedDispatcher.addCallback(this) {
        }
    }

    private fun setInitial() {
        val sharedPref: SharedPreferences = requireContext().getSharedPreferences("mySharedPreferences", Context.MODE_PRIVATE)
        token = sharedPref.getString("token", null)!!
        userId = sharedPref.getInt("userId", 0)
        calendar = Calendar.getInstance()
        prepareOptionsForSpinner()
        year = calendar!![Calendar.YEAR]
        month = calendar!![Calendar.MONTH]+1
        dayOfMonth = calendar!![Calendar.DAY_OF_MONTH]
    }

    private fun prepareOptionsForSpinner() {
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.reportTypeSpn.adapter = adapter
    }

    private fun setControl() {
        binding.txtDateTime.text = String.format("%d/%d/%d", dayOfMonth, month, year)
        recyclerViewSetup(token)
        showTrackingList(token, userId)
        setDatePicker()
        binding.reportTypeSpn.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedReportType = items[position]
                showChart(selectedReportType)
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
            }
        }

        binding.btnViewHistory.setOnClickListener {
            if (trackingHistory == null || trackingHistory!!.isEmpty()) {
                Toast.makeText(requireContext(), "No history data", Toast.LENGTH_SHORT).show()
            }
            val historyBottomSheet = HistoryBottomSheet(trackingHistory)
            historyBottomSheet.show(childFragmentManager, historyBottomSheet.tag)

        }
    }

    private fun setDatePicker() {
        binding.txtDateTime.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { datePicker, y, m, d ->
                    binding.txtDateTime.text = String.format("%d/%d/%d", d, m, y)
                    dayOfMonth = d
                    month = m
                    year = y
                    showChart(binding.reportTypeSpn.selectedItem.toString())
                }, year, month, dayOfMonth
            )
            datePickerDialog.show()
        }
    }


    private fun showChart(reportType: String) {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val ldt = LocalDateTime.of(year, month, dayOfMonth, 0, 0, 0)
        val dateTime = ldt.format(formatter)
        trackingViewModel.report.observe(viewLifecycleOwner) { reportResponse ->
            if (reportResponse != null) {
                trackingHistory = reportResponse.consumedHistory
            }
            createChart(reportResponse)
            createSummary(reportResponse)
        }
        trackingViewModel.fetchReport(userId, dateTime, reportType, token)
    }

    private fun createSummary(reportResponse: ReportResponse?) {
        val maxCal: String
        val minCal: String
        val averageCal: String
        val totalCal: String
        if (reportResponse != null) {
            maxCal = if (reportResponse.max != null) java.lang.String.format(
                "%.1f",
                reportResponse.max
            ) else "No data"
            minCal = if (reportResponse.min != null) java.lang.String.format(
                "%.1f",
                reportResponse.min
            ) else "No data"
            averageCal = if (reportResponse.average != null) java.lang.String.format(
                "%.1f",
                reportResponse.average
            ) else "No data"
            totalCal = if (reportResponse.total != null) java.lang.String.format(
                "%.1f",
                reportResponse.total
            ) else "No data"
            binding.txtMaxVal.text = maxCal
            binding.txtMinVal.text = minCal
            binding.txtAverageVal.text = averageCal
            binding.txtTotalVal.text = totalCal
        } else {
            binding.txtMaxVal.text = "No data"
            binding.txtMinVal.text = "No data"
            binding.txtAverageVal.text = "No data"
            binding.txtTotalVal.text = "No data"
        }
    }

    private fun createChart(reportResponse: ReportResponse?) {
        val labels: MutableList<String> = ArrayList()
        val entries: MutableList<BarEntry> = ArrayList()

        if (reportResponse != null) {
            val columnData: Map<String, Float>? = reportResponse.columnData
            var i = 0
            if (columnData != null) {
                for (label in columnData.keys) {
                    labels.add(label)
                    val barEntry = BarEntry(i++.toFloat(), columnData[label]!!)
                    entries.add(barEntry)
                }
            }
            val barDataSet = BarDataSet(entries, "Consumed Calories")
            barDataSet.setGradientColor(Color.rgb(240, 101, 184), Color.rgb(252, 214, 236))
            barDataSet.valueTextColor = Color.BLACK
            barDataSet.valueTextSize = 14f
            val barData = BarData(barDataSet)
            barData.barWidth = 0.75f
            binding.barChart.animateY(500)
            binding.barChart.setFitBars(true)
            binding.barChart.description.isEnabled = false
            binding.barChart.data = barData
            val xAxis: XAxis = binding.barChart.xAxis
            xAxis.setDrawAxisLine(false)
            xAxis.setDrawGridLines(false)
            xAxis.position = XAxis.XAxisPosition.BOTTOM_INSIDE
            xAxis.textSize = 9f
            xAxis.granularity = 1f
            xAxis.isGranularityEnabled = true
            xAxis.labelRotationAngle = -20f
            xAxis.valueFormatter =
                IndexAxisValueFormatter(labels.toTypedArray())
            binding.barChart.axisLeft.setDrawAxisLine(false)
            binding.barChart.axisRight.setDrawAxisLine(false)
            binding.barChart.invalidate()
        } else {
            binding.barChart.clear()
            binding.barChart.setNoDataText("No data")
        }
    }

    private fun recyclerViewSetup(token: String){
        if (token != null) {
            categoryViewModel.fetchCategory(token)
            categoryViewModel.category.observe(viewLifecycleOwner) { categoryList ->
                if (categoryList != null) {
                    binding.rcvHomeCategory.adapter = HomeCategoryAdapter(categoryList)
                }
            }
        } else {
            Toast.makeText(activity, "ERROR", Toast.LENGTH_LONG).show()
        }
    }

    private fun showTrackingList(token: String, userId: Int) {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val ldt = LocalDateTime.of(year, month, dayOfMonth, 0, 0, 0)
        val dateTime = ldt.format(formatter)
        homeViewModel.fetchReport(userId, dateTime, "day", token)
        homeViewModel.report.observe(viewLifecycleOwner) { reportResponse ->
            if (reportResponse == null) {
                binding.tvHomeCaloCount.text = "0/2100"
                binding.circularProgressIndicator.progress = 0
                binding.circularProgressIndicator.invalidate()
            }
            else{
                trackingHistory = reportResponse.consumedHistory
                var total: Double? = reportResponse!!.total
                val totalInt: Int = total!!.toInt()
                binding.tvHomeCaloCount.text = totalInt.toString() + "/2100"
                var percent: Int = ((total!! / 2100) * 100).toInt()
                binding.circularProgressIndicator.isIndeterminate = false
                binding.circularProgressIndicator.progress = percent
                binding.circularProgressIndicator.invalidate()
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
