package be.hogent.faith.faith.controller

import controllerModels.EmotionAvatarToSave
import Event
import be.hogent.faith.faith.SecurityService
import controllerModels.EventDetail
import be.hogent.faith.faith.iservice.IEventService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.IanaLinkRelations
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.io.File
import java.io.FileOutputStream
import javax.imageio.ImageIO

@RestController
@RequestMapping("/event")
class EventController @Autowired constructor(var eventService:IEventService, var securityService: SecurityService) {
    @PostMapping("/avatar")
    fun saveEmotionAvatarImage(@RequestBody emotionAvatarToSave: EmotionAvatarToSave): ResponseEntity<*> {
        val newFile = File(emotionAvatarToSave.imageFile.name)
        val os = FileOutputStream(newFile)
        os.write(emotionAvatarToSave.imageFile.bytes)
        val image = ImageIO.read(newFile)
        eventService.saveEmotionAvatarImage(image,emotionAvatarToSave.givenEvent)
        val entity = EntityModel.of(emotionAvatarToSave.givenEvent)
        val location = ServletUriComponentsBuilder.fromCurrentRequest().buildAndExpand(emotionAvatarToSave).toUri()
        return ResponseEntity.created(location).body(entity)
    }
    @PostMapping
    fun saveCurrentDetail(@RequestBody eventDetail: EventDetail): ResponseEntity<*>{
        eventService.saveCurrentDetail(eventDetail.detail,eventDetail.event)
        val entity = EntityModel.of(eventDetail.event)
        val location = ServletUriComponentsBuilder.fromCurrentRequest().buildAndExpand(eventDetail).toUri()
        return ResponseEntity.created(location).body(entity)
    }
    @DeleteMapping
    fun deleteDetail(@RequestBody eventDetail: EventDetail):ResponseEntity<*>{
        eventService.deleteDetail(eventDetail.detail,eventDetail.event)
        return ResponseEntity.noContent().build<Event>()
    }
}