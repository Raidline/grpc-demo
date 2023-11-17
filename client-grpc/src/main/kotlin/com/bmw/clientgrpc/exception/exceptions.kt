package com.bmw.clientgrpc.exception

sealed class BaseException(message : String, val code : Int) : RuntimeException(message)

class NotFoundException(message : String) : BaseException(message, 404)
