package com.barisgungorr.rickandmortyapp.ui.onboarding

import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.barisgungorr.rickandmortyapp.R
import com.barisgungorr.rickandmortyapp.databinding.FragmentOnboardingBinding


class OnboardingFragment : Fragment() {
    private lateinit var binding: FragmentOnboardingBinding
    private lateinit var mediaPlayer: MediaPlayer
    private var musicPosition: Int = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOnboardingBinding.inflate(inflater, container, false)
        musicPlayer()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clickImage()
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


