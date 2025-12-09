package com.medioambiente.adapters
import android.widget.Toast
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.medioambiente.R
import com.medioambiente.activities.ReportDetailsActivity
import com.medioambiente.models.Reporte

class ReporteAdapter(private val context: Context, private var reportes: List<Reporte>) :
    RecyclerView.Adapter<ReporteAdapter.ReporteViewHolder>() {

    class ReporteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val codigoTextView: TextView = itemView.findViewById(R.id.textViewCodigo)
        val tituloTextView: TextView = itemView.findViewById(R.id.textViewTitulo)
        val estadoTextView: TextView = itemView.findViewById(R.id.textViewEstado)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReporteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_reporte, parent, false)
        return ReporteViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReporteViewHolder, position: Int) {
        val reporte = reportes[position]
        holder.codigoTextView.text = "Código: ${reporte.codigo ?: "No disponible"}"
        holder.tituloTextView.text = "Título: ${reporte.titulo ?: "No disponible"}"
        holder.estadoTextView.text = "Estado: ${reporte.estado ?: "No disponible"}"

        holder.itemView.setOnClickListener {
            reporte.id?.let { reporteId ->
                Log.d("ReporteAdapter", "Reporte clickeado: $reporteId")
                val intent = Intent(context, ReportDetailsActivity::class.java)
                intent.putExtra("reporte_id", reporteId)
                context.startActivity(intent)
            } ?: run {
                Toast.makeText(context, "Error: El ID del reporte no es válido.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun getItemCount(): Int {
        return reportes.size
    }

    fun updateData(newReportes: List<Reporte>) {
        reportes = newReportes
        notifyDataSetChanged()
    }
}