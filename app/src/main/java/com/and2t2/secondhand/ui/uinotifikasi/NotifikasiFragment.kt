package com.and2t2.secondhand.ui.uinotifikasi

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.and2t2.secondhand.common.Status
import com.and2t2.secondhand.common.viewModelsFactory
import com.and2t2.secondhand.data.local.DatabaseSecondHand
import com.and2t2.secondhand.data.remote.ApiClient
import com.and2t2.secondhand.databinding.FragmentNotifikasiBinding
import com.and2t2.secondhand.domain.model.NotifikasiMapper
import com.and2t2.secondhand.domain.repository.DatastoreManager
import com.and2t2.secondhand.domain.repository.DatastoreViewModel
import com.and2t2.secondhand.domain.repository.NotifikasiRepo
import java.io.IOException


class NotifikasiFragment : Fragment() {

    private  var access_token : String = ""

    private lateinit var notifAdapter: NotifikasiAdapter
    private val notifikasiRepo : NotifikasiRepo by lazy { NotifikasiRepo(ApiClient.instanceNotifikasi,
        NotifikasiMapper(), DatabaseSecondHand.getInstance(requireContext())!!) }
    private val viewModel : NotifikasiViewModel by viewModelsFactory { NotifikasiViewModel(notifikasiRepo) }

    private val pref : DatastoreManager by lazy { DatastoreManager(requireContext()) }
    private val dataStore : DatastoreViewModel by lazy { DatastoreViewModel(pref) }
    private var _binding : FragmentNotifikasiBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotifikasiBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        (activity as AppCompatActivity).supportActionBar?.title = "Notifikasi"

        initRecycler()
        getData()
        onSwipeRefreshLayout()
    }

    private fun onSwipeRefreshLayout(){
        binding.swipe.setOnRefreshListener {
            Handler(Looper.getMainLooper()).postDelayed({
                binding.swipe.isRefreshing = false
            },2000)
        }
    }

    private fun getAccesstoken(){
        dataStore.getAccessToken().observe(viewLifecycleOwner){
            access_token = it
        }
    }

    private fun initRecycler(){
        val linearLayoutManager = LinearLayoutManager(requireContext())
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true

        notifAdapter = NotifikasiAdapter{ id: Int ->
            viewModel.updateNotifikasiRead(access_token,id)
        }

        binding.rvNotifikasi.apply {
            adapter = notifAdapter
            layoutManager = linearLayoutManager
        }


    }

    fun getData(){
        dataStore.getAccessToken().observe(viewLifecycleOwner){
            getDataNotifikasi(it)
            access_token = it
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
                Status.ERROR ->{
                    binding.pbLoading.isVisible = false
                    binding.tvError.isVisible = true
                    it.message
                }
                else -> {
                    "Error Occured"
                }
            }
        }
    }

}