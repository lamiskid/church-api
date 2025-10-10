package com.church

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ChurchApiApplication

fun main(args: Array<String>) {
	runApplication<ChurchApiApplication>(*args)
}
