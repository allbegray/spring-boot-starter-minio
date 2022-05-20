package allbegray.spring.minio

object EventType {
    // Supported Object Event Types
    const val OBJECT_CREATED_PUT = "s3:ObjectCreated:Put"
    const val OBJECT_CREATED_POST = "s3:ObjectCreated:Post"
    const val OBJECT_CREATED_PUT_RETENTION = "s3:ObjectCreated:PutRetention"
    const val OBJECT_CREATED_PUT_LEGAL_HOLD = "s3:ObjectCreated:PutLegalHold"
    const val OBJECT_CREATED_COPY = "s3:ObjectCreated:Copy"
    const val OBJECT_CREATED_COMPLETE_MULTIPART_UPLOAD = "s3:ObjectCreated:CompleteMultipartUpload"

    const val OBJECT_ACCESSED_HEAD = "s3:ObjectAccessed:Head"
    const val OBJECT_ACCESSED_GET = "s3:ObjectAccessed:Get"
    const val OBJECT_ACCESSED_GET_RETENTION = "s3:ObjectAccessed:GetRetention"
    const val OBJECT_ACCESSED_GET_LEGAL_HOLD = "s3:ObjectAccessed:GetLegalHold"

    const val OBJECT_REMOVED_DELETE = "s3:ObjectRemoved:Delete"
    const val OBJECT_REMOVED_DELETE_MARKER_CREATED = "s3:ObjectRemoved:DeleteMarkerCreated"

    // Supported Replication Event Types
    const val OPERATION_FAILED_REPLICATION = "s3:Replication:OperationFailedReplication"
    const val OPERATION_COMPLETED_REPLICATION = "s3:Replication:OperationCompletedReplication"
    const val OPERATION_NOT_TRACKED = "s3:Replication:OperationNotTracked"
    const val OPERATION_MISSED_THRESHOLD = "s3:Replication:OperationMissedThreshold"
    const val OPERATION_REPLICATED_AFTER_THRESHOLD = "s3:Replication:OperationReplicatedAfterThreshold"

    // Supported ILM Transition Event Types
    const val OBJECT_RESTORE_POST = "s3:ObjectRestore:Post"
    const val OBJECT_RESTORE_COMPLETED = "s3:ObjectRestore:Completed"

    // Supported Global Event Types (Only supported through ListenNotification API)
    const val BUCKET_CREATED = "s3:BucketCreated"
    const val BUCKET_REMOVED = "s3:BucketRemoved"
}