package be.hogent.faith.faith

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import org.springframework.stereotype.Service
import java.io.FileInputStream
import java.lang.Exception
import javax.annotation.PostConstruct

@Service
class FBInitialize {
    @PostConstruct
    fun initialize() {
        try {
            val serviceAccount = FileInputStream("faith-master-firebase-adminsdk-nro0u-b1aa4ed1d1.json")
            val options = FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://faith-master.firebaseio.com")
                .build()
            FirebaseApp.initializeApp(options)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}