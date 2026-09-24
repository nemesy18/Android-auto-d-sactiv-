package be.autotoggle
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService

class AutoTileService: TileService() {
    override fun onStartListening(){ super.onStartListening(); update() }
    override fun onClick(){ super.onClick(); if(!AutoController.hasPermission()){ startActivityAndCollapse(packageManager.getLaunchIntentForPackage(packageName)); return }
        val target=!AutoController.isEnabled(this); Thread { AutoController.setEnabled(target); runOnUiThread { update() } }.start()
    }
    private fun update(){ val on=AutoController.isEnabled(this); qsTile?.apply { state=if(on) Tile.STATE_ACTIVE else Tile.STATE_INACTIVE; label=if(on) "Android Auto ON" else "Android Auto OFF"; updateTile() } }
}
