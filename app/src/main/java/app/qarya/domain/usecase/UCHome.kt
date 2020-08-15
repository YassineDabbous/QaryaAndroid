package app.qarya.domain.usecase

import tn.core.domain.Failure
import tn.core.model.responses.BaseResponse
import tn.core.model.net.custom.MyCallBack
import tn.core.domain.base.Closure
import app.qarya.model.models.Commun
import app.qarya.model.models.responses.ConfigsResponse
import app.qarya.model.models.responses.HomeResponse
import app.qarya.model.models.responses.NewsResponse

class UCHome : UseCase() {

    fun home(page:Int=1, closure: Closure<List<Commun>>) {
        api.paginateHome(page).enqueue(object :MyCallBack<BaseResponse<HomeResponse>>() {
            override fun onSuccess(response: BaseResponse<HomeResponse>) {
                super.onSuccess(response)

                var list: MutableList<Commun> = ArrayList()
                if (response.data.forks != null && response.data.forks.size > 0)
                    list.addAll(response.data.forks);
                if (response.data.posts != null && response.data.posts.data.size > 0)
                    list.addAll(response.data.posts.data)
                if (response.data.users != null && response.data.users.data.size > 0)
                    list.addAll(response.data.users.data);

                /**
                if (response.users.data.size > 0)
                list.addAll(response.users.data);
                 **/
                val sortedList = list.sortedWith(compareBy({ it.modelPosition}, {it.createdAt }))

                closure.onSuccess(sortedList)


            }
            override fun onError(failure: Failure?) {
                super.onError(failure)
                closure.onError(failure)
            }

        })
    }


    fun configs(closure: Closure<ConfigsResponse>) {
        api.configs().enqueue(object :MyCallBack<BaseResponse<ConfigsResponse>>() {
            override fun onSuccess(response: BaseResponse<ConfigsResponse>) {
                super.onSuccess(response)
                closure.onSuccess(response.data)
            }
            override fun onError(failure: Failure?) {
                super.onError(failure)
                closure.onError(failure)
            }

        })
    }


    fun news(closure: Closure<NewsResponse>) {
        api.newData().enqueue(object :MyCallBack<BaseResponse<NewsResponse>>() {
            override fun onSuccess(response: BaseResponse<NewsResponse>) {
                super.onSuccess(response)
                closure.onSuccess(response.data)
            }
            override fun onError(failure: Failure?) {
                super.onError(failure)
                closure.onError(failure)
            }

        })
    }
}
