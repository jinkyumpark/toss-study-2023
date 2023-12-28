package webserver

import controller.*
import http.HttpRequest

class RequestMapping {

    fun getController(key: Key): Controller {
        return controllers
            .getOrDefault(
                key = key,
                defaultValue = GetResourceController(),
            )
    }

    data class Key(
        val method: HttpRequest.Method,
        val url: String,
    )

    companion object {
        private val controllers: Map<Key, Controller> = mapOf(
            Key(HttpRequest.Method.GET, "/") to GetIndexController(),
            Key(HttpRequest.Method.GET, "/user/list.html") to GetUserListController(),
            Key(HttpRequest.Method.POST, "/user/create.html") to PostUserCreateController(),
            Key(HttpRequest.Method.POST, "/user/login") to PostUserLoginController(),
            Key(HttpRequest.Method.POST, "/user/login") to PostUserLoginController(),
        )
    }
}