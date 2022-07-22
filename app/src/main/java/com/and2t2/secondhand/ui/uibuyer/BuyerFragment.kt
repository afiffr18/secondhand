package com.and2t2.secondhand.ui.uibuyer

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.and2t2.secondhand.AuthActivity
import com.and2t2.secondhand.R
import com.and2t2.secondhand.common.*
import com.and2t2.secondhand.data.local.WishlistId
import com.and2t2.secondhand.data.remote.dto.buyer.PostBuyerOrderBody
import com.and2t2.secondhand.data.remote.dto.wishlist.PostWishlistBody
import com.and2t2.secondhand.databinding.FragmentBuyerBinding
import com.and2t2.secondhand.domain.repository.DatastoreViewModel
import com.and2t2.secondhand.ui.uiwishlist.WishlistDBViewModel
import com.and2t2.secondhand.ui.uiwishlist.WishlistViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textfield.TextInputLayout
import org.koin.androidx.viewmodel.ext.android.viewModel


class BuyerFragment : Fragment() {

    private lateinit var dataHarga : PostBuyerOrderBody
    private var productId : Int? = null
    private var wishlistId : Int? = null

    private val viewModel : BuyerViewModel by viewModel()
    private val dataStore : DatastoreViewModel by viewModel()
    private val wishlistViewModel : WishlistViewModel by viewModel()
    private val wishlistDBViewModel : WishlistDBViewModel by viewModel()
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
        productId = arguments?.getInt("product_key")!!
        productId?.let {
            getWishlistId(it)
            getData(it)
            postWishlistBody = PostWishlistBody(it)
        }

        onNegoButtonClicked()
        onFavoritesButtonPressed()
        onBackPressed()
    }


    private fun  getData(id : Int){
        val bgOptions = RequestOptions().placeholder(R.drawable.ic_baseline_person_24)
        viewModel.getProductDetail(id).observe(viewLifecycleOwner){
            it.data.let { data ->
                Glide.with(requireContext()).load(data?.imageUrl).into(binding.ivProduk)
                Glide.with(requireContext()).load(data?.imageUser).apply(bgOptions).into(binding.ivPenjual)
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
            dataStore.getLoginState().observe(viewLifecycleOwner) {
                if (!it) {
                    showAlertDialogWithAction()
                } else {
                    showBottomSheetDialog()
                }
            }
        }
    }

    private fun showAlertDialogWithAction() {
        val dialog = AlertDialog.Builder(requireContext())
        dialog.setMessage("Anda harus login terlebih dahulu")
        dialog.setPositiveButton("Login") { _, _ ->
            startActivity(Intent(requireActivity(), AuthActivity::class.java))
        }
        dialog.setNegativeButton("Batal") { _, _ ->

        }
        dialog.setCancelable(false)
        dialog.show()
    }

    private fun showBottomSheetDialog() {
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




    private fun onFavoritesButtonPressed(){
        wishlistDBViewModel.getWishlistId(productId!!)
        wishlistDBViewModel.inWishlistId.observe(viewLifecycleOwner){ isAlreadyInWhislist ->
            if(isAlreadyInWhislist == true){
                binding.btnFavorites.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.danger))
                binding.btnFavorites.setOnClickListener {
                    wishlistId?.let { it1 -> deleteFromFavorites(it1) }
                }
            }else{
                binding.btnFavorites.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.neutral02))
                binding.btnFavorites.setOnClickListener {
                    addtoFavorites()
                }
            }
        }

    }

    private fun addtoFavorites(){
        dataStore.getAccessToken().observe(viewLifecycleOwner){ access_token ->
            val postObject = PostWishlistBody(productId!!)
            wishlistViewModel.postBuyerWishlist(access_token,postObject).observe(viewLifecycleOwner){
                when(it.status){
                    Status.LOADING ->{

                    }
                    Status.SUCCESS ->{
                        insertIntoDb()
                        showSnackbar(requireContext(),requireView(),"add to wishlist",R.color.success)
                        onFavoritesButtonPressed()
                    }
                    Status.ERROR ->{
                        showSnackbar(requireContext(),requireView(),"failed to add",R.color.danger)
                    }
                }
            }
        }
    }
    private fun deleteFromFavorites(id : Int){
        dataStore.getAccessToken().observe(viewLifecycleOwner){ access_token ->
            wishlistViewModel.deleteWishlist(access_token,id).observe(viewLifecycleOwner){
                when(it.status){
                    Status.LOADING ->{

                    }
                    Status.SUCCESS ->{
                        deleteFromDb()
                        showSnackbar(requireContext(),requireView(),"Dihapus dari wishlist",R.color.success)
                        onFavoritesButtonPressed()
                    }
                    Status.ERROR ->{
                        showSnackbar(requireContext(),requireView(),it.message.toString(),R.color.danger)
                    }
                }
            }

        }
    }

    private fun insertIntoDb(){
        val wishlistId = WishlistId(productId!!)
        wishlistDBViewModel.insertWishlist(wishlistId)
    }

    private fun deleteFromDb(){
        val wishlistId = WishlistId(productId!!)
        wishlistDBViewModel.deleteWishlistid(wishlistId)
    }

    private fun getWishlistId(productId : Int){
        dataStore.getAccessToken().observe(viewLifecycleOwner){ access_token ->
            wishlistViewModel.getBuyerWishlist(access_token).observe(viewLifecycleOwner){
                when(it.status){
                    Status.LOADING ->{

                    }
                    Status.SUCCESS ->{
                        wishlistId = it.data?.filter { data ->
                            data.productId == productId
                        }?.component1()?.id
                    }
                    Status.ERROR ->{

                    }
                }
            }
        }
    }


    private fun onBackPressed(){
        binding.ivBackButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}