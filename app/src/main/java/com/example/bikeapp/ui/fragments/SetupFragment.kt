package com.example.bikeapp.ui.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.bikeapp.R
import com.example.bikeapp.databinding.FragmentSetupBinding
import com.example.bikeapp.other.Constants.KEY_FIRST_TIME_TOGGLE
import com.example.bikeapp.other.Constants.KEY_NAME
import com.example.bikeapp.other.Constants.KEY_WEIGHT
import com.example.bikeapp.ui.MainActivity
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SetupFragment : Fragment(R.layout.fragment_setup){

    private var _binding: FragmentSetupBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var sharedPref: SharedPreferences

    @set:Inject
    var isFirstAppOpen = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentSetupBinding.bind(view)

        if(!isFirstAppOpen) {
            val navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.setupFragment, true)
                .build()
            findNavController().navigate(
                R.id.action_setupFragment_to_trackFragment,
                savedInstanceState,
                navOptions
            )
        }

        binding.btnContinue.setOnClickListener {
            val success = writePersonalDataToSharedPref()
            if(success) {
                val navOptions = NavOptions.Builder()
                    .setPopUpTo(R.id.setupFragment, true)
                    .build()
                findNavController().navigate(R.id.action_setupFragment_to_trackFragment, null, navOptions)
            } else {
                Snackbar.make(requireView(), "Please enter all the fields", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun writePersonalDataToSharedPref(): Boolean {
        val name = binding.etName.text.toString()
        val weight = binding.etWeight.text.toString()
        if(name.isEmpty() || weight.isEmpty()) {
            return false
        }
        sharedPref.edit()
            .putString(KEY_NAME, name)
            .putFloat(KEY_WEIGHT, weight.toFloat())
            .putBoolean(KEY_FIRST_TIME_TOGGLE, false)
            .apply()
        val toolbarText = "Let's go, $name!"
        (requireActivity() as MainActivity).updateToolbarTitle(toolbarText)
        return true
    }
}