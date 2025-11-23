package com.church.config

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import jakarta.annotation.PostConstruct
import org.springframework.context.annotation.Configuration
import java.io.ByteArrayInputStream
import java.io.FileInputStream

@Configuration
class FirebaseConfig {

    @PostConstruct
    fun init() {
        val serviceAccountJson = System.getenv("FIREBASE_SERVICE_ACCOUNT")
        if (serviceAccountJson != null){
            val serviceAccount = ByteArrayInputStream(serviceAccountJson.toByteArray())
            val options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build()

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options)
            }
        }

        //val serviceAccount = System.getenv("FIREBASE_SERVICE_ACCOUNT")?: FileInputStream("src/main/resources/serviceAccountKey.json")

    }
}
