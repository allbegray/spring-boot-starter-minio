package allbegray.spring.boot.autoconfigure.minio

import allbegray.spring.minio.annotation.MinioListener
import io.minio.ListenBucketNotificationArgs
import io.minio.MinioClient
import org.springframework.beans.factory.DisposableBean
import org.springframework.beans.factory.SmartInitializingSingleton
import org.springframework.boot.autoconfigure.AutoConfigureAfter
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Configuration
@AutoConfigureAfter(MinioAutoConfiguration::class)
open class MinioListenerConfiguration(
    private val minioClient: MinioClient,
    private val minioProperties: MinioProperties,
) : ApplicationContextAware, SmartInitializingSingleton, DisposableBean {

    lateinit var ctx: ApplicationContext
    private var newFixedThreadPool: ExecutorService? = null

    override fun afterSingletonsInstantiated() {
        val targets = listOf(Service::class.java, Component::class.java)
            .flatMap { ctx.getBeansWithAnnotation(it).values }
            .toSet()
            .flatMap { bean ->
                bean.javaClass.declaredMethods
                    .map { method ->
                        if (method.isAnnotationPresent(MinioListener::class.java)) {
                            bean to method
                        } else {
                            null
                        }
                    }
            }
            .filterNotNull()

        if (targets.isEmpty()) return

        newFixedThreadPool = Executors.newFixedThreadPool(targets.size)

        for ((bean, method) in targets) {
            val minioListener = method.getAnnotation(MinioListener::class.java)

            newFixedThreadPool!!.submit {
                val listenBucketNotification = minioClient.listenBucketNotification(
                    ListenBucketNotificationArgs.builder()
                        .bucket(minioListener.bucket.ifEmpty { minioProperties.defaultBucket })
                        .prefix(minioListener.prefix.ifEmpty { null })
                        .suffix(minioListener.suffix.ifEmpty { null })
                        .events(minioListener.value)
                        .build()
                )

                listenBucketNotification.use { itr ->
                    itr.forEachRemaining {
                        val events = it.get().events()
                        for (event in events) {
                            method.invoke(bean, event)
                        }
                    }
                }
            }
        }
    }

    override fun setApplicationContext(applicationContext: ApplicationContext) {
        this.ctx = applicationContext
    }

    override fun destroy() {
        newFixedThreadPool?.shutdown()
    }
}
