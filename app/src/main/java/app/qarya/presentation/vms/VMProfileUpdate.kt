package app.qarya.presentation.vms

import androidx.lifecycle.MutableLiveData
import app.qarya.domain.usecase.UCGeneral
import app.qarya.domain.usecase.UCUsers
import app.qarya.model.models.Category
import app.qarya.model.models.City
import app.qarya.model.models.User
import app.qarya.model.models.requests.UpdateEmailRequest
import app.qarya.model.models.requests.UpdatePasswordRequest
import app.qarya.model.models.requests.UpdateProfileRequest
import app.qarya.presentation.base.MyViewModel

class VMProfileUpdate : MyViewModel<User>() {

    var cities = MutableLiveData<List<City>>()
    var categories = MutableLiveData<List<Category>>()

    fun getCities() {
        UCGeneral().cities(getGenericClosure(cities))
    }
    fun categoriesOfUsers() {
        UCGeneral().categoriesOfUsers(0, getGenericClosure(categories))
    }

    fun user(id:Int){
        UCUsers().one(id, closure)
    }


    fun update(user: UpdateProfileRequest){
        UCUsers().update(user, closure)
    }
    fun updateEmail(user: UpdateEmailRequest){
        UCUsers().updateEmail(user, closure)
    }
    fun updatePassword(user: UpdatePasswordRequest){
        UCUsers().updatePassword(user, closure)
    }
}
