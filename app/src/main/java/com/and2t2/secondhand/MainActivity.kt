package com.and2t2.secondhand

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.and2t2.secondhand.common.viewModelsFactory
import com.and2t2.secondhand.databinding.ActivityMainBinding
import com.and2t2.secondhand.domain.repository.DatastoreManager
import com.and2t2.secondhand.domain.repository.DatastoreViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val pref: DatastoreManager by lazy { DatastoreManager(this) }
    private val datastoreViewModel: DatastoreViewModel by viewModelsFactory { DatastoreViewModel(pref) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setBottomNav()
    }

    private fun setBottomNav() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.navigation_notifikasi ||
                destination.id == R.id.navigation_jual ||
                destination.id == R.id.navigation_daftarjual ||
                destination.id == R.id.navigation_akun
            ) {
                datastoreViewModel.getLoginState().observe(this) {
                    if (!it) {
                        showAlertDialogWithAction()
//                        destination.id = R.id.test
                    }
                }
            }
            if (destination.id == R.id.profile ||
                destination.id == R.id.navigation_jual ||
                destination.id == R.id.previewProdukFragment ||
                destination.id == R.id.detail ||
                destination.id == R.id.buyerFragment ||
                destination.id == R.id.pengaturanAkunFragment
            ) {
                binding.bottomNavigationView.visibility = View.GONE
            } else {
                binding.bottomNavigationView.visibility = View.VISIBLE
            }
        }
        binding.bottomNavigationView.setupWithNavController(navController)
    }

    private fun showAlertDialogWithAction() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
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
}