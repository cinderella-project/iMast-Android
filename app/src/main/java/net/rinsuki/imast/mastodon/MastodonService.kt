package net.rinsuki.imast.mastodon

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * Created by user on 2018/02/13.
 */
interface MastodonService {
    @GET("/api/v1/instance")
    fun instanceInfo(): Call<MastodonInstance>

    @FormUrlEncoded
    @POST("/api/v1/apps")
    fun createApp(
            @Field("client_name") name: String,
            @Field("redirect_uris") redirectUris: String,
            @Field("scopes") scopes: String = "read write follow",
            @Field("website") website: String = "https://github.com/cinderella-project/iMast-Android"
    ): Call<MastodonApp>
}