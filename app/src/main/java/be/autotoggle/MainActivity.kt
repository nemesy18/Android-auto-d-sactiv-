package be.autotoggle

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var status: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState); setContentView(R.layout.activity_main); status=findViewById(R.id.status)
        findViewById<Button>(R.id.permission).setOnClickListener { AutoController.requestPermission(); refresh() }
        findViewById<Button>(R.id.openShizuku).setOnClickListener {
            val i = packageManager.getLaunchIntentForPackage("moe.shizuku.privileged.api")
            if (i != null) startActivity(i) else startActivity(Intent(Intent.ACTION_VIEW, android.net.Uri.parse("https://shizuku.rikka.app/download/")))
        }
        findViewById<Button>(R.id.disable).setOnClickListener { act(false) }
        findViewById<Button>(R.id.enable).setOnClickListener { act(true) }
    }
    override fun onResume() { super.onResume(); refresh() }
    private fun act(enable:Boolean) { Thread { val r=AutoController.setEnabled(enable); runOnUiThread { status.text=(if(r.first) "Commande réussie : " else "Échec : ")+r.second; refreshDelayed() } }.start() }
    private fun refreshDelayed(){ status.postDelayed({refresh()},800) }
    private fun refresh(){
        val s=if(AutoController.shizukuRunning()) "actif" else "arrêté"; val p=if(AutoController.hasPermission()) "autorisée" else "non autorisée"; val a=if(AutoController.isEnabled(this)) "ACTIVÉ" else "DÉSACTIVÉ"
        status.text="Shizuku : $s ($p)\nAndroid Auto : $a"
    }
}
