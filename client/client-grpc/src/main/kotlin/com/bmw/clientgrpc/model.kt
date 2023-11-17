package com.bmw.clientgrpc

data class Store(
    val id: Long,
    val name: String,
    val address: String,
    val phone: String,
    val teas : List<BubbleTea> = listOf()
)

data class BubbleTea(
    val id: Long,
    val name: String,
    val description: String,
    val price: Double,
    val storeId: Long
)
