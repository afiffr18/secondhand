package com.and2t2.secondhand.ui.uiprofile

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.and2t2.secondhand.R
import com.and2t2.secondhand.common.*
import com.and2t2.secondhand.data.remote.ApiClient
import com.and2t2.secondhand.data.remote.AuthService
import com.and2t2.secondhand.databinding.FragmentProfileBinding
import com.and2t2.secondhand.domain.repository.AuthRepo
import com.and2t2.secondhand.domain.repository.DatastoreManager
import com.and2t2.secondhand.domain.repository.DatastoreViewModel
import com.bumptech.glide.Glide
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File


class Profile : Fragment() {
    val fileUtil = FileUtil()
    var uri : Uri? = null

    companion object {
        const val REQUEST_CODE_ASK_PERMISSIONS = 123
        const val PICK_ID_IMAGE = 345
    }

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val authService: AuthService by lazy { ApiClient.INSTANCE_AUTH }
    private val authRepo: AuthRepo by lazy { AuthRepo(authService) }
    private val profileViewModel: ProfileViewModel by viewModelsFactory { ProfileViewModel(authRepo) }

    private val pref: DatastoreManager by lazy { DatastoreManager(requireContext()) }
    private val datastoreViewModel: DatastoreViewModel by viewModelsFactory { DatastoreViewModel(pref) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pickImageFromStorage()
        initAdapterCity()
        observeDataFromNetwork()
        doUpdate()
    }

    private fun pickImageFromStorage() {
        binding.ivPicture.setOnClickListener {
            requestPermission()
            val pickImg = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(pickImg, PICK_ID_IMAGE)
        }
    }

    private fun initAdapterCity() {
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, cityIndonesia())
        (binding.etlKota.editText as? MaterialAutoCompleteTextView)?.setAdapter(adapter)
    }

    private fun observeDataFromNetwork() {
        datastoreViewModel.getAccessToken().observe(viewLifecycleOwner) { token ->
            profileViewModel.getUser(token).observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.SUCCESS -> {
                        binding.apply {
                            if (it.data?.imageUrl != null) {
                                editIvPicture.setPadding(0,0,0,0)
                                Glide.with(requireContext())
                                    .load(it.data.imageUrl)
                                    .into(editIvPicture)
                            }

                            etNama.setText(it.data?.fullName)

                            if (it.data?.city == "Ex. Jakarta") {
                                etlKota.editText?.text?.clear()
                            } else {
                                etlKota.editText?.setText(it.data?.city)
                            }

                            if (it.data?.address == "Ex. Jl. Raya Kebayoran Lama No. 39") {
                                etAlamat.text?.clear()
                            } else {
                                etAlamat.setText(it.data?.address)
                            }

                            if (it.data?.phoneNumber == "Ex. 082132xxx") {
                                etNohp.text?.clear()
                            } else {
                                etNohp.setText(it.data?.phoneNumber)
                            }
                        }
                    }
                    Status.ERROR -> {}
                    Status.LOADING -> {}
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != AppCompatActivity.RESULT_CANCELED) {
            when (requestCode) {
                PICK_ID_IMAGE -> if (resultCode == AppCompatActivity.RESULT_OK && data != null) {
                    val img = data.data!!
                    // Munculkan image ke ImageView
                    binding.editIvPicture.setImageURI(img)
                    // Mendapatkan path
                    val imgPath = img.let { fileUtil.getPath(requireContext(), it) }
                    // Simpan ke variable global
                    uri = Uri.parse(imgPath)
                }
            }
        }
    }

    private fun doUpdate() {
        binding.btnSimpan.setOnClickListener {
            uploadData()
        }
    }

    private fun prepareFilePart(fileUri: Uri): MultipartBody.Part {
        val file = File(fileUri.path)
        Log.i("here is error", file.absolutePath)
        // Create RequestBody instance from file
        val requestFile: RequestBody = file.asRequestBody("image".toMediaTypeOrNull())

        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData("image", file.name, requestFile)
    }

    // Perizinan external storage
    private fun requestPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_CODE_ASK_PERMISSIONS
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CODE_ASK_PERMISSIONS -> if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                uploadImages()
            } else {
                Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun uploadData() {
        // Get Image from MultipartBody.Part
        val image = uri?.let { prepareFilePart(it) }

        // Get value forom TextEditText
        val etNamaLengkap = binding.etNama.text.toString()
        val etKota = binding.etlKota.editText?.text.toString()
        val etAlamat = binding.etAlamat.text.toString()
        val etNoHP = binding.etNohp.text.toString()

        val fullName = etNamaLengkap.toRequestBody("full_name".toMediaTypeOrNull())
        val phoneNumber = etNoHP.toRequestBody("phone_humber".toMediaTypeOrNull())
        val address = etAlamat.toRequestBody("address".toMediaTypeOrNull())
        val city = etKota.toRequestBody("city".toMediaTypeOrNull())

        datastoreViewModel.getAccessToken().observe(viewLifecycleOwner) { token ->
            profileViewModel.doUpdateUser(token, fullName, phoneNumber, address, city, image).observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.SUCCESS -> {
                        hideLoading()
                        showSnackbar(requireContext(), requireView(), "Berhasil Perbarui Akun", R.color.success)
                    }
                    Status.ERROR -> {
                        hideLoading()
                        showSnackbar(requireContext(), requireView(), "Gagal Perbarui Akun", R.color.danger)
                    }
                    Status.LOADING -> {
                        // Munculkan LoadingDialog
                        showLoading(requireActivity())
                    }
                }
            }
        }
    }

}