package com.and2t2.secondhand.ui.uibuyer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.and2t2.secondhand.R
import com.and2t2.secondhand.common.*
import com.and2t2.secondhand.data.remote.dto.buyer.PostBuyerOrderBody
import com.and2t2.secondhand.data.remote.dto.wishlist.PostWishlistBody
import com.and2t2.secondhand.databinding.FragmentBuyerBinding
import com.and2t2.secondhand.domain.repository.DatastoreViewModel
import com.and2t2.secondhand.ui.uiwishlist.WishlistViewModel
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textfield.TextInputLayout
import org.koin.androidx.viewmodel.ext.android.viewModel


class BuyerFragment : Fragment() {

    private lateinit var dataHarga : PostBuyerOrderBody
    private var productId : Int? = null

    private val viewModel : BuyerViewModel by viewModel()
    private val dataStore : DatastoreViewModel by viewModel()
    private val wishlistViewModel : WishlistViewModel by viewModel()
    private lateinit var postWishlistBody: PostWishlistBody
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
//        getAccesstoken()
        productId = arguments?.getInt("product_key")!!
        productId?.let {
            getData(it)
            postWishlistBody = PostWishlistBody(it)
        }
        onNegoButtonClicked()
        onFavoritesButtonPressed()
        onBackPressed()
    }


    private fun  getData(id : Int){
        viewModel.getProductDetail(id).observe(viewLifecycleOwner){
            it.data.let { data ->
                Glide.with(requireContext()).load(data?.imageUrl).into(binding.ivProduk)
                Glide.with(requireContext()).load(data?.imageUser).into(binding.ivPenjual)
                binding.tvLokasi.text = data?.lokasi
                binding.tvNamaBarang.text = data?.namaBarang
                binding.tvHargaBarang.text = data?.hargaBarang?.toRp()
                binding.tvDeskripsi.text = data?.deskripsiBarang
                binding.tvKategori.text = data?.kategori
                binding.tvNamaPenjual.text = data?.username
            }
            val isTrue: Boolean = it.data?.id == null
            binding.pbLoading.isVisible =  isTrue
        }
    }


    private fun onNegoButtonClicked(){
        binding.btnNego.setOnClickListener {

            //menampilkan dialog
            val dialog = BottomSheetDialog(requireContext(),R.style.BottomSheetDialog)
            val view = layoutInflater.inflate(R.layout.buyer_10, null)

            dialog.setContentView(view)
            dialog.show()


            //logic dialog

            val ivBarang = view.findViewById<ShapeableImageView>(R.id.iv_tawar_barang)
            val namaBarang = view.findViewById<TextView>(R.id.tv_nama_barang)
            val hargaBarang = view.findViewById<TextView>(R.id.tv_harga_barang)
            val btnKirim = view.findViewById<MaterialButton>(R.id.btn_kirim)
            val etHarga = view.findViewById<TextInputLayout>(R.id.et_harga)

            viewModel.getProductDetail(productId!!).observe(viewLifecycleOwner){
                it.data?.let { data ->
                    Glide.with(requireContext()).load(data.imageUrl)
                        .into(ivBarang)
                    namaBarang.text = data.namaBarang
                    hargaBarang.text = data.hargaBarang.toRp()
                }
            }

            btnKirim.setOnClickListener {
                val inputNumber = etHarga.editText?.text.toString()
                if(inputNumber == ""){
                    Toast.makeText(requireContext(),"Input tidak valid",Toast.LENGTH_SHORT).show()
                }else{
                    val harga = inputNumber.toInt()
                    if(harga > 0){
                        dataHarga = PostBuyerOrderBody(harga,productId!!)
                        dataStore.getAccessToken().observe(viewLifecycleOwner){ access_token ->
                            viewModel.setBuyerOrder(dataHarga,access_token).observe(viewLifecycleOwner){
                                when(it.status){
                                    Status.LOADING -> {
                                        showLoading(requireActivity())
                                    }
                                    Status.SUCCESS ->{
                                        showSnackbar(requireContext(), requireView(), "Harga tawaranmu berhasil dikirim ke penjual", R.color.success)
                                        binding.btnNego.isVisible = false
                                        binding.btnNegoSuccess.isVisible = true
                                        hideLoading()
                                        dialog.dismiss()
                                    }
                                    Status.ERROR -> {
                                        showSnackbar(requireContext(), requireView(), it.message.toString(), R.color.danger)
                                        hideLoading()
                                        dialog.dismiss()
                                    }
                                }
                            }
                        }
                    }else{
                        Toast.makeText(requireContext(),"",Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun onFavoritesButtonPressed(){
        binding.btnFavorites.setOnClickListener {
             if(isAlreadyinFavorites()){
                 addtoFavorites()
             }else{
                deleteFromFavorites()
             }
        }
    }

    private fun addtoFavorites(){
        dataStore.getAccessToken().observe(viewLifecycleOwner){ access_token ->
            wishlistViewModel.postBuyerWishlist(access_token,postWishlistBody).observe(viewLifecycleOwner){
                when(it.status){
                    Status.LOADING ->{

                    }
                    Status.SUCCESS ->{
                        showSnackbar(requireContext(),requireView(),"add to favorites",R.color.success)
                    }
                    Status.ERROR ->{
                        showSnackbar(requireContext(),requireView(),"failed to favorites",R.color.danger)
                    }
                }
            }
        }
    }
    private fun deleteFromFavorites(){

    }
    private fun isAlreadyinFavorites() : Boolean{
        return productId == 1
    }


    private fun onBackPressed(){
        binding.ivBackButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }


}