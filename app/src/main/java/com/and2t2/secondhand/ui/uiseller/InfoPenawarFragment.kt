package com.and2t2.secondhand.ui.uiseller

import android.graphics.Paint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import com.and2t2.secondhand.R
import com.and2t2.secondhand.common.showSnackbar
import com.and2t2.secondhand.databinding.FragmentInfoPenawarBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton

class InfoPenawarFragment : Fragment() {
    private var _binding : FragmentInfoPenawarBinding? = null
    private val binding get() = _binding!!


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
        acceptanceButtonOnPressed()
        hubungiButtonOnPressed()
        statusButtonOnPressed()
        backButtonOnPressed()
    }

    private fun acceptanceButtonOnPressed() {
        binding.btnTerima.setOnClickListener {
            hubungiDialog()
            binding.sudahDiterima.isVisible = true
            binding.belumDiterima.isVisible = false
        }

        binding.btnTolak.setOnClickListener {
            binding.belumDiterima.isVisible = false
            binding.sudahDiterima.isGone = true
            binding.divider1.isVisible = true

            showSnackbar(requireContext(), requireView(), "test", R.color.success)

            binding.tvProductBid.apply {
                paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                text = "Striked thru text"
            }
        }
//        binding.tvName.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
    }

    private fun hubungiDialog() {
        // on below line we are creating a new bottom sheet dialog.
        val dialog = BottomSheetDialog(requireContext())

        // on below line we are inflating a layout file which we have created.
        val view = layoutInflater.inflate(R.layout.seller_30, null)

        dialog.setContentView(view)
        dialog.show()
    }

    private fun hubungiButtonOnPressed() {
        binding.btnHubungi.setOnClickListener {
            hubungiDialog()
        }
    }

    private fun statusButtonOnPressed() {
        binding.btnStatus.setOnClickListener {
            // on below line we are creating a new bottom sheet dialog.
            val dialog = BottomSheetDialog(requireContext())

            // on below line we are inflating a layout file which we have created.
            val view = layoutInflater.inflate(R.layout.seller_28, null)

            dialog.setContentView(view)
            dialog.show()

            val buttonKirim = dialog.findViewById<Button>(R.id.btn_set_status)
            val buttonSuccess = dialog.findViewById<RadioButton>(R.id.radio_success)
            val buttonCancel = dialog.findViewById<RadioButton>(R.id.radio_cancel)


            buttonSuccess?.setOnClickListener{
                buttonKirim?.isEnabled = true
            }

            buttonCancel?.setOnClickListener{
                buttonKirim?.isEnabled = true
            }

            buttonKirim?.setOnClickListener {
                showSnackbar(requireContext(), requireView(), "Status produk", R.color.success)
                dialog.dismiss()
            }
        }
    }

    private fun backButtonOnPressed() {
        binding.backBtn.setOnClickListener {
            it.findNavController().popBackStack()
        }
    }
}