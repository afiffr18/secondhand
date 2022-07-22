package com.and2t2.secondhand.ui.uiseller.uidaftarjual.diminati

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isGone
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.and2t2.secondhand.R
import com.and2t2.secondhand.data.local.DatabaseSecondHand
import com.and2t2.secondhand.data.remote.ApiClient
import com.and2t2.secondhand.databinding.FragmentDiminatiBinding
import com.and2t2.secondhand.domain.model.SellerCategoryMapper
import com.and2t2.secondhand.domain.model.SellerOrderMapper
import com.and2t2.secondhand.domain.model.SellerProductMapper
import com.and2t2.secondhand.domain.repository.DatastoreManager
import com.and2t2.secondhand.domain.repository.DatastoreViewModel
import com.and2t2.secondhand.domain.repository.SellerRepo
import com.and2t2.secondhand.ui.uiseller.SellerProductViewModel

class Diminati : Fragment() {
    private var _binding: FragmentDiminatiBinding? = null
    private val binding get() = _binding!!

    private lateinit var diminatiAdapter: DiminatiAdapter

    private val sellerRepo: SellerRepo by lazy { SellerRepo(ApiClient.instanceSeller, SellerProductMapper(),SellerOrderMapper(), SellerCategoryMapper(), DatabaseSecondHand.getInstance(requireContext())!!) }
    private val sellerProductViewModel: SellerProductViewModel by lazy { SellerProductViewModel(sellerRepo) }

    private val pref: DatastoreManager by lazy { DatastoreManager(requireContext()) }
    private val datastoreViewModel: DatastoreViewModel by lazy { DatastoreViewModel(pref) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDiminatiBinding.inflate(inflater, container, false)
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
        diminatiAdapter = DiminatiAdapter { id ->
            val bundle = Bundle()
            bundle.putInt("buyerId", id)
            findNavController().navigate(R.id.action_navigation_daftarjual_to_infoPenawarFragment2,bundle)
        }
        binding.apply {
            rvDataOrder.adapter = diminatiAdapter
            rvDataOrder.layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun observeData() {
        datastoreViewModel.getAccessToken().observe(viewLifecycleOwner) { token ->
            sellerProductViewModel.getAllOrder(token,null).observe(viewLifecycleOwner) {
                it.data?.let { data ->
                    if (!data.isNullOrEmpty()) {
                        diminatiAdapter.updateDataRecycler(data)
                        binding.ivNoProduk.isGone = true
                    } else {
                        diminatiAdapter.updateDataRecycler(data)
                        binding.ivNoProduk.isGone = false
                    }
                }
            }
        }
    }

}