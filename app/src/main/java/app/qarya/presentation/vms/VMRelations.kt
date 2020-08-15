package app.qarya.presentation.vms

import androidx.lifecycle.MutableLiveData

import app.qarya.domain.usecase.UCRelations
import app.qarya.model.models.Relation
import app.qarya.presentation.base.MyViewModel

class VMRelations : MyViewModel<Boolean>() {

    var relationsFriends = MutableLiveData<List<Relation>>()
    var relationsRequests  = MutableLiveData<List<Relation>>()
    var relationsRequested = MutableLiveData<List<Relation>>()
    var relationsBlocked = MutableLiveData<List<Relation>>()
    var relationsSuggested = MutableLiveData<List<Relation>>()
    var relation = MutableLiveData<Relation>()

    fun friends(id: Int) {
        UCRelations().relations(this.getGenericClosure(relationsFriends), UCRelations.TYPE.friends, id)
    }
    fun suggested() {
        UCRelations().relations(this.getGenericClosure(relationsSuggested), UCRelations.TYPE.suggested)
    }
    fun requests() {
        UCRelations().relations(this.getGenericClosure(relationsRequests), UCRelations.TYPE.requests)
    }
    fun requested() {
        UCRelations().relations(this.getGenericClosure(relationsRequested), UCRelations.TYPE.requested)
    }
    fun blocked() {
        UCRelations().relations(this.getGenericClosure(relationsBlocked), UCRelations.TYPE.blocked)
    }


    fun addRelation(id: Int) {
        UCRelations().change(UCRelations.ACTION.ADD, id, getGenericClosure(relation))
    }
    fun acceptRelation(id: Int) {
        UCRelations().change(UCRelations.ACTION.ACCEPT, id, getGenericClosure(relation))
    }
    fun refuseRelation(id: Int) {
        UCRelations().change(UCRelations.ACTION.REFUSE, id, getGenericClosure(relation))
    }
}