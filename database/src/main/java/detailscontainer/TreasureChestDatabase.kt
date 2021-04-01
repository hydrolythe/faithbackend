package detailscontainer

import TreasureChest
import com.google.cloud.firestore.Firestore
import com.google.firebase.auth.FirebaseAuth

class TreasureChestDatabase(
    fbAuth: FirebaseAuth,
    firestore: Firestore
) : DetailContainerDatabase<TreasureChest>(fbAuth, firestore) {
    override val containerName: String = "treasurechest"
}