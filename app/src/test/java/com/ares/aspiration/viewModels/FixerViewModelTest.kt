package com.ares.aspiration.viewModels

import com.ares.aspiration.data.domain.FixerUseCase
import com.ares.aspiration.data.entity.Symbol
import com.ares.aspiration.data.entity.SymbolsAttributes
import com.ares.aspiration.data.entity.SymbolsSuccess
import com.ares.aspiration.util.JsonResource
import com.ares.aspiration.util.TestSchedulerProvider
import com.ares.aspiration.viewmodel.FixerViewModel
import com.google.gson.Gson
import io.reactivex.Flowable
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@RunWith(JUnit4::class)
class FixerViewModelTest {

    private lateinit var  viewModel: FixerViewModel

    @Mock
    private lateinit var useCase: FixerUseCase

    private lateinit var response: String
    private lateinit var collection: Symbol

    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        response = JsonResource.fromResource("symbols.json")
        collection = Gson().fromJson(response, Symbol::class.java)
    }

    private fun initViewModel() {
        viewModel = FixerViewModel(useCase, TestSchedulerProvider())
    }

    @Test
    fun `collection should be not null`() {
        initViewModel()
        assertNotNull(collection)
    }

    @Test
    fun `load symbols`() {
        initViewModel()

        `when`(useCase.getSymbols()).thenReturn(Flowable.just(collection))

        viewModel.loadSymbols()

        val symbols = getSymbolsData()

        assertEquals(symbols.list[1].currencyCode, "AFN")
        assertEquals(symbols.list[1].countryName, "Afghan Afghani")
    }

    private fun getSymbolsData(): SymbolsSuccess {
        val entrySet = collection.symbols.entrySet()
        val list = entrySet.map {
            SymbolsAttributes(it.key, it.value.asString)
        }
        return SymbolsSuccess(list)
    }

}