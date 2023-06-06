package com.app.testkotlin.ui.food

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.testkotlin.databinding.FragmentFoodBinding
import com.app.testkotlin.model.Food
import org.koin.androidx.viewmodel.ext.android.viewModel

class FoodFragment : Fragment() {

    private var _binding: FragmentFoodBinding? = null
    private val binding get() = _binding!!
    private val foodViewModel: FoodViewModel by viewModel()
    private var cateId: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFoodBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rcvFood.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        if (arguments != null) {
            cateId = requireArguments().getInt("cateId")
        }

        val sharedPref: SharedPreferences = requireContext().getSharedPreferences("mySharedPreferences", Context.MODE_PRIVATE)
        val token = sharedPref.getString("token", null)
        Log.i("cateId", cateId.toString())
        if (token != null) {
            foodViewModel.fetchFood(cateId, token)
            foodViewModel.food.observe(viewLifecycleOwner) { foodList ->
                if (foodList != null) {
                    Log.i("Size", foodList.size.toString())
                    var foodAdapter= FoodAdapter(foodList)
                    binding.rcvFood.adapter = foodAdapter

                    binding.svCategory.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(query: String): Boolean {
                            return true
                        }

                        override fun onQueryTextChange(newText: String): Boolean {
                            val filteredList: MutableList<Food> = ArrayList()
                            for (item in foodList) {
                                if (item.name!!.toLowerCase().contains(newText.toLowerCase())) {
                                    filteredList.add(item)
                                }
                            }
                            if (filteredList.isEmpty()) {
                                foodAdapter.mFoodList = filteredList
                            } else {
                                foodAdapter.mFoodList = filteredList
                            }
                            return false
                        }
                    })

                }
            }
        } else {
            Toast.makeText(activity, "ERROR", Toast.LENGTH_LONG).show()
        }
    }
}