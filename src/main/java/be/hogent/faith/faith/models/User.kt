package be.hogent.faith.faith.models

import lombok.Data
import java.io.Serializable


@Data
class User : Serializable {
    var uid: String? = null
    var email: String? = null

    companion object {
        private const val serialVersionUID = 4408418647685225829L
    }
}