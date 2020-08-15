package app.qarya.presentation.vms

import androidx.lifecycle.MutableLiveData
import app.qarya.domain.usecase.UCGeneric
import app.qarya.domain.usecase.UCHome
import app.qarya.domain.usecase.UCLike
import app.qarya.domain.usecase.UCRelations
import app.qarya.model.ModelType
import app.qarya.model.models.*
import app.qarya.presentation.base.MyViewModel
import app.qarya.presentation.base.MyActivity

import app.qarya.model.models.responses.LikeResponse
import app.qarya.model.models.responses.MarkResponse
import tn.core.domain.Failure
import tn.core.domain.base.Closure

class HomeViewModel : MyViewModel<List<Commun>>() {



    fun init(page:Int=1) {
        MyActivity.logHome("---------------------- REQUEST DATA FOR HOME -----------------------------")
        UCHome().home(page, closure)
    }




    var like = MutableLiveData<LikeResponse>()
    fun like(model: Model) {
        if (model is Post)
            UCLike().likePost(model.id, getGenericClosure(like))

    }
    var bookmark = MutableLiveData<MarkResponse>()
    fun bookmark(model: Model) {
        if (model is Post)
            UCLike().bookmark(model.id, getGenericClosure(bookmark))

    }


    /*var configs:MutableLiveData<ConfigsResponse>? = MutableLiveData<ConfigsResponse>()
    fun configs() {
        MyActivity.logHome("---------------------- REQUEST DATA FOR configs -----------------------------")
        UCHome().configs(getGenericClosure(configs))
    }*/




    var relation = MutableLiveData<Relation>()
    fun addRelation(id: Int) {
        UCRelations().change(UCRelations.ACTION.ADD, id, getGenericClosure(relation))
    }
    fun refuseRelation(id: Int) {
        UCRelations().change(UCRelations.ACTION.REFUSE, id, getGenericClosure(relation))
    }






    var modified = MutableLiveData<Commun>()
    fun load(mItem:Commun) {
        MyActivity.log("get horizontal list")
        if (mItem.modelType == ModelType.STORY) {
            this.paginate<Post>(mItem)
            MyActivity.log("getting horizontal posts list...")
        } else if (mItem.modelType == ModelType.NOTE) {
            this.paginate<Post>(mItem)
            MyActivity.log("getting horizontal posts list...")
        } else if (mItem.modelType == ModelType.USER) {
            this.paginate<User>(mItem)
            MyActivity.log("getting horizontal users list...")
        } else if (mItem.modelType == ModelType.RELATION) {
            this.getAll<Relation>(mItem)
            MyActivity.log("getting horizontal Relations list...")
        } else
            MyActivity.log("laho laho :(")


    }


    internal fun <YModel : Commun> paginate(mItem:Commun) {
        //pd.show()
        UCGeneric().generic(mItem.modelType, mItem.modelPath, 1, object : Closure<List<YModel>> {
            override fun onSuccess(response: List<YModel>) {
                MyActivity.log("horizontal new items count: " + response.size)
                mItem.lista.addAll(response)
                mItem.loaded = true
                modified.postValue(mItem)
                //adapter.items = response
                //setList()
            }

            override fun onError(failure: Failure) {
                handleError(failure)
            }
        })
    }

    internal fun <YModel : Commun> getAll(mItem:Commun) {
        //pd.show()
        UCGeneric().genericAll(mItem.modelType, mItem.modelPath, object : Closure<List<YModel>> {
            override fun onSuccess(response: List<YModel>) {
                MyActivity.log("horizontal new items count: " + response.size)
                mItem.lista.addAll(response)
                mItem.loaded = true
                modified.postValue(mItem)
                //adapter.items = response
                //setList()
            }

            override fun onError(failure: Failure) {
                handleError(failure)
            }
        })
    }


}
