package com.barisgungorr.rickandmortyapp.ui.splash

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import com.barisgungorr.rickandmortyapp.R
import com.barisgungorr.rickandmortyapp.databinding.FragmentSplashBinding


class SplashFragment : Fragment() {

    private lateinit var binding: FragmentSplashBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        themeMode()
    }

    private fun themeMode() {
        val switchMode = binding.switchMode

        sharedPreferences = requireContext().getSharedPreferences("MODE", Context.MODE_PRIVATE)

        val nightMode = sharedPreferences.getBoolean("nightMode", false)

        if (nightMode) {
            switchMode.isChecked = true
        }

        switchMode.setOnClickListener {
            if (nightMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                editor = sharedPreferences.edit()
                editor.putBoolean("nightMode", false)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                editor = sharedPreferences.edit()
                editor.putBoolean("nightMode", true)
            }
            editor.apply()
        }
    }
}