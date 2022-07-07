package com.and2t2.secondhand.ui.uiakun

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.and2t2.secondhand.R
import com.and2t2.secondhand.common.Status
import com.and2t2.secondhand.common.hideLoading
import com.and2t2.secondhand.common.showLoading
import com.and2t2.secondhand.common.showSnackbar
import com.and2t2.secondhand.data.local.DatabaseSecondHand
import com.and2t2.secondhand.data.remote.ApiClient
import com.and2t2.secondhand.databinding.FragmentPengaturanAkunBinding
import com.and2t2.secondhand.domain.model.AuthUserMapper
import com.and2t2.secondhand.domain.repository.AuthRepo
import com.and2t2.secondhand.domain.repository.DatastoreManager
import com.and2t2.secondhand.domain.repository.DatastoreViewModel
import com.and2t2.secondhand.ui.uiprofile.ProfileViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class PengaturanAkunFragment : Fragment() {
    private var _binding : FragmentPengaturanAkunBinding? = null
    private val binding get() = _binding!!

    private val authRepo: AuthRepo by lazy { AuthRepo(ApiClient.INSTANCE_AUTH, AuthUserMapper(), DatabaseSecondHand.getInstance(requireContext())!!) }
    private val profileViewModel: ProfileViewModel by lazy { ProfileViewModel(authRepo) }

    private val pref: DatastoreManager by lazy { DatastoreManager(requireContext()) }
    private val datastoreViewModel: DatastoreViewModel by lazy { DatastoreViewModel(pref) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPengaturanAkunBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        changePassword()
        onButtonIsPressed()
    }

    private fun changePassword() {
        binding.btnSimpan.setOnClickListener {
            val passLama = binding.editPassword.editText?.text.toString()
            val passBaru = binding.editPassBaru.editText?.text.toString()
            val passBaruConfirm = binding.editPassBaruConfirm.editText?.text.toString()

            if (passwordValidation(passLama, passBaru, passBaruConfirm)) {
                val oldPass = passLama.toRequestBody("current_password".toMediaTypeOrNull())
                val newPass = passBaru.toRequestBody("new_password".toMediaTypeOrNull())
                val newPassConfirm = passBaruConfirm.toRequestBody("confirm_password".toMediaTypeOrNull())

                datastoreViewModel.getAccessToken().observe(viewLifecycleOwner) { token ->
                    profileViewModel.changePasswordUser(token, oldPass, newPass, newPassConfirm).observe(viewLifecycleOwner) {
                        when (it.status) {
                            Status.SUCCESS -> {
                                hideLoading()
                                showSnackbar(requireContext(), requireView(), it.data?.message!!, R.color.success)
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
    }

    private fun passwordValidation(old_pass: String, new_pass: String, confirm_password: String): Boolean {
        var result = true
        if (old_pass.isEmpty()) { // jika kosong
            binding.editPassword.error = "Password tidak boleh kosong!"
            result = false
        } else {
            binding.editPassword.isErrorEnabled = false
        }

        if (new_pass.isEmpty()) { // jika kosong
            binding.editPassBaru.error = "Password tidak boleh kosong!"
            result = false
        } else if (new_pass.length < 6) { // jika kurang dari 6 karakter
            binding.editPassBaru.error = "Password minimum 6 Karakter!"
            result = false
        } else {
            binding.editPassBaru.isErrorEnabled = false
        }

        if (confirm_password.isEmpty()) { // jika kosong
            binding.editPassBaruConfirm.error = "Konfirmasi Password tidak boleh kosong!"
            result = false
        } else if (new_pass != confirm_password) { // jika konfirm pass tdk sama dgn pass
            binding.editPassBaru.error = "Password harus sama!"
            binding.editPassBaruConfirm.error = "Password harus sama!"
            result = false
        } else {
            binding.editPassBaru.isErrorEnabled = false
            binding.editPassBaruConfirm.isErrorEnabled = false
        }
        return result
    }

    private fun onButtonIsPressed() {
        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}