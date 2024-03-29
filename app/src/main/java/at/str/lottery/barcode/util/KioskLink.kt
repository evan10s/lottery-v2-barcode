package at.str.lottery.barcode.util

import android.net.Uri
import android.util.Log
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.util.concurrent.TimeUnit

const val TAG = "KioskLink";

class KioskLink : WebSocketListener() {
    private lateinit var webSocket: WebSocket

    public fun run(ip: String, kioskId: String) {
        val client: OkHttpClient = OkHttpClient.Builder()
            .readTimeout(0, TimeUnit.MILLISECONDS)
            .build()

        val url = Uri.Builder()
            .scheme("ws")
            .authority(ip)
            .path("kiosk")
            .appendPath(kioskId)
            .build()
            .toString()
        Log.d(TAG, url)

        val request: Request = Request.Builder()
            .url(url)
            .build()

        client.newWebSocket(request, this)

        client.dispatcher.executorService.shutdown()
    }

    fun sendBarcode(barcode: String?) {
        Log.d(TAG, "Send barcode $barcode")

        try {
            Log.d(TAG, "Sending barcode info")
            webSocket.send(
                JSONObject().put("barcode", barcode)
                    .put("msgType", "data")
                    .toString()
            )
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }


    override fun onOpen(webSocket: WebSocket, response: Response) {
        this.webSocket = webSocket
        Log.d(TAG, "onOpen")

        try {
            Log.d(TAG, "Sending barcode info")
            webSocket.send(
                JSONObject().put("message", "Barcode scanner connected")
                    .put("msgType", "info")
                    .toString()
            )
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosed(webSocket, code, reason)
        Log.d(TAG, "onClosed")
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosing(webSocket, code, reason)
        Log.d(TAG, "onClosing")
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        t.printStackTrace()
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        Log.d(TAG, "MESSAGE: $text")
        // The only point of this is to check for the "searchForScanner" message type
        // That's why there's no msgData here
        // The only point of this is to check for the "searchForScanner" message type
        // That's why there's no msgData here
        var msgType = ""
        try {
            val messageObj = JSONObject(text)
            msgType = messageObj.getString("msgType")
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        if (msgType == "searchForScanner") {
            try {
                webSocket.send(
                    JSONObject()
                        .put("msgType", "searchAcknowledge")
                        .put("message", "Scanner is connected")
                        .toString()
                )
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
    }
}