package com.and2t2.secondhand.ui.uihome

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.and2t2.secondhand.data.local.DatabaseSecondHand
import com.and2t2.secondhand.data.remote.ApiClient
import com.and2t2.secondhand.domain.model.*
import com.and2t2.secondhand.domain.repository.HomeRepo
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runner.manipulation.Ordering
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.check.checkModules
import org.koin.test.get
import org.koin.test.inject
import org.koin.test.mock.MockProviderRule
import org.koin.test.mock.declareMock
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations


class HomeViewModelTest : KoinTest{


    // Lazy inject property
//    private val homeViewModel : HomeViewModel by inject()
//    private val homeRepo : HomeRepo by inject()

    private val databaseModule = module {
        factory { DatabaseSecondHand.getInstance(androidContext())}
    }
    private val serviceModule = module {
        single { ApiClient.instanceSeller}
        single { ApiClient.instanceBuyer}
        single { ApiClient.instanceNotifikasi}
        single { ApiClient.INSTANCE_AUTH}
    }

    private val mapperModule = module {
        single { SellerProductMapper() }
        single { SellerOrderMapper() }
        single { SellerCategoryMapper()}
        single { BuyerProductMapper()}
        single { BuyerProductDetailMapper() }
        single { NotifikasiMapper() }
        single { AuthUserMapper() }
    }

    private val homeModule = module {
        single { HomeRepo(get(),get(),get(),get(),get()) }
        viewModel { HomeViewModel(get()) }
    }

//    @get:Rule
//    val koinTestRule = KoinTestRule.create {
//        // Your KoinApplication instance here
//        modules(homeModule,serviceModule,mapperModule,databaseModule)
//    }
    val context = mock(Context::class.java)

//    @Before
//    fun setup(){
//        MockitoAnnotations.openMocks(this)
//        startKoin{
//            androidContext(context)
//            modules(listOf(homeModule,serviceModule,mapperModule,databaseModule))
//        }
//    }

    @Test
    fun verifyKoinApp() {

        koinApplication {
            androidContext(context)
            modules(listOf(serviceModule,mapperModule,databaseModule))
            checkModules()
        }
    }



//    @Test
//    fun getKategori() = runBlocking {
//       Mockito.`when`(homeRepo.getKategori()).thenReturn(listOf(SellerCategory(0,"Semua")))
//        val kategori =  homeViewModel.getKategori().value
//        assertEquals(listOf(SellerCategory(0,"Semua")),kategori)
//    }

}