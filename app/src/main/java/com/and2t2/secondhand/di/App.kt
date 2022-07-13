package com.and2t2.secondhand.di

import android.app.Application
import com.and2t2.secondhand.data.local.DatabaseSecondHand
import com.and2t2.secondhand.data.remote.ApiClient
import com.and2t2.secondhand.domain.model.*
import com.and2t2.secondhand.domain.repository.*
import com.and2t2.secondhand.ui.uibuyer.BuyerViewModel
import com.and2t2.secondhand.ui.uihome.HomeViewModel
import com.and2t2.secondhand.ui.uilogin.LoginViewModel
import com.and2t2.secondhand.ui.uinotifikasi.NotifikasiViewModel
import com.and2t2.secondhand.ui.uiseller.uiinfopenawar.InfoPenawarViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin()
    }

    private fun initKoin(){
        startKoin{
            androidContext(this@App)
            modules(listOf(databaseModule,serviceModule,mapperModule,repoModule,viewModelModule))
        }
    }

    private val databaseModule = module {
        single{ DatabaseSecondHand.getInstance(androidContext())}
    }
    private val serviceModule = module {
        single { ApiClient.instanceSeller}
        single { ApiClient.instanceBuyer}
        single { ApiClient.instanceNotifikasi}
        single { ApiClient.INSTANCE_AUTH}
    }

    private val mapperModule = module {
        single { SellerProductMapper()}
        single { SellerOrderMapper()}
        single { SellerCategoryMapper()}
        single { BuyerProductMapper()}
        single { BuyerProductDetailMapper() }
        single { NotifikasiMapper()}
        single { AuthUserMapper()}
    }

    private val repoModule = module {
        single { DatastoreManager(get())}
        single { SellerRepo(get(),get(),get(),get(),get())}
        single { HomeRepo(get(),get(),get(),get(),get())}
        single { BuyerRepo(get(),get(),get())}
        single { NotifikasiRepo(get(),get(),get())}
        single { AuthRepo(get(),get(),get())}
    }

    private val viewModelModule = module{
        viewModel { DatastoreViewModel(get()) }
        viewModel { InfoPenawarViewModel(get()) }
        viewModel { HomeViewModel(get()) }
        viewModel { BuyerViewModel(get()) }
        viewModel { NotifikasiViewModel(get()) }
        viewModel { LoginViewModel(get()) }
    }

}