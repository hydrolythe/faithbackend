package converters

import java.util.*

class UuidConverter {

    fun toString(uuid: UUID): String {
        return uuid.toString()
    }

    fun toUuid(uuidString: String): UUID {
        return UUID.fromString(uuidString)
    }
}