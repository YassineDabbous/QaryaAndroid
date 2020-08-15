package app.qarya.presentation.vms


import app.qarya.domain.usecase.UCGeneral
import app.qarya.model.models.APath
import app.qarya.presentation.base.MyViewModel

class VMLeftDrawer : MyViewModel<List<APath>>() {

    fun menu(){
        UCGeneral().menu(closure);
    }
}
