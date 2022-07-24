package com.and2t2.secondhand.ui.uiseller

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.and2t2.secondhand.R
import com.and2t2.secondhand.common.*
import com.and2t2.secondhand.databinding.FragmentPreviewProdukBinding
import com.and2t2.secondhand.domain.model.PreviewSellerProduct
import com.and2t2.secondhand.domain.repository.DatastoreViewModel
import com.bumptech.glide.Glide
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File


class PreviewProdukFragment : Fragment() {
    private var accessToken : String? = null
    private var imgUri : Uri? = null
    private var productName : String? = null
    private var categoryId : ArrayList<Int>? = null
    private var categoryName : ArrayList<String>? = null
    private var basePrice : String? = null
    private var description : String? = null
    private var imgSellerUrl : String? = null
    private var sellerName : String? = null
    private var sellerLocation : String? = null

    private var _binding: FragmentPreviewProdukBinding? = null
    private val binding get() = _binding!!

    private val sellerProductViewModel: SellerProductViewModel by viewModel()
    private val datastoreViewModel: DatastoreViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener(requestKey = "previewrequestkey") { _, bundle ->
            val getData = bundle.getParcelable<PreviewSellerProduct>("previewkey") as PreviewSellerProduct

            accessToken = getData.token
            imgUri = getData.imageUri
            productName = getData.productName
            categoryId = getData.categoryId
            categoryName = getData.categoryName
            basePrice = getData.basePrice
            description = getData.productDescription
            imgSellerUrl = getData.sellerImgUrl
            sellerName = getData.sellerName
            sellerLocation = getData.sellerLocation

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
        requireActivity().onBackPressedDispatcher.addCallback(this, backPressedCallback)
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

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            setResult()
            requireActivity().findNavController(R.id.main_nav_host_fragment).popBackStack()
        }
    }

    private fun setResult() {
        val previewSellerProduct = PreviewSellerProduct(accessToken, imgUri, productName, categoryId, categoryName, basePrice, description, imgSellerUrl, sellerName, sellerLocation)
        val bundle = Bundle()
        bundle.putParcelable("detailkey", previewSellerProduct)
        setFragmentResult("detailrequestkey", bundle)
    }

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