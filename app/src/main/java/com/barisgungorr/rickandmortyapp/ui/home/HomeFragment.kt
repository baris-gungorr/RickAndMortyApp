package com.barisgungorr.rickandmortyapp.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.activity.addCallback
import androidx.core.content.ContentProviderCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.barisgungorr.rickandmortyapp.R
import com.barisgungorr.rickandmortyapp.data.dto.CharacterItem
import com.barisgungorr.rickandmortyapp.databinding.FragmentHomeBinding
import com.barisgungorr.rickandmortyapp.util.constanst.Constants
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(), androidx.appcompat.widget.SearchView.OnQueryTextListener,
    SearchView.OnQueryTextListener {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: HomeAdapter
    private val homeViewModel: HomeViewModel by viewModels()
    private var notFound: Boolean = false
    private var searchCharacter: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().navigate(R.id.action_homeFragment_to_splashFragment)
        }

        adapter = HomeAdapter { character ->
            onItemSelected(character)
        }

        initListComponents()
        return binding.root
    }

    private fun searchCharacters(){
        homeViewModel.onCreateList()
    }

    private fun searchCharacterByName(query: String){
        CoroutineScope(Dispatchers.IO).launch {
            homeViewModel.onCreateCharacterByName(query)
        }
    }

    @SuppressLint("RestrictedApi")
    override fun onQueryTextSubmit(query: String?): Boolean {
        if(!query.isNullOrEmpty()){
            val lowercaseQuery = query.lowercase()
            searchCharacterByName(lowercaseQuery)
            context?.let { com.barisgungorr.rickandmortyapp.util.extension.hideKeyboard(it,binding.searchView) }
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }
    @SuppressLint("NotifyDataSetChanged")
    private fun initListComponents(){
        binding.searchView.setOnQueryTextListener(this)
        searchCharacters()

        homeViewModel.charactersResponse.observe(viewLifecycleOwner , Observer { it ->

            it.let{
                val characterList : ArrayList<CharacterItem> = it.body()!!
                adapter.submitList(characterList) // adapter'a veri listesini gönder
            }

            homeViewModel.isLoading.observe(viewLifecycleOwner, Observer{
                binding.pbCharacter.isVisible = it
            })

            binding.rvCharacter.layoutManager = GridLayoutManager(activity, 2)
            binding.rvCharacter.adapter = adapter
        })

        homeViewModel.characterListItemResponse.observe(viewLifecycleOwner, Observer { it ->

            if(it.code() != Constants.NOT_FOUND_CODE){
                notFound = false
                searchCharacter = true

                it.let{
                    val characterList : ArrayList<CharacterItem> = it.body()!!.results
                    adapter.submitList(characterList)
                }
                homeViewModel.isLoading.observe(viewLifecycleOwner, Observer{
                    binding.pbCharacter.isVisible = it
                })

                binding.rvCharacter.layoutManager = GridLayoutManager(activity, 2)
                binding.rvCharacter.adapter = adapter

            }else{
                notFound = true
            }
            showNotFound()
        })
    }

    private fun onItemSelected(characterItem : CharacterItem) {
        notFound = false
        findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToDetailFragment(idCharacter = characterItem.id))
    }
    private fun showNotFound() {
        if (notFound) {
            val sbError = Snackbar.make(
                binding.root,
                R.string.character_not_found, Snackbar.ANIMATION_MODE_SLIDE
            )
                .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.white))
            sbError.show()
        }
    }
}





