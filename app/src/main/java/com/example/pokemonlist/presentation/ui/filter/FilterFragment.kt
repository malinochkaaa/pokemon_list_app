package com.example.pokemonlist.presentation.ui.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.pokemonlist.databinding.FragmentFilterBinding
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FilterFragment : Fragment() {

    private lateinit var binding: FragmentFilterBinding
    private val viewModel: FilterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        setupFilters()
        setupObservers()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupFilters() {
        typesList.forEach { type ->
            val chip = Chip(requireContext()).apply {
                text = type
                isCheckable = true
                setOnCheckedChangeListener { _, isChecked ->
                    viewModel.toggleType(type, isChecked)
                }
            }
            binding.typeChipGroup.addView(chip)
        }

        binding.heightSlider.apply {
            valueTo = 50f
            values = listOf(0f, 50f)
            setLabelFormatter { value ->
                "${value.toInt()} m"
            }
            addOnChangeListener { _, _, _ ->
                val values = binding.heightSlider.values
                viewModel.setHeightRange(values[0].toInt(), values[1].toInt())
            }
        }

        binding.weightSlider.apply {
            valueTo = 500f
            values = listOf(0f, 500f)
            setLabelFormatter { value ->
                "${value.toInt()} kg"
            }
            addOnChangeListener { _, _, _ ->
                val values = binding.weightSlider.values
                viewModel.setWeightRange(values[0].toInt(), values[1].toInt())
            }
        }

        binding.applyButton.setOnClickListener {
            viewModel.applyFilters()
        }

        binding.resetButton.setOnClickListener {
            viewModel.resetFilters()
            resetUI()
        }
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.filterResult.collect { filters ->
                setFragmentResult("filter_request", bundleOf("filters" to filters))
                findNavController().navigateUp()
            }
        }
    }

    private fun resetUI() {
        binding.typeChipGroup.clearCheck()
        binding.heightSlider.apply {
            valueFrom = 0f
            valueTo = 50f
            values = listOf(0f, 50f)
        }
        binding.weightSlider.apply {
            valueFrom = 0f
            valueTo = 500f
            values = listOf(0f, 500f)
        }
    }

    private companion object {
        val typesList = listOf("Fire", "Water", "Grass", "Electric", "Psychic",
            "Ice", "Dragon", "Dark", "Fairy", "Normal")
    }
}
