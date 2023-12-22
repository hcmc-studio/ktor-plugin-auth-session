package studio.hcmc.ktor.plugin

import io.ktor.server.application.*
import io.ktor.server.sessions.*
import io.ktor.util.*
import kotlin.random.Random
import kotlin.random.nextULong

data class CookieSession(val id: String) {
    data class Config(
        var key: String = "JSESSIONID",
        var generator: IdGenerator = DefaultIdGenerator(),
        var cookieSessionBuilder: CookieSessionBuilder<*>.() -> Unit = {},
        var onSessionCreated: suspend ApplicationCall.(CookieSession) -> Unit = {}
    )

    interface IdGenerator {
        val random: Random
        val length: Int

        fun next(): String
    }

    class DefaultIdGenerator(
        override val random: Random = Random,
        override val length: Int = 64
    ) : IdGenerator {
        @OptIn(ExperimentalStdlibApi::class)
        override fun next(): String {
            val builder = StringBuilder()
            while (builder.length < length) {
                builder.append(random.nextULong().toHexString())
            }

            return builder.toString().substring(0, length)
        }
    }

    companion object {
        val plugin = createApplicationPlugin("CookieSession", ::Config) {
            onCall { call ->
                val present = call.sessions.get<CookieSession>()
                if (present == null) {
                    val session = CookieSession(pluginConfig.generator.next())
                    call.sessions.set(session)
                    pluginConfig.onSessionCreated(call, session)
                }
            }
        }
    }
}