package com.app.testkotlin.ui.detail

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.app.testkotlin.R
import com.app.testkotlin.databinding.FragmentFoodDetailBinding
import com.app.testkotlin.dto.Favorite
import com.app.testkotlin.ui.favorite.FavoriteViewModel
import com.app.testkotlin.ui.tracking.TrackingViewModel
import com.bumptech.glide.Glide
import org.koin.androidx.viewmodel.ext.android.viewModel

class FoodDetailFragment : Fragment() {

    private var _binding: FragmentFoodDetailBinding? = null
    private val binding get() = _binding!!
    private val foodDetailViewModel: FoodDetailViewModel by viewModel()
    private val trackingViewModel: TrackingViewModel by viewModel()
    private val favoriteViewModel: FavoriteViewModel by viewModel()
    private var foodId: Int = 0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFoodDetailBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (arguments != null) {
            foodId = requireArguments().getInt("foodId")
        }

        Log.i("ID", foodId.toString())

        val sharedPref: SharedPreferences = requireContext().getSharedPreferences("mySharedPreferences", Context.MODE_PRIVATE)
        val token = sharedPref.getString("token", null)
        val userId = sharedPref.getInt("userId", 0)

        if (token != null) {
            foodDetailViewModel.fetchAccountByUsername(foodId , token)
            foodDetailViewModel.foodDetail.observe(viewLifecycleOwner) { food ->
                binding.collapsingToolbar.title = food.name
                binding.tvFoodProtein.text = food.protein.toString()
                binding.tvFoodFat.text = food.fat.toString()
                binding.tvFoodCarb.text = food.carb.toString()
                binding.tvEnergyPerServing.text = food.energyPerServing.toString()
                binding.tvFoodDescription.text = food.description
                Glide.with(this)
                    .load(food.image)
                    .into(binding.ivFoodImage)
            }
        } else {
            Toast.makeText(activity, "ERROR", Toast.LENGTH_LONG).show()
        }

        binding.btnAddTracking.setOnClickListener {
            showInputDialog(
                context = requireContext(),
                title = "Enter consummed(gram)",
                positiveButtonTitle = "OK",
                negativeButtonTitle = "Hủy"
            ) { inputText ->
                if(inputText!=null) {
                    if (token != null) {
                        trackingViewModel.addTracking(userId, foodId, inputText.toDouble(), token)
                        trackingViewModel.msg.observe(viewLifecycleOwner) { msg ->
                            if (msg == true){
                                val controller = Navigation.findNavController(
                                    requireActivity(),
                                    R.id.nav_host_fragment_activity_main
                                )
                                controller.navigate(R.id.navigation_tracking)
                            }
                        }
                    }
                }
                else {
                    Toast.makeText(requireActivity(), "Please enter your consummed", LENGTH_SHORT).show()
                }
            }
        }

        prepareFavoriteBtn(userId, token!!)

        binding.btnFavoriteAdd.setOnClickListener {
            binding.btnFavoriteAdd.isSelected = !binding.btnFavoriteAdd.isSelected
            favoriteViewModel.addFoodToFavorite(userId, foodId, token)
            favoriteViewModel.addMsg.observe(requireActivity()){
                if (it == true) {
                    Toast.makeText(requireContext(), "Favorited", LENGTH_SHORT).show()
                }
                else{
                    favoriteViewModel.deleteFoodFromFavorite(userId, foodId, token)
                    favoriteViewModel.msg.observe(requireActivity()){ it1 ->
                        if (it1 == true) {
                            Toast.makeText(requireContext(), "Unfavorited", LENGTH_SHORT).show()
                        }
                    }
                }
                
            }
        }

    }

    private fun prepareFavoriteBtn(userId: Int, token: String) {
        favoriteViewModel.fetchFavoriteFoods(userId, token)
        favoriteViewModel.favoriteFoods.observe(requireActivity()){
            var tmp = 0
            for (favorite: Favorite in it) {
                if (favorite.getFood().id === foodId) {
                    tmp++
                }
            }
            binding.btnFavoriteAdd.isSelected = tmp > 0
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun showInputDialog(context: Context, title: String, positiveButtonTitle: String, negativeButtonTitle: String, callback: (String) -> Unit) {
        val inputEditText = EditText(context)

        val dialogBuilder = AlertDialog.Builder(context)
            .setTitle(title)
            .setView(inputEditText)
            .setPositiveButton(positiveButtonTitle) { dialog, _ ->
                val inputText = inputEditText.text.toString()
                callback.invoke(inputText)
                dialog.dismiss()
            }
            .setNegativeButton(negativeButtonTitle) { dialog, _ ->
                dialog.cancel()
            }

        val dialog = dialogBuilder.create()
        dialog.show()
    }

}