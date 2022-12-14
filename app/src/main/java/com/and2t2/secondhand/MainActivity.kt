package com.and2t2.secondhand

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.and2t2.secondhand.databinding.ActivityMainBinding
import com.and2t2.secondhand.domain.repository.DatastoreViewModel
import com.and2t2.secondhand.ui.uiakun.AkunViewModel
import com.and2t2.secondhand.ui.uinotifikasi.NotifikasiViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val datastoreViewModel: DatastoreViewModel by viewModel()
    private val notifikasiViewModel : NotifikasiViewModel by viewModel()
    private val akunViewModel: AkunViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setBottomNav()
        observeData()
    }

    private fun observeData() {
        datastoreViewModel.getLoginState().observe(this) {
            if (it) {
                addNotifikasiBadge()
            }
        }
    }


    private fun setBottomNav() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.profile ||
                destination.id == R.id.navigation_jual ||
                destination.id == R.id.previewProdukFragment ||
                destination.id == R.id.detail ||
                destination.id == R.id.buyerFragment ||
                destination.id == R.id.pengaturanAkunFragment ||
                destination.id == R.id.wishlistFragment
            ) {
                binding.bottomNavigationView.visibility = View.GONE
            } else {
                binding.bottomNavigationView.visibility = View.VISIBLE
            }
            if (destination.id == R.id.navigation_notifikasi ||
                destination.id == R.id.navigation_jual ||
                destination.id == R.id.navigation_daftarjual ||
                destination.id == R.id.navigation_akun
            ) {
                datastoreViewModel.getLoginState().observe(this) {
                    if (!it) {
                        akunViewModel.deleteTable()
                        showAlertDialogWithAction()
                    }
                }
            }
        }
        binding.bottomNavigationView.setupWithNavController(navController)
    }


    private fun showAlertDialogWithAction() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_nav_host_fragment) as NavHostFragment
            val dialog = AlertDialog.Builder(this)
            dialog.setMessage("Anda harus login terlebih dahulu")
            dialog.setPositiveButton("Login") { dialogInterface, angka ->
                startActivity(Intent(this, AuthActivity::class.java))
            }
            dialog.setNegativeButton("Batal") { dialogInterface, _ ->
                navHostFragment.navController.popBackStack()
            }
            dialog.setCancelable(false)
            dialog.show()
    }

    private fun addNotifikasiBadge() {
        datastoreViewModel.getAccessToken().observe(this){ access_token ->
            notifikasiViewModel.getNotifikasi(access_token).observe(this){
                val data = it.data?.filter { data ->
                    data.read == false
                }?.size
                if (data != null && data != 0) {
                    val badge = binding.bottomNavigationView.getOrCreateBadge(R.id.navigation_notifikasi)
                    badge.isVisible = true
                    badge.number = data
                }else if(data == 0){
                    hideNotifikasiBadge()
                }


            }
        }
    }

    private fun hideNotifikasiBadge(){
        val badgeDrawable = binding.bottomNavigationView.getBadge(R.id.navigation_notifikasi)
        if (badgeDrawable != null) {
            badgeDrawable.isVisible = false
            badgeDrawable.clearNumber()
        }
    }
}