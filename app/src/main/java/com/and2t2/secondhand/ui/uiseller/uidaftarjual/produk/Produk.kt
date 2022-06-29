package com.and2t2.secondhand.ui.uiseller.uidaftarjual.produk

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.and2t2.secondhand.common.Status
import com.and2t2.secondhand.common.viewModelsFactory
import com.and2t2.secondhand.data.remote.ApiClient
import com.and2t2.secondhand.data.remote.SellerService
import com.and2t2.secondhand.databinding.FragmentProdukBinding
import com.and2t2.secondhand.domain.repository.DatastoreManager
import com.and2t2.secondhand.domain.repository.DatastoreViewModel
import com.and2t2.secondhand.domain.repository.SellerRepo

class Produk : Fragment() {
    private var _binding: FragmentProdukBinding? = null
    private val binding get() = _binding!!

    private lateinit var produkAdapter: ProdukAdapter

    private val sellerService: SellerService by lazy { ApiClient.instanceSeller }
    private val sellerRepo: SellerRepo by lazy { SellerRepo(sellerService) }
    private val produkViewModel: ProdukViewModel by lazy { ProdukViewModel(sellerRepo) }

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
        observeFromNetwork()
    }

    private fun initRecyclerView() {
        produkAdapter = ProdukAdapter()
        binding.apply {
            rvData.adapter = produkAdapter
            rvData.layoutManager = GridLayoutManager(requireContext(), 2)
        }
    }

    private fun observeFromNetwork() {
        datastoreViewModel.getAccessToken().observe(viewLifecycleOwner) { token ->
            produkViewModel.getAllProduct(token).observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.LOADING -> {}
                    Status.SUCCESS -> {
                        produkAdapter.updateDataRecycler(it.data)
                    }
                    Status.ERROR -> {
                        Toast.makeText(requireContext(), "Error Guys", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}