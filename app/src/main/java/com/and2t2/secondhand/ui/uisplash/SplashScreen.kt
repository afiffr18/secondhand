package com.and2t2.secondhand.ui.uisplash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.and2t2.secondhand.R
import com.and2t2.secondhand.common.viewModelsFactory
import com.and2t2.secondhand.databinding.FragmentSplashScreenBinding
import com.and2t2.secondhand.domain.repository.DatastoreManager
import com.and2t2.secondhand.domain.repository.DatastoreViewModel


class SplashScreen : Fragment() {
    private var _binding: FragmentSplashScreenBinding? = null
    private val binding get() = _binding!!

    private lateinit var handler: Handler

    private val pref: DatastoreManager by lazy { DatastoreManager(requireContext()) }
    private val datastoreViewModel: DatastoreViewModel by viewModelsFactory { DatastoreViewModel(pref) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSplashScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkLoginState()
    }

    private fun checkLoginState() {
        datastoreViewModel.getLoginState().observe(viewLifecycleOwner) {
            if (it) { // True
                handler = Handler(Looper.getMainLooper())
                handler.postDelayed({ moveToHome() }, 2000)
            } else { // False
                handler = Handler(Looper.getMainLooper())
                handler.postDelayed({ moveToLogin() }, 2000)
            }
        }
    }

    private fun moveToLogin() {
        findNavController().navigate(R.id.action_splashScreen_to_login)
    }

    private fun moveToHome() {
        // isi findnavcontroller dari splash ke halaman Home
    }
}