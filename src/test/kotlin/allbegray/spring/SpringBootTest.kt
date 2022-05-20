package allbegray.spring

import allbegray.spring.boot.autoconfigure.minio.MinioAutoConfiguration
import allbegray.spring.boot.autoconfigure.minio.MinioListenerConfiguration
import allbegray.spring.minio.MinioTemplate
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [MinioAutoConfiguration::class, MinioListenerConfiguration::class])
class SpringBootTest {

    @Autowired
    lateinit var minioTemplate: MinioTemplate

    @Test
    fun listBucketsTest() {
        val listBuckets = minioTemplate.listBuckets()
        Assertions.assertTrue(listBuckets.isNotEmpty())
    }
}