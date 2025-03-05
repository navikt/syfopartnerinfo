import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.routing.*
import io.ktor.server.testing.*
import no.nav.syfo.application.ApplicationState
import no.nav.syfo.application.api.registerNaisApi
import org.amshove.kluent.shouldBeEqualTo
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object SelfTestSpek : Spek({

    fun ApplicationTestBuilder.setupPodApi(applicationState: ApplicationState) {
        application {
            routing {
                registerNaisApi(applicationState)
            }
        }
    }

    describe("Successfull liveness and readyness tests") {
        it("Returns ok on is_alive") {
            testApplication {
                setupPodApi(ApplicationState(true, true))
                val response = client.get("/is_alive")
                response.status shouldBeEqualTo  HttpStatusCode.OK
                response.bodyAsText() shouldBeEqualTo "I'm alive! :)"
            }
        }
        it("Returns ok in is_ready") {
            testApplication {
                setupPodApi(ApplicationState(true, true))
                val response = client.get("/is_ready")
                response.status shouldBeEqualTo HttpStatusCode.OK
                response.bodyAsText() shouldBeEqualTo "I'm ready! :)"
            }
        }
    }
    describe("Unsuccessful liveness and readyness") {
        it("Returns internal server error when liveness check fails") {
            testApplication {
                setupPodApi(ApplicationState(false, false))
                val response = client.get("/is_alive")
                response.status shouldBeEqualTo HttpStatusCode.InternalServerError
                response.bodyAsText() shouldBeEqualTo "I'm dead x_x"
            }
        }
        it("Returns internal server error when readyness check fails") {
            testApplication {
                setupPodApi(ApplicationState(false, false))
                val response = client.get("/is_ready")
                response.status shouldBeEqualTo HttpStatusCode.InternalServerError
                response.bodyAsText() shouldBeEqualTo "Please wait! I'm not ready :("
            }
        }
    }
})
