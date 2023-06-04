package com.app.testkotlin.ui.profile

import android.app.AlertDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.app.testkotlin.R
import com.app.testkotlin.databinding.DialogChangePasswordBinding
import com.app.testkotlin.dto.Password
import com.app.testkotlin.ui.login.LoginActivity
import kotlinx.coroutines.delay
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChangePasswordDialog : DialogFragment() {
    private lateinit var progressDialog: ProgressDialog
    private val profileViewModel: ProfileViewModel by viewModel()
    private lateinit var binding: DialogChangePasswordBinding
    override fun onCreateDialog(
        savedInstanceState: Bundle?
    ): Dialog {
        binding = DialogChangePasswordBinding.inflate(requireActivity().layoutInflater)
        val builder = AlertDialog.Builder(activity)

        builder.setView(binding.root)
            .setPositiveButton("Save", null)
            .setNegativeButton("Cancel") { dialog, which ->
                dialog.cancel()
            }
        val dialog = builder.create()
        dialog.setOnShowListener {
            val positiveButton = dialog.getButton(Dialog.BUTTON_POSITIVE)
            positiveButton.setOnClickListener {
                val sharedPref: SharedPreferences = requireContext().getSharedPreferences("mySharedPreferences", Context.MODE_PRIVATE)
                val token = sharedPref.getString("token", null)
                val username = sharedPref.getString("username", null)
                val password = sharedPref.getString("password", null)
                val oldPassword = binding.editTextCurrentPassword.text.toString()
                val newPassword = binding.editTextNewPassword.text.toString()
                val confirmPassword = binding.editTextConfirmPassword.text.toString()
                val passwordObj = username?.let { it1 -> Password(newPassword, oldPassword, it1) }


                if(oldPassword == "" || newPassword == "" || confirmPassword == ""){
                    binding.tvNotification.text = "must fill all fields"
                }
                else{
                    if(oldPassword != password){
                        binding.tvNotification.text = "Your old password is incorrect"
                    }
                    else if(oldPassword == newPassword){
                        binding.tvNotification.text = "New password can't similar to old password"
                    }
                    else if(newPassword != confirmPassword){
                        binding.tvNotification.text = "Check your confirm password"
                    }
                    else{
                        Log.i("Password", newPassword)
                        if (token != null) {
                            if (passwordObj != null) {
                                profileViewModel.changePassword(token, passwordObj)
                            }
                            profileViewModel.changePasswordResponse.observe(viewLifecycleOwner) { boolenVar ->
                                if (boolenVar == true) {
//                                    dialog.dismiss()
                                    Toast.makeText(requireActivity(), "Change password sucess!", Toast.LENGTH_LONG).show()
                                    startNewActivityWithProgress()
                                }
                                else{
                                    dialog.dismiss()
                                    Toast.makeText(requireActivity(), "Something went wrong, pls try again", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                }
            }
            val dialogBackground = dialog.window?.decorView?.background as? GradientDrawable
            dialogBackground?.cornerRadius = resources.getDimension(R.dimen.dialog_corner_radius)
        }
        return dialog
    }

    private fun startNewActivityWithProgress() {
        progressDialog = ProgressDialog(requireContext())
        progressDialog.setMessage("Loading...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        Handler().postDelayed({
            val intentLogout = Intent(requireActivity(), LoginActivity::class.java)
            intentLogout.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
            startActivity(intentLogout)
            progressDialog.dismiss()
        }, 2000)
    }
}

