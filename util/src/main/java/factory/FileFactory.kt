package factory

import java.io.File

object FileFactory {
    fun makeFile(): File {
        return File("path/bestand")
    }
}