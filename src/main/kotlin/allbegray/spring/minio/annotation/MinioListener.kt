package allbegray.spring.minio.annotation

import java.lang.annotation.Inherited

@Inherited
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class MinioListener(
    vararg val value: String,
    val bucket: String = "",
    val prefix: String = "",
    val suffix: String = ""
)
