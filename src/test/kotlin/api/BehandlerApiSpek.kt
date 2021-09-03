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

class BehandlerApiSpek : Spek({
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
                with(handleRequest(HttpMethod.Get, "$API_BASE_PATH$v1BasePath$behandlerV1BasePath") {
                }) {
                    response.status() shouldBe HttpStatusCode.Unauthorized
                }
            }

            it("should return 200 OK") {
                with(handleRequest(HttpMethod.Get, "$API_BASE_PATH$v1BasePath$behandlerV1BasePath?$behandlerV1QueryParamHerid=987654321") {
                    addHeader(
                        Authorization,
                        "Bearer ${genereateJWT("2", "1")}"
                    )
                }) {
                    response.status() shouldBe HttpStatusCode.OK
                }
            }

            it("Should return 401 Unauthorized when appId not allowed") {
                with(handleRequest(HttpMethod.Get, "$API_BASE_PATH$v1BasePath$behandlerV1BasePath") {
                    addHeader(
                        Authorization,
                        "Bearer ${genereateJWT("5", "1")}"
                    )
                }) {
                    response.status() shouldBe HttpStatusCode.Unauthorized
                }
            }
        }
    }
})
