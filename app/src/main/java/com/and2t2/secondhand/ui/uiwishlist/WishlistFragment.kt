package com.and2t2.secondhand.ui.uiwishlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.and2t2.secondhand.common.Status
import com.and2t2.secondhand.databinding.FragmentWishlistBinding
import com.and2t2.secondhand.domain.repository.DatastoreViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class WishlistFragment : Fragment() {
    private var _binding : FragmentWishlistBinding? = null
    private val binding get() = _binding!!
    private lateinit var wishlistAdapter: WishlistAdapter
    private val datastoreViewModel : DatastoreViewModel by viewModel()
    private val wishlistViewModel : WishlistViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWishlistBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
        getDataWishlist()
        onBackButtonPressed()
    }

    private fun initRecycler(){
        wishlistAdapter = WishlistAdapter()
        binding.rvWishlist.apply {
            adapter = wishlistAdapter
            layoutManager = GridLayoutManager(requireContext(),2)
        }
    }

    private fun getDataWishlist(){
        datastoreViewModel.getAccessToken().observe(viewLifecycleOwner){ accessToken ->
            wishlistViewModel.getBuyerWishlist(accessToken).observe(viewLifecycleOwner){
                when(it.status){
                    Status.LOADING ->{

                    }
                    Status.SUCCESS ->{
                        it.data?.let{ data ->
                            wishlistAdapter.updateDataWishlist(data)
                        }
                    }
                    Status.ERROR ->{

                    }
                }
            }
        }
    }

    private fun onBackButtonPressed(){
        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
    }




}