package com.and2t2.secondhand.ui.uilogin

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.and2t2.secondhand.R
import com.and2t2.secondhand.common.*
import com.and2t2.secondhand.data.remote.ApiClient
import com.and2t2.secondhand.data.remote.AuthService
import com.and2t2.secondhand.data.remote.dto.auth.AuthLoginBody
import com.and2t2.secondhand.databinding.FragmentLoginBinding
import com.and2t2.secondhand.domain.repository.DatastoreManager
import com.and2t2.secondhand.domain.repository.DatastoreViewModel
import com.and2t2.secondhand.domain.repository.AuthRepo


class Login : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val authService: AuthService by lazy { ApiClient.INSTANCE_AUTH }
    private val authRepo: AuthRepo by lazy { AuthRepo(authService) }
    private val loginViewModel: LoginViewModel by viewModelsFactory { LoginViewModel(authRepo) }

    private val pref: DatastoreManager by lazy { DatastoreManager(requireContext()) }
    private val datastoreViewModel: DatastoreViewModel by viewModelsFactory { DatastoreViewModel(pref) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        doLogin()
        moveToRegisterViaClickableText()
        getBundleSnackbar()
    }

    private fun doLogin() {
        binding.btnMasuk.setOnClickListener {
            // Get value dari TextInputLayout
            val etEmail = binding.editEmail.editText?.text.toString()
            val etPassword = binding.editPassword.editText?.text.toString()
            // Login Validation
            if (loginValidation(etEmail, etPassword)) {
                val dataUser = AuthLoginBody(etEmail, etPassword)
                // Jalankan function di ViewModel
                loginViewModel.doLogin(dataUser).observe(viewLifecycleOwner) {
                    when (it.status) {
                        Status.SUCCESS -> {
                            hideLoading()
                            // Simpan login state & access token
                            datastoreViewModel.apply {
                                saveLoginState(true)
                                saveAccessToken(it.data?.accessToken!!)
                                saveIdUser(it.data.id)
                            }
                            // Pindah ke Home (tambahkan findNavController dari Login ke Home dibawah ini)
                            findNavController().navigate(R.id.action_login_to_mainFragment)

                        }
                        Status.ERROR -> {
                            hideLoading()
                            showSnackbar(requireContext(), requireView(), "Email atau Password salah!", R.color.danger)
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

    private fun loginValidation(email: String, password: String): Boolean {
        var result = true
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
            binding.editPassword.error = "Password tidak boleh kosong!"
            result = false
        } else {
            binding.editPassword.isErrorEnabled = false
        }

        return result
    }

    private fun isEmailValid(email: String?): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun moveToRegisterViaClickableText() {
        binding.clickableTextDaftar.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_register)
        }
    }

    private fun getBundleSnackbar() {
        val getValue = arguments?.getInt("idSnackbar")
        if (getValue == 1) {
            showSnackbar(requireContext(), requireView(), "Berhasil Daftar", R.color.success)
        }
    }
}