package com.and2t2.secondhand.ui.uiseller.uidaftarjual

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.and2t2.secondhand.databinding.FragmentDaftarJualBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.and2t2.secondhand.ui.uiseller.uidaftarjual.diminati.Diminati
import com.and2t2.secondhand.ui.uiseller.uidaftarjual.produk.Produk
import com.and2t2.secondhand.ui.uiseller.uidaftarjual.terjual.Terjual
import com.and2t2.secondhand.R
import com.and2t2.secondhand.common.showSnackbar
import com.and2t2.secondhand.data.local.DatabaseSecondHand
import com.and2t2.secondhand.data.remote.ApiClient
import com.and2t2.secondhand.domain.model.AuthUserMapper
import com.and2t2.secondhand.domain.repository.AuthRepo
import com.and2t2.secondhand.domain.repository.DatastoreManager
import com.and2t2.secondhand.domain.repository.DatastoreViewModel
import com.and2t2.secondhand.ui.uiprofile.ProfileViewModel
import com.bumptech.glide.Glide

class DaftarJualFragment : Fragment() {
    private var accessToken : String? = null

    private var _binding: FragmentDaftarJualBinding? = null
    private val binding get() = _binding!!

    private val authRepo: AuthRepo by lazy { AuthRepo(ApiClient.INSTANCE_AUTH, AuthUserMapper(), DatabaseSecondHand.getInstance(requireContext())!!) }
    private val profileViewModel: ProfileViewModel by lazy { ProfileViewModel(authRepo) }

    private val pref: DatastoreManager by lazy { DatastoreManager(requireContext()) }
    private val datastoreViewModel: DatastoreViewModel by lazy { DatastoreViewModel(pref) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDaftarJualBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTabAndViewPager()
        observeData()
        moveToEditProfile()
        getMsgSnackbar()
    }

    private fun setTabAndViewPager() {
        val tabLayout = binding.tlDaftarjual
        val viewPager2 = binding.viewPager2

        val listFragment = mutableListOf(
            Produk(),
            Diminati(),
            Terjual()
        )

        val daftarJualAdapter = activity?.let { DaftarJualAdapter(listFragment, it.supportFragmentManager, lifecycle) }
        binding.viewPager2.adapter = daftarJualAdapter

        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            when(position) {
                0 -> {
                    tab.text = "Produk"
                    tab.setIcon(R.drawable.ic_fi_produk)
                }
                1 -> {
                    tab.text = "Diminati"
                    tab.setIcon(R.drawable.ic_fi_diminati)
                }
                2 -> {
                    tab.text = "Terjual"
                    tab.setIcon(R.drawable.ic_fi_terjual)
                }
            }
        }.attach()
    }

    private fun observeData() {
        datastoreViewModel.getAccessToken().observe(viewLifecycleOwner) { token ->
            accessToken = token
            profileViewModel.getUser(accessToken!!).observe(viewLifecycleOwner) {
                it.data?.let { data ->
                    binding.apply {
                        if (data.imageUrl != null) {
                            ivProfileSeller.setPadding(0,0,0,0)
                            Glide.with(requireContext())
                                .load(data.imageUrl)
                                .into(ivProfileSeller)
                        }

                        tvNamaPenjual.text = data.fullName
                        tvCity.text = data.city
                    }
                }
            }
        }
    }

    private fun moveToEditProfile() {
        binding.btnEditprofile.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_daftarjual_to_profile)
        }
    }

    private fun getMsgSnackbar() {
        datastoreViewModel.getMsgSnackbar().observe(viewLifecycleOwner) {
            if (it != "default") {
                showSnackbar(requireContext(), requireView(), it, R.color.success)
            }
        }
        // kembalikan ke default
        datastoreViewModel.saveMsgSnackbar("default")
    }
}