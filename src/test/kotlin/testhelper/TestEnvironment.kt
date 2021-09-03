package testhelper

import com.auth0.jwk.JwkProvider
import com.auth0.jwk.JwkProviderBuilder
import io.ktor.application.*
import no.nav.syfo.Environment
import no.nav.syfo.application.ApplicationState
import no.nav.syfo.application.apiModule
import no.nav.syfo.services.PartnerInformasjonService
import java.nio.file.Paths

fun Application.testApiModule(
    partnerInformasjonService: PartnerInformasjonService,
) {
    val environment = testEnvironment()
    val applicationState = testApplicationState()
    val jwkProvider = testJwkProvider()
    apiModule(
        environment = environment,
        applicationState = applicationState,
        jwkProvider = jwkProvider,
        partnerInformasjonService = partnerInformasjonService
    )
}


fun testEnvironment() = Environment(8080,
    azureAppClientId = "azureAppClientId",
    azureOpenIdConfigJwksUri = "azureOpenIdConfigJwksUri",
    azureOpenIdConfigIssuer = "azureOpenIdConfigIssuer",
    databaseUrl = "",
    databasePrefix = "",
    databaseUsername = "",
    databasePassword = ""
)

fun testApplicationState() = ApplicationState(
    alive = true,
    ready = true
)

fun testJwkProvider(): JwkProvider {
    val path = "src/test/resources/jwkset.json"
    val uri = Paths.get(path).toUri().toURL()
    return JwkProviderBuilder(uri).build()
}
