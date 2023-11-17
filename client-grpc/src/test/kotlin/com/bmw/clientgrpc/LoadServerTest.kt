package com.bmw.clientgrpc

import com.bmw.clientgrpc.service.presentation.ClientGrpcService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.concurrent.CountDownLatch
import java.util.concurrent.atomic.AtomicBoolean

@SpringBootTest
class LoadServerTest {

    @Autowired
    private lateinit var clientGrpcService: ClientGrpcService

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `load server until forever`() = runTest {

        val lock = CountDownLatch(1)
        val error = AtomicBoolean(false)

        while (error.get().not()) {
            clientGrpcService.findGrpcAllStores()
                .catch {
                    lock.countDown()
                    error.set(true)
                }
                .onEach { value -> println("Received $value") }
                .onCompletion { cause -> if (cause == null) println("Flow is completed successfully") }
                .catch { cause -> println("Exception $cause happened") }
                .flowOn(Dispatchers.IO)
                .launchIn(this)
        }

        lock.await()
    }
}
