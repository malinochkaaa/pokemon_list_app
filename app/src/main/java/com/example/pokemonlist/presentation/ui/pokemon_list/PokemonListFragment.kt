package com.example.pokemonlist.presentation.ui.pokemon_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pokemonlist.R
import com.example.pokemonlist.databinding.FragmentPokemonListBinding
import com.example.pokemonlist.domain.model.PokemonDetail
import com.example.pokemonlist.domain.model.PokemonFilters
import com.example.pokemonlist.domain.model.Result
import com.example.pokemonlist.presentation.util.hide
import com.example.pokemonlist.presentation.util.hideKeyboard
import com.example.pokemonlist.presentation.util.show
import com.example.pokemonlist.presentation.util.showIf
import com.example.pokemonlist.presentation.util.snackbar
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PokemonListFragment : Fragment() {

    private lateinit var binding: FragmentPokemonListBinding
    private val viewModel: PokemonListViewModel by viewModels()
    private lateinit var pokemonAdapter: PokemonAdapter
    private var currentState: Result<List<PokemonDetail>>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPokemonListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupSwipeToRefresh()
        setupSearch()
        setupFilterButton()

        observeViewModel()
    }

    private fun setupRecyclerView() {
        pokemonAdapter = PokemonAdapter { pokemon ->
            val action = PokemonListFragmentDirections
                .actionListToDetail(pokemon.id)
            findNavController().navigate(action)
        }

        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = this@PokemonListFragment.pokemonAdapter
        }
        binding.recyclerView.setOnScrollChangeListener { _, _, _, _, _ ->
            val layoutManager = binding.recyclerView.layoutManager as LinearLayoutManager
            val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
            val totalItemCount = layoutManager.itemCount
            val currentSuccessState = currentState as? Result.Success

            if (currentSuccessState?.isFilterResult == false && totalItemCount != 0 && lastVisibleItemPosition >= totalItemCount - THRESHOLD) {
                viewModel.loadPokemon()
            }
        }
    }

    private fun setupSwipeToRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refresh()
        }
    }

    private fun setupSearch() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                hideKeyboard()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
                    viewModel.searchPokemon(newText.orEmpty())
                }
                return true
            }
        })
    }

    private fun setupFilterButton() {
        binding.filterFab.setOnClickListener {
            findNavController().navigate(R.id.action_list_to_filter)
        }

        setFragmentResultListener("filter_request") { _, bundle ->
            val filters = bundle.getParcelable<PokemonFilters>("filters")
            filters?.let {
                viewModel.applyFilters(it)
                binding.root.snackbar("Filters applied", Snackbar.LENGTH_SHORT)
            }
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.pokemonList.collect { state ->
                currentState = state
                renderState(state)
            }
        }
        lifecycleScope.launch {
            viewModel.isRefreshing.collect { isRefreshing ->
                binding.swipeRefresh.isRefreshing = isRefreshing
            }
        }
    }

    private fun renderState(state: Result<List<PokemonDetail>>) {
        when (state) {
            is Result.Loading -> {
                binding.progressBar.show()
                binding.errorLayout.hide()
                binding.emptyText.hide()
                pokemonAdapter.submitList(emptyList())
            }
            is Result.Success -> {
                binding.progressBar.hide()
                pokemonAdapter.submitList(state.data)
                binding.emptyText.showIf(state.data.isEmpty())
            }
            is Result.Error -> {
                binding.progressBar.hide()
                binding.errorLayout.show()
                binding.errorText.text = state.message
                pokemonAdapter.submitList(emptyList())
            }
        }
    }

    private companion object {
        const val THRESHOLD = 4
    }
}
