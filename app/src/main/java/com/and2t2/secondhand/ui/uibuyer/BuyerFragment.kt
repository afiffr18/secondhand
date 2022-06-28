package com.and2t2.secondhand.ui.uibuyer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.and2t2.secondhand.R
import com.and2t2.secondhand.common.toRp
import com.and2t2.secondhand.data.local.DatabaseSecondHand
import com.and2t2.secondhand.data.remote.ApiClient
import com.and2t2.secondhand.databinding.FragmentBuyerBinding
import com.and2t2.secondhand.domain.model.BuyerProductMapper
import com.and2t2.secondhand.domain.repository.BuyerRepo
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog


class BuyerFragment : Fragment() {

    private val buyerRepo : BuyerRepo by lazy { BuyerRepo(ApiClient.instanceBuyer,
        BuyerProductMapper(), DatabaseSecondHand.getInstance(requireContext())!!
    ) }
    private val viewModel : BuyerViewModel by lazy { BuyerViewModel(buyerRepo) }

    private var _binding : FragmentBuyerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
        onNegoButtonClicked()
    }

    private fun  setData(id : Int){
        viewModel.getProductDetail(id).observe(viewLifecycleOwner){
            it.data.let { data ->
                Glide.with(requireContext()).load(data?.imageUrl).into(binding.ivProduk)
                binding.tvLokasi.text = data?.lokasi
                binding.tvNamaBarang.text = data?.namaBarang
                binding.tvHargaBarang.text = data?.hargaBarang?.toRp()
                binding.tvDeskripsi.text = data?.deskripsiBarang
                binding.tvKategori.text = data?.kategori
            }
            val isTrue: Boolean = it.data?.id == null
            binding.pbLoading.isVisible =  isTrue
        }
    }

    private fun onNegoButtonClicked(){
        binding.btnNego.setOnClickListener {
            // on below line we are creating a new bottom sheet dialog.
            val dialog = BottomSheetDialog(requireContext())

            // on below line we are inflating a layout file which we have created.
            val view = layoutInflater.inflate(R.layout.buyer_10, null)

            dialog.setContentView(view)
            dialog.show()
        }
    }



}