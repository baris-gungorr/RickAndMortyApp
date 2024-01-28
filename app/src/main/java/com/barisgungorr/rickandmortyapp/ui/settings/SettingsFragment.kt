package com.barisgungorr.rickandmortyapp.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.barisgungorr.rickandmortyapp.R
import com.barisgungorr.rickandmortyapp.databinding.FragmentSettingsBinding
import com.barisgungorr.rickandmortyapp.ui.activity.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale


@AndroidEntryPoint
class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLanguage(viewModel.getLanguage())
    }

    private fun setLanguage(language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)

        val config = resources.configuration
        config.setLocale(locale)

        resources.updateConfiguration(config, resources.displayMetrics)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTransition()
        setupThemeMode()
        setupLanguageSelection()
    }

    private fun setupTransition() {
        binding.ivGoToHomeF.setOnClickListener {
            findNavController().navigate(R.id.actionSettingsToMain)
        }
    }

    private fun setupThemeMode() {
        val isNightMode = viewModel.getNightMode()
        binding.switchMode.isChecked = isNightMode
        binding.switchMode.setOnClickListener {
            if (isNightMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                viewModel.setNightMode(false)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                viewModel.setNightMode(true)
            }
        }
    }

    private fun setupLanguageSelection() {
        val language = viewModel.getLanguage()
        binding.btnLanguage.text = language
        binding.btnLanguage.setOnClickListener {
            val popup = PopupMenu(requireContext(), it)
            popup.menuInflater.inflate(R.menu.popup_menu, popup.menu)

            val prefLanguage = viewModel.getLanguage()
            popup.menu.findItem(R.id.action_turkish).isChecked = prefLanguage == "en"
            popup.menu.findItem(R.id.action_english).isChecked = prefLanguage == "tr"
            popup.show()

            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.action_turkish -> {
                        viewModel.setLanguage("tr")
                        restartActivity()
                        true
                    }

                    R.id.action_english -> {
                        viewModel.setLanguage("en")
                        restartActivity()
                        true
                    }
                    else -> false
                }
            }
        }
    }

    private fun restartActivity() {
        val intent = Intent(requireContext(), MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish()
    }
}