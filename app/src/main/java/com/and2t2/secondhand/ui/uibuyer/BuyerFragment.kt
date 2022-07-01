package com.and2t2.secondhand.ui.uibuyer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
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
        getData(545)
        onNegoButtonClicked()
    }

//    private fun getAccesstoken(){
//        dataStore.getAccessToken().observe(viewLifecycleOwner){
//            access_token = it
//        }
//    }

    private fun  getData(id : Int){
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

            //menampilkan dialog
            val dialog = BottomSheetDialog(requireContext())
            val view = layoutInflater.inflate(R.layout.buyer_10, null)

            dialog.setContentView(view)
            dialog.show()


            //logic dialog

            val ivBarang = view.findViewById<ShapeableImageView>(R.id.iv_tawar_barang)
            val namaBarang = view.findViewById<TextView>(R.id.tv_nama_barang)
            val hargaBarang = view.findViewById<TextView>(R.id.tv_harga_barang)
            val btnKirim = view.findViewById<MaterialButton>(R.id.btn_kirim)
            val etHarga = view.findViewById<TextInputLayout>(R.id.et_harga)

            viewModel.getProductDetail(545).observe(viewLifecycleOwner){
                it.data?.let { data ->
                    Glide.with(ivBarang).load("https://firebasestorage.googleapis.com/v0/b/market-final-project.appspot.com/o/products%2FPR-1655719625930-kusuka.png?alt=media")
                        .into(ivBarang)
                    namaBarang.text = data.namaBarang
                    hargaBarang.text = data.hargaBarang.toRp()
                }
            }

            btnKirim.setOnClickListener {
                val harga = etHarga.editText?.text.toString().toInt()
                dataHarga = PostBuyerOrderBody(harga,545)
                dataStore.getAccessToken().observe(viewLifecycleOwner){ access_token ->
                }
                viewModel.setBuyerOrder(dataHarga).observe(viewLifecycleOwner){
                    when(it.status){
                        Status.LOADING -> {
                            showLoading(requireActivity())
                        }
                        Status.SUCCESS ->{
                            showSnackbar(requireContext(), requireView(), "Harga tawaranmu berhasil dikirim ke penjual", R.color.success)
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

        }
    }



}