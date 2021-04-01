package be.hogent.faith.faith.iservice

import detail.AudioDetail
import java.io.File

interface IAudioDetailService{
    fun onSaveClicked(file: File): AudioDetail
}