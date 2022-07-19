package com.and2t2.secondhand.ui.uihome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.and2t2.secondhand.R
import com.and2t2.secondhand.common.Status
import com.and2t2.secondhand.common.hideKeyboard
import com.and2t2.secondhand.common.onDone
import com.and2t2.secondhand.data.local.DatabaseSecondHand
import com.and2t2.secondhand.data.remote.ApiClient
import com.and2t2.secondhand.databinding.FragmentHomeBinding
import com.and2t2.secondhand.domain.model.BuyerProductMapper
import com.and2t2.secondhand.domain.model.SellerCategory
import com.and2t2.secondhand.domain.model.SellerCategoryMapper
import com.and2t2.secondhand.domain.repository.HomeRepo
import org.koin.androidx.viewmodel.ext.android.viewModel


class Home : Fragment() {

    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val listOfCategory: MutableList<SellerCategory> = mutableListOf()
    private lateinit var kategoriAdapter: KategoriAdapter
    private lateinit var productAdapter: ProductAdapter

    private val viewModel : HomeViewModel by viewModel()

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
        onTopofListClicked()
    }


    private fun initKategoriRecycler(){
        val linearLayoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        kategoriAdapter = KategoriAdapter{ id ->
            if (id == 0){
                getDataProduct()

            }else{
                getDataByKategori(id)
            }
        }
        binding.rvListCategoryHomeProduct.apply {
            adapter = kategoriAdapter
            layoutManager = linearLayoutManager
        }

    }
    private fun getKategori(){
        viewModel.getKategori().observe(viewLifecycleOwner){
            when(it.status){
                Status.LOADING ->{

                }
                Status.SUCCESS ->{
                    val newKategori = SellerCategory(0,"Semua")
                    listOfCategory.add(newKategori)
                    it.data?.map { dataKategori ->
                        listOfCategory.add(dataKategori)
                    }
                    kategoriAdapter.updateDataKategori(listOfCategory)
                }
                Status.ERROR ->{

                }
            }

        }
    }


    private fun initProduct(){
        productAdapter = ProductAdapter {
            val bundle = Bundle()
            bundle.putInt("product_key",it)
            findNavController().navigate(R.id.action_navigation_home_to_buyerFragment,bundle)
        }
        binding.rvListProductHomeProduct.apply {
            adapter = productAdapter
            layoutManager = GridLayoutManager(requireContext(),2)
        }
    }
    private fun getDataProduct(){
        viewModel.getBuyerProduct(null,null).observe(viewLifecycleOwner){
            it.data?.let { BuyerProduct ->
                productAdapter.updateDataProduct(BuyerProduct)
                binding.rvListProductHomeProduct.scrollToPosition(0)
            }
        }
    }

    private fun getDataByKategori(id : Int){
        viewModel.getBuyerProduct(id,null).observe(viewLifecycleOwner){
            it.data?.let { BuyerProduct ->
                productAdapter.updateDataProduct(BuyerProduct)
            }
        }
    }


    private fun getDataBySearch(){
        //from icon search klik
        binding.ivProductSearch.setOnClickListener {
            val search = binding.etProductSearch.editableText.toString()
            viewModel.getBuyerProduct(null,search).observe(viewLifecycleOwner){
                it.data?.let { BuyerProduct ->
                    productAdapter.updateDataProduct(BuyerProduct)
                }
            }
            binding.etProductSearch.editableText.clear()
            hideKeyboard()
        }

        binding.etProductSearch.onDone {
            val search = binding.etProductSearch.editableText.toString()
            viewModel.getBuyerProduct(null,search).observe(viewLifecycleOwner){
                it.data?.let { BuyerProduct ->
                    productAdapter.updateDataProduct(BuyerProduct)
                }
            }
            binding.etProductSearch.editableText.clear()
        }
    }

    private fun onTopofListClicked(){
        binding.fabToTopList.setOnClickListener {
            binding.rvListProductHomeProduct.smoothScrollToPosition(0)
        }
    }



}