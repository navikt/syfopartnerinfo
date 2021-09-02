package api

import com.auth0.jwk.JwkProviderBuilder
import genereateJWT
import getListPartnerInformasjon
import io.ktor.auth.authenticate
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.routing.routing
import io.ktor.server.testing.TestApplicationEngine
import io.ktor.server.testing.handleRequest
import io.mockk.mockk
import java.nio.file.Paths
import no.nav.syfo.Environment
import no.nav.syfo.aksessering.api.registerBehandlerApi
import no.nav.syfo.application.api.installContentNegotiation
import no.nav.syfo.application.authentication.installJwtAuthentication
import no.nav.syfo.services.PartnerInformasjonService
import org.amshove.kluent.shouldBe
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

class BehandlerApiSpek : Spek({
    val partnerInformasjonService: PartnerInformasjonService = mockk()
    io.mockk.coEvery { partnerInformasjonService.finnPartnerInformasjon(any()) } returns getListPartnerInformasjon()
    fun withTestApplicationForApi(receiver: TestApplicationEngine, block: TestApplicationEngine.() -> Unit) {
        receiver.start()
        val environment = Environment(8080,
                jwtIssuer = "https://sts.issuer.net/myid",
                appIds = "2,3".split(","),
                clientId = "1",
                aadAccessTokenUrl = "",
                aadDiscoveryUrl = "",
                databaseUrl = "",
                databasePrefix = "",
                databaseUsername = "",
                databasePassword = ""
                )
        val path = "src/test/resources/jwkset.json"
        val uri = Paths.get(path).toUri().toURL()
        val jwkProvider = JwkProviderBuilder(uri).build()
        receiver.application.installContentNegotiation()
        receiver.application.installJwtAuthentication(environment, jwkProvider)
        receiver.application.routing { authenticate { registerBehandlerApi(partnerInformasjonService) } }

        return receiver.block()
    }

    describe("Validate elektroniskAbonoment with authentication") {
        withTestApplicationForApi(TestApplicationEngine()) {
            it("Should return 401 Unauthorized") {
                with(handleRequest(HttpMethod.Get, "/v1/behandler") {
                }) {
                    response.status() shouldBe HttpStatusCode.Unauthorized
                }
            }

            it("should return 200 OK") {
                with(handleRequest(HttpMethod.Get, "/v1/behandler?herid=987654321") {
                    addHeader(
                            "Authorization",
                            "Bearer ${genereateJWT("2", "1")}"
                    )
                }) {
                    response.status() shouldBe HttpStatusCode.OK
                }
            }

            it("Should return 401 Unauthorized when appId not allowed") {
                with(handleRequest(HttpMethod.Get, "/v1/behandler") {
                    addHeader(
                            "Authorization",
                            "Bearer ${genereateJWT("5", "1")}"
                    )
                }) {
                    response.status() shouldBe HttpStatusCode.Unauthorized
                }
            }
        }
    }
})
