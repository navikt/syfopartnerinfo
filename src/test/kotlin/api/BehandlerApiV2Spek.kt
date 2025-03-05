package api

import genereateJWT
import getListPartnerInformasjon
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.routing.*
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

    fun ApplicationTestBuilder.setupApi() {
        application {
            routing {
                testApiModule(
                    partnerInformasjonService = partnerInformasjonService,
                )
            }
        }
    }

    describe("Validate elektroniskAbonoment with authentication") {
        it("Should return 401 Unauthorized") {
            testApplication {
                setupApi()
                val response = client.get("$API_BASE_PATH$v2BasePath$behandlerV2BasePath")
                response.status shouldBe HttpStatusCode.Unauthorized
            }
        }

        it("should return 200 OK") {
            testApplication {
                val token = genereateJWT(
                    consumerClientId = "2",
                    audience = testEnvironment().azureAppClientId,
                    issuer = testEnvironment().azureOpenIdConfigIssuer,
                )
                setupApi()
                val response = client.get("$API_BASE_PATH$v2BasePath$behandlerV2BasePath?$behandlerV2QueryParamHerid=987654321") {
                    bearerAuth(token!!)
                }
                response.status shouldBe HttpStatusCode.OK
            }
        }
    }
})
