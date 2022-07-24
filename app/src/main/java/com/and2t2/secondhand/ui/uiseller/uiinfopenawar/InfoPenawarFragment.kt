package com.and2t2.secondhand.ui.uiseller.uiinfopenawar

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.and2t2.secondhand.R
import com.and2t2.secondhand.common.*
import com.and2t2.secondhand.data.remote.dto.seller.SellerOrderStatusBody
import com.and2t2.secondhand.databinding.FragmentInfoPenawarBinding
import com.and2t2.secondhand.databinding.Seller30Binding
import com.and2t2.secondhand.domain.repository.DatastoreManager
import com.and2t2.secondhand.domain.repository.DatastoreViewModel
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import org.koin.androidx.viewmodel.ext.android.viewModel


class InfoPenawarFragment : Fragment() {
    private var _binding : FragmentInfoPenawarBinding? = null
    private val binding get() = _binding!!

    private lateinit var infoPenawarAdapter: InfoPenawarAdapter

    private val dataStore : DatastoreViewModel by viewModel()

    private val infoPenawarViewModel : InfoPenawarViewModel by viewModel()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInfoPenawarBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
        getAccessToken()
        backButtonOnPressed()
    }

    private fun getAccessToken(){
        dataStore.getAccessToken().observe(viewLifecycleOwner){
            val buyerId = arguments?.getInt("buyerId")
            Log.e("id",buyerId.toString())
            buyerId?.let { it1 -> getDataOrder(it, it1) }
        }
    }

    private fun initRecycler(){
        infoPenawarAdapter = InfoPenawarAdapter{ status,id ->
            dataStore.getAccessToken().observe(viewLifecycleOwner){ access_token ->
                updateOrderStatus(access_token, status, id)
            }
        }
        binding.rvContentProduct.apply {
            adapter = infoPenawarAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }



    private fun updateOrderStatus(access_token : String, status : String,id : Int){
        when (status) {
            "accepted" -> {
                onStatusAccepted(access_token, id)
            }
            "declined" -> {
                onStatusDeclined(access_token, id)
            }
            "status" -> {
                statusButtonOnPressed(access_token, id)
            }
            "hubungi" -> {
                hubungiButtonOnPressed(access_token, id)
            }
        }

    }

    private fun onStatusAccepted(access_token: String,id: Int){
        val statusBody = SellerOrderStatusBody("accepted")
        infoPenawarViewModel.updateSellerOrderStatus(access_token,id,statusBody).observe(viewLifecycleOwner){
            when(it.status){
                Status.LOADING ->{
                    showLoading(requireActivity())
                }
                Status.SUCCESS ->{
                    it.data?.let { data->
                        showSnackbar(requireContext(),requireView(),"Status produk berhasil diperbarui(${data.status})",R.color.success)
                    }
                    hideLoading()
                    hubungiButtonOnPressed(access_token, id)
                }
                Status.ERROR ->{
                    showSnackbar(requireContext(),requireView(),it.message.toString(),R.color.danger)
                    hideLoading()
                }
            }
        }
    }

    private fun onStatusDeclined(access_token: String,id: Int){
        val statusBody = SellerOrderStatusBody("declined")
        infoPenawarViewModel.updateSellerOrderStatus(access_token,id,statusBody).observe(viewLifecycleOwner){
            when(it.status){
                Status.LOADING ->{
                    showLoading(requireActivity())
                }
                Status.SUCCESS ->{
                    it.data?.let { data->
                        showSnackbar(requireContext(),requireView(),"Status produk berhasil diperbarui(${data.status})",R.color.success)
                    }
                    hideLoading()
                }
                Status.ERROR ->{
                    showSnackbar(requireContext(),requireView(),it.message.toString(),R.color.danger)
                    hideLoading()
                }
            }
        }
    }

    private fun hubungiButtonOnPressed(access_token: String,id : Int){
        //menampilkan dialog
        val dialog = BottomSheetDialog(requireContext())
        val binding = Seller30Binding.inflate(LayoutInflater.from(requireContext()))

        dialog.setContentView(binding.root)
        dialog.show()
        infoPenawarViewModel.getSellerOrderById(access_token,id).observe(viewLifecycleOwner){
           when(it.status){
               Status.LOADING ->{

               }
               Status.SUCCESS ->{
                   it.data?.let{ data ->
                       Glide.with(requireContext()).load(data.imageProduct).into(binding.ivProductImage)
                       binding.tvProductName.text = data.productName
                       binding.tvProductPrice.text = data.basePrice?.toRp()
                       binding.tvProductBid.text = data.price?.toRp()
                       binding.tvName.text = data.buyerName
                       binding.tvCity.text = data.buyerLocation
                       binding.btnContact.setOnClickListener {
                           data.phoneNumber?.let { it1 -> openWhatsapp(it1.toFormatPhone()) }
                           dialog.dismiss()
                       }
                   }
               }
               Status.ERROR -> {

               }
           }
        }
    }

    private fun openWhatsapp(smsNumber : String){
        try {
            val sendIntent = Intent("android.intent.action.MAIN")
            sendIntent.action = Intent.ACTION_SEND
            sendIntent.type = "text/plain"
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Hello,thanks for ordering my product")
            sendIntent.putExtra("jid", "$smsNumber@s.whatsapp.net")
            sendIntent.setPackage("com.whatsapp")
            startActivity(sendIntent)
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Error\n$e", Toast.LENGTH_SHORT).show()
        }
    }

    private fun statusButtonOnPressed(access_token: String,id: Int) {

            // on below line we are creating a new bottom sheet dialog.
            val dialog = BottomSheetDialog(requireContext())

            // on below line we are inflating a layout file which we have created.
            val view = layoutInflater.inflate(R.layout.seller_28, null)

            dialog.setContentView(view)
            dialog.show()
            val buttonKirim = dialog.findViewById<Button>(R.id.btn_set_status)
            val buttonSuccess = dialog.findViewById<RadioButton>(R.id.radio_success)
            val buttonCancel = dialog.findViewById<RadioButton>(R.id.radio_cancel)

            var tes : String? = null
            buttonSuccess?.setOnClickListener{
                buttonKirim?.isEnabled = true
                tes = "sold"
            }

            buttonCancel?.setOnClickListener{
                buttonKirim?.isEnabled = true
                tes = "available"
            }

            buttonKirim?.setOnClickListener {
                if(tes != ""){
                    val body = SellerOrderStatusBody(tes!!)
                    infoPenawarViewModel.updateSellerProductStatus(access_token,id,body).observe(viewLifecycleOwner){
                        when(it.status){
                            Status.LOADING ->{
                                showLoading(requireActivity())
                            }
                            Status.SUCCESS ->{
                                it.data?.let { data->
                                    showSnackbar(requireContext(),requireView(),"Status produk berhasil diperbarui(${data.status})",R.color.success)
                                }
                                hideLoading()
                            }
                            Status.ERROR ->{
                                showSnackbar(requireContext(),requireView(),it.message.toString(),R.color.danger)
                                hideLoading()
                            }
                        }
                    }
                }else{
                    showSnackbar(requireContext(), requireView(),"Diterima belum ada logic", R.color.success)
                }
                dialog.dismiss()
            }



    }

    private fun getDataOrder(accessToken : String,buyerId: Int){
        infoPenawarViewModel.getSellerOrder(accessToken,null).observe(viewLifecycleOwner){
            when(it.status){
                Status.LOADING ->{

                }
                Status.SUCCESS ->{
                    val dataFilter = it.data?.filter {
                        it.buyerId == buyerId
                    }
                    dataFilter?.let { data ->
                        infoPenawarAdapter.updateDataOrder(data)
                        binding.tvName.text = data.component1().buyerName ?: "Nama Pembeli"
                        binding.tvCity.text = data.component1().buyerLocation ?: "Kota"
                    }
                }
                Status.ERROR ->{
                    Toast.makeText(requireContext(),it.message,Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun backButtonOnPressed() {
        binding.backBtn.setOnClickListener {
            it.findNavController().popBackStack()
        }
    }
}