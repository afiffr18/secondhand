package com.and2t2.secondhand.ui.uiseller.uidaftarjual

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.and2t2.secondhand.databinding.FragmentDaftarJualBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.and2t2.secondhand.ui.uiseller.uidaftarjual.diminati.Diminati
import com.and2t2.secondhand.ui.uiseller.uidaftarjual.produk.Produk
import com.and2t2.secondhand.ui.uiseller.uidaftarjual.terjual.Terjual
import com.and2t2.secondhand.R

class DaftarJualFragment : Fragment() {
    private var _binding: FragmentDaftarJualBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDaftarJualBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTabAndViewPager()
    }

    private fun setTabAndViewPager() {
        val tabLayout = binding.tlDaftarjual
        val viewPager2 = binding.viewPager2

        val listFragment = mutableListOf<Fragment>(
            Produk(),
            Diminati(),
            Terjual()
        )

        val daftarJualAdapter = activity?.let { DaftarJualAdapter(listFragment, it.supportFragmentManager, lifecycle) }
        binding.viewPager2.adapter = daftarJualAdapter

        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            when(position) {
                0 -> {
                    tab.text = "Produk"
                    tab.setIcon(R.drawable.ic_fi_produk)
                }
                1 -> {
                    tab.text = "Diminati"
                    tab.setIcon(R.drawable.ic_fi_diminati)
                }
                2 -> {
                    tab.text = "Terjual"
                    tab.setIcon(R.drawable.ic_fi_terjual)
                }
            }
        }.attach()
    }
}