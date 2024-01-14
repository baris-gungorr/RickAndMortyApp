package com.barisgungorr.rickandmortyapp.ui.settings

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.fragment.findNavController
import com.barisgungorr.rickandmortyapp.R
import com.barisgungorr.rickandmortyapp.databinding.FragmentFavoriteBinding
import com.barisgungorr.rickandmortyapp.databinding.FragmentSettingsBinding
import com.barisgungorr.rickandmortyapp.ui.activity.MainActivity
import java.util.Locale


class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        transition()
        themeMode()
        selectLanguage()
        return binding.root
    }

    private fun transition() {
        binding.ivHome.setOnClickListener {
            findNavController().navigate(R.id.actionSettingsToMain)
        }
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
            editor.commit()
        }
    }

    private fun selectLanguage() = with(binding) {
        btnLanguage.setOnClickListener{
            val popup = PopupMenu(requireContext(), btnLanguage)
            popup.menuInflater.inflate(R.menu.popup_menu, popup.menu)
            popup.show()

            popup.setOnMenuItemClickListener { item ->
                when(item.itemId) {
                    R.id.action_turkish -> {
                        val language = "tr"
                        val locale = Locale(language)
                        Locale.setDefault(locale)
                        val config = Configuration()
                        config.locale = locale
                        requireContext().resources.updateConfiguration(config, requireContext().resources.displayMetrics)
                        val intent = Intent(requireContext(), MainActivity::class.java)
                        startActivity(intent)
                        requireActivity().finish()
                        true
                    }
                    R.id.action_english -> {
                        val language = "en"
                        val locale = Locale(language)
                        Locale.setDefault(locale)
                        val config = Configuration()
                        config.locale = locale
                        requireContext().resources.updateConfiguration(config, requireContext().resources.displayMetrics)


                        val intent = Intent(requireContext(), MainActivity::class.java)
                        startActivity(intent)
                        requireActivity().finish()
                        true
                    }
                    else -> false
                }
            }
        }
    }
}