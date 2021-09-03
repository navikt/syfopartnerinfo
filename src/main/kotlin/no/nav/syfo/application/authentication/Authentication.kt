package no.nav.syfo.application.authentication

import com.auth0.jwk.JwkProvider
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.auth.jwt.JWTPrincipal
import io.ktor.auth.jwt.jwt
import no.nav.syfo.Environment
import org.slf4j.Logger
import org.slf4j.LoggerFactory

val log: Logger = LoggerFactory.getLogger("no.nav.syfo.application.authentication")

fun Application.installJwtAuthenticationV1(
    environment: Environment,
    jwtIssuerType: JwtIssuerType,
    jwkProvider: JwkProvider
) {
    install(Authentication) {
        jwt(name = jwtIssuerType.name) {
            verifier(jwkProvider, environment.jwtIssuer)
            validate { credentials ->
                val appId: String = credentials.payload.getClaim("appid").asString()
                log.info("authorization attempt for $appId")

                val envappids = environment.appIds.joinToString()
                log.info("appidid $appId list $envappids")

                val clientId = environment.appIds
                val aud = credentials.payload.audience
                log.info("clientid $clientId,  aud $aud")

                if (appId in environment.appIds && environment.clientId in credentials.payload.audience) {
                    log.info("authorization ok")
                    return@validate JWTPrincipal(credentials.payload)
                }
                log.info("authorization failed")
                return@validate null
            }
        }
    }
}
