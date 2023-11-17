package com.bmw.clientgrpc

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ClientGrpcApplication

fun main(args: Array<String>) {
    runApplication<ClientGrpcApplication>(*args)
}
