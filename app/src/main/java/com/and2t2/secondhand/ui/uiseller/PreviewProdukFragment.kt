package com.and2t2.secondhand.ui.uiseller

import android.app.AlertDialog
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isGone
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import com.and2t2.secondhand.R
import com.and2t2.secondhand.common.*
import com.and2t2.secondhand.data.local.DatabaseSecondHand
import com.and2t2.secondhand.data.remote.ApiClient
import com.and2t2.secondhand.databinding.FragmentPreviewProdukBinding
import com.and2t2.secondhand.domain.model.AuthUserMapper
import com.and2t2.secondhand.domain.model.PreviewSellerProduct
import com.and2t2.secondhand.domain.model.SellerCategoryMapper
import com.and2t2.secondhand.domain.model.SellerProductMapper
import com.and2t2.secondhand.domain.repository.AuthRepo
import com.and2t2.secondhand.domain.repository.DatastoreManager
import com.and2t2.secondhand.domain.repository.DatastoreViewModel
import com.and2t2.secondhand.domain.repository.SellerRepo
import com.and2t2.secondhand.ui.uiprofile.ProfileViewModel
import com.bumptech.glide.Glide
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File


class PreviewProdukFragment : Fragment() {

    private var _binding: FragmentPreviewProdukBinding? = null
    private val binding get() = _binding!!

    private val sellerRepo: SellerRepo by lazy {SellerRepo(ApiClient.instanceSeller, SellerProductMapper(), SellerCategoryMapper(), DatabaseSecondHand.getInstance(requireContext())!!)}
    private val sellerProductViewModel: SellerProductViewModel by lazy { SellerProductViewModel(sellerRepo) }

    private val pref: DatastoreManager by lazy { DatastoreManager(requireContext()) }
    private val datastoreViewModel: DatastoreViewModel by lazy { DatastoreViewModel(pref) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener(requestKey = "previewrequestkey") { _, bundle ->
            val getData = bundle.getParcelable<PreviewSellerProduct>("previewkey") as PreviewSellerProduct

            val accessToken = getData.token
            val imgUri = getData.imageUri
            val productName = getData.productName
            val categoryId = getData.categoryId
            val categoryName = getData.categoryName
            val basePrice = getData.basePrice
            val description = getData.productDescription
            val imgSellerUrl = getData.sellerImgUrl
            val sellerName = getData.sellerName
            val sellerLocation = getData.sellerLocation

            binding.apply {
                binding.ivProduk.setImageURI(imgUri)
                tvNamaBarang.text = productName
                tvKategori.text = categoryName?.joinToString()
                tvHargaBarang.text = basePrice?.toInt()?.toRp()
                tvDeskripsi.text = description
                tvLokasi.text = sellerLocation
                tvNamaPenjual.text = sellerName
                if (imgSellerUrl != null) {
                    Glide.with(requireContext())
                        .load(imgSellerUrl)
                        .into(ivPenjual)
                }
            }
            publishProduct(accessToken, productName, description, basePrice, categoryId, imgUri, sellerLocation)
            backButtonOnPressed(accessToken, productName, description, basePrice, categoryId, categoryName, imgUri, sellerLocation, sellerName, imgSellerUrl)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentPreviewProdukBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

//    private fun observeDataUser() {
//        datastoreViewModel.getAccessToken().observe(viewLifecycleOwner) { token ->
//            accessToken = token
//            profileViewModel.getUser(accessToken!!).observe(viewLifecycleOwner) {
//                it.data?.let { data ->
//                    binding.apply {
//                        if (data.imageUrl != null) {
//                            Glide.with(requireContext())
//                                .load(data.imageUrl)
//                                .into(ivPenjual)
//                        }
//
//                        tvNamaPenjual.text = data.fullName
//                        city = data.city
//
//                        if (data.city == "Ex. Jakarta") {
//                            tvLokasi.isGone = true
//                        } else {
//                            tvLokasi.text = city
//                        }
//                    }
//                }
//            }
//        }
//    }

    private fun publishProduct(token: String?, productName: String?, description: String?, basePrice: String?, categoryId: ArrayList<Int>?, imgUri: Uri?, location: String?) {
        binding.btnTerbitkan.setOnClickListener {
            val kategori = categoryId?.joinToString()

            val namaProduk = productName!!.toRequestBody("name".toMediaTypeOrNull())
            val hargaBarang = basePrice!!.toRequestBody("basePrice".toMediaTypeOrNull())
            val categoryIds = kategori!!.toRequestBody("categoryIds".toMediaTypeOrNull())
            val deskripsi = description!!.toRequestBody("description".toMediaTypeOrNull())
            val lokasi = location!!.toRequestBody("location".toMediaTypeOrNull())
            val image = prepareFilePart(imgUri!!)

            sellerProductViewModel.postProduct(token!!, namaProduk, hargaBarang, categoryIds, deskripsi, lokasi, image).observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.SUCCESS -> {
                        hideLoading()
                        datastoreViewModel.saveMsgSnackbar("Produk berhasil diterbitkan")
                        findNavController().navigate(R.id.action_previewProdukFragment_to_navigation_daftarjual)
                    }
                    Status.ERROR -> {
                        hideLoading()
                        showSnackbar(requireContext(), requireView(), it.message!!, R.color.danger)
                    }
                    Status.LOADING -> {
                        showLoading(requireActivity())
                    }
                }
            }
        }
    }

    private fun prepareFilePart(fileUri: Uri): MultipartBody.Part {
        val file = File(fileUri.path)
        Log.i("PATH IMAGE", file.absolutePath)
        // Create RequestBody instance from file
        val requestFile: RequestBody = file.asRequestBody("image/jpg".toMediaTypeOrNull())

        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData("image", file.name, requestFile)
    }

    private fun backButtonOnPressed(
        token: String?,
        productName: String?,
        description: String?,
        basePrice: String?,
        categoryId: ArrayList<Int>?,
        categoryname: ArrayList<String>?,
        imgUri: Uri?,
        location: String?,
        imgSeller: String?,
        sellerName: String?
    ) {
        binding.ivBackButton.setOnClickListener {
            val previewSellerProduct = PreviewSellerProduct(token, imgUri, productName, categoryId, categoryname, basePrice, description, imgSeller, sellerName, location)
            val bundle = Bundle()
            bundle.putParcelable("detailkey", previewSellerProduct)
            setFragmentResult("detailrequestkey", bundle)
            findNavController().popBackStack()
        }
    }
}