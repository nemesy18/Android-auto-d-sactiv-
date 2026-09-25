package be.autotoggle

class PrivilegedService : IPrivilegedService.Stub() {

    override fun execute(enable: Boolean): Int {
        val packageName = "com.google.android.projection.gearhead"

        val command = if (enable) {
            arrayOf("pm", "enable", packageName)
        } else {
            arrayOf("pm", "disable-user", "--user", "0", packageName)
        }

        val process = Runtime.getRuntime().exec(command)
        process.inputStream.bufferedReader().use { it.readText() }
        process.errorStream.bufferedReader().use { it.readText() }

        return process.waitFor()
    }
}
