package com.medioambiente.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.medioambiente.R
import com.medioambiente.activities.MiembroDetailActivity
import com.medioambiente.models.Miembro

class EquipoAdapter(
    private val equipo: List<Miembro>
) : RecyclerView.Adapter<EquipoAdapter.MiembroViewHolder>() {

    class MiembroViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ShapeableImageView = itemView.findViewById(R.id.miembroImageView)
        val nameTextView: TextView = itemView.findViewById(R.id.miembroNameTextView)
        val cargoTextView: TextView = itemView.findViewById(R.id.miembroCargoTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MiembroViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_miembro, parent, false)
        return MiembroViewHolder(view)
    }

    override fun onBindViewHolder(holder: MiembroViewHolder, position: Int) {
        val miembro = equipo[position]
        holder.nameTextView.text = miembro.nombre
        holder.cargoTextView.text = miembro.cargo


        Glide.with(holder.itemView.context)
            .load(miembro.fotoUrl)
            .placeholder(R.drawable.ic_placeholder_image)
            .error(R.drawable.ic_error_image)
            .into(holder.imageView)

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, MiembroDetailActivity::class.java).apply {
                putExtra("miembro", miembro)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return equipo.size
    }
}