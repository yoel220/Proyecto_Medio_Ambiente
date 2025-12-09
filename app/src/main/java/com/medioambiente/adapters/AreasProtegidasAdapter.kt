package com.medioambiente.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.medioambiente.R
import com.medioambiente.models.AreaProtegida

class AreasProtegidasAdapter(
    private var areas: List<AreaProtegida>,
    private val clickListener: (AreaProtegida) -> Unit
) : RecyclerView.Adapter<AreasProtegidasAdapter.AreaViewHolder>() {

    class AreaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imagen: ImageView = view.findViewById(R.id.area_imagen)
        val nombre: TextView = view.findViewById(R.id.area_nombre)
        val tipo: TextView = view.findViewById(R.id.area_tipo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AreaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_area_protegida, parent, false)
        return AreaViewHolder(view)
    }

    override fun onBindViewHolder(holder: AreaViewHolder, position: Int) {
        val area = areas[position]
        holder.nombre.text = area.nombre
        holder.tipo.text = "Tipo: ${area.tipo}"

        Glide.with(holder.itemView.context)
            .load(area.imagen)
            .placeholder(R.drawable.placeholder_area)
            .error(R.drawable.ic_error_image)
            .into(holder.imagen)

        holder.itemView.setOnClickListener {
            clickListener(area)
        }
    }

    override fun getItemCount() = areas.size

    fun updateData(newAreas: List<AreaProtegida>) {
        areas = newAreas
        notifyDataSetChanged()
    }
}