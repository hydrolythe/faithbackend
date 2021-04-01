package detailscontainer

import Cinema
import com.google.cloud.firestore.Firestore
import com.google.firebase.auth.FirebaseAuth

class CinemaDatabase(
    fbAuth: FirebaseAuth,
    firestore: Firestore
) : DetailContainerDatabase<Cinema>(fbAuth, firestore) {
    override val containerName: String = "cinema"
}