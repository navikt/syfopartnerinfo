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
    val jwkProvider = testJwkProviderV1()
    apiModule(
        environment = environment,
        applicationState = applicationState,
        jwkProviderV1 = jwkProvider,
        jwkProviderV2 = jwkProvider,
        partnerInformasjonService = partnerInformasjonService
    )
}


fun testEnvironment() = Environment(8080,
    jwtIssuer = "https://sts.issuer.net/myid",
    appIds = "2,3".split(","),
    clientId = "1",
    aadAccessTokenUrl = "",
    aadDiscoveryUrl = "",
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

fun testJwkProviderV1(): JwkProvider {
    val path = "src/test/resources/jwkset.json"
    val uri = Paths.get(path).toUri().toURL()
    return JwkProviderBuilder(uri).build()
}
