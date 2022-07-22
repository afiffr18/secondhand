package com.and2t2.secondhand.ui.uiseller.uidaftarjual.terjual

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isGone
import androidx.recyclerview.widget.LinearLayoutManager
import com.and2t2.secondhand.data.local.DatabaseSecondHand
import com.and2t2.secondhand.data.remote.ApiClient
import com.and2t2.secondhand.databinding.FragmentTerjualBinding
import com.and2t2.secondhand.domain.model.SellerCategoryMapper
import com.and2t2.secondhand.domain.model.SellerOrderMapper
import com.and2t2.secondhand.domain.model.SellerProductMapper
import com.and2t2.secondhand.domain.repository.DatastoreManager
import com.and2t2.secondhand.domain.repository.DatastoreViewModel
import com.and2t2.secondhand.domain.repository.SellerRepo
import com.and2t2.secondhand.ui.uiseller.SellerProductViewModel
import com.and2t2.secondhand.ui.uiseller.uidaftarjual.diminati.DiminatiAdapter

class Terjual : Fragment() {
    private var _binding: FragmentTerjualBinding? = null
    private val binding get() = _binding!!

    private lateinit var terjualAdapter: TerjualAdapter

    private val sellerRepo: SellerRepo by lazy { SellerRepo(
        ApiClient.instanceSeller, SellerProductMapper(),
        SellerOrderMapper(), SellerCategoryMapper(), DatabaseSecondHand.getInstance(requireContext())!!) }
    private val sellerProductViewModel: SellerProductViewModel by lazy { SellerProductViewModel(sellerRepo) }

    private val pref: DatastoreManager by lazy { DatastoreManager(requireContext()) }
    private val datastoreViewModel: DatastoreViewModel by lazy { DatastoreViewModel(pref) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentTerjualBinding.inflate(inflater, container, false)
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
        terjualAdapter = TerjualAdapter { id ->
            val bundle = Bundle()
            bundle.putInt("orderId", id)
        }
        binding.apply {
            rvDataTerjual.adapter = terjualAdapter
            rvDataTerjual.layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun observeData() {
        datastoreViewModel.getAccessToken().observe(viewLifecycleOwner) { token ->
            sellerProductViewModel.getAllOrder(token,null).observe(viewLifecycleOwner) {
                val filterData = it.data?.filter { data ->
                    data.status == "accepted"
                }
                terjualAdapter.updateDataRecycler(filterData!!)
                terjualAdapter.notifyDataSetChanged()
            }
        }
    }

}