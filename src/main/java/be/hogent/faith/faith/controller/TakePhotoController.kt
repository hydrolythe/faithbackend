package be.hogent.faith.faith.controller

import be.hogent.faith.faith.SecurityService
import be.hogent.faith.faith.iservice.ITakePhotoService
import be.hogent.faith.faith.service.TakePhotoService
import com.google.common.io.Files
import controllerModels.DetailFile
import detail.PhotoDetail
import org.apache.tomcat.util.http.fileupload.IOUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.hateoas.EntityModel
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.nio.file.StandardCopyOption
import javax.servlet.http.HttpServletRequest
import kotlin.math.roundToInt

@RestController
@RequestMapping("/photo")
class TakePhotoController @Autowired constructor(val takePhotoService: ITakePhotoService, var securityService: SecurityService) {
    @PutMapping
    fun savePhoto(@RequestPart(value = "file") file: MultipartFile):EntityModel<PhotoDetail>{
        val inputStream = file.bytes
        var randomInt = 0
        while(randomInt==0||File("src/main/resources/picture-$randomInt.jpeg").isFile){
            randomInt = (Math.random()*1000000000).roundToInt()
        }
        val targetFile = File("src/main/resources/picture-$randomInt.jpeg")
        targetFile.writeBytes(inputStream)
        val photo = takePhotoService.onSaveClicked(targetFile)
        return EntityModel.of(photo)
    }
}