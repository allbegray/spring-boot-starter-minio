package allbegray.spring.boot.autoconfigure.minio

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "spring.minio")
class MinioProperties(
    val url: String,
    val accessKey: String,
    val secretKey: String,
    val defaultBucket: String = "",
    val createBucket: Boolean = false,
)
