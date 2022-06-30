package com.and2t2.secondhand.ui.uiseller

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.and2t2.secondhand.R
import com.and2t2.secondhand.databinding.FragmentDaftarJualBinding
import com.and2t2.secondhand.databinding.FragmentPreviewProdukBinding


class PreviewProdukFragment : Fragment() {

    private var _binding: FragmentPreviewProdukBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentPreviewProdukBinding.inflate(inflater, container, false)
        return binding.root
    }

}