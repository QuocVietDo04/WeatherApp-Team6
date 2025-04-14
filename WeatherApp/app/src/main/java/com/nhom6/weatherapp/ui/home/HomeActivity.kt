package com.nhom6.weatherapp.ui.home

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.doquocviet.weatherapp.storage.SharedPreferencesManager
import com.google.android.gms.location.LocationServices
import com.nhom6.weatherapp.R
import com.nhom6.weatherapp.data.CurrentLocation
import com.nhom6.weatherapp.databinding.ActivityHomeBinding
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

class HomeActivity : AppCompatActivity() {

    companion object {
        const val REQUEST_KEY_MANUAL_LOCATION_SEARCH = "manuallocationSearch"
        const val KEY_LOCATION_TEXT = "locationText"
        const val KEY_LATITUDE = "latitude"
        const val KEY_LONGITUDE = "longitude"
    }

    private var _binding: ActivityHomeBinding? = null
    private val binding get() = requireNotNull(_binding)

    private val homeViewModel: HomeViewModel by viewModel()

    private val homeAdapter by lazy {
        HomeAdapter(
            onLocationClicked = {  }
        )
    }

    private val sharedPreferencesManager: SharedPreferencesManager by inject()

    // FusedLocationProviderClient là một lớp trong Android cung cấp API để lấy vị trí của thiết bị
    private val fusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(this)
    }

    // Geocoder là một lớp trong Android dùng để chuyển đổi giữa địa chỉ và tọa độ địa lý (latitude, longitude)}
    private val geocoder by lazy { Geocoder(this) }


    // Kiểm tra quyền truy cập vị trí có được cấp hay không.
    private fun isLocationPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_FINE_LOCATION // Ở đây là quyền truy cập vị trí chi tiết của người dùng.
        ) == PackageManager.PERMISSION_GRANTED  // Kiểm tra xem quyền có được cấp (PERMISSION_GRANTED) hay không.
    }

    // Hàm lấy vị trí hiện tại của người dùng
    private fun getCurrentLocation() {
        homeViewModel.getCurrentLocation(fusedLocationProviderClient, geocoder)
    }

    // Hàm xử lý quyền truy cập vị trí
    private fun proceedWithLocationPermission() {
        if (isLocationPermissionGranted()) {
            // Đã được cấp → Lấy vị trí hiện tại
            getCurrentLocation()
        } else {
            // Chưa được cấp → Gọi hàm yêu cầu quyền
            showPermissionDialog()
        }
    }

    // Hàm hiển thị hộp thoại yêu cầu quyền truy cập vị trí
    private fun showPermissionDialog() {
        AlertDialog.Builder(this) // 'this' là context, dùng được trong Activity. Nếu trong Fragment thì dùng 'requireContext()'
            .setTitle("Yêu cầu quyền vị trí")
            .setMessage("Ứng dụng cần quyền truy cập vị trí để hiển thị thời tiết hiện tại.")
            .setPositiveButton("Cho phép") { _, _ ->
                requestLocationPermission()
            }
            .setNegativeButton("Từ chối", null)
            .show()
    }

    // Hàm dùng để gửi yêu cầu xin quyền truy cập vị trí từ người dùng
    private fun requestLocationPermission() {
        // Gọi đến biến locationPermissionLauncher (đã đăng ký từ trước),
        // sử dụng hàm launch(...) để yêu cầu cấp nhiều quyền cùng lúc
        locationPermissionLauncher.launch(
            // Truyền vào một mảng các quyền cần yêu cầu
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    // Biến để yêu cầu đăng ký nhiều quyền và xử lý kết quả trả về
    private val locationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions() // Sử dụng hợp đồng yêu cầu nhiều quyền
    ) { permissions -> // Đây là callback nhận kết quả sau khi yêu cầu quyền

        // Kiểm tra người dùng có cấp quyền truy cập vị trí chính xác không
        val fineLocationGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false

        // Kiểm tra người dùng có cấp quyền truy cập vị trí gần đúng không
        val coarseLocationGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false

        // Nếu một trong hai quyền được cấp → gọi hàm lấy vị trí hiện tại
        if (fineLocationGranted || coarseLocationGranted) {
            getCurrentLocation()
        } else {
            // Nếu cả hai quyền đều bị từ chối → hiển thị thông báo
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        proceedWithLocationPermission()


        // Thực hiện các thao tác sau khi view được tạo
        setWeatherDataAdapter()
        setObservers()
//        setListeners()
//        if (!isInitialLocationSet) {
//            setCurrentLocation(sharedPreferencesManager.getCurrentLocation())
//            isInitialLocationSet = true
//        }
    }

    private fun setObservers() {
        with(homeViewModel) {
            currentLocation.observe(this@HomeActivity) {
                val currentLocationDataState = it.getContentIfNotHandled() ?: return@observe

                if (currentLocationDataState.isLoading) {
                    showLoading()
                }

                currentLocationDataState.currentLocation?.let { currentLocation ->
                    hideLoading()
//                    sharedPreferencesManager.saveCurrentLocation(currentLocation)
                    setCurrentLocation(currentLocation)
                }

                currentLocationDataState.error?.let { error ->
                    hideLoading()
                    Toast.makeText(this@HomeActivity, error, Toast.LENGTH_SHORT).show()
                }
            }
//            weatherData.observe(this@HomeActivity) {
//                val weatherDataState = it.getContentIfNotHandled() ?: return@observe
//                binding.swipeRefreshLayout.isRefreshing = weatherDataState.isLoading
//
//                weatherDataState.currentWeather?.let { currentWeather ->
//                    weatherDataAdapter.setCurrentWeather(currentWeather)
//                }
//
//                weatherDataState.forecast?.let { forecast ->
//                    weatherDataAdapter.setForecastData(forecast)
//                }
//
//                weatherDataState.error?.let { error ->
//                    Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
//                }
//            }
        }
    }
    private fun setWeatherDataAdapter() {
        binding.weatherDataRecyclerView.itemAnimator = null
        binding.weatherDataRecyclerView.adapter = homeAdapter
    }

    private fun showLoading() {
        with(binding) {
            weatherDataRecyclerView.visibility = View.GONE
            swipeRefreshLayout.isEnabled = false
            swipeRefreshLayout.isRefreshing = true
        }
    }

    private fun hideLoading() {
        with(binding) {
            weatherDataRecyclerView.visibility = View.VISIBLE
            swipeRefreshLayout.isEnabled = true
            swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun setCurrentLocation(currentLocation: CurrentLocation? = null) {
        homeAdapter.setCurrentLocation(currentLocation ?: CurrentLocation())
//        currentLocation?.let { getWeatherData(it) }
    }

}