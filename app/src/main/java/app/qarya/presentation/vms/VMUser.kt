package app.qarya.presentation.vms

import app.qarya.domain.usecase.UCUsers
import app.qarya.presentation.base.MyViewModel
import app.qarya.model.models.User

import java.io.File

import androidx.lifecycle.MutableLiveData
import app.qarya.domain.usecase.UCRelations
import app.qarya.model.models.Relation


class VMUser() : MyViewModel<User>() {
    /*private val state: SavedStateHandle
    val liveDate = state.getLiveData("liveData", Random.nextInt().toString())
    fun saveState() {
        state.set("liveData", liveDate.value)
    }*/

    lateinit var user: User

    fun isInitialized(): Boolean{
        return this::user.isInitialized
    }

    var upload = MutableLiveData<User>()
    var relation = MutableLiveData<Relation>()


    //users+"/update/"+user.getId();
    fun init(id: Int) {
        UCUsers().one(id, closure)
    }

    fun upload(data: File) {
        UCUsers().uploadPicture(data, getGenericClosure(upload))
    }

    enum class Relations{
        ADD, BLOCK, ACCEPT, REMOVE//, REFUSE
    }
    fun friendship(r:Relations=Relations.ADD){
        when(r){
            Relations.ADD -> UCRelations().change(UCRelations.ACTION.ADD, user.id, getGenericClosure(relation))
            Relations.ACCEPT -> UCRelations().change(UCRelations.ACTION.ACCEPT, user.id, getGenericClosure(relation))
            Relations.BLOCK -> UCRelations().change(UCRelations.ACTION.BLOCK, user.id, getGenericClosure(relation))
            Relations.REMOVE -> UCRelations().change(UCRelations.ACTION.REFUSE, user.id, getGenericClosure(relation))
        }
    }

}