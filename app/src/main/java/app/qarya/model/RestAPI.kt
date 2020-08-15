package app.qarya.model

import app.qarya.model.models.*
import app.qarya.model.models.requests.*
import app.qarya.model.models.responses.*
import app.qarya.model.models.User
import okhttp3.MultipartBody
import okhttp3.RequestBody

import retrofit2.Call
import retrofit2.http.*
import tn.core.model.responses.PagingResponse
import tn.core.model.responses.BaseResponse

interface RestAPI {


    @POST("oauth/facebook")
    fun loginFacebook(@Body body: FacebookLoginRequest): Call<BaseResponse<AuthResponse>>
    @POST("login")
    fun login(@Body body: LoginRequest): Call<BaseResponse<AuthResponse>>
    @POST("register")
    fun register(@Body body: RegisterRequest): Call<BaseResponse<AuthResponse>>
    @POST("recover")
    fun recover(@Query("email") page: String): Call<BaseResponse<Any>>


    // /v1/menu
    // /v1/configs
    // /v1/data
    // /v1/broadcasts



    //@Headers("Cacheable: 86400")
    @GET("homevii")
    fun paginateHome(@Query("page") page: Int): Call<BaseResponse<HomeResponse>>

    @Headers("Cacheable: 86400")
    @GET("menu")
    fun menu(): Call<BaseResponse<List<APath>>>

    @Headers("Cacheable: 86400")
    @GET("configs")
    fun configs(): Call<BaseResponse<ConfigsResponse>>

    @GET("data")
    fun newData(): Call<BaseResponse<NewsResponse>>



    @GET("posts/custom/{path}")
    fun customPosts(@Path("path") path: String, @Query("page") page: Int): Call<BaseResponse<PagingResponse<Post>>>
    @GET("stories/custom/{path}")
    fun customStories(@Path("path") path: String, @Query("page") page: Int): Call<BaseResponse<PagingResponse<Post>>>
    @GET("users/custom/{path}")
    fun customUsers(@Path("path") path: String, @Query("page") page: Int): Call<BaseResponse<PagingResponse<User>>>
    @GET("relations/custom/{path}")
    fun customRelations(@Path("path") path: String, @Query("page") page: Int): Call<BaseResponse<List<Relation>>>


    @POST("reports")
    fun report(@Body report:ReportRequest): Call<BaseResponse<Any>>

    @GET("liker/{id}/{type}")
    fun like(@Path("id") id: Int, @Path("type") type: Int): Call<BaseResponse<LikeResponse>>

    @GET("bookmarks/{id}")
    fun bookmark(@Path("id") id: Int): Call<BaseResponse<MarkResponse>>
    @GET("bookmarks}")
    fun bookmarks(): Call<BaseResponse<PagingResponse<Post>>>


    @GET("rooms")
    fun rooms(): Call<BaseResponse<List<Room>>>
    @PUT("rooms/{id}")
    fun roomsUpdate(@Path("id") id: Int, @Body body: Room): Call<BaseResponse<Room>>

    @GET("apps")
    fun apps(): Call<BaseResponse<List<App>>>
    @GET("notifications")
    fun notifications(): Call<BaseResponse<List<Notification>>>


    @GET("suggested")
    fun suggested(): Call<BaseResponse<List<Relation>>>
    @GET("blocked")
    fun blocked(): Call<BaseResponse<List<Relation>>>
    @GET("requested")
    fun requested(): Call<BaseResponse<List<Relation>>>
    @GET("requests")
    fun requests(): Call<BaseResponse<List<Relation>>>
    @GET("users/{id}/friends")
    fun relationsFor(@Path("id") id: Int): Call<BaseResponse<List<Relation>>>

    //Ekhdemni.relations+"/"+operation+"/"+model.id
    @GET("relations/{operation}/{id}")
    fun relationsChange(@Path("operation") operation: Int, @Path("id") id: Int): Call<BaseResponse<Relation>>


    @GET("alerts")
    fun alerts(): Call<BaseResponse<List<Alert>>>


    @GET("broadcasts")
    fun broadcasts(): Call<BaseResponse<List<Broadcast>>>





    @POST("conversations")
    fun pushMessage(@Body body: MessageSetter): Call<BaseResponse<List<Message>>>
    @GET("conversations/{id}")
    fun getMessages(@Path("id") id: Int, @Query("page") page: Int): Call<BaseResponse<PagingResponse<Message>>>
    @GET("conversations/users/{id}")
    fun getMessagesWithUser(@Path("id") id: Int, @Query("page") page: Int): Call<BaseResponse<PagingResponse<Message>>>

    @DELETE("conversations/messages/{id}/delete")
    fun deleteMessage(@Path("id") int: Int): Call<BaseResponse<GeneralResponse>>

    @PUT("conversations/lock/{id}")
    fun lockConversation(@Path("id") int: Int): Call<BaseResponse<GeneralResponse>>

    @GET("conversations")
    fun getConversations(@Query("page") page: Int): Call<BaseResponse<PagingResponse<Conversation>>>

    @PUT("users/status/{type}")
    fun status(@Path("type") type: Int): Call<BaseResponse<GeneralResponse>>
    @PUT("users/private/privacy")
    fun privateChatPrivacy(): Call<BaseResponse<GeneralResponse>>


