package com.medioambiente.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.medioambiente.R
import com.medioambiente.adapters.DeveloperAdapter
import com.medioambiente.models.Developer

class AcercaActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var developerAdapter: DeveloperAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_acerca)

        recyclerView = findViewById(R.id.recyclerViewAcerca)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val developers = listOf(
            Developer(
                nombre = "Felibell Brazoban López",
                matricula = "2021-2308",
                telefono = "(809) 000-0000",
                telegram = "FBL06",
                foto = R.drawable.felibell
            ),
            Developer(
                nombre = "Engels Germosen",
                matricula = "2023-1387",
                telefono = "849-405-5900",
                telegram = "@josefeliciano126",
                foto = R.drawable.engels
            ),

            Developer(
                nombre = "Adriel Padilla",
                matricula = "2022-2161",
                telefono = "8298126638",
                telegram = "@Adrielapx",
                foto = R.drawable.adriel

            ),


            Developer(
                nombre = "Anderson Batista Mateo",
                matricula = "2023-1396",
                telefono = "809-969-1424",
                telegram = "@yoel215",
                foto = R.drawable.anderson


            ),

            Developer(
                nombre = "Yoelmis M. Espirisanto Cedano",
                matricula = "2023-1817",
                telefono = "829-266-7493",
                telegram = "@yoel215",
                foto = R.drawable.yoelmis


            ),






            )

        developerAdapter = DeveloperAdapter(developers)
        recyclerView.adapter = developerAdapter
    }
}