package com.example.bikeapp.ui.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bikeapp.R
import com.example.bikeapp.adapters.TrackAdapter
import com.example.bikeapp.databinding.FragmentTrackBinding
import com.example.bikeapp.other.Constants.REQUEST_CODE_BACKGROUND
import com.example.bikeapp.other.Constants.REQUEST_CODE_LOCATION
import com.example.bikeapp.other.Constants.REQUEST_CODE_NOTIFICATIONS
import com.example.bikeapp.other.SortType
import com.example.bikeapp.ui.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

@AndroidEntryPoint
class TrackFragment : Fragment(R.layout.fragment_track), EasyPermissions.PermissionCallbacks{

    private var _binding: FragmentTrackBinding? = null
    private val binding get() = _binding!!

    private lateinit var trackAdapter: TrackAdapter

    private val viewModel: MainViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentTrackBinding.bind(view)

        requestPermissionsStepByStep()
        setupRecyclerView()

        when(viewModel.sortType) {
            SortType.DATE -> binding.spFilter.setSelection(0)
            SortType.TIME -> binding.spFilter.setSelection(1)
            SortType.DISTANCE -> binding.spFilter.setSelection(2)
            SortType.AVG_SPEED -> binding.spFilter.setSelection(3)
            SortType.CALORIES_BURNED -> binding.spFilter.setSelection(4)
        }

        binding.spFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}

            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                when(pos) {
                    0 -> viewModel.sortTracks(SortType.DATE)
                    1 -> viewModel.sortTracks(SortType.TIME)
                    2 -> viewModel.sortTracks(SortType.DISTANCE)
                    3 -> viewModel.sortTracks(SortType.AVG_SPEED)
                    4 -> viewModel.sortTracks(SortType.CALORIES_BURNED)
                }
            }
        }

        viewModel.tracks.observe(viewLifecycleOwner, Observer {
            trackAdapter.submitList(it)
        })

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_trackFragment_to_trackingFragment)
        }
    }

    private fun setupRecyclerView() = binding.rvRuns.apply {
        trackAdapter = TrackAdapter()
        adapter = trackAdapter
        layoutManager = LinearLayoutManager(requireContext())
    }

    private fun requestPermissionsStepByStep() {
        when {
            !hasFineAndCoarse() -> {
                EasyPermissions.requestPermissions(
                    this,
                    "Это приложение требует доступ к вашему местоположению",
                    REQUEST_CODE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            }

            Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q &&
                    ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED -> {
                EasyPermissions.requestPermissions(
                    this,
                    "Это приложение требует доступ к вашему местоположению в фоновом режиме",
                    REQUEST_CODE_BACKGROUND,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                )
            }

            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                    ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED -> {
                EasyPermissions.requestPermissions(
                    this,
                    "Это приложение требует разрешение на отправку уведомлений",
                    REQUEST_CODE_NOTIFICATIONS,
                    Manifest.permission.POST_NOTIFICATIONS
                )
            }
        }
    }

    private fun hasFineAndCoarse(): Boolean {
        return EasyPermissions.hasPermissions(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        when (requestCode) {
            REQUEST_CODE_LOCATION -> {
                requestPermissionsStepByStep()
            }
            REQUEST_CODE_BACKGROUND -> {
                requestPermissionsStepByStep()
            }
            REQUEST_CODE_NOTIFICATIONS -> {
                requestPermissionsStepByStep()
            }
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        } else {
            requestPermissionsStepByStep()
        }
    }
}