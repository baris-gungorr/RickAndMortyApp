package com.barisgungorr.rickandmortyapp.ui.favorite

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.barisgungorr.rickandmortyapp.R
import com.barisgungorr.rickandmortyapp.data.entity.Favorite
import com.barisgungorr.rickandmortyapp.databinding.FragmentFavoriteBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FavoriteFragment : Fragment() {
    private lateinit var binding: FragmentFavoriteBinding
    private val viewModel: FavoriteViewModel by viewModels()
    private lateinit var adapter: FavoriteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initVariables()
        observe()
        initViews()

    }

    private fun initVariables() {
        val layoutManager = LinearLayoutManager(requireContext())
        binding.rv.layoutManager = layoutManager
    }

    private fun observe() {

        viewModel.favoriteList.observe(viewLifecycleOwner) { favorites ->

            val isFavoritesEmpty = favorites.isEmpty()

            binding.ivEmpty.isVisible = isFavoritesEmpty
            binding.tvEmpty.isVisible = isFavoritesEmpty


            adapter = FavoriteAdapter(
                callbacks = object : FavoriteAdapter.FavoriteCallBack {
                    override fun onDeleteFavorite(favorite: Favorite) {
                        showDeleteFavoriteDialog(favorite)
                    }
                }
            )
            binding.rv.adapter = adapter
            adapter.submitList(favorites.orEmpty())
        }
    }

    private fun showDeleteFavoriteDialog(favorite: Favorite) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(R.string.favorite_page_title)
        builder.setMessage(getString(R.string.favorite_page_delete_tv, favorite.characterName))
        // builder.setIcon(R.drawable.ic_app_icon)
        builder.setPositiveButton(R.string.favorite_page_yes_tv) { dialog, which ->

            viewModel.deleteFavorite(characterId = favorite.characterId)
            dialog.dismiss()
        }
        builder.setNegativeButton("NO") { dialog, which ->
            dialog.dismiss()
        }
        builder.show()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initViews() = with(binding) {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                viewModel.searchFavorite(newText)
                return false
            }
        })
        searchView.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {

                binding.searchView.isIconified = false
            }
            false
        }
        ivHome.setOnClickListener {
            // findNavController().navigate(R.id.favoriteToMain)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getFavorites()
    }
}
