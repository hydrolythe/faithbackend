package be.hogent.faith.faith.iservice

import detail.PhotoDetail
import java.io.File

interface ITakePhotoService {
    fun onSaveClicked(_tempPhotoSaveFile: File): PhotoDetail
}