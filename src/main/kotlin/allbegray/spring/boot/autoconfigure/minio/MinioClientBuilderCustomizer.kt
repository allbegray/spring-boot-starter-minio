package allbegray.spring.boot.autoconfigure.minio

import io.minio.MinioClient

@FunctionalInterface
interface MinioClientBuilderCustomizer {
    fun customize(builder: MinioClient.Builder)
}