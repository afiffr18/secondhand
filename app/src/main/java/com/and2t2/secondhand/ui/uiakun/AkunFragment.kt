package com.and2t2.secondhand.ui.uiakun

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.and2t2.secondhand.R
import com.and2t2.secondhand.common.viewModelsFactory
import com.and2t2.secondhand.databinding.FragmentAkunBinding
import com.and2t2.secondhand.domain.repository.DatastoreManager
import com.and2t2.secondhand.domain.repository.DatastoreViewModel


class AkunFragment : Fragment() {
    private var _binding : FragmentAkunBinding? = null
    private val binding get() = _binding!!

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
        buttonEditOnPressed()
        logoutButtonOnPressed()
    }

    private fun buttonEditOnPressed(){
        binding.tvUbahAkun.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_akun_to_profile)
        }
        binding.tvPengaturanAkun.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_akun_to_profile)
        }
    }

    private fun logoutButtonOnPressed() {
        binding.tvKelaurAkun.setOnClickListener {
            datastoreViewModel.apply {
                saveLoginState(false)
                deleteAllData()
            }
            findNavController().navigate(R.id.action_navigation_akun_to_login2)
        }
    }

}