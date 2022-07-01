package com.and2t2.secondhand.ui.uihome

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.and2t2.secondhand.R
import com.and2t2.secondhand.common.Status
import com.and2t2.secondhand.common.viewModelsFactory
import com.and2t2.secondhand.data.remote.ApiClient
import com.and2t2.secondhand.databinding.FragmentHomeBinding
import com.and2t2.secondhand.domain.repository.HomeRepo


class Home : Fragment() {

    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val homeRepo : HomeRepo by lazy { HomeRepo(ApiClient.instanceSeller) }
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

    }

    private fun getKategori(){
        viewModel.getKategori().observe(viewLifecycleOwner){
            when(it.status){
                Status.LOADING ->{

                }
                Status.SUCCESS ->{

                }
                Status.ERROR ->{

                }
            }
        }
    }


}