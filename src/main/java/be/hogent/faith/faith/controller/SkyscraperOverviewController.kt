package be.hogent.faith.faith.controller

import User
import be.hogent.faith.faith.SecurityService
import controllerModels.UserGoal
import be.hogent.faith.faith.iservice.ISkyscraperOverviewService
import goals.Goal
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.IanaLinkRelations
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

@RestController
@RequestMapping("/skyscraper")
class SkyscraperOverviewController @Autowired constructor(val skyscraperOverviewService: ISkyscraperOverviewService, var securityService: SecurityService) {
    @GetMapping
    fun getGoals(@RequestBody user:User): EntityModel<List<Goal>> {
        val goals = skyscraperOverviewService.loadGoals(user)
        return EntityModel.of(goals)
    }
    @PostMapping
    fun addNewGoal(@RequestBody user:User): ResponseEntity<*> {
        skyscraperOverviewService.addNewGoal(user)
        val entity = EntityModel.of(user)
        val location = ServletUriComponentsBuilder.fromCurrentRequest().buildAndExpand(user).toUri()
        return ResponseEntity.created(location).body(entity)
    }
    @PutMapping("/{newDescription}")
    fun updateGoal(@RequestBody userGoal: UserGoal, @PathVariable newDescription:String):ResponseEntity<*>{
        skyscraperOverviewService.updateGoalDescription(userGoal.givenGoal,newDescription,userGoal.user)
        return ResponseEntity.ok(EntityModel.of(userGoal.user))
    }
}