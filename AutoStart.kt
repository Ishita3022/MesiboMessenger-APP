package org.mesibo.messenger



import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AutoStart : BroadcastReceiver() {
    //Alarm alarm = new Alarm();
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED || intent.action == MainApplication.restartIntent) {
            StartUpActivity.newInstance(context, true)
        }
    }
}
