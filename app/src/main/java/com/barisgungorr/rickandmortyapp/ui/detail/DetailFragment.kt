package com.barisgungorr.rickandmortyapp.ui.detail

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.barisgungorr.rickandmortyapp.R
import com.barisgungorr.rickandmortyapp.data.dto.CharacterItem
import com.barisgungorr.rickandmortyapp.data.dto.Episodes
import com.barisgungorr.rickandmortyapp.databinding.FragmentDetailBinding
import com.barisgungorr.rickandmortyapp.ui.episode.EpisodeAdapter
import com.barisgungorr.rickandmortyapp.ui.episode.EpisodeViewModel
import com.barisgungorr.rickandmortyapp.ui.home.HomeViewModel
import com.barisgungorr.rickandmortyapp.util.extension.Url
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class DetailFragment : Fragment() {
    private val args: DetailFragmentArgs by navArgs()
    private val characterViewModel: HomeViewModel by viewModels()
    private val episodesViewModel: EpisodeViewModel by viewModels()
    private val viewModel: DetailViewModel by viewModels()
    private lateinit var adapter: EpisodeAdapter
    private lateinit var binding: FragmentDetailBinding
    private lateinit var characterItem: CharacterItem
    private lateinit var listEpisodes: Episodes


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        listEpisodes = Episodes()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    private fun init() {
        observe()
        searchCharacters()
    }

    private fun searchCharacters() {

       lifecycleScope.launch {
            characterViewModel.loadCharacterItemById(args.idCharacter.toString())
        }
    }

    private fun observe() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().navigate(DetailFragmentDirections.actionDetailToHomeFragment())
        }

        observeCharacterResponse()
        observeEpisodesListResponse()
        observeEpisodesResponse()
    }

    private fun observeCharacterResponse() {
        characterViewModel.characterItemResponse.observe(viewLifecycleOwner) {
            it?.body()?.let { body ->
                characterItem = body

                lifecycleScope.launch {
                        searchEpisodes(characterItem)
                        initViews()
                        binding.progressBar.isVisible = false

                }
            } ?: run {
                binding.progressBar.isVisible = true
            }
        }
    }

    private fun observeEpisodesListResponse() {
        episodesViewModel.episodesListResponse.observe(viewLifecycleOwner) { it ->
            it?.let {
                listEpisodes = it.body()!!
                setRecyclerViewEpisodes()
            }
        }
    }

    private fun observeEpisodesResponse() {
        episodesViewModel.episodesResponse.observe(viewLifecycleOwner) { it ->
            it?.let {
                listEpisodes.clear()
                listEpisodes.add(it.body()!!)
                setRecyclerViewEpisodes()
            }
        }
    }

    private fun searchEpisodes(characterItem: CharacterItem) {
        val episodes = Url.obtainNumFromURLs(characterItem.episode)

        if (episodes.contains(",")) {
            episodesViewModel.onCreateListEpisodes(episodes)
        } else {
            episodesViewModel.onCreateEpisode(episodes)
        }
    }


    private fun initViews()  = with(binding){
        characterItem.bindToUI()

            val scaleXAnimation = ObjectAnimator.ofFloat(ivDetailCharacter, "scaleX", 1.5f)
            val scaleYAnimation = ObjectAnimator.ofFloat(ivDetailCharacter, "scaleY", 1.5f).apply {
                duration = 1000
                repeatMode = ValueAnimator.REVERSE
                repeatCount = ValueAnimator.INFINITE
            }

            viewModel.startAnimation.observe(viewLifecycleOwner) {
                scaleXAnimation.start()
                scaleYAnimation.start()
            }

            viewModel.stopAnimation.observe(viewLifecycleOwner) {
                scaleXAnimation.cancel()
                scaleYAnimation.cancel()
                ivDetailCharacter.scaleX = 1f
                ivDetailCharacter.scaleY = 1f
            }

            ivDetailCharacter.setOnClickListener {
                viewModel.startAnimation()
            }

            root.setOnClickListener {
                viewModel.stopAnimation()
            }

            btnMedia.setOnClickListener {
                findNavController().navigate(R.id.actionDetailsToMedia)
            }
        }
        private fun CharacterItem.bindToUI() {
            binding.apply {
                Glide.with(ivDetailCharacter.context)
                    .load(image)
                    .into(ivDetailCharacter)

                tvDetailName.text = name
                tvDetailStatus.text = status
                tvDetailSpecies.text = species
                tvDetailLocation.text = location.name
                tvDetailGender.text = gender

                when (status.lowercase()) {
                    "alive" -> ivDetailAlive.background = ContextCompat.getDrawable(requireContext(), R.drawable.baseline_alive)
                    "dead" -> ivDetailAlive.background = ContextCompat.getDrawable(requireContext(), R.drawable.baseline_dead)
                    "unknown" -> ivDetailAlive.background = ContextCompat.getDrawable(requireContext(), R.drawable.baseline_unknown)
                }
            }
        }

    private fun setRecyclerViewEpisodes() = with(binding) {
        adapter = EpisodeAdapter()
        rvEpisodes.layoutManager = LinearLayoutManager(activity)
        rvEpisodes.adapter = adapter
        adapter.submitList(listEpisodes)

        binding.btnFavEmpty.setOnClickListener {
            binding.btnFavEmpty.setImageResource(R.drawable.baseline_favorite_24)
            viewModel.save(characterItem)
        }

        ivGoHome.setOnClickListener {
            findNavController().navigate(R.id.actionDetailToHomeFragment)
        }
    }
}
