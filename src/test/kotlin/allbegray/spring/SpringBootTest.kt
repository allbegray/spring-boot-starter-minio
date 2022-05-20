package allbegray.spring

import allbegray.spring.boot.autoconfigure.minio.MinioAutoConfiguration
import allbegray.spring.boot.autoconfigure.minio.MinioListenerConfiguration
import allbegray.spring.boot.autoconfigure.minio.MinioProperties
import allbegray.spring.minio.MinioTemplate
import allbegray.spring.minio.annotation.MinioListener
import io.minio.messages.Event
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.stereotype.Component

@Component
class TestMinioListener {

    @MinioListener("s3:ObjectCreated:Put", "s3:ObjectCreated:Post")
    fun putOrPostEvent(event: Event) {
        println("putOrPostEvent : $event")
    }

    @MinioListener("s3:ObjectRemoved:*")
    fun deleteEvent(event: Event) {
        println("deleteEvent : $event")
    }
}

@SpringBootTest(classes = [MinioAutoConfiguration::class, MinioListenerConfiguration::class, TestMinioListener::class])
class SpringBootTest {

    @Autowired
    lateinit var minioTemplate: MinioTemplate

    @Autowired
    lateinit var minioProperties: MinioProperties

    @Test
    fun listenerTest() {
        try {
            minioTemplate.putObject("text.txt", "test")
            Thread.sleep(1 * 1000)
            minioTemplate.removeObject("text.txt")
            Thread.sleep(1 * 1000)
        } finally {
            minioTemplate.removeBucket(minioProperties.defaultBucket)
        }
    }

    @Test
    fun listBucketsTest() {
        val listBuckets = minioTemplate.listBuckets()
        Assertions.assertTrue(listBuckets.isNotEmpty())
    }
}