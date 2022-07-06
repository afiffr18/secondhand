package com.and2t2.secondhand.ui.uisplash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.and2t2.secondhand.AuthActivity
import com.and2t2.secondhand.MainActivity
import com.and2t2.secondhand.R
import com.and2t2.secondhand.databinding.ActivitySplashBinding
import com.and2t2.secondhand.domain.repository.DatastoreManager
import com.and2t2.secondhand.domain.repository.DatastoreViewModel

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private lateinit var handler: Handler

    private val pref: DatastoreManager by lazy { DatastoreManager(this) }
    private val datastoreViewModel: DatastoreViewModel by lazy { DatastoreViewModel(pref) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        checkLoginState()
    }

    private fun checkLoginState() {
        datastoreViewModel.getLoginState().observe(this) {
            if (it == true) {
                handler = Handler(Looper.getMainLooper())
                handler.postDelayed({
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }, 2000)
            } else {
                handler = Handler(Looper.getMainLooper())
                handler.postDelayed({
                    startActivity(Intent(this, AuthActivity::class.java))
                    finish()
                }, 2000) // 2 detik
            }
        }
    }
}