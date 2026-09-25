package be.autotoggle

import android.content.ComponentName
import android.content.Context
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.IBinder
import rikka.shizuku.Shizuku
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

object AutoController {

    private const val ANDROID_AUTO =
        "com.google.android.projection.gearhead"

    fun hasPermission(): Boolean {
        return try {
            Shizuku.pingBinder() &&
                Shizuku.checkSelfPermission() ==
                PackageManager.PERMISSION_GRANTED
        } catch (_: Exception) {
            false
        }
    }

    fun requestPermission() {
        try {
            if (Shizuku.pingBinder() &&
                Shizuku.checkSelfPermission() !=
                PackageManager.PERMISSION_GRANTED
            ) {
                Shizuku.requestPermission(1001)
            }
        } catch (_: Exception) {
        }
    }

    fun isEnabled(context: Context): Boolean {
        return try {
            val state =
                context.packageManager.getApplicationEnabledSetting(
                    ANDROID_AUTO
                )

            state != PackageManager.COMPONENT_ENABLED_STATE_DISABLED &&
            state != PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER
        } catch (_: Exception) {
            true
        }
    }

    fun setEnabled(context: Context, enable: Boolean): Boolean {

        if (!hasPermission()) return false

        val latch = CountDownLatch(1)
        var service: IPrivilegedService? = null

        val connection = object : ServiceConnection {

            override fun onServiceConnected(
                name: ComponentName?,
                binder: IBinder?
            ) {
                service =
                    IPrivilegedService.Stub.asInterface(binder)

                latch.countDown()
            }

            override fun onServiceDisconnected(
                name: ComponentName?
            ) {
                service = null
            }
        }

        val args = Shizuku.UserServiceArgs(
            ComponentName(
                context,
                PrivilegedService::class.java
            )
        )
            .daemon(false)
            .version(1)

        return try {

            Shizuku.bindUserService(args, connection)

            if (!latch.await(10, TimeUnit.SECONDS)) {
                false
            } else {
                service?.execute(enable) == 0
            }

        } catch (_: Exception) {
            false

        } finally {
            try {
                Shizuku.unbindUserService(
                    args,
                    connection,
                    true
                )
            } catch (_: Exception) {
            }
        }
    }
}
