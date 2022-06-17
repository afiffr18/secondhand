package com.and2t2.secondhand.ui.uiregister

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.and2t2.secondhand.R
import com.and2t2.secondhand.common.*
import com.and2t2.secondhand.data.remote.dto.ApiClient
import com.and2t2.secondhand.data.remote.dto.AuthService
import com.and2t2.secondhand.databinding.FragmentRegisterBinding
import com.and2t2.secondhand.domain.repository.AuthRepo
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody


class Register : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val authService: AuthService by lazy { ApiClient.INSTANCE_AUTH }
    private val authRepo: AuthRepo by lazy { AuthRepo(authService) }
    private val registerViewModel: RegisterViewModel by viewModelsFactory { RegisterViewModel(authRepo) }

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
    }

    private fun doRegister() {
        binding.btnDaftar.setOnClickListener {
            // Get value dari TextInputLayout
            val etNamaLengkap = binding.editNamaLengkap.editText?.text.toString()
            val etEmail = binding.editEmail.editText?.text.toString()
            val etPassword = binding.editBuatpassword.editText?.text.toString()
            val myPhone = "Ex. 082132xxx"
            val myAddress = "Ex. Jl. Raya Kebayoran Lama No. 39"

            if (registerValidation(etNamaLengkap, etEmail, etPassword)) {
                val namaLengkap = etNamaLengkap.toRequestBody("full_name".toMediaTypeOrNull())
                val email = etEmail.toRequestBody("email".toMediaTypeOrNull())
                val password = etPassword.toRequestBody("password".toMediaTypeOrNull())
                val phoneNumber = myPhone.toRequestBody("phone_humber".toMediaTypeOrNull())
                val address = myAddress.toRequestBody("address".toMediaTypeOrNull())

                registerViewModel.doRegister(namaLengkap,email,password,phoneNumber,address).observe(viewLifecycleOwner) {
                    when (it.status) {
                        Status.SUCCESS -> {
                            hideLoading()
                            val bundle = Bundle()
                            bundle.putInt("idSnackbar", 1)
                            findNavController().navigate(R.id.action_register_to_login, bundle)
                        }
                        Status.ERROR -> {
                            hideLoading()
                            showSnackbar(requireContext(), requireView(), "Gagal, Email sudah terpakai", R.color.danger)
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
            findNavController().navigate(R.id.action_register_to_login)
        }
    }

    private fun moveToLoginViaClickableText() {
        binding.clickableTextMasuk.setOnClickListener {
            findNavController().navigate(R.id.action_register_to_login)
        }
    }
}