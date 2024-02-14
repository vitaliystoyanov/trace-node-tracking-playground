package io.architecture.playground.data.remote.websocket.scarlet

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.reflect.TypeToken
import com.tinder.scarlet.Message
import com.tinder.scarlet.MessageAdapter
import okio.Buffer
import org.json.JSONObject
import java.io.OutputStreamWriter
import java.io.StringReader
import java.lang.reflect.Type
import kotlin.text.Charsets.UTF_8


/*
*
* Multiple @Receive annotated methods called for the same Message issue
* https://github.com/Tinder/Scarlet/issues/37#issuecomment-449871932
*
* */
class CustomGsonMessageAdapter<T> private constructor(
    private val gson: Gson,
    private val typeAdapter: TypeAdapter<T>,
    private val annotations: Array<Annotation>
) : MessageAdapter<T> {


    override fun fromMessage(message: Message): T {
        val stringValue = when (message) {
            is Message.Text -> message.value
            is Message.Bytes -> String(message.value)
        }
        val annotation = annotations.first { isReceiveAnnotation(it) } as Target

        return if (annotation.type == "") {
            deserializeJson(stringValue)
        } else {
            deserializeJsonGivenReceiveType(stringValue, annotation.type)
        }
    }

    override fun toMessage(data: T): Message {
        val buffer = Buffer()
        val writer = OutputStreamWriter(buffer.outputStream(), UTF_8)
        val jsonWriter = gson.newJsonWriter(writer)
        typeAdapter.write(jsonWriter, data)
        jsonWriter.close()
        val stringValue = buffer.readByteString().utf8()
        return Message.Text(stringValue)
    }

    private fun isReceiveAnnotation(annotation: Annotation): Boolean {
        return when (annotation) {
            is Target -> true
            else -> false
        }
    }

    private fun deserializeJson(jsonString: String): T {
        val jsonReader = gson.newJsonReader(StringReader(jsonString))
        return typeAdapter.read(jsonReader)!!
    }

    private fun deserializeJsonGivenReceiveType(jsonString: String, type: String): T {
        val jsonObject = JSONObject(jsonString)
        val typeFromJson = jsonObject.getString("type")

        if (typeFromJson != type) throw Exception()

        return deserializeJson(jsonString)
    }

    class Factory(
        private val gson: Gson = DEFAULT_GSON
    ) : MessageAdapter.Factory {

        @RequiresApi(Build.VERSION_CODES.P)
        override fun create(type: Type, annotations: Array<Annotation>): MessageAdapter<*> {
            val typeAdapter = gson.getAdapter(TypeToken.get(type))
            return CustomGsonMessageAdapter(gson, typeAdapter, annotations)
        }

        companion object {
            private val DEFAULT_GSON = Gson()
        }
    }
}