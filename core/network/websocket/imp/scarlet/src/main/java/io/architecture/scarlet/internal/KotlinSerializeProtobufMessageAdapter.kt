package io.architecture.scarlet.internal

import android.util.Log
import com.tinder.scarlet.Message
import com.tinder.scarlet.MessageAdapter
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.protobuf.ProtoBuf
import kotlinx.serialization.serializer
import org.jetbrains.annotations.ApiStatus.Experimental
import java.lang.reflect.Type
import kotlin.reflect.KClass

@Experimental
class KotlinSerializeProtobufMessageAdapter<T : Any>(
    private val typeClass: KClass<T>,
    private val annotations: Array<Annotation>
) : MessageAdapter<T> {

    @OptIn(InternalSerializationApi::class)
    override fun fromMessage(message: Message): T {
        val bytes = when (message) {
            is Message.Text -> throw RuntimeException("Not supported message format!")
            is Message.Bytes -> message.value
        }
        val annotation = annotations.first { isReceiveAnnotation(it) } as Target

        return deserialize(typeClass.serializer(), bytes)
    }

    @OptIn(InternalSerializationApi::class)
    override fun toMessage(data: T): Message {
        return Message.Bytes(serialize(typeClass.serializer(), data))
    }

    @OptIn(ExperimentalSerializationApi::class)
    private fun serialize(serializer: KSerializer<T>, data: T): ByteArray {
        return ProtoBuf.encodeToByteArray(serializer, data)
    }

    @OptIn(ExperimentalSerializationApi::class)
    private fun deserialize(serializer: KSerializer<T>, bytes: ByteArray): T {
        return ProtoBuf.decodeFromByteArray(serializer, bytes)
    }

    private fun isReceiveAnnotation(annotation: Annotation): Boolean {
        return when (annotation) {
            is Target -> true
            else -> false
        }
    }

    class Factory : MessageAdapter.Factory {

        override fun create(type: Type, annotations: Array<Annotation>): MessageAdapter<*> {
            require(type is Class<*>)
            Log.d("SCARLET_ADAPTER", "MessageAdapter Factory: created adapter for ${type.name}")
            return KotlinSerializeProtobufMessageAdapter(
                type::class,
                annotations
            ) // TODO type::class wrong parameter
        }
    }
}