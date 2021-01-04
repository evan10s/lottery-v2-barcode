package at.str.lottery.barcode.model

import android.net.Uri
import android.util.Log
import at.str.lottery.barcode.util.UriAdapter
import com.google.gson.Gson
import com.google.gson.GsonBuilder

class KioskConfig(
    var kioskId: String = "",
    var serverUrl: Uri? = null
) {
    /**
     * Indicates if this KioskConfig is valid (i.e., not empty, etc.)
     */
    fun isValid(): Boolean {
        return kioskId.isNotBlank() && serverUrl != null
    }

    companion object {
        private const val TAG = "KioskConfig"

        private const val KIOSK_ID_LENGTH = 6

        /**
         * Validation method to identify kiosk configuration barcodes, which should have the format
         * {
         *      kiosk_id: string,
         *      server_url: string IP address or URl
         * }
         *
         * For the purpose of this function and our use case, this is just a really simple
         * length-based heuristic that should be more than sufficient
         */
        @JvmStatic
        fun isKioskConfigBarcode(barcode: String): Boolean {
            return barcode.length > KIOSK_ID_LENGTH
        }

        @JvmStatic
        fun decodeKioskConfigBarcode(barcode: String): KioskConfig? {
            return try {
                val gson = GsonBuilder()
                    .registerTypeAdapter(Uri::class.java, UriAdapter())
                    .create()

                val kioskConfig = gson.fromJson(barcode, KioskConfig::class.java)
                Log.i(TAG, "[kc] Kiosk ID: ${kioskConfig.kioskId} and server URL: ${kioskConfig.serverUrl}")

                kioskConfig
            } catch (e: Throwable) {
                Log.e(TAG, "Error during kiosk config barcode detection: ${e.localizedMessage}")
                null
            }
        }


    }
}