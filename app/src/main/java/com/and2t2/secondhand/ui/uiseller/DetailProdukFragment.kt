package com.and2t2.secondhand.ui.uiseller

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.and2t2.secondhand.R
import com.and2t2.secondhand.common.viewModelsFactory
import com.and2t2.secondhand.data.local.DatabaseSecondHand
import com.and2t2.secondhand.data.remote.ApiClient
import com.and2t2.secondhand.databinding.FragmentAkunBinding
import com.and2t2.secondhand.databinding.FragmentDetailProdukBinding
import com.and2t2.secondhand.domain.model.SellerProductMapper
import com.and2t2.secondhand.domain.repository.DatastoreManager
import com.and2t2.secondhand.domain.repository.DatastoreViewModel
import com.and2t2.secondhand.domain.repository.SellerRepo
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class DetailProdukFragment : Fragment() {
    private var _binding : FragmentDetailProdukBinding? = null
    private val binding get() = _binding!!

    private val sellerRepo: SellerRepo by lazy {SellerRepo(ApiClient.instanceSeller,
        SellerProductMapper(), DatabaseSecondHand.getInstance(requireContext())!!)}
    private val sellerProductViewModel: SellerProductViewModel by viewModelsFactory { SellerProductViewModel(sellerRepo) }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailProdukBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        backButtonOnPressed()
    }

    private fun addProduct() {
        binding.btnTerbitkan.setOnClickListener{
            //Get value dari TextInputLayout
            val etNamaProduk = binding.editNamaProduk.editText?.text.toString()
            val etHargaProduk = binding.editHarga.editText?.text.toString()
            val etKategori = binding.etlKategori.editText?.text.toString()
            val etDeskripsi = binding.editDeskripsi.editText?.text.toString()
            val etFotoProduk = "??"

            val name = etNamaProduk.toRequestBody("name".toMediaTypeOrNull())
            val basePrice = etHargaProduk.toRequestBody("basePrice".toMediaTypeOrNull())
            val categoryIds = etKategori.toRequestBody("categoryIds".toMediaTypeOrNull())
            val description = etDeskripsi.toRequestBody("description".toMediaTypeOrNull())
        }
    }

    private fun backButtonOnPressed() {
        binding.backBtn.setOnClickListener {
            it.findNavController().popBackStack()
        }
    }
}