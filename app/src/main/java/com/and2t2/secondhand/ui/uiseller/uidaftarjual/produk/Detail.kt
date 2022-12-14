package com.and2t2.secondhand.ui.uiseller.uidaftarjual.produk

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.and2t2.secondhand.R
import com.and2t2.secondhand.common.*
import com.and2t2.secondhand.databinding.FragmentDetailBinding
import com.and2t2.secondhand.domain.repository.DatastoreViewModel
import com.and2t2.secondhand.ui.uiprofile.ProfileViewModel
import com.and2t2.secondhand.ui.uiseller.SellerProductViewModel
import com.bumptech.glide.Glide
import org.koin.androidx.viewmodel.ext.android.viewModel

class Detail : Fragment() {
    private var accessToken : String? = null

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val sellerProductViewModel: SellerProductViewModel by viewModel()
    private val profileViewModel: ProfileViewModel by viewModel()
    private val datastoreViewModel: DatastoreViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeDataProduct()
        backButtonOnPressed()
        deleteButtonOnPressed()
        updateButtonOnPressed()
    }

    private fun getProductId(): Int? {
        return arguments?.getInt("sellerProductId")
    }

    private fun observeDataProduct() {
        datastoreViewModel.getAccessToken().observe(viewLifecycleOwner) { token ->
            accessToken = token
            sellerProductViewModel.getProductById(accessToken!!, getProductId()!!).observe(viewLifecycleOwner) { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        hideLoading()
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
                        hideLoading()
                        showSnackbar(requireContext(), requireView(), resource.message!!, R.color.danger)
                    }
                    Status.LOADING -> {
                        showLoading(requireActivity())
                    }
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

    private fun backButtonOnPressed() {
        binding.ivBackButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun deleteButtonOnPressed() {
        binding.btnDelete.setOnClickListener {
            showDeleteDialog()
        }
    }

    private fun showDeleteDialog() {
        val dialog = AlertDialog.Builder(requireContext())
        dialog.setMessage("Hapus produk ini ?")
        dialog.setCancelable(true)
        dialog.setPositiveButton("Hapus") { dialogInterface, _ ->
            sellerProductViewModel.deleteProductById(accessToken!!, getProductId()!!).observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.SUCCESS -> {
                        hideLoading()
                        datastoreViewModel.saveMsgSnackbar(it.data?.message!!)
                        findNavController().navigate(R.id.action_detail_to_navigation_daftarjual)
                    }
                    Status.ERROR -> {
                        hideLoading()
                        showSnackbar(requireContext(), requireView(), it.message!!, R.color.danger)
                    }
                    Status.LOADING -> {
                        // Munculkan LoadingDialog
                        dialogInterface.dismiss()
                        showLoading(requireActivity())
                    }
                }
            }
        }
        dialog.setNegativeButton("Batal") { dialogInterface, _ ->
            dialogInterface.dismiss()
        }
        dialog.show()
    }

    private fun updateButtonOnPressed() {
        binding.btnUpdate.setOnClickListener {
            datastoreViewModel.saveTriggerUpdateProduct(true)
            val bundle = Bundle()
            bundle.putInt("updateProductId", getProductId()!!)
            findNavController().navigate(R.id.action_detail_to_navigation_jual, bundle)
        }
    }
}