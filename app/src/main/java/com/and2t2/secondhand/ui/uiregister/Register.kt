package com.and2t2.secondhand.ui.uiregister

import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.and2t2.secondhand.R
import com.and2t2.secondhand.common.*
import com.and2t2.secondhand.data.local.DatabaseSecondHand
import com.and2t2.secondhand.data.remote.ApiClient
import com.and2t2.secondhand.databinding.FragmentRegisterBinding
import com.and2t2.secondhand.domain.model.AuthUserMapper
import com.and2t2.secondhand.domain.repository.AuthRepo
import com.and2t2.secondhand.domain.repository.DatastoreManager
import com.and2t2.secondhand.domain.repository.DatastoreViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File


class Register : Fragment() {
    private val fileUtil = FileUtil()
    private var uri : Uri? = null

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val authRepo: AuthRepo by lazy { AuthRepo(ApiClient.INSTANCE_AUTH, AuthUserMapper(), DatabaseSecondHand.getInstance(requireContext())!!) }
    private val registerViewModel: RegisterViewModel by viewModelsFactory { RegisterViewModel(authRepo) }

    private val pref: DatastoreManager by lazy { DatastoreManager(requireContext()) }
    private val datastoreViewModel: DatastoreViewModel by lazy { DatastoreViewModel(pref) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        doRegister()
        moveToLoginViaBackPress()
        moveToLoginViaClickableText()
        getImgFromAssets()
    }

    private fun getImgFromAssets() {
        // Get Img dari Assets
        val assets = requireContext().assets.open("default_img_user.jpg")
        // Convert ke Bitmap
        val bitmap = BitmapFactory.decodeStream(assets)
        // Mendapatkan path
        val imgPath = bitmap.let { bitmap1 -> bitmapToUri(requireContext(), bitmap1).let { fileUtil.getPath(requireContext(), it) } }
        // Simpan ke variable global
        uri = Uri.parse(imgPath)
    }

    private fun prepareFilePart(fileUri: Uri): MultipartBody.Part {
        val file = File(fileUri.path)
        Log.i("PATH IMAGE", file.absolutePath)
        // Create RequestBody instance from file
        val requestFile: RequestBody = file.asRequestBody("image".toMediaTypeOrNull())

        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData("image", file.name, requestFile)
    }

    private fun doRegister() {
        binding.btnDaftar.setOnClickListener {
            // Get Image from MultipartBody.Part
            val image = uri?.let { prepareFilePart(it) }

            // Get value dari TextInputLayout
            val etNamaLengkap = binding.editNamaLengkap.editText?.text.toString()
            val etEmail = binding.editEmail.editText?.text.toString()
            val etPassword = binding.editBuatpassword.editText?.text.toString()
            val myPhone = "Ex. 082132xxx"
            val myAddress = "Ex. Jl. Raya Kebayoran Lama No. 39"
            val myCity = "Ex. Jakarta"

            if (registerValidation(etNamaLengkap, etEmail, etPassword)) {
                val fullName = etNamaLengkap.toRequestBody("full_name".toMediaTypeOrNull())
                val email = etEmail.toRequestBody("email".toMediaTypeOrNull())
                val password = etPassword.toRequestBody("password".toMediaTypeOrNull())
                val phoneNumber = myPhone.toRequestBody("phone_humber".toMediaTypeOrNull())
                val address = myAddress.toRequestBody("address".toMediaTypeOrNull())
                val city = myCity.toRequestBody("city".toMediaTypeOrNull())

                registerViewModel.doRegister(fullName,email,password,phoneNumber,address,city,image).observe(viewLifecycleOwner) {
                    when (it.status) {
                        Status.SUCCESS -> {
                            hideLoading()
                            datastoreViewModel.saveMsgSnackbar("Berhasil Daftar")
                            findNavController().navigate(R.id.action_register_to_login)
                        }
                        Status.ERROR -> {
                            hideLoading()
                            showSnackbar(requireContext(), requireView(), it.message!!, R.color.danger)
                        }
                        Status.LOADING -> {
                            // Munculkan LoadingDialog
                            showLoading(requireActivity())
                        }
                    }
                }
            }
        }
    }

    private fun registerValidation(namaLengkap: String, email: String, password: String): Boolean {
        var result = true
        if (namaLengkap.isEmpty()) { // jika kosong
            binding.editNamaLengkap.error = "Nama tidak boleh kosong!"
            result = false
        } else {
            binding.editNamaLengkap.isErrorEnabled = false
        }

        if (email.isEmpty()) { // jika kosong
            binding.editEmail.error = "Email tidak boleh kosong!"
            result = false
        } else if (!isEmailValid(email)) {
            binding.editEmail.error = "Email tidak valid!"
            result = false
        } else {
            binding.editEmail.isErrorEnabled = false
        }

        if (password.isEmpty()) { // jika kosong
            binding.editBuatpassword.error = "Password tidak boleh kosong!"
            result = false
        } else if (password.length <= 6) { // jika kurang dari 6 karakter
            binding.editBuatpassword.error = "Password minimal 6 karakter!"
            result = false
        } else {
            binding.editBuatpassword.isErrorEnabled = false
        }

        return result
    }

    private fun isEmailValid(email: String?): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun moveToLoginViaBackPress() {
        binding.backBtn.setOnClickListener {
            it.findNavController().popBackStack()
        }
    }

    private fun moveToLoginViaClickableText() {
        binding.clickableTextMasuk.setOnClickListener {
            findNavController().navigate(R.id.action_register_to_login)
        }
    }
}