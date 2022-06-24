package com.and2t2.secondhand.ui.uiprofile

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.and2t2.secondhand.R
import com.and2t2.secondhand.common.*
import com.and2t2.secondhand.data.local.DatabaseSecondHand
import com.and2t2.secondhand.data.remote.ApiClient
import com.and2t2.secondhand.databinding.FragmentProfileBinding
import com.and2t2.secondhand.domain.model.AuthUserMapper
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
import java.io.ByteArrayOutputStream
import java.io.File


class Profile : Fragment() {
    private val fileUtil = FileUtil()
    private var uri : Uri? = null

    companion object { const val REQUEST_CODE_PERMISSION = 100 }

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val authRepo: AuthRepo by lazy { AuthRepo(ApiClient.INSTANCE_AUTH, AuthUserMapper(), DatabaseSecondHand.getInstance(requireContext())!!) }
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
        pickImgAndRequestPermission()
        initAdapterCity()
        doUpdate()
        observeDataFromNetwork()
    }

    private fun pickImgAndRequestPermission() {
        binding.ivPicture.setOnClickListener {
            checkingPermission()
        }
    }

    private fun checkingPermission() {
        // apakah permission sudah di setujui atau belum
        if (isGranted(
                requireActivity(),
                Manifest.permission.CAMERA,
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                REQUEST_CODE_PERMISSION,
            )
        ) {
            chooseImageDialog()
        }
    }

    private fun isGranted(
        activity: Activity,
        permission: String,
        permissions: Array<String>,
        request: Int,
    ): Boolean {
        val permissionCheck = ActivityCompat.checkSelfPermission(activity, permission)
        return if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            // klau udah di tolak sebelumnya
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                showPermissionDeniedDialog()
            }
            // klau belum pernah di tolak (request pertama kali)
            else {
                ActivityCompat.requestPermissions(activity, permissions, request)
            }
            false
        } else {
            true
        }
    }

    // dialoag yg muncul kalau user menolak permission yg di butuhkan
    private fun showPermissionDeniedDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Permission Denied")
            .setMessage("Permission is denied, Please allow permissions from App Settings.")
            .setPositiveButton(
                "App Settings"
            ) { _, _ ->
                // mengarahkan user untuk buka halaman setting
                val intent = Intent()
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                val uri = Uri.fromParts("package", requireActivity().packageName, null)
                intent.data = uri
                startActivity(intent)
            }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
            .show()
    }

    private fun chooseImageDialog() {
        AlertDialog.Builder(requireContext())
            .setMessage("Pilih Gambar")
            .setPositiveButton("Gallery") { _, _ -> openGallery() }
            .setNegativeButton("Camera") { _, _ -> openCamera() }
            .show()
    }

    // buat buka gallery
    private fun openGallery() {
        requireActivity().intent.type = "image/*"
        galleryResult.launch("image/*")
    }

    // buat dapetin URI image gallery
    private val galleryResult =
        registerForActivityResult(ActivityResultContracts.GetContent()) { result ->
            // Menampilkan image dari gallery ke ImageView
            binding.editIvPicture.apply {
                setPadding(0,0,0,0)
                setImageURI(result)
            }
            // Mendapatkan path
            val imgPath = result.let { fileUtil.getPath(requireContext(), it) }
            // Simpan ke variable global
            uri = Uri.parse(imgPath)
        }

    // buat open camera
    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraResult.launch(cameraIntent)
    }

    // buat dapetin bitmap image dari camera
    private val cameraResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                // dapetin data bitmap dari intent
                val bitmap = result.data!!.extras?.get("data") as Bitmap
                // Menampilkan image dari capture camera ke ImageView
                binding.editIvPicture.apply {
                    setPadding(0,0,0,0)
                    setImageBitmap(bitmap)
                }
                // Mendapatkan path
                val imgPath = bitmap.let { bitmap1 -> getImageUri(requireContext(), bitmap1).let { fileUtil.getPath(requireContext(), it) } }
                // Simpan ke variable global
                uri = Uri.parse(imgPath)
            }
        }

    // Convert Bitmap to Uri
    fun getImageUri(inContext: Context, inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
            inContext.contentResolver,
            inImage,
            "intuenty",
            null
        )
        Log.d("image uri", path)
        return Uri.parse(path)
    }

    private fun initAdapterCity() {
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, cityIndonesia())
        (binding.etlKota.editText as? MaterialAutoCompleteTextView)?.setAdapter(adapter)
    }

    private fun doUpdate() {
        binding.btnSimpan.setOnClickListener {
            uploadData()
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
                        observeDataFromNetwork()
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

    private fun prepareFilePart(fileUri: Uri): MultipartBody.Part {
        val file = File(fileUri.path)
        Log.i("PATH IMAGE", file.absolutePath)
        // Create RequestBody instance from file
        val requestFile: RequestBody = file.asRequestBody("image".toMediaTypeOrNull())

        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData("image", file.name, requestFile)
    }

    private fun observeDataFromNetwork() {
        datastoreViewModel.getAccessToken().observe(viewLifecycleOwner) { token ->
            profileViewModel.getUser(token).observe(viewLifecycleOwner) {
                it.data?.let { data ->
                    binding.apply {
                        if (data.imageUrl != null) {
                            editIvPicture.setPadding(0,0,0,0)
                            Glide.with(requireContext())
                                .load(data.imageUrl)
                                .into(editIvPicture)
                        }

                        etNama.setText(data.fullName)

                        if (data.city == "Ex. Jakarta") {
                            etlKota.editText?.text?.clear()
                        } else {
                            etlKota.editText?.setText(data.city)
                        }

                        if (data.address == "Ex. Jl. Raya Kebayoran Lama No. 39") {
                            etAlamat.text?.clear()
                        } else {
                            etAlamat.setText(data.address)
                        }

                        if (data.phoneNumber == "Ex. 082132xxx") {
                            etNohp.text?.clear()
                        } else {
                            etNohp.setText(data.phoneNumber)
                        }
                    }
                }
            }
        }
    }

}