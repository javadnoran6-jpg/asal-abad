package com.example.util

import android.content.Context

/**
 * سن حساب: روز اول (نصب/ساخت حساب) صفر است و هر ۲۴ ساعت یکی بالا می‌رود.
 */
object AccountAge {
    private const val PREFS = "asal_account"
    private const val KEY_JOIN = "join_ms"
    private const val DAY_MS = 24L * 60 * 60 * 1000

    fun days(context: Context): Int {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        var join = prefs.getLong(KEY_JOIN, 0L)
        val now = System.currentTimeMillis()
        if (join == 0L || join > now) {
            join = now
            prefs.edit().putLong(KEY_JOIN, join).apply()
        }
        return ((now - join) / DAY_MS).toInt()
    }

    /** با حذف حساب، سن دوباره از صفر شروع می‌شود. */
    fun reset(context: Context) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit().remove(KEY_JOIN).apply()
    }
}
