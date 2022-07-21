package com.and2t2.secondhand.ui.uiwishlist

import androidx.lifecycle.Observer
import com.and2t2.secondhand.common.Resource
import com.and2t2.secondhand.data.remote.ApiClient
import com.and2t2.secondhand.domain.model.Wishlist
import com.and2t2.secondhand.domain.model.WishlistMapper
import com.and2t2.secondhand.domain.repository.WishlistRepo
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.check.checkModules
import org.koin.test.inject
import org.mockito.Mock
import org.mockito.Mockito

class WishlistViewModelTest : KoinTest{
    private val wishlistRepo : WishlistRepo by inject()
    private val wishlistViewModel : WishlistViewModel by inject()

    @Mock
    lateinit var observer : Observer<Resource<List<Wishlist>>>
    @Mock
    lateinit var wishlist: List<Wishlist>

    val moduleWishlist = module {
        single { ApiClient.instanceBuyer}
        single { WishlistMapper()}
        single { WishlistRepo(get(),get()) }
        viewModel { WishlistViewModel(get()) }
    }
    @Before
    fun setUp() {
        startKoin {
            modules(listOf(moduleWishlist))
        }
    }

    @Test
    fun verifyKoinApp() {
        koinApplication {
            modules(listOf(moduleWishlist))
            checkModules()
        }
    }

    @Test
    fun getBuyerWishlist() = runBlocking {
        Mockito.`when`(wishlistRepo.getBuyerWishlist("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6ImFmaWZmdWppYXJhaG1hbjIwMDBAZ21haWwuY29tIiwiaWF0IjoxNjU4MjI4MDA1fQ.hJsLEsCzRJngk231JExyb4FPF3J1lPGajwjvxDaYv-4")).thenReturn(wishlist)
        val test = wishlistViewModel.getBuyerWishlist("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6ImFmaWZmdWppYXJhaG1hbjIwMDBAZ21haWwuY29tIiwiaWF0IjoxNjU4MjI4MDA1fQ.hJsLEsCzRJngk231JExyb4FPF3J1lPGajwjvxDaYv-4").observeForever(observer)

    }

    fun getKategori() = runBlocking {
//       Mockito.`when`(homeRepo.getKategori()).thenReturn(listOf(SellerCategory(0,"Semua")))
//        val kategori =  homeViewModel.getKategori().value
//        assertEquals(listOf(SellerCategory(0,"Semua")),kategori)
    }
}


