package com.and2t2.secondhand.ui.uiseller.uidaftarjual.produk

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.and2t2.secondhand.R
import com.and2t2.secondhand.data.local.DatabaseSecondHand
import com.and2t2.secondhand.data.remote.ApiClient
import com.and2t2.secondhand.databinding.FragmentProdukBinding
import com.and2t2.secondhand.domain.model.SellerProductMapper
import com.and2t2.secondhand.domain.repository.DatastoreManager
import com.and2t2.secondhand.domain.repository.DatastoreViewModel
import com.and2t2.secondhand.domain.repository.SellerRepo
import com.and2t2.secondhand.ui.uiseller.SellerProductViewModel

class Produk : Fragment() {
    private var _binding: FragmentProdukBinding? = null
    private val binding get() = _binding!!

    private lateinit var produkAdapter: ProdukAdapter

    private val sellerRepo: SellerRepo by lazy { SellerRepo(ApiClient.instanceSeller, SellerProductMapper(), DatabaseSecondHand.getInstance(requireContext())!!) }
    private val sellerProductViewModel: SellerProductViewModel by lazy { SellerProductViewModel(sellerRepo) }

    private val pref: DatastoreManager by lazy { DatastoreManager(requireContext()) }
    private val datastoreViewModel: DatastoreViewModel by lazy { DatastoreViewModel(pref) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentProdukBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        observeData()
    }

    private fun initRecyclerView() {
        produkAdapter = ProdukAdapter { id ->
            if (id == 0) {
                // Action Button Add
                findNavController().navigate(R.id.action_navigation_daftarjual_to_navigation_jual)
            } else {
                val bundle = Bundle()
                bundle.putInt("sellerProductId", id)
                // Move to Preview Product
                findNavController().navigate(R.id.action_navigation_daftarjual_to_previewProdukFragment, bundle)
            }
        }
        binding.apply {
            rvData.adapter = produkAdapter
            rvData.layoutManager = GridLayoutManager(requireContext(), 2)
        }
    }

    private fun observeData() {
        datastoreViewModel.getAccessToken().observe(viewLifecycleOwner) { token ->
            sellerProductViewModel.getAllProduct(token).observe(viewLifecycleOwner) {
                val btn = view?.findViewById<View>(R.id.parent_btn_add_product)
                it.data?.let { data ->
                    if (!data.isNullOrEmpty()) {
                        btn?.layoutParams?.width = ViewGroup.LayoutParams.MATCH_PARENT
                        btn?.layoutParams?.height = ViewGroup.LayoutParams.WRAP_CONTENT
                    }
                    produkAdapter.updateDataRecycler(data)
                }
            }
        }
    }
}