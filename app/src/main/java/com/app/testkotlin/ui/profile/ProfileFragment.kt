package com.app.testkotlin.ui.profile

import android.app.Activity.RESULT_OK
import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.app.testkotlin.databinding.FragmentProfileBinding
import com.app.testkotlin.dto.UserAvatar
import com.app.testkotlin.ui.login.LoginActivity
import com.bumptech.glide.Glide
import com.google.gson.Gson
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val profileViewModel: ProfileViewModel by viewModel()
    private val PICK_IMAGE_REQUEST = 1
    private var userId = 0
    private var token: String = ""

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                openGallery()
            } else {
            }
        }

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
        token = sharedPref.getString("token", null)!!
        val username = sharedPref.getString("username", null)
        userId = sharedPref.getInt("userId", 0)
        logOut()
        changePassword()
        updateProfile()
        changeAvatar()
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
                    binding.profileHeight.text = account.user!!.height.toString() + "cm"
                    binding.profileWeight.text = account.user!!.weight.toString() + "kg"
                }
            }
        } else {
            Toast.makeText(activity, "ERROR", Toast.LENGTH_LONG).show()
        }
    }

    private fun changeAvatar() {
        binding.profileImage.setOnClickListener {
            checkPermissionAndOpenGallery()
        }
    }

    private fun checkPermissionAndOpenGallery() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            openGallery()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            val imageUri = data.data
            val imageFile = File(imageUri?.let { getPathFromUri(it) })
            val requestBody = RequestBody.create(MediaType.parse("image/*"), imageFile)
            val imagePart = MultipartBody.Part.createFormData("image", imageFile.name, requestBody)

            val firstName: String = binding.profileFirstname.text.toString()
            val lastName: String = binding.profileLastname.text.toString()
            val email: String = binding.profileEmail.text.toString()
            val userAvatar = UserAvatar(lastName, firstName, email, userId)
            val userJson = Gson().toJson(userAvatar)
            Log.i("UserAvatar", userJson)
            Log.i("UserAvatar", imagePart.toString())
            val userBody = RequestBody.create(MediaType.parse("application/json"), userJson)

            profileViewModel.uploadAvatar(token, userBody, imagePart)
            profileViewModel.msgImage.observe(requireActivity()){
                if(it == true){
                    Toast.makeText(requireActivity(), "Avatar already uploaded", Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(requireActivity(), "ERROR", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun getPathFromUri(uri: Uri): String {
        val cursor: Cursor? = requireContext().contentResolver.query(uri, null, null, null, null)
        cursor?.moveToFirst()
        val columnIndex: Int = cursor?.getColumnIndex(MediaStore.Images.ImageColumns.DATA) ?: -1
        val filePath: String = cursor?.getString(columnIndex) ?: ""
        cursor?.close()
        return filePath
    }


    private fun updateProfile() {
        binding.btnEditProfile.setOnClickListener {
            val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
            val editDialogFragment = EditProfileDialogFragment()
            editDialogFragment.show(fragmentManager, "EditProfileDialogFragment")
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