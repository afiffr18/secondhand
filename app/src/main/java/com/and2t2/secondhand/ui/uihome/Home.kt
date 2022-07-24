package com.and2t2.secondhand.ui.uihome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.and2t2.secondhand.R
import com.and2t2.secondhand.common.Status
import com.and2t2.secondhand.common.autoScroll
import com.and2t2.secondhand.common.hideKeyboard
import com.and2t2.secondhand.common.onDone
import com.and2t2.secondhand.data.local.WishlistId
import com.and2t2.secondhand.databinding.FragmentHomeBinding
import com.and2t2.secondhand.domain.model.BuyerProduct
import com.and2t2.secondhand.domain.repository.DatastoreManager
import com.and2t2.secondhand.domain.repository.DatastoreViewModel
import com.and2t2.secondhand.ui.uiwishlist.WishlistDBViewModel
import com.and2t2.secondhand.ui.uiwishlist.WishlistViewModel
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.viewModel


class Home : Fragment() {

    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var kategoriAdapter: KategoriAdapter
    private lateinit var productAdapter: ProductAdapter
    private lateinit var bannerAdapter : BannerAdapterN
    private lateinit var viewPager2: ViewPager2
    private lateinit var dataWishlist : List<BuyerProduct>
    private val viewModel : HomeViewModel by viewModel()
    private val datastoreViewModel : DatastoreViewModel by viewModel()

    private val wishlistViewModel : WishlistViewModel by viewModel()
    private val wishlistDBViewModel : WishlistDBViewModel by viewModel()

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
        initTablayout()
        getDataBanner()
        getKategori()
        initProduct()
        importDataWishlistIdToDb()
        getDataProduct()
        getDataBySearch()
        onTopofListClicked()

    }

    private fun initTablayout(){
        viewPager2 = initBannerViewPager()
        val tabLayout = binding.indicatorTabLayout
        TabLayoutMediator(tabLayout, viewPager2) { _, _ -> }.attach()
    }

    private fun initBannerViewPager() = binding.carouselViewPager.apply{
        bannerAdapter = BannerAdapterN()
        binding.carouselViewPager.apply {
            adapter = bannerAdapter
        }
    }


    private fun getDataBanner(){
        viewModel.getSellerBanner().observe(viewLifecycleOwner){
            when(it.status){
                Status.LOADING ->{

                }
                Status.SUCCESS ->{
                    it.data?.let{ dataBanner ->
                        bannerAdapter.updateDataBanner(dataBanner)
                    }
                    viewPager2.autoScroll(3000)
                }
                Status.ERROR ->{

                }
            }
        }
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
            it.data?.let{ dataKategori ->
                kategoriAdapter.updateDataKategori(dataKategori)
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

    private fun importDataWishlistIdToDb(){
        datastoreViewModel.getLoginState().observe(viewLifecycleOwner){ loginState->
            if(loginState){
                datastoreViewModel.getAccessToken().observe(viewLifecycleOwner){ access_token ->
                    wishlistViewModel.getBuyerWishlist(access_token).observe(viewLifecycleOwner){
                        when(it.status){
                            Status.LOADING ->{

                            }
                            Status.SUCCESS ->{
                                it.data?.map { dataWishlist ->
                                    val wishlistId = WishlistId(dataWishlist.productId!!)
                                    wishlistDBViewModel.insertWishlist(wishlistId)
                                }
                            }
                            Status.ERROR ->{

                            }
                        }
                    }
                }
            }
        }
    }



}