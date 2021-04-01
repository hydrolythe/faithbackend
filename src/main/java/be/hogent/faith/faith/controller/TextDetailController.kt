package be.hogent.faith.faith.controller

import be.hogent.faith.faith.SecurityService
import be.hogent.faith.faith.iservice.ITextDetailService
import controllerModels.OverwritableTextDetail
import controllerModels.Token
import detail.TextDetail
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.hateoas.EntityModel
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/text")
class TextDetailController @Autowired constructor(val textDetailService: ITextDetailService, var securityService: SecurityService) {
    @PutMapping("/text")
    fun writeText(@RequestBody textDetail:TextDetail):EntityModel<Token>{
        val result = textDetailService.loadExistingDetail(textDetail)
        return EntityModel.of(Token(result))
    }

    @PutMapping
    fun createNewDetail(@RequestBody text:Token): EntityModel<TextDetail> {
        textDetailService.insertText(text.token!!)
        val result = textDetailService.onSaveClicked()
        return EntityModel.of(result)
    }
    @PutMapping("/over")
    fun overwriteDetail(@RequestBody overwritableTextDetail:OverwritableTextDetail):EntityModel<TextDetail>{
        textDetailService.insertText(overwritableTextDetail.text)
        textDetailService.loadExistingDetail(overwritableTextDetail.textDetail)
        val result = textDetailService.onSaveClicked()
        return EntityModel.of(result)
    }

}