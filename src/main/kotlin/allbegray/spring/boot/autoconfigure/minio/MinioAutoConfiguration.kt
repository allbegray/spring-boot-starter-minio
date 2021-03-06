package allbegray.spring.boot.autoconfigure.minio

import allbegray.spring.minio.MinioTemplate
import io.minio.BucketExistsArgs
import io.minio.MakeBucketArgs
import io.minio.MinioClient
import okhttp3.OkHttpClient
import org.springframework.beans.factory.ObjectProvider
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@ConditionalOnClass(MinioClient::class)
@EnableConfigurationProperties(MinioProperties::class)
open class MinioAutoConfiguration(private val minioProperties: MinioProperties) {

    @Bean
    @ConditionalOnMissingBean(OkHttpClient::class)
    open fun okHttpClient(): OkHttpClient = OkHttpClient()

    @Bean
    @ConditionalOnMissingBean(MinioClient::class)
    open fun minioClient(
        okHttpClient: OkHttpClient,
        configurationCustomizers: ObjectProvider<MinioClientBuilderCustomizer>,
    ): MinioClient {
        val minioClient = MinioClient.builder()
            .endpoint(minioProperties.url)
            .credentials(minioProperties.accessKey, minioProperties.secretKey)
            .also { builder ->
                configurationCustomizers.forEach { it.customize(builder) }
            }
            .build()!!

        if (minioProperties.createBucket) {
            val bucket = minioProperties.defaultBucket
            if (minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build()).not()) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build())
            }
        }

        return minioClient
    }

    @Bean
    open fun minioTemplate(minioClient: MinioClient): MinioTemplate =
        MinioTemplate(minioClient, minioProperties.defaultBucket)
}
