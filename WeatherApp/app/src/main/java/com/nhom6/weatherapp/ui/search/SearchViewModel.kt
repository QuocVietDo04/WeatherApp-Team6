package com.nhom6.weatherapp.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nhom6.weatherapp.data.remote.model.RemoteLocation
import com.nhom6.weatherapp.data.repository.LocationRepository
import kotlinx.coroutines.launch

// Lớp ViewModel này quản lý trạng thái tìm kiếm và dữ liệu trả về từ repository.
class SearchViewModel(private val locationRepository: LocationRepository) : ViewModel() {

    // MutableLiveData chứa trạng thái tìm kiếm. Được sử dụng để thông báo thay đổi UI.
    private val _searchResult = MutableLiveData<SearchResultDataState>()
    val searchResult: LiveData<SearchResultDataState> get() = _searchResult

    // Hàm tìm kiếm vị trí, gọi từ UI khi người dùng nhập vào query tìm kiếm.
    fun searchLocation(query: String) {
        // Chạy coroutine bất đồng bộ trong viewModelScope
        viewModelScope.launch {
            // Gửi trạng thái 'loading' đến UI khi bắt đầu tìm kiếm
            emitSearchResultUiState(isLoading = true)

            // Gọi repository để tìm kiếm vị trí
            val searchResult = locationRepository.searchLocation(query)

            // Kiểm tra nếu không có kết quả, thông báo lỗi cho UI
            if (searchResult.isNullOrEmpty()) {
                emitSearchResultUiState(error = "Location not found, please try again.")
            } else {
                // Nếu có kết quả, gửi danh sách các vị trí tìm thấy cho UI
                val filtered = searchResult.filter { it.osmType == "relation" }
                emitSearchResultUiState(locations = filtered)
            }
        }
    }

    // Hàm cập nhật trạng thái tìm kiếm vào LiveData (_searchResult)
    private fun emitSearchResultUiState(
        isLoading: Boolean = false, // Trạng thái loading
        locations: List<RemoteLocation>? = null, // Kết quả tìm kiếm
        error: String? = null // Thông báo lỗi nếu có
    ) {
        // Tạo đối tượng SearchResultDataState với các trạng thái cần thiết
        val searchResultDataState = SearchResultDataState(isLoading, locations, error)

        // Cập nhật giá trị của _searchResult, sẽ thông báo UI về sự thay đổi
        _searchResult.value = searchResultDataState
    }

    // Data class này dùng để lưu trữ trạng thái kết quả tìm kiếm: loading, kết quả tìm thấy, hoặc lỗi.
    data class SearchResultDataState(
        val isLoading: Boolean, // Cờ trạng thái cho biết có đang tải hay không
        val locations: List<RemoteLocation>?, // Danh sách vị trí tìm được
        val error: String? // Thông báo lỗi nếu có
    )
}
