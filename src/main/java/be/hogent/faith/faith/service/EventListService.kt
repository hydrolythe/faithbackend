package be.hogent.faith.faith.service

import Event
import User
import be.hogent.faith.faith.iservice.IEventListService
import io.reactivex.rxjava3.observers.DisposableCompletableObserver
import io.reactivex.rxjava3.subscribers.DisposableSubscriber
import usecases.event.DeleteEventUseCase
import usecases.event.GetEventsUseCase

class EventListService(
    private val getEventsUseCase: GetEventsUseCase,
    private val deleteEventUseCase: DeleteEventUseCase
): IEventListService {

    var vlag = false
    var events = listOf<Event>()

    override fun loadEvents(user: User):List<Event> {
        getEventsUseCase.execute(GetEventsUseCase.Params(user.uuid),
            object : DisposableSubscriber<List<Event>>() {
                override fun onComplete() {
                    vlag = true
                }

                override fun onNext(t: List<Event>) {
                    events = t
                }

                override fun onError(e: Throwable) {

                }
            })
        while(!vlag){
            Thread.sleep(1000)
        }
        return events
    }
    override fun deleteEvent(event: Event, user:User) {
        deleteEventUseCase.execute(DeleteEventUseCase.Params(event, user), object : DisposableCompletableObserver() {
            override fun onComplete() {

            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
            }
        })
    }
}