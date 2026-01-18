package com.example.bikeapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.bikeapp.R
import com.example.bikeapp.db.Track
import com.example.bikeapp.other.TrackingUtility
import com.bumptech.glide.Glide
import com.example.bikeapp.databinding.ItemTrackBinding
import java.text.SimpleDateFormat
import java.util.*

class TrackAdapter : RecyclerView.Adapter<TrackAdapter.TrackViewHolder>() {


    inner class TrackViewHolder(val binding: ItemTrackBinding) : RecyclerView.ViewHolder(binding.root)

    val diffCallback = object : DiffUtil.ItemCallback<Track>() {
        override fun areItemsTheSame(oldItem: Track, newItem: Track): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Track, newItem: Track): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    fun submitList(list: List<Track>) = differ.submitList(list)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val binding = ItemTrackBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TrackViewHolder(binding)
    }

    override fun getItemCount(): Int {
       return differ.currentList.size
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = differ.currentList[position]
        val binding = holder.binding

        Glide.with(binding.root).load(track.img).into(binding.ivRunImage)

        val calendar = Calendar.getInstance().apply {
            timeInMillis = track.timestamp
        }
        val dateFormat = SimpleDateFormat("dd.MM.yy", Locale.getDefault())
        binding.tvDate.text = dateFormat.format(calendar.time)

        val avgSpeed = "${track.avgSpeedInKMH}km/h"
        binding.tvAvgSpeed.text = avgSpeed

        val distanceInKm = "${track.distanceInMeters / 1000f}km"
        binding.tvDistance.text = distanceInKm

        binding.tvTime.text = TrackingUtility.getFormattedStopWatchTime(track.timeInMillis)

        val caloriesBurned = "${track.caloriesBurned}kcal"
        binding.tvCalories.text = caloriesBurned
    }
}














