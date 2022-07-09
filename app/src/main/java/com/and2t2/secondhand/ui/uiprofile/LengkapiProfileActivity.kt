package com.and2t2.secondhand.ui.uiprofile

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.and2t2.secondhand.MainActivity
import com.and2t2.secondhand.R
import com.and2t2.secondhand.common.*
import com.and2t2.secondhand.data.local.DatabaseSecondHand
import com.and2t2.secondhand.data.remote.ApiClient
import com.and2t2.secondhand.databinding.ActivityLengkapiProfileBinding
import com.and2t2.secondhand.domain.model.AuthUserMapper
import com.and2t2.secondhand.domain.repository.AuthRepo
import com.and2t2.secondhand.domain.repository.DatastoreManager
import com.and2t2.secondhand.domain.repository.DatastoreViewModel
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class LengkapiProfileActivity : AppCompatActivity() {
    private val fileUtil = FileUtil()
    private var uri : Uri? = null

    private lateinit var binding: ActivityLengkapiProfileBinding

    private val authRepo: AuthRepo by lazy { AuthRepo(ApiClient.INSTANCE_AUTH, AuthUserMapper(), DatabaseSecondHand.getInstance(this)!!) }
    private val profileViewModel: ProfileViewModel by lazy { ProfileViewModel(authRepo) }

    private val pref: DatastoreManager by lazy { DatastoreManager(this) }
    private val datastoreViewModel: DatastoreViewModel by lazy { DatastoreViewModel(pref) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLengkapiProfileBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        pickImgAndRequestPermission()
        initAdapterCity()
        getNama()
        btnSimpanOnPressed()
    }

    private fun pickImgAndRequestPermission() {
        binding.ivPicture.setOnClickListener {
            checkingPermission()
        }
    }

    private fun checkingPermission() {
        // apakah permission sudah di setujui atau belum
        if (isGranted(
                this,
                Manifest.permission.CAMERA,
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                Profile.REQUEST_CODE_PERMISSION,
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
        AlertDialog.Builder(this)
            .setTitle("Permission Denied")
            .setMessage("Permission is denied, Please allow permissions from App Settings.")
            .setPositiveButton(
                "App Settings"
            ) { _, _ ->
                // mengarahkan user untuk buka halaman setting
                val intent = Intent()
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                val uri = Uri.fromParts("package", this.packageName, null)
                intent.data = uri
                startActivity(intent)
            }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
            .show()
    }

    private fun chooseImageDialog() {
        AlertDialog.Builder(this)
            .setMessage("Pilih Gambar")
            .setPositiveButton("Gallery") { _, _ -> openGallery() }
            .setNegativeButton("Camera") { _, _ -> openCamera() }
            .show()
    }

    // buat buka gallery
    private fun openGallery() {
        this.intent.type = "image/*"
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
            val imgPath = result.let { fileUtil.getPath(this, it) }
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
                val imgPath = fileUtil.getPath(this, fileUtil.bitmapToUri(this, bitmap))
                // Simpan ke variable global
                uri = Uri.parse(imgPath)
            }
        }

    private fun initAdapterCity() {
        val adapter = ArrayAdapter(this, R.layout.list_item, cityIndonesia())
        (binding.etlKota.editText as? MaterialAutoCompleteTextView)?.setAdapter(adapter)
    }

    private fun getNama() {
        val nama = intent.extras?.getString("nama_lengkap")
        if (nama != null) {
            binding.etNama.setText(nama)
        }
    }

    private fun btnSimpanOnPressed() {
        binding.btnSimpan.setOnClickListener {
            doUpdate()
        }
    }

    private fun doUpdate() {
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

        if (profileValidation(etNamaLengkap, etKota, etAlamat, etNoHP)) {
            datastoreViewModel.getAccessToken().observe(this) { token ->
                profileViewModel.doUpdateUser(token, fullName, phoneNumber, address, city, image).observe(this) {
                    when (it.status) {
                        Status.SUCCESS -> {
                            hideLoading()
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        }
                        Status.ERROR -> {
                            hideLoading()
                            showSnackbar(this, binding.root, it.message!!, R.color.danger)
                        }
                        Status.LOADING -> {
                            showLoading(this)
                        }
                    }
                }
            }
        }
    }

    private fun profileValidation(namaLengkap: String, kota: String, alamat: String, noHp: String): Boolean {
        var result = true
        if (namaLengkap.isEmpty()) { // jika kosong
            binding.etlNama.error = "Nama tidak boleh kosong!"
            result = false
        }  else {
            binding.etlNama.isErrorEnabled = false
        }

        if (kota.isEmpty()) { // jika kosong
            binding.etlKota.error = "Kota tidak boleh kosong!"
            result = false
        } else {
            binding.etlKota.isErrorEnabled = false
        }

        if (alamat.isEmpty()) { // jika kosong
            binding.etlAlamat.error = "Alamat tidak boleh kosong!"
            result = false
        }  else {
            binding.etlAlamat.isErrorEnabled = false
        }

        if (noHp.isEmpty()) { // jika kosong
            binding.etlNohp.error = "No. HP tidak boleh kosong!"
            result = false
        } else {
            binding.etlNohp.isErrorEnabled = false
        }

        return result
    }

    private fun prepareFilePart(fileUri: Uri): MultipartBody.Part {
        val file = File(fileUri.path)
        Log.i("PATH IMAGE", file.absolutePath)
        // Create RequestBody instance from file
        val requestFile: RequestBody = file.asRequestBody("image/jpg".toMediaTypeOrNull())

        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData("image", file.name, requestFile)
    }
}