package com.barisgungorr.rickandmortyapp.ui.favorite

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.barisgungorr.rickandmortyapp.R
import com.barisgungorr.rickandmortyapp.data.entity.Favorite
import com.barisgungorr.rickandmortyapp.databinding.FragmentFavoriteBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FavoriteFragment : Fragment() {
    private lateinit var binding: FragmentFavoriteBinding
    private val viewModel: FavoriteViewModel by viewModels()


    private val adapter: FavoriteAdapter by lazy {
        FavoriteAdapter(
            callbacks = object : FavoriteAdapter.FavoriteCallBack {
                override fun onDeleteFavorite(favorite: Favorite) {
                    showDeleteFavoriteDialog(favorite)
                }
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initVariables()
        observe()
        initViews()
        swipeToCallBack()
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

            binding.rv.adapter = adapter
            adapter.submitList(favorites.orEmpty())
        }
    }

    private fun showDeleteFavoriteDialog(favorite: Favorite) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(R.string.favorite_page_title)
        builder.setMessage(getString(R.string.favorite_page_delete_tv, favorite.characterName))
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


        ivHome.setOnClickListener {
            findNavController().navigate(R.id.actionFavoriteToMainFragment)
        }

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {

                viewModel.searchFavorite(s.toString())
            }
        })
    }

    private fun swipeToCallBack() {
        val swipeToDeleteCallback = object : SwipeToDeleteCallback() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.bindingAdapterPosition

                viewModel.favoriteList.value?.get(position)?.let {
                    showDeleteFavoriteDialog(it)
                }

                binding.rv.adapter?.notifyItemRemoved(position)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(binding.rv)
    }

    override fun onResume() {
        super.onResume()
        viewModel.getFavorites()
    }
}
