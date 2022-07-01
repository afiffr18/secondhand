package com.and2t2.secondhand.ui.uiseller

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.and2t2.secondhand.R
import com.and2t2.secondhand.common.Status
import com.and2t2.secondhand.common.toRp
import com.and2t2.secondhand.data.local.DatabaseSecondHand
import com.and2t2.secondhand.data.remote.ApiClient
import com.and2t2.secondhand.databinding.FragmentPreviewProdukBinding
import com.and2t2.secondhand.domain.model.AuthUserMapper
import com.and2t2.secondhand.domain.model.SellerProductMapper
import com.and2t2.secondhand.domain.repository.AuthRepo
import com.and2t2.secondhand.domain.repository.DatastoreManager
import com.and2t2.secondhand.domain.repository.DatastoreViewModel
import com.and2t2.secondhand.domain.repository.SellerRepo
import com.and2t2.secondhand.ui.uiprofile.ProfileViewModel
import com.bumptech.glide.Glide


class PreviewProdukFragment : Fragment() {

    private var _binding: FragmentPreviewProdukBinding? = null
    private val binding get() = _binding!!

    private val sellerRepo: SellerRepo by lazy { SellerRepo(ApiClient.instanceSeller, SellerProductMapper(), DatabaseSecondHand.getInstance(requireContext())!!) }
    private val sellerProductViewModel: SellerProductViewModel by lazy { SellerProductViewModel(sellerRepo) }

    private val authRepo: AuthRepo by lazy { AuthRepo(ApiClient.INSTANCE_AUTH, AuthUserMapper(), DatabaseSecondHand.getInstance(requireContext())!!) }
    private val profileViewModel: ProfileViewModel by lazy { ProfileViewModel(authRepo) }

    private val pref: DatastoreManager by lazy { DatastoreManager(requireContext()) }
    private val datastoreViewModel: DatastoreViewModel by lazy { DatastoreViewModel(pref) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentPreviewProdukBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeDataProduct()
    }

    private fun getProductId(): Int? {
        return arguments?.getInt("sellerProductId")
    }

    private fun observeDataProduct() {
        datastoreViewModel.getAccessToken().observe(viewLifecycleOwner) { token ->
            sellerProductViewModel.getProductById(token, 448).observe(viewLifecycleOwner) { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        binding.apply {
                            Glide.with(requireContext())
                                .load(resource.data?.imageUrl)
                                .into(binding.ivProduk)

                            tvNamaBarang.text = resource.data?.name
                            tvKategori.text = resource.data?.categories?.joinToString { it.name }
                            tvHargaBarang.text = resource.data?.basePrice?.toRp()
                            tvDeskripsi.text = resource.data?.description

                            observeDataUser(token)
                        }
                    }
                    Status.ERROR -> {
                        Toast.makeText(requireContext(), resource.message, Toast.LENGTH_SHORT).show()
                    }
                    Status.LOADING -> {}
                }
            }
        }
    }

    private fun observeDataUser(accessToken: String) {
        profileViewModel.getUser(accessToken).observe(viewLifecycleOwner) {
            it.data?.let { data ->
                binding.apply {
                    if (data.imageUrl != null) {
                        Glide.with(requireContext())
                            .load(data.imageUrl)
                            .into(ivPenjual)
                    }

                    tvNamaPenjual.text = data.fullName
                    tvLokasi.text = data.city
                }
            }
        }
    }

}