package com.jgeig001.masterdarkmode

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

class MainActivity : AppCompatActivity() {

    private lateinit var modeDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        setUpDisplayMode()
    }

    private fun setUpDisplayMode() {
        if (!listOf(
                AppCompatDelegate.MODE_NIGHT_NO,
                AppCompatDelegate.MODE_NIGHT_YES,
                AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            ).contains(
                AppCompatDelegate.getDefaultNightMode()
            )
        ) {
            // app restart
            val mode: String? =
                SharedPreferencesManager.getString(
                    applicationContext,
                    DisplayModeState.DISPLAY_MODE
                )
            if (mode == SharedPreferencesManager.DEFAULT_STRING) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            } else {
                AppCompatDelegate.setDefaultNightMode(DisplayModeState.getInt(mode))
            }
        }
    }


    fun onOpenDisplayModeAlertDialog(view: View) {
        val curPos = when (AppCompatDelegate.getDefaultNightMode()) {
            AppCompatDelegate.MODE_NIGHT_NO -> 0
            AppCompatDelegate.MODE_NIGHT_YES -> 1
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM -> 2
            AppCompatDelegate.MODE_NIGHT_UNSPECIFIED -> {
                if (supportsSystemDarkMode()) {
                    2
                } else {
                    0
                }
            }
            else -> 0 // light mode as default
        }
        var items = arrayOf<CharSequence>(
            getString(R.string.mode_radio_light),
            getString(R.string.mode_radio_dark)
        )
        if (supportsSystemDarkMode()) {
            items += getString(R.string.mode_radio_system)
        }

        val builder =
            androidx.appcompat.app.AlertDialog.Builder(this).setTitle(R.string.mode_dialog_title)
                .setSingleChoiceItems(items, curPos) { dialog, i ->
                    onThemeSelection(i)
                    dialog.dismiss() // close
                }
                .setNegativeButton(android.R.string.cancel) { dialog, _ -> dialog.dismiss() }
        modeDialog = builder.create()
        modeDialog.show()
    }

    private fun supportsSystemDarkMode(): Boolean {
        return android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.P
    }

    private fun onThemeSelection(position: Int) {
        when (position) {
            0 -> {
                SharedPreferencesManager.write(
                    this,
                    DisplayModeState.DISPLAY_MODE,
                    DisplayModeState.getString(AppCompatDelegate.MODE_NIGHT_NO)
                )
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            1 -> {
                SharedPreferencesManager.write(
                    this,
                    DisplayModeState.DISPLAY_MODE,
                    DisplayModeState.getString(AppCompatDelegate.MODE_NIGHT_YES)
                )
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            2 -> {
                SharedPreferencesManager.write(
                    this,
                    DisplayModeState.DISPLAY_MODE,
                    DisplayModeState.getString(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                )
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }
            else -> {
                SharedPreferencesManager.write(
                    this,
                    DisplayModeState.DISPLAY_MODE,
                    DisplayModeState.getString(AppCompatDelegate.MODE_NIGHT_NO)
                )
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

}