package io.net.spcokie.infra.oss

import io.minio.*
import io.minio.messages.DeleteObject
import io.quarkus.logging.Log
import io.smallrye.mutiny.Uni
import io.smallrye.mutiny.coroutines.asUni
import io.vertx.mutiny.core.Vertx
import jakarta.enterprise.context.ApplicationScoped
import kotlinx.coroutines.*

/**
 *@author Augenstern
 *@since 2023/10/10
 */
@ApplicationScoped
@OptIn(ExperimentalCoroutinesApi::class)
class ImageOss(private val minioClient: MinioClient) {

    companion object {
        private const val BUCKET = "image"
    }

    private val scope = CoroutineScope(SupervisorJob() + CoroutineName("imageOss") + Dispatchers.IO)

    fun fetchImage(path: String): Uni<ByteArray> {
        return scope.async {
            val args = GetObjectArgs.builder().bucket(BUCKET).`object`(path).build()
            minioClient.getObject(args).use {
                it.readBytes()
            }
        }.asUni()
    }

    fun saveImage(path: String, bytes: ByteArray): Uni<ObjectWriteResponse> {
        return scope.async {
            bytes.inputStream().use {
                val args = PutObjectArgs.builder().bucket(BUCKET).`object`(path)
                    .stream(it, -1, ObjectWriteArgs.MIN_MULTIPART_SIZE.toLong()).build()
                minioClient.putObject(args)
            }
        }.asUni()
    }

    fun removeImages(paths: List<String>): Uni<Void> {
        val context = Vertx.currentContext()
        return Uni.createFrom().item(paths.map { DeleteObject(it) })
            .call { deleteObject ->
                scope.async {
                    val args = RemoveObjectsArgs.builder().bucket(BUCKET).objects(deleteObject).build()
                    minioClient.removeObjects(args).forEach {
                        val deleteError = it.get()
                        if (deleteError != null) {
                            Log.error(deleteError.message())
                        }
                    }
                }.asUni()
            }
            .emitOn { context.runOnContext(it) }
            .replaceWithVoid()
    }

}