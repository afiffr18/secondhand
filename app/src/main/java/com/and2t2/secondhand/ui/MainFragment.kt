package com.and2t2.secondhand.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import com.and2t2.secondhand.R
import com.and2t2.secondhand.databinding.FragmentMainBinding
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupBottomNavigationBar()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupBottomNavigationBar() {
        val navView = binding.bottomNavigationView
        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.main_nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if(destination.id == R.id.profile||
                destination.id == R.id.navigation_jual){
                navView.visibility = View.GONE
            } else {
                navView.visibility = View.VISIBLE
            }

            if (destination.id == R.id.login2){
                findNavController().popBackStack()
            }
            navView.isVisible = destination.id != R.id.buyerFragment2
        }
        navView.setupWithNavController(navController)
    }
}