package com.barisgungorr.rickandmortyapp.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.activity.addCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.barisgungorr.rickandmortyapp.R
import com.barisgungorr.rickandmortyapp.data.dto.CharacterItem
import com.barisgungorr.rickandmortyapp.databinding.FragmentHomeBinding
import com.barisgungorr.rickandmortyapp.ui.bottomsheet.BottomSheetFragment

import com.barisgungorr.rickandmortyapp.util.constanst.Constants
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(), androidx.appcompat.widget.SearchView.OnQueryTextListener,
    SearchView.OnQueryTextListener , NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: HomeAdapter
    private val homeViewModel: HomeViewModel by viewModels()
    private var notFound: Boolean = false
    private var searchCharacter: Boolean = false

    private lateinit var drawerLayout: DrawerLayout
    private val bottomSheetFragment = BottomSheetFragment()


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        //   val fragmentManager = parentFragmentManager
        // val fragmentTransaction = fragmentManager.beginTransaction()

        adapter = HomeAdapter { character ->
            onItemSelected(character)
        }

        drawerLayout = binding.drawerLayout

        val toolbar = binding.toolbar
        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        val navigationView = binding.navView
        navigationView.setNavigationItemSelectedListener(this)

        val toggle = ActionBarDrawerToggle(
            requireActivity(),
            drawerLayout,
            toolbar,
            R.string.home_fragment_open_nav,
            R.string.home_fragment_close_nav
        )
        toggle.syncState()

        initListComponents()
        return binding.root
    }

    private fun searchCharacters() {
        homeViewModel.onCreateList()
    }

    private fun searchCharacterByName(query: String) {
        CoroutineScope(Dispatchers.IO).launch {
            homeViewModel.onCreateCharacterByName(query)
        }
    }

    @SuppressLint("RestrictedApi")
    override fun onQueryTextSubmit(query: String?): Boolean {
        if (!query.isNullOrEmpty()) {
            val lowercaseQuery = query.lowercase()
            searchCharacterByName(lowercaseQuery)
            context?.let {
                com.barisgungorr.rickandmortyapp.util.extension.hideKeyboard(
                    it,
                    binding.searchView
                )
            }
        }
        return true
    }
    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initListComponents() {
        binding.searchView.setOnQueryTextListener(this)
        searchCharacters()

        homeViewModel.charactersResponse.observe(viewLifecycleOwner, Observer { it ->

            it.let {
                val characterList: ArrayList<CharacterItem> = it.body()!!
                adapter.submitList(characterList)
            }
            homeViewModel.isLoading.observe(viewLifecycleOwner, Observer {
                binding.pbCharacter.isVisible = it
            })

            binding.rvCharacter.layoutManager = GridLayoutManager(activity, 2)
            binding.rvCharacter.adapter = adapter
        })

        homeViewModel.characterListItemResponse.observe(viewLifecycleOwner, Observer { it ->

            if (it.code() != Constants.NOT_FOUND_CODE) {
                notFound = false
                searchCharacter = true

                it.let {
                    val characterList: ArrayList<CharacterItem> = it.body()!!.results
                    adapter.submitList(characterList)
                }
                homeViewModel.isLoading.observe(viewLifecycleOwner, Observer {
                    binding.pbCharacter.isVisible = it
                })

                binding.rvCharacter.layoutManager = GridLayoutManager(activity, 2)
                binding.rvCharacter.adapter = adapter

            } else {
                notFound = true
            }
            showNotFound()
        })
    }
    private fun onItemSelected(characterItem: CharacterItem) {
        notFound = false
        findNavController().navigate(HomeFragmentDirections.actionHomeToDetailFragment(idCharacter = characterItem.id))
    }

    private fun showNotFound() {
        if (notFound) {
            val sbError = Snackbar.make(
                binding.root,
                R.string.home_fragment_character_not, Snackbar.ANIMATION_MODE_SLIDE
            )
                .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.white))
            sbError.show()
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if (isEnabled) {
                isEnabled = false
                findNavController().popBackStack(R.id.homeFragment, false)
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_settings ->
                findNavController().navigate(R.id.actionHomeToSettings)
            R.id.nav_about -> {
                bottomSheetFragment.show(parentFragmentManager, bottomSheetFragment.tag)
            }
            R.id.nav_logout ->
                AlertDialog.Builder(requireContext()).apply {
                    setMessage("are you exit")
                    setTitle("rick and morty")
                    setIcon(R.drawable.baseline_info_24)
                    setPositiveButton("yes") { _, _ ->
                        requireActivity().finish()
                    }
                    setNegativeButton("NO") { _, _ -> }
                }.show()

            R.id.nav_share -> {
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Rick and Morty")
                shareIntent.putExtra(Intent.EXTRA_TEXT, "https://github.com/baris-gungorr/RickAndMortyApp")
                startActivity(Intent.createChooser(shareIntent, "Share with"))
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
/*
  private fun loadingData() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                homeViewModel.listData.collectLatest { pagingData -> adapter.submitData(pagingData) }
            } catch (e: Exception) {
                Log.e("Error", "Not Loading Data !")
            }
        }
    }
 */

}



