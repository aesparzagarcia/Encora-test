package com.ares.aspiration.viewModels

import com.ares.aspiration.data.domain.FixerUseCase
import com.ares.aspiration.data.entity.Latest
import com.ares.aspiration.data.entity.LatestSuccess
import com.ares.aspiration.data.entity.RatesAttributes
import com.ares.aspiration.util.JsonResource
import com.ares.aspiration.util.TestSchedulerProvider
import com.ares.aspiration.viewmodel.LatestRatesViewModel
import com.google.gson.Gson
import io.reactivex.Flowable
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import junit.framework.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@RunWith(JUnit4::class)
class LatestRatesViewModelTest {

    private lateinit var  viewModel: LatestRatesViewModel

    @Mock
    private lateinit var useCase: FixerUseCase

    private lateinit var response: String
    private lateinit var collection: Latest

    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        response = JsonResource.fromResource("latest.json")
        collection = Gson().fromJson(response, Latest::class.java)
    }

    private fun initViewModel() {
        viewModel = LatestRatesViewModel(useCase, TestSchedulerProvider())
    }

    @Test
    fun `collection should be not null`() {
        initViewModel()
        assertNotNull(collection)
    }

    @Test
    fun `load latest success`() {
        initViewModel()

        `when`(useCase.getLatest("EUR")).thenReturn(Flowable.just(collection))

        viewModel.loadLatest("EUR")

        val latest = getLatestRatesData()

        assertTrue(collection.success)
        assertEquals(latest.list[0].countryCode, "AED")
        assertEquals(latest.list[0].quantity, 4.050158)
    }

    private fun getLatestRatesData(): LatestSuccess {
        val entrySet = collection.rates.entrySet()
        val list = entrySet.map {
            RatesAttributes(it.key, it.value.asDouble)
        }
        return LatestSuccess(list)
    }
}