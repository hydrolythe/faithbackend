package controllerModels

import detail.DrawingDetail
import org.springframework.web.multipart.MultipartFile
import java.io.File

data class OverwritableImageDetail(val detail: DrawingDetail, val imageFile: File)
