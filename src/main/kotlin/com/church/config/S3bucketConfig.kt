package com.church.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.S3Configuration
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import java.net.URI

@Configuration
class S3bucketConfig(
    private val awsProperties: AwsProperties
) {
/*    @Bean
    fun s3Client(): S3Client {
        val credentials = AwsBasicCredentials.create(
            awsProperties.accessKey,
            awsProperties.secretKey
        )

        return S3Client.builder()
            .region(Region.of(awsProperties.region))
            .credentialsProvider(StaticCredentialsProvider.create(credentials))
            .endpointOverride(URI.create("http://localhost:9009"))
            .forcePathStyle(true)
            .build()
    }


    @Bean
    fun s3Presigner(): S3Presigner {
        val credentials = AwsBasicCredentials.create(
            awsProperties.accessKey,
            awsProperties.secretKey
        )

       return S3Presigner.builder()
            .region(Region.of(awsProperties.region))
               .credentialsProvider(StaticCredentialsProvider.create(credentials))
            .endpointOverride(URI.create("http://localhost:9009"))
            .build()
    }*/


    @Bean
    fun s3Client(): S3Client {
        val credentials = AwsBasicCredentials.create(
            awsProperties.accessKey,
            awsProperties.secretKey
        )

        return S3Client.builder()
            .region(Region.of(awsProperties.region))
            .credentialsProvider(StaticCredentialsProvider.create(credentials))
            .endpointOverride(URI.create("http://10.0.2.2:9009"))
            .serviceConfiguration(
                S3Configuration.builder()
                    .pathStyleAccessEnabled(true)
                    .build()
            )
            .build()
    }

    @Bean
    fun s3Presigner(): S3Presigner {
        val credentials = AwsBasicCredentials.create(
            awsProperties.accessKey,
            awsProperties.secretKey
        )

        return S3Presigner.builder()
            .region(Region.of(awsProperties.region))
            .credentialsProvider(StaticCredentialsProvider.create(credentials))
            .endpointOverride(URI.create("http://10.0.2.2:9009"))
            .serviceConfiguration(
                S3Configuration.builder()
                    .pathStyleAccessEnabled(true)
                    .build()
            )
            .build()
}
}
