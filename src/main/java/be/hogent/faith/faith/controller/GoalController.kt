package be.hogent.faith.faith.controller

import be.hogent.faith.faith.SecurityService
import controllerModels.UserGoal
import be.hogent.faith.faith.iservice.IGoalService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.IanaLinkRelations
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

@RestController
@RequestMapping("/goal")
class GoalController @Autowired constructor(var goalService: IGoalService, var securityService: SecurityService) {
    @PostMapping
    fun onSaveButtonClicked(@RequestBody userGoal: UserGoal): ResponseEntity<*> {
        goalService.onSaveButtonClicked(userGoal.givenGoal,userGoal.user)
        val entity = EntityModel.of(userGoal.user)
        val location = ServletUriComponentsBuilder.fromCurrentRequest().buildAndExpand(userGoal).toUri()
        return ResponseEntity.created(location).body(entity)
    }
}