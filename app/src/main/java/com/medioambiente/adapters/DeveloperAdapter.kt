package com.medioambiente.adapters

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.medioambiente.R
import com.medioambiente.models.Developer

class DeveloperAdapter(private val developerList: List<Developer>) :
    RecyclerView.Adapter<DeveloperAdapter.DeveloperViewHolder>() {

    inner class DeveloperViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.developerImageView)
        val nameTextView: TextView = itemView.findViewById(R.id.developerNameTextView)
        val matriculaTextView: TextView = itemView.findViewById(R.id.developerMatriculaTextView)
        val phoneButton: ImageButton = itemView.findViewById(R.id.phoneButton)
        val telegramButton: ImageButton = itemView.findViewById(R.id.telegramButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeveloperViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_developer, parent, false)
        return DeveloperViewHolder(view)
    }

    override fun onBindViewHolder(holder: DeveloperViewHolder, position: Int) {
        val developer = developerList[position]
        holder.imageView.setImageResource(developer.foto)
        holder.nameTextView.text = developer.nombre
        holder.matriculaTextView.text = "Matrícula: ${developer.matricula}"


        holder.phoneButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:${developer.telefono}")
            }
            holder.itemView.context.startActivity(intent)
        }


        holder.telegramButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://t.me/${developer.telegram}")
            }
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = developerList.size
}