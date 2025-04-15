package com.nhom6.weatherapp.ui.location//package com.nhom6.weatherapp.ui.savedlocation
//
//import android.annotation.SuppressLint
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import androidx.recyclerview.widget.RecyclerView
//import com.nhom6.weatherapp.R
//import com.nhom6.weatherapp.data.remote.model.RemoteLocation
//import com.nhom6.weatherapp.databinding.ItemLocationBinding
//
//class LocationAdapter (
//    private val onLocationClicked: (RemoteLocation) -> Unit
//) : RecyclerView.Adapter<LocationAdapter.LocationViewHolder>() {
//
//    private val locations = mutableListOf<RemoteLocation>()
//
//    @SuppressLint("NotifyDataSetChanged")
//    fun setData(data: List<RemoteLocation>) {
//        locations.clear()
//        locations.addAll(data)
//        notifyDataSetChanged()
//    }
//
//    // Tạo ViewHolder cho item
//    inner class LocationViewHolder(private val binding: ItemLocationBinding) :
//        RecyclerView.ViewHolder(binding.root) {
//
//        fun bind(remoteLocation: RemoteLocation) {
//            with(remoteLocation) {
//                val name = "$name, $region"
//                val country = "{$country}"
//                binding.textRemoteLocation.text = location
//                binding.root.setOnClickListener { onLocationClicked(remoteLocation) }
//            }
//        }
//    }
//
//    // Cung cấp số lượng item trong danh sách
//    override fun getItemCount(): Int = 9 // Tạm thời hiển thị 9 item
//
//    // Tạo ViewHolder từ layout item_location
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_location, parent, false)
//        return LocationViewHolder(view)
//    }
//
//    // Gán dữ liệu cho mỗi item
//    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
//        val itemView = holder.itemView
//
//        // Thiết lập nội dung cho mỗi mục trong RecyclerView
//        itemView.textCity.text = "City $position"
//        itemView.textCountry.text = "Country $position"
//        itemView.imageIcon.setImageResource(R.drawable.ic_launcher_background)
//    }
//}
