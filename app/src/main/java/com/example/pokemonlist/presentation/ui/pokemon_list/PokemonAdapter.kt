package com.example.pokemonlist.presentation.ui.pokemon_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pokemonlist.R
import com.example.pokemonlist.databinding.ItemPokemonBinding
import com.example.pokemonlist.domain.model.PokemonDetail
import com.example.pokemonlist.presentation.util.getTypeColor
import com.example.pokemonlist.presentation.util.loadImage
import java.util.Locale

class PokemonAdapter(
    private val onItemClick: (PokemonDetail) -> Unit
) : ListAdapter<PokemonDetail, PokemonAdapter.PokemonViewHolder>(PokemonDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val binding = ItemPokemonBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return PokemonViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class PokemonViewHolder(
        private val binding: ItemPokemonBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(getItem(position))
                }
            }
        }

        fun bind(pokemon: PokemonDetail) {
            binding.apply {
                pokemonName.text = pokemon.name.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(
                        Locale.getDefault()
                    ) else it.toString()
                }
                pokemonId.text = root.context.getString(
                    R.string.pokemon_id_text,
                    pokemon.id.toString().padStart(3, '0'),
                )
                pokemonImage.setBackgroundColor(root.context.getColor(getTypeColor(pokemon.types?.first() ?: "")))
                pokemonImage.loadImage(
                    url = pokemon.imageUrl,
                    placeholder = R.drawable.ic_launcher_foreground,
                    error = R.drawable.ic_error_red,
                    shouldCenterCrop = true,
                )
            }
        }
    }

    class PokemonDiffCallback : DiffUtil.ItemCallback<PokemonDetail>() {
        override fun areItemsTheSame(oldItem: PokemonDetail, newItem: PokemonDetail): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PokemonDetail, newItem: PokemonDetail): Boolean {
            return oldItem == newItem
        }
    }
}
