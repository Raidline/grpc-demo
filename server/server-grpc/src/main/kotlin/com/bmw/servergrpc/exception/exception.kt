package com.bmw.servergrpc.exception

sealed class BaseException(reason : String, val code : Int) : RuntimeException(reason)

class NotFoundException(reason : String, code : Int = 404) : BaseException(reason, code)
