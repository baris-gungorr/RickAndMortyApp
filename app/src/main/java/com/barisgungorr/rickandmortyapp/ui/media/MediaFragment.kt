package com.barisgungorr.rickandmortyapp.ui.media

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.barisgungorr.rickandmortyapp.R
import com.barisgungorr.rickandmortyapp.databinding.FragmentDetailBinding
import com.barisgungorr.rickandmortyapp.databinding.FragmentMediaBinding
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import java.net.URL


class MediaFragment : Fragment() {
    private lateinit var binding: FragmentMediaBinding
    private var exoPlayer: ExoPlayer? = null
    private var playbackPosition: Long = 0
    private var playWhenReady = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        binding = FragmentMediaBinding.inflate(inflater, container, false)
        preparePlayer()
        goToHomeFragment()
        return binding.root
    }

    private fun preparePlayer() {
        exoPlayer = ExoPlayer.Builder(requireContext()).build()
        exoPlayer?.playWhenReady = true
        binding.playerView.player = exoPlayer

        val uri = "android.resource://" + requireContext().packageName + "/" + R.raw.media
        val mediaItem = MediaItem.fromUri(uri)
        val mediaSource = DefaultMediaSourceFactory(requireContext()).createMediaSource(mediaItem)

        exoPlayer?.setMediaSource(mediaSource)
        exoPlayer?.seekTo(playbackPosition)
        exoPlayer?.playWhenReady = playWhenReady
        exoPlayer?.prepare()
    }

    private fun releasePlayer(){
        exoPlayer?.let { player ->
            playbackPosition = player.currentPosition
            playWhenReady = player.playWhenReady
            player.release()
            exoPlayer = null
        }
    }

    override fun onStop() {
        super.onStop()
        releasePlayer()
    }

    override fun onPause() {
        super.onPause()
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        releasePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        releasePlayer()
    }

  private fun goToHomeFragment(){
      binding.ivMediaToHome.setOnClickListener {
          findNavController().navigate(R.id.actionMediaToHome)
      }
    }
}