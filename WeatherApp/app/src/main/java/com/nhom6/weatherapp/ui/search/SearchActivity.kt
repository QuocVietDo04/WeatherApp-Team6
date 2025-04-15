package com.nhom6.weatherapp.ui.search

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.doquocviet.weatherapp.storage.SharedPreferencesManager
import com.nhom6.weatherapp.data.CurrentLocation
import com.nhom6.weatherapp.data.remote.model.RemoteLocation
import com.nhom6.weatherapp.databinding.ActivitySearchBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

class SearchActivity : AppCompatActivity() {

    private var _binding: ActivitySearchBinding? = null
    private val binding get() = requireNotNull(_binding)

    private var searchJob: Job? = null

    private val searchViewModel: SearchViewModel by viewModel()

    private val searchAdapter = SearchAdapter(
        onLocationClicked = { remoteLocation ->
            setLocation(remoteLocation)
        }
    )

    // SharedPreferencesManager để lưu trữ và truy xuất dữ liệu
    private val sharedPreferencesManager: SharedPreferencesManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setListeners()
        setupSearchRecyclerView()
        setObservers()
    }

    private fun setupSearchRecyclerView() {
        with(binding.searchRecyclerView) {
            addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
            adapter = searchAdapter
        }
    }

    private fun setListeners() {
        binding.btnCancel.setOnClickListener { finish() }

        binding.inputSearch.editText?.doOnTextChanged { text, _, _, _ ->
            searchJob?.cancel()
            searchJob = lifecycleScope.launch {
                delay(200)
                val query = text.toString()
                if (query.isNotBlank()) {
                    searchLocation(query)
                }
            }
        }

        binding.inputSearch.editText?.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                hideSoftKeyboard()
                val query = binding.inputSearch.editText?.text
                if (!query.isNullOrBlank()) {
                    searchLocation(query.toString())
                }
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }

    private fun setLocation(remoteLocation: RemoteLocation) {
        val currentLocation = CurrentLocation(
            osmId = remoteLocation.osmId,
            location = remoteLocation.name,
            latitude = remoteLocation.lat,
            longitude = remoteLocation.lon
        )
        sharedPreferencesManager.saveCurrentLocation(currentLocation)

        // Lưu danh sách vị trí vào SharedPreferences
        val locationList = sharedPreferencesManager.getLocationList()?.toMutableList() ?: mutableListOf()
        if (locationList.none { it.location == currentLocation.location }) {
            locationList.add(currentLocation)
            sharedPreferencesManager.saveLocationList(locationList)
        }

        finish()
    }

    private fun setObservers() {
        searchViewModel.searchResult.observe(this) { searchResultDataState ->
            if (searchResultDataState == null) return@observe

            if (searchResultDataState.isLoading) {
                binding.searchRecyclerView.visibility = View.GONE
            }

            searchResultDataState.locations?.let { remoteLocations ->
                binding.searchRecyclerView.visibility = View.VISIBLE
                searchAdapter.setData(remoteLocations)
            }

            searchResultDataState.error?.let { error ->
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun searchLocation(query: String) {
        searchViewModel.searchLocation(query)
    }

    private fun hideSoftKeyboard() {
        val inputManager = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(binding.inputSearch.editText?.windowToken, 0)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}