package at.str.lottery.barcode.model

import kotlinx.datetime.*

data class BarcodeScanResult(
    var data: String = "",
    var success: Boolean = false,
    var message: String = "",
    var timestamp: Instant = Clock.System.now(),
) {
    fun isRecent(now: Instant = Clock.System.now()): Boolean {
        return timestamp.until(
            now,
            DateTimeUnit.SECOND,
            TimeZone.currentSystemDefault()
        ) <= RECENT_THRESHOLD_SECONDS
    }

    companion object {
        private const val RECENT_THRESHOLD_SECONDS = 5
    }
}