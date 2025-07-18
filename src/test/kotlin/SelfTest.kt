import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.routing.*
import io.ktor.server.testing.*
import no.nav.syfo.application.ApplicationState
import no.nav.syfo.application.api.registerNaisApi
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class SelfTest {

    fun ApplicationTestBuilder.setupPodApi(applicationState: ApplicationState) {
        application {
            routing {
                registerNaisApi(applicationState)
            }
        }
    }

    @Nested
    @DisplayName("Successful liveness and readiness tests")
    inner class Successful {
        @Test
        fun `Returns ok on is_alive`() {
            testApplication {
                setupPodApi(ApplicationState(true, true))
                val response = client.get("/is_alive")
                assertEquals(HttpStatusCode.OK, response.status)
                assertEquals("I'm alive! :)", response.bodyAsText())
            }
        }

        @Test
        fun `Returns ok in is_ready`() {
            testApplication {
                setupPodApi(ApplicationState(true, true))
                val response = client.get("/is_ready")
                assertEquals(HttpStatusCode.OK, response.status)
                assertEquals("I'm ready! :)", response.bodyAsText())
            }
        }
    }

    @Nested
    @DisplayName("Unsuccessful liveness and readiness")
    inner class Unsuccessful {
        @Test
        fun `Returns internal server error when liveness check fails`() {
            testApplication {
                setupPodApi(ApplicationState(false, false))
                val response = client.get("/is_alive")
                assertEquals(HttpStatusCode.InternalServerError, response.status)
                assertEquals("I'm dead x_x", response.bodyAsText())
            }
        }

        @Test
        fun `Returns internal server error when readiness check fails`() {
            testApplication {
                setupPodApi(ApplicationState(false, false))
                val response = client.get("/is_ready")
                assertEquals(HttpStatusCode.InternalServerError, response.status)
                assertEquals("Please wait! I'm not ready :(", response.bodyAsText())
            }
        }
    }
}
