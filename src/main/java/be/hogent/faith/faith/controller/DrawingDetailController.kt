package be.hogent.faith.faith.controller

import be.hogent.faith.faith.SecurityService
import controllerModels.OverwritableImageDetail
import be.hogent.faith.faith.iservice.IDrawingDetailService
import controllerModels.DetailFile
import detail.DrawingDetail
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.hateoas.EntityModel
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.FileOutputStream
import javax.imageio.ImageIO
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/draw")
class DrawingDetailController @Autowired constructor(var drawingDetailService: IDrawingDetailService, var securityService: SecurityService){
    @PutMapping
    fun createNewDetail(@RequestPart(value="file") file:MultipartFile):EntityModel<DrawingDetail>{
        val image = ImageIO.read(file.inputStream)
        val detail = drawingDetailService.onBitMapAvailable(image)
        return EntityModel.of(detail)
    }

    @PutMapping("/over")
    fun overWriteDetail(@RequestBody overwritableDetail: OverwritableImageDetail):EntityModel<DrawingDetail>{
        val image = ImageIO.read(overwritableDetail.imageFile)
        drawingDetailService.loadExistingDetail(overwritableDetail.detail)
        val newDetail = drawingDetailService.onBitMapAvailable(image)
        return EntityModel.of(newDetail)
    }
}