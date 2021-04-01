package be.hogent.faith.faith.controller

import TreasureChest
import User
import be.hogent.faith.faith.SecurityService
import be.hogent.faith.faith.iservice.IDetailsContainerService
import detail.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.IanaLinkRelations
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.net.URI

@RestController
@RequestMapping("/treasure")
class TreasureChestController @Autowired constructor(val treasureChestService:IDetailsContainerService<TreasureChest>, var securityService: SecurityService) {
    @GetMapping("/{uuid}")
    fun loadDetails(@PathVariable uuid:String):EntityModel<TreasureChest>{
        val backpack = treasureChestService.loadDetails(uuid)
        return EntityModel.of(backpack)
    }

    @PostMapping("/{uuid}")
    fun saveTextDetail(@PathVariable uuid:String, @RequestBody expandedDetail: ExpandedDetail):ResponseEntity<*>{
        treasureChestService.saveDetail(uuid,expandedDetail)
        val entity = EntityModel.of(expandedDetail)
        val location = ServletUriComponentsBuilder.fromCurrentRequest().buildAndExpand(expandedDetail).toUri()
        return ResponseEntity.created(location).body(entity)
    }

    @DeleteMapping("/{uuid}")
    fun deleteTextDetail(@PathVariable uuid:String, @RequestBody detail: Detail):ResponseEntity<*>{
        treasureChestService.deleteDetail(uuid,detail)
        return ResponseEntity.noContent().build<User>()
    }
}