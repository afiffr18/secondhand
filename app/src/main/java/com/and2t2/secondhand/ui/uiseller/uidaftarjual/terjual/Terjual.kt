package com.and2t2.secondhand.ui.uiseller.uidaftarjual.terjual

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.and2t2.secondhand.databinding.FragmentTerjualBinding
import com.and2t2.secondhand.domain.repository.DatastoreViewModel
import com.and2t2.secondhand.ui.uiseller.SellerProductViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class Terjual : Fragment() {
    private var _binding: FragmentTerjualBinding? = null
    private val binding get() = _binding!!

    private lateinit var terjualAdapter: TerjualAdapter

    private val sellerProductViewModel: SellerProductViewModel by viewModel()
    private val datastoreViewModel: DatastoreViewModel by viewModel()

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
        val linearLayoutManager = LinearLayoutManager(requireContext())
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        terjualAdapter = TerjualAdapter { id ->
            val bundle = Bundle()
            bundle.putInt("orderId", id)
        }
        binding.apply {
            rvDataTerjual.adapter = terjualAdapter
            rvDataTerjual.layoutManager = linearLayoutManager
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeData() {
        datastoreViewModel.getAccessToken().observe(viewLifecycleOwner) { token ->
            sellerProductViewModel.getAllOrder(token,null).observe(viewLifecycleOwner) {
                val filterData = it.data?.filter { data ->
                    data.productStatus == "sold"
                }
                val removeDuplicateProduct = filterData?.distinctBy {
                    it.productId
                }
                terjualAdapter.updateDataRecycler(removeDuplicateProduct!!)
                terjualAdapter.notifyDataSetChanged()
            }
        }
    }

}