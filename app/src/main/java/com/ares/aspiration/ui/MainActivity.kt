package com.ares.aspiration.ui

import android.content.DialogInterface
import android.content.Intent
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
import com.ares.aspiration.data.entity.Symbol
import com.ares.aspiration.data.entity.SymbolsAttributes
import com.ares.aspiration.data.entity.SymbolsSuccess
import com.ares.aspiration.databinding.ActivityMainBinding
import com.ares.aspiration.di.DaggerMainComponent
import com.ares.aspiration.di.module.FixerModule
import com.ares.aspiration.ui.iviews.FixerItem
import com.ares.aspiration.viewmodel.FixerViewModel
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Section
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: FixerViewModel
    private val groupAdapter = GroupAdapter<GroupieViewHolder>()
    private lateinit var symbol: Symbol
    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initInjector()

        viewModel = viewModelProvider(viewModelFactory)
        viewModel.loadSymbols()

        initLiveData()
        initListeners()
    }

    private fun openLatestRatesScreen(currency: String) {
        val i = Intent(this@MainActivity, LatestRatesActivity::class.java)
        i.putExtra(CURRENCY, currency)
        startActivity(i)
    }

    private fun initLiveData() {
        viewModel.error.observe(this, Observer { error ->
            showDialogAlert(error, { showErrorLabel() }, this)
            binding.editTextSearch.isEnabled = false
        })

        viewModel.symbols.observe(this, Observer { symbol ->
            this.symbol = symbol
            loadViews()
        })

        viewModel.state.observe(this, Observer {
            when (it) {
                is LoaderState.ShowLoading -> binding.progressBar.isVisible = true
                is LoaderState.HideLoading -> binding.progressBar.isVisible = false
            }
        })
    }

    private fun loadViews() {
        groupAdapter += Section().apply {
            getSymbolsData(symbol).list.forEach { symbol ->
                add(FixerItem(symbol.currencyCode, symbol.countryName, { openLatestRatesScreen(it) }, SOURCE.SYMBOLS.name))
            }
        }

        val layoutManagerS = GridLayoutManager(this, groupAdapter.spanCount).apply {
            spanSizeLookup = groupAdapter.spanSizeLookup
        }

        binding.symbolsList.apply {
            layoutManager = layoutManagerS
            adapter = groupAdapter
        }
    }

    private fun initListeners() {
        binding.editTextSearch.addTextChangedListener { textToFind ->
            if (textToFind.isNullOrEmpty()) {
                binding.imageViewClear.isVisible = false
            } else  {
                binding.imageViewClear.isVisible = true
            }
            filterCurrency(getSymbolsData(symbol).list.distinctBy { it.currencyCode }.filter {
                groupAdapter.clear()
                it.currencyCode.startsWith(textToFind.toString().trim(), true)
            })
        }

        binding.imageViewClear.setOnClickListener {
            binding.editTextSearch.setText("")
        }
    }

    private fun filterCurrency(symbols: List<SymbolsAttributes>) {
        symbols.forEach {
            groupAdapter += Section().apply {
                add(FixerItem(it.currencyCode, it.countryName, { openLatestRatesScreen(it) }, SOURCE.SYMBOLS.name))
            }

            val layoutManagerS = GridLayoutManager(this, groupAdapter.spanCount).apply {
                spanSizeLookup = groupAdapter.spanSizeLookup
            }

            binding.symbolsList.apply {
                layoutManager = layoutManagerS
                adapter = groupAdapter
            }
        }
    }

    private fun initInjector() {
        DaggerMainComponent
            .builder()
            .fixerModule(FixerModule())
            .build()
            .injectMain(this)
    }

    private fun getSymbolsData(body: Symbol): SymbolsSuccess {
        val entrySet = body.symbols.entrySet()
        val list = entrySet.map {
            SymbolsAttributes(it.key, it.value.asString)
        }
        return SymbolsSuccess(list)
    }

    override fun onPause() {
        super.onPause()
        compositeDisposable.clear()
    }

    private fun showErrorLabel() {
        binding.textViewError.isVisible = true
    }
}