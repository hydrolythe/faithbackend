package detailscontainer

import Backpack
import com.google.cloud.firestore.Firestore
import com.google.firebase.auth.FirebaseAuth

class BackpackDatabase(
    fbAuth: FirebaseAuth,
    firestore: Firestore
) : DetailContainerDatabase<Backpack>(fbAuth, firestore) {
    override val containerName: String = "backpack"
}