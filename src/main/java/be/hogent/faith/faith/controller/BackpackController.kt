package be.hogent.faith.faith.controller

import Backpack
import DetailArray
import User
import be.hogent.faith.faith.SecurityService
import be.hogent.faith.faith.iservice.IDetailsContainerService
import controllerModels.DetailFile
import detail.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.ByteArrayResource
import org.springframework.core.io.InputStreamResource
import org.springframework.core.io.Resource
import org.springframework.hateoas.EntityModel
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths


@RestController
@RequestMapping("/backpack")
class BackpackController @Autowired constructor(var backpackService: IDetailsContainerService<Backpack>, var securityService: SecurityService)
{
    @GetMapping("/{uuid}")
    fun loadDetails(@PathVariable uuid:String):EntityModel<DetailArray>{
        val backpack = backpackService.loadDetails(uuid)
        val details = mutableListOf<ExpandedDetail>()
        backpack.details.forEach {
            when(it){
                is AudioDetail -> details.add(ExpandedDetail(it.file,it.title,it.uuid,it.dateTime,it.thumbnail,DetailType.AUDIO))
                is DrawingDetail -> details.add(ExpandedDetail(it.file,it.title,it.uuid,it.dateTime,it.thumbnail,DetailType.DRAWING))
                is PhotoDetail -> details.add(ExpandedDetail(it.file,it.title,it.uuid,it.dateTime,it.thumbnail,DetailType.PHOTO))
                is TextDetail -> details.add(ExpandedDetail(it.file,it.title,it.uuid,it.dateTime,it.thumbnail,DetailType.TEXT))
                is VideoDetail -> details.add(ExpandedDetail(it.file,it.title,it.uuid,it.dateTime,it.thumbnail,DetailType.VIDEO))
                is YoutubeVideoDetail -> details.add(ExpandedDetail(it.file,it.title,it.uuid,it.dateTime,it.thumbnail,DetailType.YOUTUBE))
            }
        }
        return EntityModel.of(DetailArray(details))
    }

    @PutMapping("/detail/{uuid}")
    fun getCurrentDetailFile(@PathVariable uuid:String, @RequestBody detail:Detail):EntityModel<Detail>{
        val detail = backpackService.getCurrentDetailFile(uuid,detail)
        return EntityModel.of(detail)
    }

    @PostMapping("/{uuid}")
    fun saveDetail(@PathVariable uuid:String, @RequestBody expandedDetail:ExpandedDetail):ResponseEntity<*>{
        backpackService.saveDetail(uuid,expandedDetail)
        val entity = EntityModel.of(expandedDetail)
        val location = ServletUriComponentsBuilder.fromCurrentRequest().buildAndExpand(expandedDetail).toUri()
        return ResponseEntity.created(location).body(entity)
    }

    @PutMapping("/{uuid}")
    fun deleteDetail(@PathVariable uuid:String, @RequestBody detail:Detail):ResponseEntity<*>{
        backpackService.deleteDetail(uuid,detail)
        return ResponseEntity.noContent().build<User>()
    }

    @PutMapping
    fun getFileInput(@RequestBody file: DetailFile):ResponseEntity<Resource>{
        val way = file.file.absolutePath
        val path = Paths.get(way)
        val input = ByteArrayResource(Files.readAllBytes(path))
        return ResponseEntity.ok().contentLength(file.file.length()).contentType(MediaType.APPLICATION_OCTET_STREAM).body(input)
    }
}