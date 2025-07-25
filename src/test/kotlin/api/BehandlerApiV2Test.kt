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
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import testhelper.testApiModule
import testhelper.testEnvironment

class BehandlerApiV2Test {
    private val partnerInformasjonService = mockk<PartnerInformasjonService>()

    init {
        io.mockk.coEvery { partnerInformasjonService.finnPartnerInformasjon(any()) } returns getListPartnerInformasjon()
    }

    fun ApplicationTestBuilder.setupApi() {
        application {
            routing {
                testApiModule(
                    partnerInformasjonService = partnerInformasjonService,
                )
            }
        }
    }

    @Test
    fun `Should return 401 Unauthorized`() {
        testApplication {
            setupApi()
            val response = client.get("$API_BASE_PATH$v2BasePath$behandlerV2BasePath")
            assertEquals(HttpStatusCode.Unauthorized, response.status)
        }
    }

    @Test
    fun `Should return 200 OK`() {
        testApplication {
            val token = genereateJWT(
                consumerClientId = "2",
                audience = testEnvironment().azureAppClientId,
                issuer = testEnvironment().azureOpenIdConfigIssuer,
            )
            setupApi()
            val response =
                client.get("$API_BASE_PATH$v2BasePath$behandlerV2BasePath?$behandlerV2QueryParamHerid=987654321") {
                    bearerAuth(token!!)
                }
            assertEquals(HttpStatusCode.OK, response.status)
        }
    }
}
