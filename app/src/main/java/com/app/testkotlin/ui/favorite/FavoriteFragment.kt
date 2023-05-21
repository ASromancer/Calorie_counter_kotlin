package com.app.testkotlin.ui.favorite

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.app.testkotlin.R
import com.app.testkotlin.databinding.FragmentFavoriteBinding
import com.app.testkotlin.databinding.FragmentHomeBinding
import com.app.testkotlin.ui.category.CategoryViewModel
import com.app.testkotlin.ui.home.HomeCategoryAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    private val favoriteViewModel: FavoriteViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rcvFavorites.layoutManager = GridLayoutManager(requireContext(), 3, GridLayoutManager.VERTICAL, false)

        val sharedPref: SharedPreferences = requireContext().getSharedPreferences("mySharedPreferences", Context.MODE_PRIVATE)
        val token = sharedPref.getString("token", null)
        val userId = sharedPref.getInt("userId", 0)

        if (token != null) {
            favoriteViewModel.fetchFavoriteFoods(userId, token)
            favoriteViewModel.favoriteFoods.observe(viewLifecycleOwner) { favoriteList ->
                if (favoriteList != null) {
                    binding.rcvFavorites.adapter = FavoriteAdapter(favoriteList)
                }
            }


        } else {
            Toast.makeText(activity, "ERROR", Toast.LENGTH_LONG).show()
        }
    }


}