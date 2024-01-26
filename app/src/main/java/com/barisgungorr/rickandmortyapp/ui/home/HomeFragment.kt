package com.barisgungorr.rickandmortyapp.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import com.barisgungorr.rickandmortyapp.R
import com.barisgungorr.rickandmortyapp.data.dto.CharacterItem
import com.barisgungorr.rickandmortyapp.databinding.FragmentHomeBinding
import com.barisgungorr.rickandmortyapp.ui.bottomsheet.BottomSheetFragment
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: FragmentHomeBinding
    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var drawerLayout: DrawerLayout
    private val bottomSheetFragment = BottomSheetFragment()
    private var searchJob: Job? = null

    private val adapter: HomeAdapter by lazy {
        HomeAdapter { character ->
            onItemSelected(character)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        if (homeViewModel.charactersResponse.value == null) {
            loadData()
        }
        observeData()
    }

    private fun setupUI() {
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
        initListComponents()
    }

    private fun loadData() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                homeViewModel.listData.collectLatest { pagingData ->
                    adapter.submitData(pagingData)
                    homeViewModel.isLoading.postValue(false)
                }
            } catch (e: Exception) {
                Log.e("Error", "Not Loading Data !")
            } finally {
                homeViewModel.isLoading.postValue(false)
            }
        }
    }
    private fun observeData() {
        observeCharacterSearchResults()
        observeCharacterItemResponse()
        observeLoadingState()
    }

    private fun observeLoadingState() {
        viewLifecycleOwner.lifecycleScope.launch {
            adapter.loadStateFlow.collectLatest { loadStates ->
                binding.pbCharacter.isVisible = loadStates.refresh is LoadState.Loading
                binding.tvError.isVisible = loadStates.refresh is LoadState.Error
            }
        }
    }

    private fun searchCharacters(query: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            homeViewModel.loadCharacterByName(query)
        }
    }

    private fun initListComponents() {
        binding.etSearch.addTextChangedListener { editable ->
            searchJob?.cancel()
            searchJob = viewLifecycleOwner.lifecycleScope.launch {
                delay(100)
                editable?.let {
                    if (editable.toString().isNotEmpty()) {
                        searchCharacters(editable.toString())
                    }
                }
            }
        }
        binding.rvCharacter.layoutManager = GridLayoutManager(requireActivity(), 2)
        binding.rvCharacter.adapter = adapter
    }

    private fun observeCharacterSearchResults() {
        homeViewModel.charactersResponse.observe(viewLifecycleOwner) { response ->
            if (response.isSuccessful) {
                adapter.submitData(
                    lifecycle,
                    PagingData.from(response.body()?.results ?: emptyList())
                )
                homeViewModel.isLoading.postValue(false)
            }
        }
    }

    private fun observeCharacterItemResponse() {
        homeViewModel.characterItemResponse.observe(viewLifecycleOwner) { response ->
            if (!response.isSuccessful) {
                binding.tvError.text = "An error occurred: ${response.errorBody()?.string()}"
                binding.tvError.isVisible = true
            } else {
                binding.tvError.isVisible = false
            }
        }
    }

    private fun onItemSelected(characterItem: CharacterItem) {
        setFragmentResult("characterId", bundleOf("id" to characterItem.id))
        findNavController().navigate(HomeFragmentDirections.actionHomeToDetailFragment(characterItem.id))
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_settings -> navigateToSettings()
            R.id.nav_about -> showAboutBottomSheet()
            R.id.nav_logout -> showLogoutDialog()
            R.id.nav_share -> shareApp()
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun navigateToSettings() {
        findNavController().navigate(R.id.actionHomeToSettings)
    }

    private fun showAboutBottomSheet() {
        bottomSheetFragment.show(parentFragmentManager, bottomSheetFragment.tag)
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