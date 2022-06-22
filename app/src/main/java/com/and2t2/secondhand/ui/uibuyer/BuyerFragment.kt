package com.and2t2.secondhand.ui.uibuyer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.and2t2.secondhand.R
import com.and2t2.secondhand.common.Status
import com.and2t2.secondhand.common.toRp
import com.and2t2.secondhand.data.remote.ApiClient
import com.and2t2.secondhand.databinding.FragmentBuyerBinding
import com.and2t2.secondhand.domain.model.BuyerProductMapper
import com.and2t2.secondhand.domain.repository.BuyerRepo
import com.bumptech.glide.Glide
//import id.afif.binarchallenge7.Model.Status


class BuyerFragment : Fragment() {

    private val buyerRepo : BuyerRepo by lazy { BuyerRepo(ApiClient.instanceBuyer,
        BuyerProductMapper()
    ) }
    private val viewModel : BuyerViewModel by lazy { BuyerViewModel(buyerRepo) }

    private var _binding : FragmentBuyerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentBuyerBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.hide()
        setData(108)
    }

    private fun setData(id : Int){
        viewModel.getProductById(id).observe(viewLifecycleOwner){
            when(it.status){
                Status.LOADING -> {
                    binding.pbLoading.isVisible = true
                }
                Status.SUCCESS ->{
                    binding.pbLoading.isVisible = false
                    it.data?.let { data ->
                        Glide.with(requireContext()).load(data.imageUrl).into(binding.ivProduk)
                        binding.tvLokasi.text = data.lokasi
                        binding.tvNamaBarang.text = data.namaBarang
                        binding.tvHargaBarang.text = data.hargaBarang.toRp()
                        binding.tvDeskripsi.text = data.deskripsiBarang
                        binding.tvKategori.text = if(data.kategori.isNullOrEmpty()){
                            "Kategori"
                        }else{
                            data.kategori[0].name
                        }
                    }
                }
                Status.ERROR ->{
                    Toast.makeText(requireContext(),"Error Ocured",Toast.LENGTH_SHORT).show()
                    binding.pbLoading.isVisible = false
                }
            }
        }
    }



}