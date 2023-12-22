package studio.hcmc.ktor.plugin

import io.ktor.server.application.*
import io.ktor.server.sessions.*

fun ApplicationCall.session(): CookieSession {
    return sessions.get<CookieSession>() ?: throw IllegalStateException("CookieSession is absent. Check the plugin is properly installed.")
}