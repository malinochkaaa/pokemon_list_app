package com.example.pokemonlist.presentation.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.pokemonlist.R
import com.example.pokemonlist.databinding.FragmentPokemonDetailBinding
import com.example.pokemonlist.domain.model.PokemonDetail
import com.example.pokemonlist.domain.model.Result
import com.example.pokemonlist.presentation.util.capitalize
import com.example.pokemonlist.presentation.util.getTypeColor
import com.example.pokemonlist.presentation.util.hide
import com.example.pokemonlist.presentation.util.loadImage
import com.example.pokemonlist.presentation.util.show
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Locale

@AndroidEntryPoint
class PokemonDetailFragment : Fragment() {

    private lateinit var binding: FragmentPokemonDetailBinding
    private val viewModel: PokemonDetailViewModel by viewModels()
    private val args: PokemonDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPokemonDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        loadPokemonDetail()
        observeViewModel()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun loadPokemonDetail() {
        viewModel.loadPokemonDetail(args.pokemonId)
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.pokemonDetail.collect { resource ->
                when (resource) {
                    is Result.Loading -> {
                        binding.progressBar.show()
                        binding.contentLayout.hide()
                        binding.errorLayout.hide()
                    }

                    is Result.Success -> {
                        binding.progressBar.hide()
                        binding.contentLayout.show()
                        displayPokemonDetails(resource.data)
                    }

                    is Result.Error -> {
                        binding.progressBar.hide()
                        binding.contentLayout.hide()
                        binding.errorLayout.show()
                        binding.errorText.text = resource.message
                    }
                }
            }
        }
    }

    private fun displayPokemonDetails(pokemon: PokemonDetail) {
        binding.apply {
            toolbar.title = pokemon.name.capitalize()
            pokemonImage.loadImage(
                url = pokemon.imageUrl,
                placeholder = R.drawable.ic_launcher_foreground,
                error = R.drawable.ic_error_red,
            )
            pokemonName.text = pokemon.name.capitalize()
            pokemonId.text = getString(
                R.string.pokemon_id_text,
                pokemon.id.toString().padStart(3, '0'),
            )
            heightValue.text = getString(
                R.string.pokemon_height_text,
                pokemon.height,
            )
            weightValue.text = getString(
                R.string.pokemon_weight_text,
                pokemon.weight,
            )

            pokemon.types?.let { displayPokemonTypes(it) }
        }
    }

    private fun displayPokemonTypes(types: List<String>) {
        binding.typesChipGroup.removeAllViews()

        types.forEach { typeName ->
            val chip = Chip(requireContext()).apply {
                text = typeName.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
                chipBackgroundColor = ContextCompat.getColorStateList(
                    requireContext(),
                    getTypeColor(typeName)
                )
                setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            }
            binding.typesChipGroup.addView(chip)
        }
    }
}
