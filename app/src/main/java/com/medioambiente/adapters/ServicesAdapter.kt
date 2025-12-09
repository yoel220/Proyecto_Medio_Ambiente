package com.medioambiente.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.medioambiente.R
import com.medioambiente.models.Servicio

class ServicesAdapter(private var services: List<Servicio>) :
    RecyclerView.Adapter<ServicesAdapter.ServiceViewHolder>() {

    class ServiceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val icon: TextView = itemView.findViewById(R.id.serviceIconTextView)
        val title: TextView = itemView.findViewById(R.id.serviceNameTextView)
        val description: TextView = itemView.findViewById(R.id.serviceDescriptionTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_service, parent, false)
        return ServiceViewHolder(view)
    }

    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        val servicio = services[position]
        holder.icon.text = servicio.icono
        holder.title.text = servicio.nombre
        holder.description.text = servicio.descripcion
    }

    override fun getItemCount(): Int {
        return services.size
    }

    fun updateData(newServices: List<Servicio>) {
        services = newServices
        notifyDataSetChanged()
    }
}