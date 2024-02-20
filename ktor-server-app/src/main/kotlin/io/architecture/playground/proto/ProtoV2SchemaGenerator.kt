import io.architecture.network.websocket.api.model.NetworkClientTime
import io.architecture.network.websocket.api.model.NetworkServerTime
import io.architecture.network.websocket.api.model.NetworkTrace
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.protobuf.schema.ProtoBufSchemaGenerator
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardOpenOption


@OptIn(ExperimentalSerializationApi::class)
fun main() {
    val descriptors = listOf(
        NetworkTrace.serializer().descriptor,
        NetworkClientTime.serializer().descriptor,
        NetworkServerTime.serializer().descriptor
    )
    descriptors.forEach {
        val content = ProtoBufSchemaGenerator.generateSchemaText(it)
        println(content)
        println("=".repeat(100))

        val fileName = it.serialName.substringAfterLast(".")
        writeProto(fileName, content)
    }
}

private fun writeProto(name: String, content: String) {
    val fileName = "${name}.proto"
    val file = File("${File("").absoluteFile}/${fileName}")

    println("Proto: ${file.absoluteFile} ")

    if (!file.exists()) {
        file.delete()
        file.createNewFile()
    }
    Files.write(file.toPath(), content.toByteArray(), StandardOpenOption.WRITE)
}