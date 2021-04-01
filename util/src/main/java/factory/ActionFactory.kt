package factory

import goals.Action
import goals.ActionStatus

object ActionFactory {

    fun makeAction(): Action {
        return Action(ActionStatus.ACTIVE).also { it.description = DataFactory.randomString() }
    }
}