package com.and2t2.secondhand.ui.uiakun

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.and2t2.secondhand.MainActivity
import com.and2t2.secondhand.R
import com.and2t2.secondhand.databinding.FragmentAkunBinding
import com.and2t2.secondhand.domain.repository.DatastoreViewModel
import com.and2t2.secondhand.ui.uiprofile.ProfileViewModel
import com.bumptech.glide.Glide
import org.koin.androidx.viewmodel.ext.android.viewModel


class AkunFragment : Fragment() {
    private var _binding : FragmentAkunBinding? = null
    private val binding get() = _binding!!

    private val profileViewModel: ProfileViewModel by viewModel()
    private val datastoreViewModel: DatastoreViewModel by viewModel()
    private val akunViewModel: AkunViewModel by viewModel()

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
        wishlistButtonOnPressed()
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

    private fun wishlistButtonOnPressed(){
        binding.wishlist.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_akun_to_wishlistFragment)
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
            startActivity(Intent(requireContext(), MainActivity::class.java))
            requireActivity().finish()
            akunViewModel.deleteTable()
            clearLoginState()
        }
    }

    private fun clearLoginState() {
        datastoreViewModel.apply {
            saveLoginState(false)
            deleteAllData()
        }
    }
}