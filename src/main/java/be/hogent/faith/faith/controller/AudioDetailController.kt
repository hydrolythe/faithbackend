package be.hogent.faith.faith.controller

import be.hogent.faith.faith.SecurityService
import be.hogent.faith.faith.iservice.IAudioDetailService
import be.hogent.faith.faith.service.AudioDetailService
import controllerModels.DetailFile
import detail.AudioDetail
import org.apache.tomcat.util.http.fileupload.IOUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.hateoas.EntityModel
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import javax.servlet.http.HttpServletRequest
import java.io.OutputStream

import java.io.InputStream
import kotlin.math.roundToInt


@RestController
@RequestMapping("/audio")
class AudioDetailController @Autowired constructor(var detailService: IAudioDetailService, var securityService: SecurityService) {

    @PutMapping
    fun getAudioDetail(@RequestPart(value = "file") file:MultipartFile): EntityModel<AudioDetail> {
        val inputStream = file.bytes
        var randomInt = 0
        while(randomInt==0||File("src/main/resources/audio-$randomInt.mpeg").isFile){
            randomInt = (Math.random()*1000000000).roundToInt()
        }
        val targetFile = File("src/main/resources/audio-$randomInt.mpeg")
        targetFile.writeBytes(inputStream)
        return EntityModel.of(detailService.onSaveClicked(targetFile))
    }
}