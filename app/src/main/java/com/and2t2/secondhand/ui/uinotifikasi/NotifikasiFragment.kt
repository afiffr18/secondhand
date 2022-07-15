package com.and2t2.secondhand.ui.uinotifikasi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.and2t2.secondhand.R
import com.and2t2.secondhand.common.Status
import com.and2t2.secondhand.databinding.FragmentNotifikasiBinding
import com.and2t2.secondhand.domain.repository.DatastoreViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class NotifikasiFragment : Fragment() {

    private  var accessToken : String = ""

    private lateinit var notifAdapter: NotifikasiAdapter
    private val viewModel : NotifikasiViewModel by viewModel()
    private val dataStore : DatastoreViewModel by viewModel()
    private var _binding : FragmentNotifikasiBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNotifikasiBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
        getData()
//        addBadge()
    }


//    private fun addBadge() {
//        var badge = bottomNav.getOrCreateBadge(menuItemId)
//        badge.isVisible = true
//// An icon only badge will be displayed unless a number is set:
//        badge.number = 99
//    }

    private fun initRecycler(){
        val linearLayoutManager = LinearLayoutManager(requireContext())
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true

        notifAdapter = NotifikasiAdapter{ id: Int ->
            updateDataNotifikasi(accessToken,id)
            findNavController().navigate(R.id.action_navigation_notifikasi_to_infoPenawarFragment2)
        }

        binding.rvNotifikasi.apply {
            adapter = notifAdapter
            layoutManager = linearLayoutManager
        }


    }

    private fun getData(){
        dataStore.getAccessToken().observe(viewLifecycleOwner){
            getDataNotifikasi(it)
            accessToken = it
        }
    }

    private fun getDataNotifikasi(access_token : String){
        viewModel.getNotifikasi(access_token).observe(viewLifecycleOwner){
            it.data?.let{ dataNotif ->
                notifAdapter.updateDataNotif(dataNotif)
            }
            val isTrue : Boolean = it.data.isNullOrEmpty()
            binding.pbLoading.isVisible = isTrue
            binding.tvError.text = when(it.status){
                Status.ERROR -> {
                    binding.pbLoading.isVisible = false
                    it.message
                }
                else -> {
                    binding.pbLoading.isVisible = false
                    "Tidak ada data"
                }
            }
        }
    }

    private fun updateDataNotifikasi(access_token: String,id : Int){
        viewModel.updateNotifikasiRead(access_token, id).observe(viewLifecycleOwner){
            when(it.status){
                Status.LOADING ->{

                }
                Status.SUCCESS ->{

                }
                Status.ERROR ->{
                    Toast.makeText(requireContext(),it.message,Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}