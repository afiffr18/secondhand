package com.and2t2.secondhand.ui.uihome

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.and2t2.secondhand.R
import com.and2t2.secondhand.common.Status
import com.and2t2.secondhand.common.hideKeyboard
import com.and2t2.secondhand.common.viewModelsFactory
import com.and2t2.secondhand.data.local.DatabaseSecondHand
import com.and2t2.secondhand.data.remote.ApiClient
import com.and2t2.secondhand.databinding.FragmentHomeBinding
import com.and2t2.secondhand.domain.model.BuyerProductMapper
import com.and2t2.secondhand.domain.repository.HomeRepo


class Home : Fragment() {

    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var kategoriAdapter: KategoriAdapter
    private lateinit var productAdapter: ProductAdapter


    private val homeRepo : HomeRepo by lazy { HomeRepo(ApiClient.instanceSeller,ApiClient.instanceBuyer,
    BuyerProductMapper(), DatabaseSecondHand.getInstance(requireContext())!!
    ) }
    private val viewModel : HomeViewModel by viewModelsFactory { HomeViewModel(homeRepo) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initKategoriRecycler()
        getKategori()
        initProduct()
        getDataProduct()
        getDataBySearch()
    }


    private fun initKategoriRecycler(){
        kategoriAdapter = KategoriAdapter{ id ->
            getDataByKategori(id)
        }
        binding.rvListCategoryHomeProduct.apply {
            adapter = kategoriAdapter
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        }
    }
    private fun getKategori(){
        viewModel.getKategori().observe(viewLifecycleOwner){
            when(it.status){
                Status.LOADING ->{

                }
                Status.SUCCESS ->{
                    it.data?.let {
                            it1 -> kategoriAdapter.updateDataKategori(it1)
                    }
                }
                Status.ERROR ->{
                    Toast.makeText(requireContext(),it.message,Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    private fun initProduct(){
        productAdapter = ProductAdapter {

        }
        binding.rvListProductHomeProduct.apply {
            adapter = productAdapter
            layoutManager = GridLayoutManager(requireContext(),2)
        }
    }
    private fun getDataProduct(){
        viewModel.getBuyerProduct(null,null,null).observe(viewLifecycleOwner){
            it.data?.let { BuyerProduct ->
                productAdapter.updateDataProduct(BuyerProduct)
            }
        }
    }

    private fun getDataByKategori(id : Int){
        viewModel.getBuyerProduct(null,id,null).observe(viewLifecycleOwner){
            it.data?.let { BuyerProduct ->
                productAdapter.updateDataProduct(BuyerProduct)
            }
        }
    }

    private fun getDataBySearch(){
        binding.ivProductSearch.setOnClickListener {
            val search = binding.etProductSearch.editableText.toString()
            viewModel.getBuyerProduct(null,null,search).observe(viewLifecycleOwner){
                it.data?.let { BuyerProduct ->
                    productAdapter.updateDataProduct(BuyerProduct)
                }
            }
            binding.etProductSearch.editableText.clear()
            hideKeyboard()
        }
    }


}