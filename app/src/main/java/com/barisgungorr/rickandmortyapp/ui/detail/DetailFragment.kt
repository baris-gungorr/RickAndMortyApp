package com.barisgungorr.rickandmortyapp.ui.detail

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
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
    private lateinit var context: Context

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        searchCharacters()
        listEpisodes = Episodes()
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        context = inflater.context

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observe()
    }

    private fun observe() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().navigate(DetailFragmentDirections.actionDetailToHomeFragment())
        }

        characterViewModel.characterItemResponse.observe(viewLifecycleOwner, Observer { it ->
            it?.let {
                characterItem = it.body()!!
                searchEpisodes(characterItem)
                initViews()
            }
        })

        episodesViewModel.episodesListResponse.observe(viewLifecycleOwner, Observer { it ->
            it?.let {
                listEpisodes.clear()
                listEpisodes = it.body()!!
                setRecyclerViewEpisodes()
            }
        })

        episodesViewModel.episodesResponse.observe(viewLifecycleOwner, Observer { it ->
            it?.let {
                listEpisodes.clear()
                listEpisodes.add(it.body()!!)
                setRecyclerViewEpisodes()
            }
        })
    }

    private fun searchCharacters() {
        CoroutineScope(Dispatchers.IO).launch {
            characterViewModel.loadCharacterItemById(args.idCharacter.toString())
        }
    }

    private fun searchEpisodes(characterItem: CharacterItem) {
        CoroutineScope(Dispatchers.IO).launch {
            val episodes = Url.obtainNumFromURLs(characterItem.episode)

            if (episodes.contains(",")) {
                episodesViewModel.onCreateListEpisodes(episodes)
            } else {
                episodesViewModel.onCreateEpisode(episodes)
            }
        }
    }

    private fun initViews() = with(binding) {
        Glide.with(binding.ivDetailCharacter.context)
            .load(characterItem.image)
            .into(binding.ivDetailCharacter)

        val scaleXAnimation = ObjectAnimator.ofFloat(binding.ivDetailCharacter, "scaleX", 1.5f)
        val scaleYAnimation = ObjectAnimator.ofFloat(binding.ivDetailCharacter, "scaleY", 1.5f)
        scaleXAnimation.duration = 1000
        scaleYAnimation.duration = 1000

        scaleXAnimation.repeatMode = ValueAnimator.REVERSE
        scaleXAnimation.repeatCount = ValueAnimator.INFINITE
        scaleYAnimation.repeatMode = ValueAnimator.REVERSE
        scaleYAnimation.repeatCount = ValueAnimator.INFINITE

        viewModel.startAnimation.observe(viewLifecycleOwner, Observer {
            scaleXAnimation.start()
            scaleYAnimation.start()
        })

        viewModel.stopAnimation.observe(viewLifecycleOwner, Observer {
            scaleXAnimation.cancel()
            scaleYAnimation.cancel()
            ivDetailCharacter.scaleX = 1f
            ivDetailCharacter.scaleY = 1f
        })

        ivDetailCharacter.setOnClickListener {
            viewModel.startAnimation()
        }

        root.setOnClickListener {
            viewModel.stopAnimation()
        }
        tvDetailName.text = characterItem.name
        tvDetailStatus.text = characterItem.status

        when (characterItem.status.lowercase()) {
            "alive" -> ivDetailAlive.background =
                ContextCompat.getDrawable(context, R.drawable.baseline_alive)

            "dead" -> ivDetailAlive.background =
                ContextCompat.getDrawable(context, R.drawable.baseline_dead)

            "unknown" -> ivDetailAlive.background =
                ContextCompat.getDrawable(context, R.drawable.baseline_unknown)
        }
        tvDetailSpecies.text = characterItem.species
        tvDetailLocation.text = characterItem.location.name
        tvDetailGender.text = characterItem.gender

        btnMedia.setOnClickListener {
            findNavController().navigate(R.id.actionDetailsToMedia)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setRecyclerViewEpisodes() {
        adapter = EpisodeAdapter()
        binding.rvEpisodes.layoutManager = LinearLayoutManager(activity)
        binding.rvEpisodes.adapter = adapter
        adapter.submitList(listEpisodes)

        binding.btnFavEmpty.setOnClickListener() {
            binding.btnFavEmpty.setImageResource(R.drawable.baseline_favorite_24)

            viewModel.save(
                characterItem.id,
                characterItem.name,
                characterItem.status,
                characterItem.species,
                characterItem.gender,
                characterItem.location.name,
                characterItem.image
            )
        }

        binding.ivHome.setOnClickListener {
            findNavController().navigate(R.id.actionDetailToHomeFragment)
        }
    }
}

