package com.app.testkotlin.ui.profile

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.app.testkotlin.R
import com.app.testkotlin.databinding.DialogEditProfileBinding
import com.app.testkotlin.dto.UserProfile
import com.google.gson.Gson
import okhttp3.MediaType
import okhttp3.RequestBody
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Calendar

class EditProfileDialogFragment : DialogFragment() {

    private lateinit var binding: DialogEditProfileBinding
    private val profileViewModel: ProfileViewModel by viewModel()

    override fun onCreateDialog(
        savedInstanceState: Bundle?
    ): Dialog {
        binding = DialogEditProfileBinding.inflate(requireActivity().layoutInflater)
        val builder = AlertDialog.Builder(activity)

        builder.setView(binding.root)
            .setPositiveButton("Save", null)
            .setNegativeButton("Cancel") { dialog, which ->
                dialog.cancel()
            }
        val dialog = builder.create()
        val sharedPref: SharedPreferences = requireContext().getSharedPreferences("mySharedPreferences", Context.MODE_PRIVATE)
        val token = sharedPref.getString("token", null)
        val userId = sharedPref.getInt("userId", 0)
        if (token != null) {
            callGetUser(token, userId)
        }
        binding.edtDob.setOnClickListener { showDatePicker() }
        dialog.setOnShowListener {
            val positiveButton = dialog.getButton(Dialog.BUTTON_POSITIVE)
            positiveButton.setOnClickListener {
                if (token != null) {
                    callUpdateProfile(userId, token)
                }
            }
            val dialogBackground = dialog.window?.decorView?.background as? GradientDrawable
            dialogBackground?.cornerRadius = resources.getDimension(R.dimen.dialog_corner_radius)
        }
        return dialog
    }

    private fun callUpdateProfile(userId: Int, token: String) {
        val genderId: Int = binding.radioGroupGender.checkedRadioButtonId
        val firstname: String = binding.edtFirstname.text.toString()
        val lastname: String = binding.edtLastname.text.toString()
        val dob: String = binding.edtDob.text.toString()
        val strWeight: String = binding.edtWeight.text.toString()
        val strHeight: String = binding.edtHeight.text.toString()
        val weight = strWeight.toDouble()
        val height = strHeight.toDouble()
        val email: String = binding.edtEmail.text.toString()
        val gender: Boolean = genderId == binding.radioMale.getId()
        val userProfile = UserProfile(
            userId,
            firstname,
            lastname,
            dob,
            gender,
            weight,
            height,
            email
        )
        val gson = Gson()
        val userJson = gson.toJson(userProfile)
        val userBody = RequestBody.create(MediaType.parse("application/json"), userJson)
        profileViewModel.putUserData(token, userBody)
        profileViewModel.msg.observe(requireActivity()){
            if(it == true){
                dismiss()
                Toast.makeText(activity, "Success!", Toast.LENGTH_LONG).show()
            }
            else{
                Toast.makeText(activity, "ERROR!", Toast.LENGTH_LONG).show()

            }
        }
    }

    private fun callGetUser(token: String, id: Int) {
        profileViewModel.getUserById(token, id)
        profileViewModel.user.observe(requireActivity()){
            binding.edtFirstname.setText(it.firstName)
            binding.edtLastname.setText(it.lastName)
            binding.edtDob.setText(it.dob)
            binding.edtWeight.setText(it.weight.toString())
            binding.edtHeight.setText(it.height.toString())
            binding.edtEmail.setText(it.email)
            val radioId: Int = if (it.gender) {
                binding.radioMale.id
            } else {
                binding.radioFemale.id
            }
            binding.radioGroupGender.check(radioId)
        }
    }

    private fun showDatePicker() {
        val c = Calendar.getInstance()
        val year = c[Calendar.YEAR]
        val month = c[Calendar.MONTH]
        val day = c[Calendar.DAY_OF_MONTH]

        val datePickerDialog = DatePickerDialog(
            requireActivity(),
            { view, year, monthOfYear, dayOfMonth ->
                binding.edtDob.setText(year.toString() + "-" + (monthOfYear + 1) + "-" + dayOfMonth)
            }, year, month, day
        )
        datePickerDialog.show()
    }
}