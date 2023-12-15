package com.barisgungorr.rickandmortyapp.ui.home

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.annotation.RequiresExtension
import androidx.compose.runtime.snapshots.Snapshot.Companion.observe
import androidx.core.view.isGone
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.barisgungorr.rickandmortyapp.R
import com.barisgungorr.rickandmortyapp.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeAdapter: HomeAdapter
    private val viewModel: HomeViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observe()
    }


    @SuppressLint("ClickableViewAccessibility")
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    private fun initViews() = with(binding) {
        homeAdapter = HomeAdapter()
        binding.rvCharacter.apply {
            adapter = homeAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
            setHasFixedSize(true)
        }
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                viewModel.search(newText)
                return false
            }
        })

        searchView.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                searchView.isIconified = false
            }
            false
        }

    }

    private fun observe() {
        lifecycleScope.launch {
            viewModel.state.collect{
                when{
                    it.load-> binding.pbCharacter.visibility = View.VISIBLE
                    it.success.isNotEmpty()->{
                        binding.pbCharacter.isGone = true
                        homeAdapter.submitList(it.success)
                    }
                    it.fail.isNotBlank()->{
                        binding.pbCharacter.isGone = true
                        Toast.makeText(requireContext(), it.fail, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}