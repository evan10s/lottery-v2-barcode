package at.str.lottery.barcode.util

import android.net.Uri
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

/**
 * Gson can't handle Uri's, hence this.'
 * From a friendly StackOverflow-er (but Kotlin-ified): https://stackoverflow.com/a/32404511
 */
public final class UriAdapter : TypeAdapter<Uri>() {
    override fun write(out: JsonWriter?, uri: Uri?) {
        out?.value(uri?.toString())
    }

    override fun read(input: JsonReader?): Uri {
        return Uri.parse(input?.nextString())
    }

}