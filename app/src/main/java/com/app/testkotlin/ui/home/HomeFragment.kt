package com.app.testkotlin.ui.home

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.testkotlin.databinding.FragmentHomeBinding
import com.app.testkotlin.ui.category.CategoryViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val categoryViewModel: CategoryViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rcvHomeCategory.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        val sharedPref: SharedPreferences = requireContext().getSharedPreferences("mySharedPreferences", Context.MODE_PRIVATE)
        val token = sharedPref.getString("token", null)

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}