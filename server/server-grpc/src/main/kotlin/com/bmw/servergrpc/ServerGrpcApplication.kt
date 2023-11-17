package com.bmw.servergrpc

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ServerGrpcApplication

fun main(args: Array<String>) {
    runApplication<ServerGrpcApplication>(*args)
}
