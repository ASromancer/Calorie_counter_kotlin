package com.app.testkotlin.ui.profile

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.app.testkotlin.databinding.FragmentProfileBinding
import com.app.testkotlin.ui.login.LoginActivity
import com.bumptech.glide.Glide
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val profileViewModel: ProfileViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPref: SharedPreferences = requireContext().getSharedPreferences("mySharedPreferences", Context.MODE_PRIVATE)
        val token = sharedPref.getString("token", null)
        val username = sharedPref.getString("username", null)
        logOut()
        changePassword()
        if (token != null) {
            profileViewModel.fetchAccountByUsername(username, token)
            profileViewModel.account.observe(viewLifecycleOwner) { account ->
                if (account != null) {
                    Glide.with(requireActivity())
                        .load(account.user?.image)
                        .into(binding.profileImage)
                    binding.profileDob.text = account.user!!.dob
                    binding.profileEmail.text = account.user!!.email
                    if(account.user!!.gender){
                        binding.profileGender.text = "Male"
                    }
                    else{
                        binding.profileGender.text = "Female"
                    }
                    binding.profileFirstname.text = account.user!!.firstName
                    binding.profileLastname.text = account.user!!.lastName
                    binding.profileHeight.text = account.user!!.height.toString()
                    binding.profileWeight.text = account.user!!.weight.toString()
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

    private fun logOut() {
        binding.btnLogout.setOnClickListener {
            val intentLogout = Intent(requireActivity(), LoginActivity::class.java)
            intentLogout.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
            startActivity(intentLogout)
        }
    }

    private fun changePassword(){
        binding.btnChangePassword.setOnClickListener {
            showChangePasswordDialog()
        }
    }

    private fun showChangePasswordDialog() {
        val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
        val dialogFragment = ChangePasswordDialog()
        dialogFragment.show(fragmentManager, "ChangePasswordDialog")
    }



}