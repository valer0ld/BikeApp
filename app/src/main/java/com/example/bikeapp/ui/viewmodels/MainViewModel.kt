package com.example.bikeapp.ui.viewmodels

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bikeapp.db.Track
import com.example.bikeapp.other.SortType
import com.example.bikeapp.repositories.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val mainRepository: MainRepository
): ViewModel() {

    private val tracksSortedByDate = mainRepository.getAllTracksSortedByDate()
    private val tracksSortedByDistance = mainRepository.getAllTracksSortedByDistance()
    private val tracksSortedByCaloriesBurned = mainRepository.getAllTracksSortedByCaloriesBurned()
    private val tracksSortedByTimeInMillis = mainRepository.getAllTracksSortedByTimeInMillis()
    private val tracksSortedByAvgSpeed = mainRepository.getAllTracksSortedByAvgSpeed()

    val tracks = MediatorLiveData<List<Track>>()

    var sortType = SortType.DATE

    init {
        tracks.addSource(tracksSortedByDate) { result ->
            if(sortType == SortType.DATE) {
                result?.let { tracks.value = it }
            }
        }
        tracks.addSource(tracksSortedByAvgSpeed) { result ->
            if(sortType == SortType.AVG_SPEED) {
                result?.let { tracks.value = it }
            }
        }
        tracks.addSource(tracksSortedByCaloriesBurned) { result ->
            if(sortType == SortType.CALORIES_BURNED) {
                result?.let { tracks.value = it }
            }
        }
        tracks.addSource(tracksSortedByDistance) { result ->
            if(sortType == SortType.DISTANCE) {
                result?.let { tracks.value = it }
            }
        }
        tracks.addSource(tracksSortedByTimeInMillis) { result ->
            if(sortType == SortType.TIME) {
                result?.let { tracks.value = it }
            }
        }
    }

    fun sortTracks(sortType: SortType) = when(sortType) {
        SortType.DATE -> tracksSortedByDate.value?.let { tracks.value = it }
        SortType.TIME -> tracksSortedByTimeInMillis.value?.let { tracks.value = it }
        SortType.AVG_SPEED -> tracksSortedByAvgSpeed.value?.let { tracks.value = it }
        SortType.DISTANCE -> tracksSortedByDistance.value?.let { tracks.value = it }
        SortType.CALORIES_BURNED -> tracksSortedByCaloriesBurned.value?.let { tracks.value = it }
    }.also {
        this.sortType = sortType
    }

    fun insertTrack(track: Track) = viewModelScope.launch {
        mainRepository.insertTrack(track)
    }
}