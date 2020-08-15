package app.qarya.presentation.vms

import androidx.lifecycle.MutableLiveData

import app.qarya.domain.usecase.UCRelations
import app.qarya.model.models.Relation
import app.qarya.presentation.base.MyViewModel

class VMLister : MyViewModel<Boolean>() {

    var relationsFriends = MutableLiveData<List<Relation>>()

    fun friends(id: Int) {
        UCRelations().relations(this.getGenericClosure(relationsFriends), UCRelations.TYPE.friends, id)
    }
}