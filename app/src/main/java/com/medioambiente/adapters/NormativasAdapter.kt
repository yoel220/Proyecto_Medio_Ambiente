package com.medioambiente.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.medioambiente.R
import com.medioambiente.models.Normativa

class NormativasAdapter(private val normativas: List<Normativa>) :
    RecyclerView.Adapter<NormativasAdapter.NormativaViewHolder>() {

    class NormativaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tituloTextView: TextView = itemView.findViewById(R.id.textViewTitulo)
        val descripcionTextView: TextView = itemView.findViewById(R.id.textViewDescripcion)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NormativaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_normativa, parent, false)
        return NormativaViewHolder(view)
    }

    override fun onBindViewHolder(holder: NormativaViewHolder, position: Int) {
        val normativa = normativas[position]
        holder.tituloTextView.text = normativa.titulo
        holder.descripcionTextView.text = normativa.descripcion
    }

    override fun getItemCount(): Int {
        return normativas.size
    }
}