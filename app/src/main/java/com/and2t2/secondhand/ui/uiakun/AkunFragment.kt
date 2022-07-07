package com.and2t2.secondhand.ui.uiakun

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.and2t2.secondhand.AuthActivity
import com.and2t2.secondhand.R
import com.and2t2.secondhand.common.viewModelsFactory
import com.and2t2.secondhand.data.local.DatabaseSecondHand
import com.and2t2.secondhand.data.remote.ApiClient
import com.and2t2.secondhand.databinding.FragmentAkunBinding
import com.and2t2.secondhand.domain.model.AuthUserMapper
import com.and2t2.secondhand.domain.repository.AuthRepo
import com.and2t2.secondhand.domain.repository.DatastoreManager
import com.and2t2.secondhand.domain.repository.DatastoreViewModel
import com.and2t2.secondhand.ui.uiprofile.ProfileViewModel
import com.bumptech.glide.Glide


class AkunFragment : Fragment() {
    private var _binding : FragmentAkunBinding? = null
    private val binding get() = _binding!!

    private val authRepo: AuthRepo by lazy { AuthRepo(ApiClient.INSTANCE_AUTH, AuthUserMapper(), DatabaseSecondHand.getInstance(requireContext())!!) }
    private val profileViewModel: ProfileViewModel by lazy { ProfileViewModel(authRepo) }

    private val pref: DatastoreManager by lazy { DatastoreManager(requireContext()) }
    private val datastoreViewModel: DatastoreViewModel by viewModelsFactory { DatastoreViewModel(pref) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAkunBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
        buttonEditOnPressed()
        buttonPengaturanOnPressed()
        logoutButtonOnPressed()
    }

    private fun observeData() {
        datastoreViewModel.getAccessToken().observe(viewLifecycleOwner) { token ->
            profileViewModel.getUser(token).observe(viewLifecycleOwner) {
                it.data?.let { data ->
                    binding.apply {
                        if (data.imageUrl != null) {
                            editIvPicture.setPadding(0,0,0,0)
                            Glide.with(requireContext())
                                .load(data.imageUrl)
                                .into(editIvPicture)
                        }
                    }
                }
            }
        }
    }

    private fun buttonEditOnPressed() {
        binding.ubahAkun.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_akun_to_profile)
        }
    }

    private fun buttonPengaturanOnPressed() {
        binding.pengaturanAkun.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_akun_to_pengaturanAkunFragment)
        }
    }

    private fun logoutButtonOnPressed() {
        binding.logout.setOnClickListener {
            datastoreViewModel.apply {
                saveLoginState(false)
                deleteAllData()
            }
            startActivity(Intent(requireContext(), AuthActivity::class.java))
            requireActivity().finish()
        }
    }

}