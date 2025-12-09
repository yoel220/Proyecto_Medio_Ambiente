package com.medioambiente.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.medioambiente.R
import com.medioambiente.models.Medida

class MedidasAdapter(
    private val medidas: List<Medida>,
    private val onItemClick: (Medida) -> Unit
) : RecyclerView.Adapter<MedidasAdapter.MedidaViewHolder>() {

    class MedidaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val iconTextView: TextView = itemView.findViewById(R.id.medidaIconTextView)
        val titleTextView: TextView = itemView.findViewById(R.id.medidaTitleTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedidaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_medida, parent, false)
        return MedidaViewHolder(view)
    }

    override fun onBindViewHolder(holder: MedidaViewHolder, position: Int) {
        val medida = medidas[position]
        holder.titleTextView.text = medida.titulo
        holder.iconTextView.text = medida.icono
        holder.itemView.setOnClickListener {
            onItemClick(medida)
        }
    }

    override fun getItemCount(): Int {
        return medidas.size
    }
}