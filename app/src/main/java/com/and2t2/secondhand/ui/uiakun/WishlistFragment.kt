package com.and2t2.secondhand.ui.uiakun

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.and2t2.secondhand.MainActivity
import com.and2t2.secondhand.R
import com.and2t2.secondhand.databinding.FragmentAkunBinding
import com.and2t2.secondhand.databinding.FragmentWishlistBinding

class WishlistFragment : Fragment() {
    private var _binding : FragmentWishlistBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWishlistBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        backButtonOnPressed()
    }

    private fun backButtonOnPressed() {
        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}