package com.church.service


import com.church.exception.ApiException
import com.church.model.account.Account
import com.church.model.account.RoleType
import com.church.model.account.UserRole
import com.church.model.refreshtoken.RefreshToken
import com.church.model.verification.VerificationToken
import com.church.payload.LoginRequest
import com.church.payload.LoginResponse
import com.church.payload.RegisterRequest
import com.church.payload.refreshtoken.RefreshTokenRequest
import com.church.payload.refreshtoken.RefreshTokenResponse
import com.church.repository.AccountRepository
import com.church.repository.RefreshTokenRepository
import com.church.repository.UserRoleRepository
import com.church.repository.VerificationTokenRepository
import com.church.security.JwtUtil
import com.church.security.User
import com.church.util.EmailUtil
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.UUID

@Service
class AuthenticationService(
    private val passwordEncoder: PasswordEncoder,
    private val authenticationManager: AuthenticationManager,
    private val accountRepository: AccountRepository,
    private val userRoleRepository: UserRoleRepository,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val jwtUtils: JwtUtil,
    private val emailUtil: EmailUtil,
    private val verificationTokenRepository: VerificationTokenRepository
)
{

    @Transactional
    fun register(registerRequest: RegisterRequest) {
        if (accountRepository.existsByEmail(registerRequest.email)) {
            throw UsernameNotFoundException("Email already exists")
        }
            val account = Account(
                email = registerRequest.email,
                password = passwordEncoder.encode(registerRequest.password)
            )

            val savedAccount = accountRepository.save(account)

            val userRole = UserRole(
                roleType = RoleType.USER,
                account = savedAccount
            )

        userRoleRepository.save(userRole)
        resendVerificationToken(registerRequest.email)
    }

    fun login(authRequest: LoginRequest): LoginResponse {
        val authentication = try {
            authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(
                    authRequest.email,
                    authRequest.password
                )
            )
        } catch (ex: BadCredentialsException) {
            throw BadCredentialsException(ex.message)
        }

        SecurityContextHolder.getContext().authentication = authentication

        if (authentication.isAuthenticated) {
            val userAccountDetails = authentication.principal as User

            val roles = userAccountDetails.authorities
                .map { it.authority.removePrefix("ROLE_") }

           val refreshToken = createRefreshToken(authRequest.email)

            return LoginResponse(
                username = userAccountDetails.username,
                accessToken = jwtUtils.generateToken(userAccountDetails),
                refreshToken =refreshToken.token,
                roles= roles
            )
        } else {
            throw UsernameNotFoundException("Invalid user request!")
        }
    }



    fun generateAccessTokenFromRefreshToken(request: RefreshTokenRequest): RefreshTokenResponse {

        val refreshToken = findByToken(request.token)
        val tokenVerification = verifyExpiration(refreshToken)

        val account = accountRepository.findById(refreshToken.userId).orElseThrow { BadCredentialsException("Refresh token is invalid")}

        val user = User(account,account.userRoles.toList())

        val accessToken = jwtUtils.generateToken(user)

        return RefreshTokenResponse(
            accessToken = accessToken,
            refreshToken = request.token,
            expiresIn = tokenVerification.expiresAt
        )
    }

    private fun findByToken(token: String): RefreshToken {
        return refreshTokenRepository.findByToken(token)?: throw ApiException("Refresh token is invalid!!!!")
    }

    private fun verifyExpiration(token: RefreshToken): RefreshToken {
        token.expiresAt?.let {
            if (it < System.currentTimeMillis()) {
                refreshTokenRepository.delete(token)
                throw ApiException("Refresh token ${token.token} was expired")
            }
        }
        return token
    }

    private fun createRefreshToken(email: String): RefreshToken {
        val expirationTimeMillis = System.currentTimeMillis() + 60 * 60 * 1000 * 50

        val account = accountRepository.findByEmail(email)
            ?: throw BadCredentialsException("Invalid credentials")

        val refreshToken = RefreshToken(
            userId = account.id!!,
            token = UUID.randomUUID().toString(),
            expiresAt = expirationTimeMillis,
            createdAt = System.currentTimeMillis()
        )

        return refreshTokenRepository.save(refreshToken)
    }


    fun resendVerificationToken(email: String) {
        val token = generateToken()
        val expiration = LocalDateTime.now().plusMinutes(10)

        verificationTokenRepository.save(
            VerificationToken(
                token = token,
                email = email,
                expiresAt = expiration
            )
        )

        emailUtil.sendVerificationEmail(email, token, generateToken())
    }

    private fun generateToken(): String {
        return (1..5)
            .map { (0..9).random() }
            .joinToString("")
    }
}
