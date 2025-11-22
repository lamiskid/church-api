package com.church

import com.church.model.account.Account
import com.church.model.account.RoleType
import com.church.model.account.UserRole
import com.church.repository.AccountRepository
import com.church.repository.UserRoleRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder

@EnableAsync
@SpringBootApplication
class ChurchApiApplication(
	private val accountRepository: AccountRepository,
	private val userRoleRepository: UserRoleRepository,
	private val passwordEncoder: PasswordEncoder,
):CommandLineRunner {
	override fun run(vararg args: String?) {
		if (accountRepository.existsByUsername("admin")) {
			println("Username already initialize")
			//throw UsernameNotFoundException("Username already exists")
		}else{

			val account = Account(
				email = "admin@email.com",
				username = "admin",
				firstName = "admin",
				lastName = "pastor",
				password = passwordEncoder.encode("admin")
			)

			val savedAccount = accountRepository.save(account)

			val userRole = UserRole(
				roleType = RoleType.ADMIN,
				account = savedAccount
			)

			userRoleRepository.save(userRole)
		}

	}
}

fun main(args: Array<String>) {
	runApplication<ChurchApiApplication>(*args)
}
