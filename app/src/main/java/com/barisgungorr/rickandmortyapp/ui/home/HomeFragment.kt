package com.barisgungorr.rickandmortyapp.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import androidx.core.widget.addTextChangedListener
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
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
class HomeFragment : Fragment(), SearchView.OnQueryTextListener, NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: FragmentHomeBinding
    private val homeViewModel: HomeViewModel by viewModels()
    private var notFound: Boolean = false
    private var searchCharacter: Boolean = false
    private lateinit var drawerLayout: DrawerLayout
    private val bottomSheetFragment = BottomSheetFragment()
    private val adapter: HomeAdapter by lazy {
        HomeAdapter { character ->
            onItemSelected(character)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        drawerLayout = binding.drawerLayout

        val toolbar = binding.toolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)

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
        isLoading()
        loadingData()
        initListComponents()
        return binding.root
    }

    private fun loadingData() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                homeViewModel.listData.collectLatest { pagingData ->
                    adapter.submitData(pagingData)
                }
            } catch (e: Exception) {
                Log.e("Error", "Not Loading Data !")
            }finally {
                homeViewModel.isLoading.postValue(false)
            }
        }
    }
    private fun isLoading() {
        viewLifecycleOwner.lifecycleScope.launch {
            adapter.loadStateFlow.collectLatest { loadStates ->
                binding.pbCharacter.isVisible = loadStates.refresh is LoadState.Loading
                binding.tvError.isVisible = loadStates.refresh is LoadState.Error
            }
        }
    }

    private fun searchCharacters(query: String) {
       CoroutineScope(Dispatchers.IO).launch {
            homeViewModel.loadCharacterByName(query)
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (!query.isNullOrEmpty()) {
            val lowercaseQuery = query.lowercase()
            searchCharacters(lowercaseQuery)
            context?.let {
                com.barisgungorr.rickandmortyapp.util.extension.hideKeyboard(
                    it,
                    binding.etSearch
                )
            }
            return true
        }
      return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return newText != null
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun initListComponents() {

        binding.etSearch.addTextChangedListener {
            searchCharacters(it.toString())
        }

        binding.rvCharacter.layoutManager = GridLayoutManager(requireActivity(), 2)
        binding.rvCharacter.adapter = adapter

        homeViewModel.charactersResponse.observe(viewLifecycleOwner, Observer { responseApi ->

            responseApi.body()?.results?.let { characterList ->
                val pagingData: PagingData<CharacterItem> = PagingData.from(characterList)
                adapter.submitData(lifecycle, pagingData)
            }
        })



        homeViewModel.characterListItemResponse.observe(viewLifecycleOwner, Observer { response ->

            if (response.code() != Constants.NOT_FOUND_CODE) {
                notFound = false
                searchCharacter = true

                response.body()?.results?.let { characterList ->
                    val pagingData: PagingData<CharacterItem> = PagingData.from(characterList)
                    adapter.submitData(lifecycle, pagingData)
                }
            } else {
                notFound = true
            }

            showNotFound()
        })
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

    private fun onItemSelected(characterItem: CharacterItem) {
        notFound = false
        findNavController().navigate(HomeFragmentDirections.actionHomeToDetailFragment(idCharacter = characterItem.id))
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_settings -> findNavController().navigate(R.id.actionHomeToSettings)
            R.id.nav_about -> bottomSheetFragment.show(parentFragmentManager, bottomSheetFragment.tag)
            R.id.nav_logout -> showLogoutDialog()
            R.id.nav_share -> shareApp()
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun showLogoutDialog() {
        AlertDialog.Builder(requireContext()).apply {
            setMessage("Are you sure you want to exit?")
            setTitle("Rick and Morty")
            setIcon(R.drawable.baseline_info_24)
            setPositiveButton("Yes") { _, _ ->
                requireActivity().finish()
            }
            setNegativeButton("No") { _, _ -> }
        }.show()
    }

    private fun shareApp() {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Rick and Morty")
        shareIntent.putExtra(Intent.EXTRA_TEXT, "https://github.com/baris-gungorr/RickAndMortyApp")
        startActivity(Intent.createChooser(shareIntent, "Share with"))
    }
}

