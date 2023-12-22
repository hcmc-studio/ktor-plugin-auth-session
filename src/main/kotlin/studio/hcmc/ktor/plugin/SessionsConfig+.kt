package studio.hcmc.ktor.plugin

import io.ktor.server.sessions.*

fun SessionsConfig.cookieSession(config: CookieSession.Config) {
    cookie<CookieSession>(config.key) {
        serializer = object : SessionSerializer<CookieSession> {
            override fun deserialize(text: String) = CookieSession(text)
            override fun serialize(session: CookieSession) = session.id
        }

        apply(config.cookieSessionBuilder)
    }
}