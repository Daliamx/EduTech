package com.example.registrousuario

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.registrousuario.data.FavoriteSubject

class FavoritesAdapter(
    private var favorites: List<FavoriteSubject>,
    private val onEditClick: (FavoriteSubject) -> Unit,
    private val onDeleteClick: (FavoriteSubject) -> Unit
) : RecyclerView.Adapter<FavoritesAdapter.FavoriteViewHolder>() {

    class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvEmoji: TextView = itemView.findViewById(R.id.tvEmojiMateria)
        val tvNombre: TextView = itemView.findViewById(R.id.tvNombreMateria)
        val tvDescripcion: TextView = itemView.findViewById(R.id.tvDescripcionMateria)
        val btnEditar: ImageButton = itemView.findViewById(R.id.btnEditarMateria)
        val btnEliminar: ImageButton = itemView.findViewById(R.id.btnEliminarMateria)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_favorite_subject, parent, false)
        return FavoriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val item = favorites[position]
        holder.tvEmoji.text = item.emoji
        holder.tvNombre.text = item.nombre
        holder.tvDescripcion.text = if (item.descripcion.isBlank()) "Sin notas o descripción" else item.descripcion

        holder.btnEditar.setOnClickListener { onEditClick(item) }
        holder.btnEliminar.setOnClickListener { onDeleteClick(item) }
    }

    override fun getItemCount(): Int = favorites.size

    fun updateList(newList: List<FavoriteSubject>) {
        favorites = newList
        notifyDataSetChanged()
    }
}