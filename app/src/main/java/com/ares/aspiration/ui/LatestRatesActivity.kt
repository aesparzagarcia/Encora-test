package com.ares.aspiration.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.ares.aspiration.R
import com.ares.aspiration.abstraction.state.LoaderState
import com.ares.aspiration.abstraction.util.SOURCE
import com.ares.aspiration.abstraction.util.plusAssign
import com.ares.aspiration.abstraction.util.showDialogAlert
import com.ares.aspiration.abstraction.util.showToast
import com.ares.aspiration.abstraction.util.viewModelProvider
import com.ares.aspiration.abstraction.util.Constants.BASE
import com.ares.aspiration.abstraction.util.Constants.CURRENCY_RESTRICTED_ERROR
import com.ares.aspiration.data.entity.Latest
import com.ares.aspiration.data.entity.LatestSuccess
import com.ares.aspiration.data.entity.RatesAttributes
import com.ares.aspiration.databinding.ActivityLatestRatesBinding
import com.ares.aspiration.di.DaggerMainComponent
import com.ares.aspiration.di.module.FixerModule
import com.ares.aspiration.ui.iviews.FixerItem
import com.ares.aspiration.viewmodel.LatestRatesViewModel
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Section
import java.lang.NumberFormatException
import javax.inject.Inject

class LatestRatesActivity: AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: LatestRatesViewModel
    private lateinit var binding: ActivityLatestRatesBinding
    private lateinit var base: String
    private lateinit var latest: Latest
    private val groupAdapter = GroupAdapter<GroupieViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLatestRatesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        base = intent.getStringExtra(BASE).orEmpty()

        initInjector()

        viewModel = viewModelProvider(viewModelFactory)

        initLiveData()

        viewModel.loadLatest(base)

        initListeners()
    }

    private fun initLiveData() {
        viewModel.latest.observe(this, Observer { latest ->
            this.latest = latest
            binding.editTextCurrency.hint = resources.getString(R.string.current_base, latest.base)
            drawList()
        })

        viewModel.errorCode.observe(this, Observer { error ->
            if (error == CURRENCY_RESTRICTED_ERROR) {
                showDialogAlert(resources.getString(R.string.free_api_error), this)
            } else {
                showDialogAlert(resources.getString(R.string.free_api_error), this)
            }
            binding.editTextCurrency.isEnabled = false
        })

        viewModel.error.observe(this, Observer { error ->
            showDialogAlert(error, this)
            binding.editTextCurrency.isEnabled = false
        })

        viewModel.state.observe(this, Observer {
            when (it) {
                is LoaderState.ShowLoading -> binding.progressBar.isVisible = true
                is LoaderState.HideLoading -> binding.progressBar.isVisible = false
            }
        })
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initListeners() {
        binding.editTextCurrency.addTextChangedListener {
            groupAdapter.clear()
            if (it.isNullOrBlank()) {
                binding.imageViewClear.isVisible = false
                drawList()
            } else {
                binding.imageViewClear.isVisible = true
                try {
                    drawList(Integer.parseInt(it.toString()))
                } catch (ex: NumberFormatException) {
                    showToast(resources.getString(R.string.max_input_exceeded))
                }
            }
        }

        binding.imageViewBack.setOnClickListener {
            finish()
        }

        binding.imageViewClear.setOnClickListener {
            binding.editTextCurrency.setText("")
        }
    }

    private fun drawList(quantity: Int = 1) {
        groupAdapter += Section().apply {
            getLatestRatesData(latest).list.forEach { latestRates ->
                val totalQuantity = latestRates.quantity * quantity
                add(FixerItem(latestRates.countryCode, totalQuantity.toString(), { }, SOURCE.LATEST.name))
            }

            val layoutManagerS = GridLayoutManager(this@LatestRatesActivity, groupAdapter.spanCount).apply {
                spanSizeLookup = groupAdapter.spanSizeLookup
            }

            binding.latestList.apply {
                layoutManager = layoutManagerS
                adapter = groupAdapter
            }
        }
    }

    private fun getLatestRatesData(body: Latest): LatestSuccess {
        val entrySet = body.rates.entrySet()
        val list = entrySet.map {
            RatesAttributes(it.key, it.value.asDouble)
        }
        return LatestSuccess(list)
    }

    private fun initInjector() {
        DaggerMainComponent
            .builder()
            .fixerModule(FixerModule())
            .build()
            .injectLatest(this)
    }
}