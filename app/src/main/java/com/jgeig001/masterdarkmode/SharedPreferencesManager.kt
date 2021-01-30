package com.jgeig001.masterdarkmode

import android.content.Context

object SharedPreferencesManager {

    const val DEFAULT_STRING = ""

    private const val SHARED_FILE = "SHARED_FILE"

    fun write(context: Context, key: String, value: String) {
        val sharedPref = context.getSharedPreferences(SHARED_FILE, Context.MODE_PRIVATE)
        sharedPref.edit().putString(key, value).apply()
    }

    fun getString(context: Context, key: String): String? {
        val sharedPref = context.getSharedPreferences(SHARED_FILE, Context.MODE_PRIVATE)
        return sharedPref.getString(key, DEFAULT_STRING)
    }

}