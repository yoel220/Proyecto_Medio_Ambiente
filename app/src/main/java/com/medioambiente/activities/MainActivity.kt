package com.medioambiente.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.medioambiente.R
import com.medioambiente.adapters.ImageSliderAdapter
import com.medioambiente.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val handler = Handler(Looper.getMainLooper())
    private val DELAY_MS: Long = 3000 // 3 segundos
    private val runnable = object : Runnable {
        override fun run() {
            if (binding.viewPager.adapter != null && binding.viewPager.adapter!!.itemCount > 0) {
                val nextItem = (binding.viewPager.currentItem + 1) % binding.viewPager.adapter!!.itemCount
                binding.viewPager.setCurrentItem(nextItem, true)
            }
            handler.postDelayed(this, DELAY_MS)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageList = listOf(
            R.drawable.img1,
            R.drawable.img2,
            R.drawable.img3,
            R.drawable.img4
        )

        val adapter = ImageSliderAdapter(imageList)
        binding.viewPager.adapter = adapter


        val loginStatusTextView: TextView = binding.root.findViewById(R.id.loginStatusTextView)
        val toolbarLogo: ImageView = binding.root.findViewById(R.id.toolbarLogo)


        loginStatusTextView.setOnClickListener {
            val sharedPref = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
            val isLoggedIn = sharedPref.getBoolean("is_logged_in", false)

            if (isLoggedIn) {
                logout()
            } else {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        }


        toolbarLogo.setOnClickListener {
            Toast.makeText(this, "Ya estás en el menú principal", Toast.LENGTH_SHORT).show()
        }


        val aboutUsButton: MaterialButton = binding.root.findViewById(R.id.aboutUsButton)
        val acercaButton: MaterialButton = binding.root.findViewById(R.id.acercaButton)
        val voluntarioButton: MaterialButton = binding.root.findViewById(R.id.voluntarioButton)
        val areasProtegidasButton: MaterialButton = binding.root.findViewById(R.id.areasProtegidasButton)
        val servicesButton: MaterialButton = binding.root.findViewById(R.id.servicesButton)
        val newsButton: MaterialButton = binding.root.findViewById(R.id.newsButton)
        val videosButton: MaterialButton = binding.root.findViewById(R.id.videosButton)
        val medidasButton: MaterialButton = binding.root.findViewById(R.id.medidasButton)
        val equipoButton: MaterialButton = binding.root.findViewById(R.id.equipoButton)
        val mapaButton: MaterialButton = binding.root.findViewById(R.id.mapaButton)
        val normativasButton: MaterialButton = binding.root.findViewById(R.id.normativasButton)
        val reportButton: MaterialButton = binding.root.findViewById(R.id.reportButton)
        val misReportesButton: MaterialButton = binding.root.findViewById(R.id.misReportesButton)
        val mapaReportesButton: MaterialButton = binding.root.findViewById(R.id.mapaReportesButton)
        val changePasswordButton: MaterialButton = binding.root.findViewById(R.id.changePasswordButton)


        aboutUsButton.setOnClickListener {
            startActivity(Intent(this, AboutUsActivity::class.java))
        }
        acercaButton.setOnClickListener {
            startActivity(Intent(this, AcercaActivity::class.java))
        }
        voluntarioButton.setOnClickListener {
            startActivity(Intent(this, VoluntarioActivity::class.java))
        }
        areasProtegidasButton.setOnClickListener {
            startActivity(Intent(this, AreasProtegidasActivity::class.java))
        }
        servicesButton.setOnClickListener {
            startActivity(Intent(this, ServicesActivity::class.java))
        }
        newsButton.setOnClickListener {
            startActivity(Intent(this, NewsActivity::class.java))
        }
        videosButton.setOnClickListener {
            startActivity(Intent(this, VideosActivity::class.java))
        }
        medidasButton.setOnClickListener {
            startActivity(Intent(this, MedidasActivity::class.java))
        }
        equipoButton.setOnClickListener {
            startActivity(Intent(this, EquipoActivity::class.java))
        }
        mapaButton.setOnClickListener {
            startActivity(Intent(this, MapaActivity::class.java))
        }
        normativasButton.setOnClickListener {
            startActivity(Intent(this, NormativasActivity::class.java))
        }

        reportButton.setOnClickListener {
            startActivity(Intent(this, ReportActivity::class.java))
        }
        misReportesButton.setOnClickListener {
            startActivity(Intent(this, MisReportesActivity::class.java))
        }
        mapaReportesButton.setOnClickListener {
            startActivity(Intent(this, MapaReportesActivity::class.java))
        }
        changePasswordButton.setOnClickListener {
            startActivity(Intent(this, ChangePasswordActivity::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        checkLoginStatus()
        handler.postDelayed(runnable, DELAY_MS)
    }

    override fun onStop() {
        super.onStop()
        handler.removeCallbacks(runnable)
    }

    private fun checkLoginStatus() {
        val sharedPref = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPref.getBoolean("is_logged_in", false)
        val userName = sharedPref.getString("user_name", "")


        val loginStatusTextView: TextView? = binding.root.findViewById(R.id.loginStatusTextView)
        val reportButton: MaterialButton? = binding.root.findViewById(R.id.reportButton)
        val misReportesButton: MaterialButton? = binding.root.findViewById(R.id.misReportesButton)
        val mapaReportesButton: MaterialButton? = binding.root.findViewById(R.id.mapaReportesButton)
        val changePasswordButton: MaterialButton? = binding.root.findViewById(R.id.changePasswordButton)
        val normativasButton: MaterialButton? = binding.root.findViewById(R.id.normativasButton)

        if (isLoggedIn && !userName.isNullOrEmpty()) {
            loginStatusTextView?.text = "Cerrar Sesión ($userName)"


            reportButton?.visibility = View.VISIBLE
            misReportesButton?.visibility = View.VISIBLE
            mapaReportesButton?.visibility = View.VISIBLE
            changePasswordButton?.visibility = View.VISIBLE
            normativasButton?.visibility = View.VISIBLE


            reportButton?.isEnabled = true
            misReportesButton?.isEnabled = true
            mapaReportesButton?.isEnabled = true
            changePasswordButton?.isEnabled = true
            normativasButton?.isEnabled = true

        } else {
            loginStatusTextView?.text = "Iniciar Sesión"


            reportButton?.visibility = View.GONE
            misReportesButton?.visibility = View.GONE
            mapaReportesButton?.visibility = View.GONE
            changePasswordButton?.visibility = View.GONE
            normativasButton?.visibility = View.GONE


            reportButton?.isEnabled = false
            misReportesButton?.isEnabled = false
            mapaReportesButton?.isEnabled = false
            changePasswordButton?.isEnabled = false
            normativasButton?.isEnabled = false
        }
    }

    private fun logout() {
        val sharedPref = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            clear()
            apply()
        }
        checkLoginStatus()
        Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show()
    }
}