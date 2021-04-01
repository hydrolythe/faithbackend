package converters

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LocalDateTimeConverter {

    fun toDate(value: String): LocalDateTime {
        return LocalDateTime.parse(value)
    }

    fun toString(value: LocalDateTime): String {
        return value.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    }
}