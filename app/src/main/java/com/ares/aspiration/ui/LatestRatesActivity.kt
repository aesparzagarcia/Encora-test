package com.ares.aspiration.ui

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.ares.aspiration.abstraction.state.LoaderState
import com.ares.aspiration.abstraction.util.*
import com.ares.aspiration.abstraction.util.Constants.CURRENCY
import com.ares.aspiration.data.entity.*
import com.ares.aspiration.databinding.ActivityLatestRatesBinding
import com.ares.aspiration.di.DaggerMainComponent
import com.ares.aspiration.di.module.FixerModule
import com.ares.aspiration.ui.iviews.FixerItem
import com.ares.aspiration.viewmodel.LatestRatesViewModel
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Section
import javax.inject.Inject

class LatestRatesActivity: AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: LatestRatesViewModel
    private lateinit var binding: ActivityLatestRatesBinding
    private lateinit var currency: String
    private lateinit var latest: Latest
    private val groupAdapter = GroupAdapter<GroupieViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLatestRatesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        currency = intent.getStringExtra(CURRENCY) ?: ""

        initInjector()

        viewModel = viewModelProvider(viewModelFactory)

        initLiveData()

        viewModel.loadLatest(currency)

        initListeners()
    }

    private fun initLiveData() {
        viewModel.latest.observe(this, Observer { latest ->
            this.latest = latest
            binding.editTextCurrency.hint = "Current base ${latest.base}"
            drawList()
        })

        viewModel.error.observe(this, Observer { error ->
            showDialogAlert(error, { showErrorLabel() }, this)
            binding.editTextCurrency.isEnabled = false
            binding.editTextCurrency.hint = error
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
                drawList(it.toString().toInt())
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

    private fun showErrorLabel() {
        binding.textViewError.isVisible = true
    }
}