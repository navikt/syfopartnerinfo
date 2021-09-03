package no.nav.syfo.application.authentication

import com.auth0.jwk.JwkProvider
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import net.logstash.logback.argument.StructuredArguments
import no.nav.syfo.Environment
import org.slf4j.Logger
import org.slf4j.LoggerFactory

val log: Logger = LoggerFactory.getLogger("no.nav.syfo.application.authentication")

fun Application.installJwtAuthentication(
    environment: Environment,
    v1JwkProvider: JwkProvider,
    v2AcceptedAudienceList: List<String>,
    v2JwtIssuer: String,
    v2JwkProvider: JwkProvider,
) {
    install(Authentication) {
        configureJwtV1(
            environment = environment,
            jwkProvider = v1JwkProvider,
        )
        configureJwtV2(
            acceptedAudienceList = v2AcceptedAudienceList,
            jwtIssuer = v2JwtIssuer,
            jwkProvider = v2JwkProvider,
        )
    }
}

fun Authentication.Configuration.configureJwtV1(
    environment: Environment,
    jwkProvider: JwkProvider
) {
    jwt(name = JwtIssuerType.INTERNAL_AZUREAD_VEILEDER_V1.name) {
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

fun Authentication.Configuration.configureJwtV2(
    acceptedAudienceList: List<String>,
    jwtIssuer: String,
    jwkProvider: JwkProvider,
) {
    jwt(name = JwtIssuerType.INTERNAL_AZUREAD_VEILEDER_V2.name) {
        verifier(jwkProvider, jwtIssuer)
        validate { credential ->
            if (hasExpectedAudience(credential, acceptedAudienceList)) {
                JWTPrincipal(credential.payload)
            } else {
                log.info(
                    "Auth: Unexpected audience for jwt {}, {}",
                    StructuredArguments.keyValue("issuer", credential.payload.issuer),
                    StructuredArguments.keyValue("audience", credential.payload.audience)
                )
                null
            }
        }
    }
}

fun hasExpectedAudience(credentials: JWTCredential, expectedAudience: List<String>): Boolean {
    return expectedAudience.any { credentials.payload.audience.contains(it) }
}
