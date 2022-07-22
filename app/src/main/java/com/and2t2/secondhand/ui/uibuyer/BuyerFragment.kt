package com.and2t2.secondhand.ui.uibuyer

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.and2t2.secondhand.AuthActivity
import com.and2t2.secondhand.R
import com.and2t2.secondhand.common.*
import com.and2t2.secondhand.data.local.DatabaseSecondHand
import com.and2t2.secondhand.data.remote.ApiClient
import com.and2t2.secondhand.data.remote.dto.buyer.PostBuyerOrderBody
import com.and2t2.secondhand.databinding.FragmentBuyerBinding
import com.and2t2.secondhand.domain.model.BuyerProductDetailMapper
import com.and2t2.secondhand.domain.repository.BuyerRepo
import com.and2t2.secondhand.domain.repository.DatastoreManager
import com.and2t2.secondhand.domain.repository.DatastoreViewModel
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textfield.TextInputLayout


class BuyerFragment : Fragment() {

    private lateinit var dataHarga : PostBuyerOrderBody
    private var productId : Int? = null

    private val datastoreViewModel: DatastoreViewModel by viewModelsFactory { DatastoreViewModel(pref) }

    private val buyerRepo : BuyerRepo by lazy { BuyerRepo(ApiClient.instanceBuyer,
        BuyerProductDetailMapper(), DatabaseSecondHand.getInstance(requireContext())!!
    ) }
    private val viewModel : BuyerViewModel by lazy { BuyerViewModel(buyerRepo) }

    private val pref : DatastoreManager by lazy { DatastoreManager(requireContext()) }
    private val dataStore : DatastoreViewModel by viewModelsFactory { DatastoreViewModel(pref) }

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
        productId?.let { getData(it) }
        onNegoButtonClicked()
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
            datastoreViewModel.getLoginState().observe(requireActivity()) {
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
        dialog.setPositiveButton("Login") { dialogInterface, _ ->
            startActivity(Intent(requireContext(), AuthActivity::class.java))
            requireActivity().finish()
        }
        dialog.setNegativeButton("Batal") { dialogInterface, _ ->

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

    private fun onBackPressed(){
        binding.ivBackButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}