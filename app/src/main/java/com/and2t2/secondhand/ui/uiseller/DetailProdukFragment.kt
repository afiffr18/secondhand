package com.and2t2.secondhand.ui.uiseller

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.and2t2.secondhand.R
import com.and2t2.secondhand.common.*
import com.and2t2.secondhand.databinding.FragmentDetailProdukBinding
import com.and2t2.secondhand.domain.model.PreviewSellerProduct
import com.and2t2.secondhand.domain.repository.DatastoreViewModel
import com.and2t2.secondhand.ui.uiprofile.Profile
import com.and2t2.secondhand.ui.uiprofile.ProfileViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.chip.Chip
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class DetailProdukFragment : Fragment() {
    private val arrayCategoryId: ArrayList<Int> = ArrayList()
    private val arrayCategoryName: ArrayList<String> = ArrayList()
    private val fileUtil = FileUtil()
    private var uri : Uri? = null
    private var accessToken : String? = null
    private var imgSellerUrl : String? = null
    private var sellerName : String? = null
    private var city : String? = null

    private var _binding : FragmentDetailProdukBinding? = null
    private val binding get() = _binding!!

    private val sellerCategoryViewModel: SellerCategoryViewModel by viewModel()
    private val sellerProductViewModel: SellerProductViewModel by viewModel()
    private val profileViewModel: ProfileViewModel by viewModel()

    private val datastoreViewModel: DatastoreViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener(requestKey = "detailrequestkey") { _, bundle ->
            val getData = bundle.getParcelable<PreviewSellerProduct>("detailkey") as PreviewSellerProduct

            val idCategory = getData.categoryId
            val categoryName = getData.categoryName

            binding.apply {
                ivPicture.setImageURI(getData.imageUri)
                editNamaProduk.setText(getData.productName)
                editHarga.setText(getData.basePrice)
                editDeskripsi.setText(getData.productDescription)
                editImage.setText(R.string.app_name)
            }

            if (idCategory?.size != null) {
                for (i in 0 until idCategory.size) {
                    val id = idCategory.elementAt(i)
                    val nama = categoryName?.elementAt(i)
                    createChips(id, nama!!)
                    Log.d("CREATE CHIPS", "ID: $id, NAME: $nama")
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailProdukBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        backButtonOnPressed()
        observeCategory()
        pickImgAndRequestPermission()
        getUserData()
        addProduct()
        previewProduct()
        isButtonUpdateClicked()
    }

    private fun observeCategory() {
        sellerCategoryViewModel.getCategory().observe(viewLifecycleOwner) { resource ->
            resource.data?.let { list ->
                val adapter = ArrayAdapter(requireContext(), R.layout.list_item, list.map { it.name })
                val mACTV = view?.findViewById<MaterialAutoCompleteTextView>(R.id.etl_kategori)
                mACTV?.setAdapter(adapter)

                mACTV?.setOnItemClickListener { _, _, position, _ ->
                    val idCategory = list.map { it.id }[position]
                    val categoryName = list.map { it.name }[position]

                    // Masukkan ke ArrayList
                    arrayCategoryId.add(idCategory)
                    arrayCategoryName.add(categoryName!!)

                    // Setelah kategori dipilih langsung cleartext
                    binding.etlKategori.text.clear()

                    // Buat chips kategori yang dipilih
                    createChips(idCategory, categoryName)
                }
            }
        }
    }

    private fun createChips(idCategory: Int, categoryName: String) {
        val chip = Chip(requireContext())
        chip.apply {
            text = categoryName
            isCloseIconVisible = true
            isClickable = true
            isCheckable = false
            binding.chipGroup.addView(chip as View)
            setOnCloseIconClickListener {
                // Hapus view chips
                binding.chipGroup.removeView(chip as View)
                // Hapus element pada ArrayList
                arrayCategoryId.remove(idCategory)
                arrayCategoryName.remove(categoryName)
            }
        }
        binding.editKategori.setText(R.string.app_name)
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
            binding.apply {
                ivPicture.setImageURI(result)
                editImage.setText(R.string.app_name)
            }
            // Mendapatkan path
            val imgPath = result?.let { fileUtil.getPath(requireContext(), it) }
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
                binding.apply {
                    ivPicture.setImageBitmap(bitmap)
                    editImage.setText(R.string.app_name)
                }
                // Mendapatkan path
                val imgPath = fileUtil.getPath(requireContext(), fileUtil.bitmapToUri(requireContext(), bitmap))
                // Simpan ke variable global
                uri = Uri.parse(imgPath)
            }
        }

    private fun getUserData() {
        datastoreViewModel.getAccessToken().observe(viewLifecycleOwner) { token ->
            accessToken = token
            profileViewModel.getUser(accessToken!!).observe(viewLifecycleOwner) {
                it.data?.let { data ->
                    imgSellerUrl = data.imageUrl
                    sellerName = data.fullName
                    city = data.city
                }
            }
        }
    }

    private fun addProduct() {
        // Text Watcher
        binding.editNamaProduk.addTextChangedListener(textWatcher)
        binding.editHarga.addTextChangedListener(textWatcher)
        binding.editDeskripsi.addTextChangedListener(textWatcher)
        binding.editKategori.addTextChangedListener(textWatcher)
        binding.editImage.addTextChangedListener(textWatcher)

        binding.btnTerbitkan.setOnClickListener {
            // Get Image from MultipartBody.Part
            val image = uri?.let { prepareFilePart(it) }

            //Get value dari TextInputLayout
            val etNamaProduk = binding.editNamaProduk.text.toString()
            val etHargaProduk = binding.editHarga.text.toString()
            val etKategori = arrayCategoryId.joinToString()
            val etDeskripsi = binding.editDeskripsi.text.toString()

            val name = etNamaProduk.toRequestBody("name".toMediaTypeOrNull())
            val basePrice = etHargaProduk.toRequestBody("basePrice".toMediaTypeOrNull())
            val categoryIds = etKategori.toRequestBody("categoryIds".toMediaTypeOrNull())
            val description = etDeskripsi.toRequestBody("description".toMediaTypeOrNull())
            val location = city!!.toRequestBody("location".toMediaTypeOrNull())

            sellerProductViewModel.postProduct(accessToken!!, name, basePrice, categoryIds, description, location, image).observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.SUCCESS -> {
                        hideLoading()
                        datastoreViewModel.saveMsgSnackbar("Produk berhasil diterbitkan")
                        findNavController().navigate(R.id.action_navigation_jual_to_navigation_daftarjual)
                    }
                    Status.ERROR -> {
                        hideLoading()
                        showSnackbar(requireContext(), requireView(), it.message!!, R.color.danger)
                    }
                    Status.LOADING -> {
                        showLoading(requireActivity())
                    }
                }
            }
        }
    }

    private fun previewProduct() {
        binding.btnPreview.setOnClickListener {
            // Get Uri Foto Produk
            val imgUri = uri!!

            // Get value dari TextInputLayout
            val etNamaProduk = binding.editNamaProduk.text.toString()
            val etHargaProduk = binding.editHarga.text.toString()
            val etDeskripsi = binding.editDeskripsi.text.toString()

            val previewSellerProduct = PreviewSellerProduct(accessToken, imgUri, etNamaProduk, arrayCategoryId, arrayCategoryName, etHargaProduk, etDeskripsi, imgSellerUrl, sellerName, city)

            val bundle = Bundle()
            bundle.putParcelable("previewkey", previewSellerProduct)
            setFragmentResult("previewrequestkey", bundle)

            findNavController().navigate(R.id.action_navigation_jual_to_previewProdukFragment)
        }
    }

    private fun isButtonUpdateClicked() {
        datastoreViewModel.getTriggerUpdateProduct().observe(viewLifecycleOwner) {
            if (it) {
                binding.apply {
                    btnPreview.isGone = true
                    btnTerbitkan.isGone = true
                    btnUpdate.isGone = false
                }
                observeDataForUpdate()
            }
        }
        datastoreViewModel.saveTriggerUpdateProduct(false)
    }

    private fun observeDataForUpdate() {
        val productId = arguments?.getInt("updateProductId")
        if (productId != null) {
            sellerProductViewModel.getProductById(accessToken!!, productId).observe(viewLifecycleOwner) { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        binding.apply {
                            editNamaProduk.setText(resource.data?.name)
                            editHarga.setText(resource.data?.basePrice.toString())

                            val idCategory = resource.data?.categories?.map { it.id }
                            val categoryName = resource.data?.categories?.map { it.name }
                            val kategori = resource.data?.categories
                            if (kategori != null) {
                                for (i in kategori.indices) {
                                    // Get element dari index ke
                                    val id = idCategory?.elementAt(i)
                                    val name = categoryName?.elementAt(i)
                                    // Masukkan ke ArrayList
                                    arrayCategoryId.add(id!!)
                                    arrayCategoryName.add(name!!)
                                    // Buat chips
                                    createChips(id, name)
                                }
                            }

                            editDeskripsi.setText(resource.data?.description)

                            if (resource.data?.imageUrl != null) {
                                Glide.with(requireContext())
                                    .asBitmap()
                                    .load(resource.data.imageUrl)
                                    .into(object : CustomTarget<Bitmap>() {
                                        override fun onResourceReady(
                                            resource: Bitmap,
                                            transition: Transition<in Bitmap>?
                                        ) {
                                            ivPicture.setImageBitmap(resource)
                                            val imgPath = fileUtil.getPath(requireContext(), fileUtil.bitmapToUri(requireContext(), resource))
                                            uri = Uri.parse(imgPath)
                                        }
                                        override fun onLoadCleared(placeholder: Drawable?) {}
                                    })
                                binding.editImage.setText(R.string.app_name)
                            }
                        }

                        updateProduct(productId)
                    }
                    Status.ERROR -> {
                        showSnackbar(requireContext(), requireView(), resource.message!!, R.color.danger)
                    }
                    Status.LOADING -> {}
                }
            }
        }
    }

    private fun updateProduct(id: Int?) {
        binding.btnUpdate.setOnClickListener {
            // Get Image from MultipartBody.Part
            val image = uri?.let { prepareFilePart(it) }

            //Get value dari TextInputLayout
            val etNamaProduk = binding.editNamaProduk.text.toString()
            val etHargaProduk = binding.editHarga.text.toString()
            val etKategori = arrayCategoryId.joinToString()
            val etDeskripsi = binding.editDeskripsi.text.toString()

            val name = etNamaProduk.toRequestBody("name".toMediaTypeOrNull())
            val basePrice = etHargaProduk.toRequestBody("basePrice".toMediaTypeOrNull())
            val categoryIds = etKategori.toRequestBody("categoryIds".toMediaTypeOrNull())
            val description = etDeskripsi.toRequestBody("description".toMediaTypeOrNull())
            val location = city!!.toRequestBody("location".toMediaTypeOrNull())

            sellerProductViewModel.updateProductById(accessToken!!, id!!, name, description, basePrice, categoryIds, location, image).observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.SUCCESS -> {
                        hideLoading()
                        datastoreViewModel.saveMsgSnackbar("Produk berhasil diperbarui")
                        findNavController().navigate(R.id.action_navigation_jual_to_navigation_daftarjual)
                    }
                    Status.ERROR -> {
                        hideLoading()
                        showSnackbar(requireContext(), requireView(), it.message!!, R.color.danger)
                    }
                    Status.LOADING -> {
                        showLoading(requireActivity())
                    }
                }
            }
        }
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            val etNamaProduk = binding.editNamaProduk.text.toString()
            val etHargaProduk = binding.editHarga.text.toString()
            val etDeskripsi = binding.editDeskripsi.text.toString()
            val etKategori = binding.editKategori.text.toString()
            val etImage = binding.editImage.text.toString()

            binding.btnTerbitkan.isEnabled = etNamaProduk.isNotEmpty() && etHargaProduk.isNotEmpty() && etKategori.isNotEmpty() && etDeskripsi.isNotEmpty() && etImage.isNotEmpty()
            binding.btnPreview.isEnabled = etNamaProduk.isNotEmpty() && etHargaProduk.isNotEmpty() && etKategori.isNotEmpty() && etDeskripsi.isNotEmpty() && etImage.isNotEmpty()
        }

        override fun afterTextChanged(p0: Editable?) {}
    }

    private fun prepareFilePart(fileUri: Uri): MultipartBody.Part {
        val file = File(fileUri.path)
        Log.i("PATH IMAGE", file.absolutePath)
        // Create RequestBody instance from file
        val requestFile: RequestBody = file.asRequestBody("image/jpg".toMediaTypeOrNull())

        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData("image", file.name, requestFile)
    }

    private fun backButtonOnPressed() {
        binding.backBtn.setOnClickListener {
            it.findNavController().popBackStack()
        }
    }
}