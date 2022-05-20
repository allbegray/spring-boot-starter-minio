package allbegray.spring.minio

import io.minio.*
import io.minio.messages.*
import java.io.ByteArrayInputStream
import java.io.File
import java.io.InputStream

class MinioTemplate(
    private val minioClient: MinioClient,
    private val defaultBucket: String,
) {
    fun listBuckets(): List<Bucket> {
        return minioClient.listBuckets(ListBucketsArgs.builder().build())
    }

    fun bucketExists(name: String): Boolean {
        return minioClient.bucketExists(BucketExistsArgs.builder().bucket(name).build())
    }

    fun makeBucket(name: String) {
        return minioClient.makeBucket(MakeBucketArgs.builder().bucket(name).build())
    }

    fun removeBucket(name: String) {
        return minioClient.removeBucket(RemoveBucketArgs.builder().bucket(name).build())
    }

    fun getBucketVersioning(bucket: String = defaultBucket): VersioningConfiguration {
        return minioClient.getBucketVersioning(GetBucketVersioningArgs.builder().bucket(bucket).build())
    }

    fun statObject(path: String, bucket: String = defaultBucket): StatObjectResponse {
        return minioClient.statObject(StatObjectArgs.builder().bucket(bucket).`object`(path).build())
    }

    // @formatter:off
    fun putObject(path: String, text: String, partSize: Long = 0, bucket: String = defaultBucket): ObjectWriteResponse {
        val bytea = text.toByteArray()
        return putObject(path, ByteArrayInputStream(bytea), bytea.size.toLong(), partSize, bucket,)
    }
    // @formatter:on

    // @formatter:off
    fun putObject(path: String, file: File, partSize: Long = 0, bucket: String = defaultBucket): ObjectWriteResponse {
        return putObject(path, file.inputStream(), file.length(), partSize, bucket,)
    }
    // @formatter:on

    // @formatter:off
    fun putObject(path: String, `is`: InputStream, objectSize: Long, partSize: Long = 0, bucket: String = defaultBucket): ObjectWriteResponse {
        return `is`.use {
            minioClient.putObject(PutObjectArgs.builder().stream(it, objectSize, partSize).bucket(bucket).`object`(path).build())!!
        }
    }
    // @formatter:on

    // @formatter:off
    fun uploadSnowballObjects(snowballObject: List<SnowballObject>, bucket: String = defaultBucket): ObjectWriteResponse {
        return minioClient.uploadSnowballObjects(UploadSnowballObjectsArgs.builder().bucket(bucket).objects(snowballObject).build())
    }
    // @formatter:on

    // @formatter:off
    fun getObject(path: String, bucket: String = defaultBucket): GetObjectResponse {
        return minioClient.getObject(GetObjectArgs.builder().bucket(bucket).`object`(path).build())!!
    }
    // @formatter:on

    // @formatter:off
    fun downloadObject(path: String, dest: File, overwrite: Boolean = false, bucket: String = defaultBucket) {
        minioClient.downloadObject(DownloadObjectArgs.builder().bucket(bucket).`object`(path).overwrite(overwrite).filename(dest.absolutePath).build())
    }
    // @formatter:on

    // @formatter:off
    fun removeObject(path: String, bucket: String = defaultBucket) {
        return minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucket).`object`(path).build())
    }
    // @formatter:on

    // @formatter:off
    fun removeObjects(deleteObjects: List<DeleteObject>, bucket: String = defaultBucket): Sequence<DeleteError> {
        return minioClient.removeObjects(RemoveObjectsArgs.builder().bucket(bucket).objects(deleteObjects).build()).asSequence().map { it.get() }
    }
    // @formatter:on

    // @formatter:off
    fun listObjects(prefix: String, recursive: Boolean = false, bucket: String = defaultBucket): Sequence<Item> {
        return minioClient.listObjects(ListObjectsArgs.builder().bucket(bucket).prefix(prefix).recursive(recursive).build()).asSequence().map { it.get() }
    }
    // @formatter:on

    // @formatter:off
    fun makeDir(path: String, bucket: String = defaultBucket): ObjectWriteResponse {
        val fixed = if (path.endsWith("/").not()) "$path/" else path
        return putObject(fixed, ByteArrayInputStream(ByteArray(0)), 0, bucket = bucket)
    }
    // @formatter:on
}
