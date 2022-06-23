package com.and2t2.secondhand.ui.uinotifikasi

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.and2t2.secondhand.common.Status
import com.and2t2.secondhand.data.local.DatabaseSecondHand
//import com.and2t2.secondhand.data.local.DatabaseSecondHand
import com.and2t2.secondhand.data.remote.ApiClient
import com.and2t2.secondhand.databinding.FragmentNotifikasiBinding
import com.and2t2.secondhand.domain.model.NotifikasiMapper
import com.and2t2.secondhand.domain.repository.NotifikasiRepo

//import com.and2t2.secondhand.domain.repository.NotifikasiRepo
//import id.afif.binarchallenge7.Model.Status


class NotifikasiFragment : Fragment() {


    private lateinit var notifAdapter: NotifikasiAdapter
    private val notifikasiRepo : NotifikasiRepo by lazy { NotifikasiRepo(ApiClient.instanceNotifikasi,
        NotifikasiMapper(), DatabaseSecondHand.getInstance(requireContext())!!) }
    private val viewModel : NotifikasiViewModel by lazy { NotifikasiViewModel(notifikasiRepo) }

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
        (activity as AppCompatActivity).supportActionBar?.title = "Notifikasi"
        initRecycler()
        getDataNotifikasi()
        onSwipeRefreshLayout()
    }

    private fun onSwipeRefreshLayout(){
        binding.swipe.setOnRefreshListener {
            Handler(Looper.getMainLooper()).postDelayed({
                binding.swipe.isRefreshing = false
            },2000)
        }
    }

    private fun initRecycler(){
        notifAdapter = NotifikasiAdapter{ id: Int ->  
            viewModel.updateNotifikasiRead("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6ImFmaWZAbWFpbC5jb20iLCJpYXQiOjE2NTU0NzkxMzd9.NEn3MajCccdWpLkHiAFAhez3DaFEPIdor7-MDxG9HoE",id)
        }
        binding.rvNotifikasi.adapter = notifAdapter
        binding.rvNotifikasi.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun getDataNotifikasi(){
        viewModel.notifikasi.observe(viewLifecycleOwner){
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