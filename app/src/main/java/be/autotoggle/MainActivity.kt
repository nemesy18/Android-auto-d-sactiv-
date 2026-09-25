package be.autotoggle

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import rikka.shizuku.Shizuku

class MainActivity : AppCompatActivity() {

    private lateinit var status: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        status = findViewById(R.id.status)

        findViewById<Button>(R.id.permission).setOnClickListener {
            AutoController.requestPermission()
            refreshDelayed()
        }

        findViewById<Button>(R.id.openShizuku).setOnClickListener {
            val intent =
                packageManager.getLaunchIntentForPackage("moe.shizuku.privileged.api")

            if (intent != null) {
                startActivity(intent)
            }
        }

        findViewById<Button>(R.id.disable).setOnClickListener {
            act(false)
        }

        findViewById<Button>(R.id.enable).setOnClickListener {
            act(true)
        }
    }

    override fun onResume() {
        super.onResume()
        refresh()
    }

    private fun act(enable: Boolean) {
        status.text = "Commande en cours…"

        Thread {
            val success = AutoController.setEnabled(this, enable)

            runOnUiThread {
                status.text =
                    if (success) {
                        if (enable) {
                            "Android Auto activé."
                        } else {
                            "Android Auto désactivé."
                        }
                    } else {
                        "Échec de la commande. Vérifiez Shizuku et son autorisation."
                    }

                refreshDelayed()
            }
        }.start()
    }

    private fun refreshDelayed() {
        status.postDelayed({ refresh() }, 800)
    }

    private fun refresh() {
        val shizuku =
            try {
                if (Shizuku.pingBinder()) "actif" else "arrêté"
            } catch (_: Exception) {
                "arrêté"
            }

        val permission =
            if (AutoController.hasPermission()) {
                "autorisée"
            } else {
                "non autorisée"
            }

        val androidAuto =
            if (AutoController.isEnabled(this)) {
                "ACTIVÉ"
            } else {
                "DÉSACTIVÉ"
            }

        status.text =
            "Shizuku : $shizuku ($permission)\nAndroid Auto : $androidAuto"
    }
}
