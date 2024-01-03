package com.barisgungorr.rickandmortyapp.ui.splash

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.barisgungorr.rickandmortyapp.databinding.FragmentSplashBinding
import android.media.MediaPlayer
import androidx.navigation.fragment.findNavController
import com.barisgungorr.rickandmortyapp.R


class SplashFragment : Fragment() {

    private lateinit var binding: FragmentSplashBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var mediaPlayer: MediaPlayer
    private var musicPosition: Int = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSplashBinding.inflate(inflater, container, false)
        musicPlayer()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        themeMode()
        clickImage()

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

    private fun musicPlayer() {
        if (!::mediaPlayer.isInitialized || !mediaPlayer.isPlaying) {
            mediaPlayer = MediaPlayer.create(requireContext(), R.raw.music)
            mediaPlayer.isLooping = true
            mediaPlayer.start()
        }
    }

    private fun clickImage() {
        binding.imageViewEyes.setOnClickListener {
            mediaPlayer.stop()
            pageTransition()
            playClickSound()
        }
    }

    private fun pageTransition() {
        findNavController().navigate(R.id.actionSplahToHomeFragment)
    }

    private fun playClickSound() {
        val clickSound = MediaPlayer.create(requireContext(), R.raw.mix)
        clickSound.start()

        clickSound.setOnCompletionListener { mp ->
            mp.release()
        }
    }

    override fun onPause() {
        super.onPause()
        mediaPlayer.pause()
        musicPosition = mediaPlayer.currentPosition
    }

    override fun onResume() {
        super.onResume()
        mediaPlayer.seekTo(musicPosition)
        mediaPlayer.start()
    }
}


