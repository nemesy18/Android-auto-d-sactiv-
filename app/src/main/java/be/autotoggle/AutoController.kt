package be.autotoggle

import android.content.Context
import android.content.pm.PackageManager
import rikka.shizuku.Shizuku
import rikka.shizuku.ShizukuRemoteProcess

object AutoController {
    // Current Android Auto package on Google/Samsung builds.
    const val AA = "com.google.android.projection.gearhead"
    const val REQ = 42

    fun shizukuRunning() = try { Shizuku.pingBinder() } catch (_: Throwable) { false }
    fun hasPermission() = shizukuRunning() && Shizuku.checkSelfPermission() == PackageManager.PERMISSION_GRANTED
    fun requestPermission() { if (shizukuRunning()) Shizuku.requestPermission(REQ) }

    fun isEnabled(context: Context): Boolean = try {
        val ai = context.packageManager.getApplicationInfo(AA, PackageManager.ApplicationInfoFlags.of(0))
        ai.enabled
    } catch (_: Throwable) { true }

    fun setEnabled(enable: Boolean): Pair<Boolean,String> {
        if (!hasPermission()) return false to "Shizuku n'est pas actif ou autorisé."
        return try {
            val cmd = if (enable) arrayOf("pm", "enable", AA) else arrayOf("pm", "disable-user", "--user", "0", AA)
            val p: ShizukuRemoteProcess = Shizuku.newProcess(cmd, null, null)
            val out = p.inputStream.bufferedReader().readText().trim()
            val err = p.errorStream.bufferedReader().readText().trim()
            val code = p.waitFor()
            (code == 0) to (if (code == 0) out.ifBlank { "OK" } else err.ifBlank { "Erreur $code" })
        } catch (t: Throwable) { false to (t.message ?: t.javaClass.simpleName) }
    }
}
