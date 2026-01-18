package com.example.bikeapp.repositories

import com.example.bikeapp.db.Track
import com.example.bikeapp.db.TrackDao
import javax.inject.Inject


class MainRepository @Inject constructor(
    val trackDao: TrackDao
) {
    suspend fun insertTrack(track: Track) = trackDao.insertTrack(track)

    suspend fun deleteTrack(track: Track) = trackDao.deleteTrack(track)

    fun getAllTracksSortedByDate() = trackDao.getAllTracksSortedByDate()

    fun getAllTracksSortedByDistance() = trackDao.getAllTracksSortedByDistance()

    fun getAllTracksSortedByTimeInMillis() = trackDao.getAllTracksSortedByTimeInMillis()

    fun getAllTracksSortedByCaloriesBurned() = trackDao.getAllTracksSortedByCaloriesBurned()

    fun getAllTracksSortedByAvgSpeed() = trackDao.getAllTracksSortedByAvgSpeed()

    fun getTotalCaloriesBurned() = trackDao.getTotalCaloriesBurned()

    fun getTotalDistance() = trackDao.getTotalDistance()

    fun getTotalAvgSpeed() = trackDao.getTotalAvgSpeed()

    fun getTotalTimeInMillis() = trackDao.getTotalTimeInMillis()
}