package com.church.util


import com.church.config.AwsProperties
import com.church.payload.s3.PresignedUploadResponse
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest
import java.time.Duration
import java.util.*

@Service
class S3ServiceUtil(
    private val s3Client: S3Client,
    private val s3Presigner: S3Presigner,
    private val awsProperties: AwsProperties
) {

    fun uploadFile(file: MultipartFile, folder: String = "uploads"): String {
        val key = "$folder/${UUID.randomUUID()}-${file.originalFilename}"

        s3Client.putObject(
            PutObjectRequest.builder()
                .bucket(awsProperties.bucketName)
                .key(key)
                .contentType(file.contentType)
                .build(),
            RequestBody.fromInputStream(file.inputStream, file.size)
        )

        return getFileUrl(key)
    }

    fun getFileUrl(key: String): String {
        return "https://${awsProperties.bucketName}.s3.${awsProperties.region}.amazonaws.com/$key"
    }

    fun deleteFile(key: String) {
        s3Client.deleteObject(
            DeleteObjectRequest.builder()
                .bucket(awsProperties.bucketName)
                .key(key)
                .build()
        )
    }

    fun listFiles(prefix: String = ""): List<String> {
        val response = s3Client.listObjectsV2(
            ListObjectsV2Request.builder()
                .bucket(awsProperties.bucketName)
                .prefix(prefix)
                .build()
        )

        return response.contents().map { it.key() }
    }

    fun generatePresignedUrl(key: String, durationMinutes: Long = 15): String {
        val presigner = S3Presigner.create()

        val request = PutObjectRequest.builder()
            .bucket(awsProperties.bucketName)
            .key(key)
            .build()

        val presignRequest = PutObjectPresignRequest.builder()
            .signatureDuration(java.time.Duration.ofMinutes(durationMinutes))
            .putObjectRequest(request)
            .build()

        return presigner.presignPutObject(presignRequest).url().toString()
    }

    fun generatePresignedUploadUrl(
        folder: String,
        userId: UUID,
        fileName: String,
        contentType: String
    ): PresignedUploadResponse {
        val sanitizedFileName = fileName.replace(Regex("[^A-Za-z0-9._-]"), "_")
        val objectKey = "$folder/$userId/${UUID.randomUUID()}_$sanitizedFileName"

        val putObjectRequest = PutObjectRequest.builder()
            .bucket(awsProperties.bucketName)
            .key(objectKey)
            .contentType(contentType)
            .build()

        val presignRequest = PutObjectPresignRequest.builder()
            .signatureDuration(Duration.ofMinutes(10))
            .putObjectRequest(putObjectRequest)
            .build()

        val presignedRequest = s3Presigner.presignPutObject(presignRequest)

        return PresignedUploadResponse(
            uploadUrl = presignedRequest.url(),
            objectKey = objectKey
        )
    }


}
