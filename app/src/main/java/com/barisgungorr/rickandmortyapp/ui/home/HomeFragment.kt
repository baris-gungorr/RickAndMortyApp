package com.barisgungorr.rickandmortyapp.ui.home

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresExtension
import androidx.compose.runtime.snapshots.Snapshot.Companion.observe
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.barisgungorr.rickandmortyapp.R
import com.barisgungorr.rickandmortyapp.databinding.FragmentHomeBinding
import kotlinx.coroutines.launch


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeAdapter: HomeAdapter
  //  private val viewModel: HomeViewModel by viewModels()
    private val viewModel: HomeViewModel by activityViewModels()


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


    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    private fun initViews() {
        homeAdapter = HomeAdapter()
        binding.rvCharacter.apply {
            adapter = homeAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
            setHasFixedSize(true)
        }
        lifecycleScope.launch {
            viewModel.state.collect{
                when{
                    it.load-> binding.pbCharacter.visibility = View.VISIBLE
                    it.success.isNotEmpty()->{
                        binding.pbCharacter.visibility = View.GONE
                        homeAdapter.submitList(it.success)
                    }
                    it.fail.isNotBlank()->{
                        binding.pbCharacter.visibility = View.GONE
                        Toast.makeText(requireContext(), it.fail, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }

    private fun observe() {

    }


}