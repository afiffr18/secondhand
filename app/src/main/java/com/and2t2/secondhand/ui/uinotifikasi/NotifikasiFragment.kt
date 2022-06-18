package com.and2t2.secondhand.ui.uinotifikasi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.and2t2.secondhand.data.local.DatabaseNotifikasi
import com.and2t2.secondhand.data.remote.ApiClient
import com.and2t2.secondhand.databinding.FragmentNotifikasiBinding
import com.and2t2.secondhand.domain.model.NotifikasiMapper
import com.and2t2.secondhand.domain.repository.NotifikasiRepo
import id.afif.binarchallenge7.Model.Status
import id.afif.binarchallenge8.domain.util.Resource


class NotifikasiFragment : Fragment() {


    private lateinit var notifAdapter: NotifikasiAdapter
    private val notifikasiRepo : NotifikasiRepo by lazy { NotifikasiRepo(ApiClient.instanceNotifikasi,
        NotifikasiMapper(), DatabaseNotifikasi.getInstance(requireContext())!!) }
    private val viewModel : NotifikasiViewModel by lazy { NotifikasiViewModel(notifikasiRepo) }

    private var _binding : FragmentNotifikasiBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentNotifikasiBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = "Notifikasi"
        initRecycler()
        getDataNotifikasi()
    }

    private fun initRecycler(){
        notifAdapter = NotifikasiAdapter()
        binding.rvNotifikasi.adapter = notifAdapter
        binding.rvNotifikasi.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun getDataNotifikasi(){

        viewModel.notifikasi.observe(viewLifecycleOwner){
            it.data?.let{ dataNotif ->
                notifAdapter.updateDataNotif(dataNotif)
            }
            if (it.data.isNullOrEmpty()){
                when(it.status){
                    Status.LOADING ->{
                        binding.pbLoading.isVisible = true
                    }
                    Status.SUCCESS ->{
                        binding.pbLoading.isVisible = false
                        it.data?.let{ dataNotif ->
                            notifAdapter.updateDataNotif(dataNotif)
                        }
                    }
                    Status.ERROR->{
                        binding.pbLoading.isVisible = false
                        binding.tvError.text = it.message
                    }
                }
            }

        }
    }

}