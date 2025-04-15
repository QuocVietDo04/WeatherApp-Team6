package com.nhom6.weatherapp.ui.search

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nhom6.weatherapp.data.remote.model.RemoteLocation
import com.nhom6.weatherapp.databinding.ItemSearchBinding

class SearchAdapter (
    private val onLocationClicked: (RemoteLocation) -> Unit
) : RecyclerView.Adapter<SearchAdapter.LocationViewHolder>() {

    private val locations = mutableListOf<RemoteLocation>()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<RemoteLocation>) {
        locations.clear()
        locations.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val binding = ItemSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LocationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        val remoteLocation = locations[position]
        holder.bind(remoteLocation)
    }

    override fun getItemCount(): Int {
        return locations.size
    }

    inner class LocationViewHolder(private val binding: ItemSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(remoteLocation: RemoteLocation) {
            with(remoteLocation) {
                val location = displayName
                binding.textRemoteLocation.text = location
                binding.root.setOnClickListener { onLocationClicked(remoteLocation) }
            }
        }
    }
}