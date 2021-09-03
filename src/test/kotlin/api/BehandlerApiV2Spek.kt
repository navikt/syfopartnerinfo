package api

import genereateJWT
import getListPartnerInformasjon
import io.ktor.http.*
import io.ktor.http.HttpHeaders.Authorization
import io.ktor.server.testing.*
import io.mockk.mockk
import no.nav.syfo.aksessering.api.*
import no.nav.syfo.application.API_BASE_PATH
import no.nav.syfo.services.PartnerInformasjonService
import org.amshove.kluent.shouldBe
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import testhelper.testApiModule
import testhelper.testEnvironment

class BehandlerApiV2Spek : Spek({
    val partnerInformasjonService = mockk<PartnerInformasjonService>()
    io.mockk.coEvery { partnerInformasjonService.finnPartnerInformasjon(any()) } returns getListPartnerInformasjon()
    fun withTestApplicationForApi(receiver: TestApplicationEngine, block: TestApplicationEngine.() -> Unit) {
        receiver.start()
        receiver.application.testApiModule(
            partnerInformasjonService = partnerInformasjonService,
        )
        return receiver.block()
    }

    describe("Validate elektroniskAbonoment with authentication") {
        withTestApplicationForApi(TestApplicationEngine()) {
            it("Should return 401 Unauthorized") {
                with(handleRequest(HttpMethod.Get, "$API_BASE_PATH$v1BasePath$behandlerV2BasePath") {
                }) {
                    response.status() shouldBe HttpStatusCode.Unauthorized
                }
            }

            it("should return 200 OK") {
                val token = genereateJWT(
                    consumerClientId = "2",
                    audience = testEnvironment().azureAppClientId,
                    issuer = testEnvironment().azureOpenIdConfigIssuer,
                )
                with(handleRequest(HttpMethod.Get, "$API_BASE_PATH$v2BasePath$behandlerV2BasePath?$behandlerV2QueryParamHerid=987654321") {
                    addHeader(
                        Authorization,
                        "Bearer $token"
                    )
                }) {
                    response.status() shouldBe HttpStatusCode.OK
                }
            }
        }
    }
})
