package net.rinsuki.imast.mastodon

import com.squareup.moshi.Json

/**
 * Created by user on 2018/02/13.
 */
data class MastodonApp (
        val name: String,
        val website: String?,
        @Json(name = "client_id") var clientId: String,
        @Json(name = "client_secret") var clientSecret: String
) {
    fun getAuthorizeUrl(hostName: String, scope: String = "read+write+follow", state: String = ""): String {
        val opt = hashMapOf(
                "client_id" to this.clientId,
                "redirect_uri" to "imast://callback",
                "scope" to scope,
                "response_type" to "code",
                "state" to state
        ).map {
            it.key + "=" + it.value
        }.joinToString("&")
        return "https://"+hostName+"/oauth/authorize?"+opt
    }
}