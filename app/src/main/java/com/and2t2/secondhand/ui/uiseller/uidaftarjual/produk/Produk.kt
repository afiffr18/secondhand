package com.and2t2.secondhand.ui.uiseller.uidaftarjual.produk

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.and2t2.secondhand.R
import com.and2t2.secondhand.databinding.FragmentProdukBinding
import com.and2t2.secondhand.domain.repository.DatastoreViewModel
import com.and2t2.secondhand.ui.uiseller.SellerProductViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class Produk : Fragment() {
    private var _binding: FragmentProdukBinding? = null
    private val binding get() = _binding!!

    private lateinit var produkAdapter: ProdukAdapter

    private val sellerProductViewModel: SellerProductViewModel by viewModel()
    private val datastoreViewModel: DatastoreViewModel by viewModel()

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
                // Move to Detail
                findNavController().navigate(R.id.action_navigation_daftarjual_to_detail, bundle)
            }
        }
        binding.apply {
            rvData.adapter = produkAdapter
            rvData.layoutManager = GridLayoutManager(requireContext(), 2)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
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
                    produkAdapter.notifyDataSetChanged()
                }
//                val btn = view?.findViewById<View>(R.id.parent_btn_add_product)
//                it.data?.let { data ->
//                    if (!data.isNullOrEmpty()) {
//                        btn?.layoutParams?.width = ViewGroup.LayoutParams.MATCH_PARENT
//                        btn?.layoutParams?.height = ViewGroup.LayoutParams.WRAP_CONTENT
//                    }
//                    produkAdapter.updateDataRecycler(data)
//                }
            }
        }
    }
}