    @Headers("Cacheable: 86400")
    @GET("locations")
    fun locations(): Call<BaseResponse<List<City>>>

    @Headers("Cacheable: 86400")
    @GET("categories")
    fun categories(): Call<BaseResponse<List<Category>>>

    @GET("categories/all")
    fun categoriesAll(): Call<BaseResponse<List<Category>>>
    @GET("categories/{id}/children")
    fun getCategories(@Path("id") id: Int): Call<BaseResponse<List<Category>>>

    @POST("broadcasts")
    fun follow(@Body follow:FollowRequest): Call<BaseResponse<FollowResponse>>


    @GET("categories/users")
    fun usersCategories(): Call<BaseResponse<List<Category>>>
    @GET("categories/stores")
    fun storesCategories(): Call<BaseResponse<List<Category>>>
    @GET("categories/posts")
    fun postsCategories(): Call<BaseResponse<List<Category>>>
    @GET("categories/notes")
    fun notesCategories(): Call<BaseResponse<List<Category>>>
    @GET("categories/products")
    fun productsCategories(): Call<BaseResponse<List<Category>>>

    @GET("products_types/{id}/children")
    fun productsCategories(@Path("id") id: Int): Call<BaseResponse<List<Category>>>

    @GET("products_types/{id}/products")
    fun getProducts(@Path("id") id: Int, @Query("page") page: Int=1): Call<BaseResponse<PagingResponse<Post>>>




    @GET("hashtags/{tag}")
    fun getTagPosts(@Path("tag") tag: String, @Query("page") page: Int=1): Call<BaseResponse<PagingResponse<Post>>>
    @GET("locations/{id}/posts")
    fun getLocationPosts(@Path("id") id: Int, @Query("page") page: Int=1): Call<BaseResponse<PagingResponse<Post>>>
    @GET("users/{id}/posts")
    fun getUserPosts(@Path("id") id: Int, @Query("page") page: Int=1): Call<BaseResponse<PagingResponse<Post>>>
    @GET("categories/{id}/feed")
    fun getCategoryPosts(@Path("id") id: Int, @Query("page") page: Int=1): Call<BaseResponse<PagingResponse<Post>>>

    @GET("posts/{id}")
    fun getPost(@Path("id") id: Int): Call<BaseResponse<Post>>

    @POST("posts")
    fun pushPost(@Body post: PostSetter): Call<BaseResponse<Post>>
    @POST("stories")
    fun pushStory(@Body post: PostSetter): Call<BaseResponse<Post>>
    @POST("notes")
    fun pushNote(@Body post: PostSetter): Call<BaseResponse<Post>>
    @POST("products")
    fun pushProduct(@Body post: PostSetter): Call<BaseResponse<Post>>

    @DELETE("posts/{id}")
    fun deletePost(@Path("id") id: Int): Call<BaseResponse<GeneralResponse>>



    @GET("comments/{type}/{id}")
    fun getComments(@Path("type") type: String, @Path("id") id: String, @Query("page") page: Int=1): Call<BaseResponse<PagingResponse<Comment>>>

    @POST("comments")
    fun commentsPush(@Body comment: CommentSetter): Call<BaseResponse<List<Comment>>>

    @DELETE("comments/{id}")
    fun commentsDelete(@Path("id") id: Int): Call<BaseResponse<GeneralResponse>>



    @POST("search")
    fun searchPosts(@Body filter: Filter, @Query("page") page: Int=1): Call<BaseResponse<PagingResponse<Post>>>
    @POST("search/users")
    fun searchUsers(@Body filter: Filter, @Query("page") page: Int=1): Call<BaseResponse<PagingResponse<User>>>


    @GET("users/online")
    fun getUsersOnline(@Query("page") page: Int): Call<BaseResponse<PagingResponse<User>>>
    @GET("users/random")
    fun getUsersRandomOnline(): Call<BaseResponse<List<User>>>

    @GET("users")
    fun getUsers(@Query("page") page: Int): Call<BaseResponse<PagingResponse<User>>>
    @GET("users/{id}")
    fun getUser(@Path("id") id: Int): Call<BaseResponse<User>>
    @PUT("users")
    fun updateUser(@Body user: UpdateProfileRequest): Call<BaseResponse<User>>
    @PUT("users/email")
    fun updateEmail(@Body data: UpdateEmailRequest): Call<BaseResponse<User>>
    @PUT("users/password")
    fun updatePassword(@Body user: UpdatePasswordRequest): Call<BaseResponse<User>>

    @Multipart
    @POST("users/photo")
    fun uploadPicture(@Part file:MultipartBody.Part, @Part("file") name:RequestBody): Call<BaseResponse<User>>

    @Multipart
    @POST("files")
    fun uploadImage(@Part file:MultipartBody.Part, @Part("image") name:RequestBody): Call<BaseResponse<SFile>>
    @Multipart
    @POST("files")
    fun uploadVideo(@Part file:MultipartBody.Part, @Part("video") name:RequestBody): Call<BaseResponse<SFile>>
    @Multipart
    @POST("files")
    fun uploadAudio(@Part file:MultipartBody.Part, @Part("audio") name:RequestBody): Call<BaseResponse<SFile>>

}
