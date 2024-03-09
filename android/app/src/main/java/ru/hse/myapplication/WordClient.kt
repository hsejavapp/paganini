//package ru.hse.myapplication
//
//import io.grpc.ManagedChannel
//import io.grpc.ManagedChannelBuilder
//import io.grpc.StatusRuntimeException
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.withContext
//import ru.hse.myapplication.generated.WordServiceGrpc
//import ru.hse.myapplication.generated.WordRequest
//import ru.hse.myapplication.generated.WordResponse
//
//// Асинхронный клиент для RPC
//class WordClient(private val channel: ManagedChannel) {
//
//    private val stub: WordServiceGrpc.WordServiceBlockingStub = WordServiceGrpc.newBlockingStub(channel)
//
//    // Метод для отправки слова на сервер и получения ответа
//    suspend fun sendWord(word: String): WordResponse {
//        val request = WordRequest.newBuilder().setWord(word).build()
//
//        return withContext(Dispatchers.IO) {
//
//        try {
//            stub.sendWord(request)
//        } catch (e: StatusRuntimeException) {
//            // Обработка ошибки
//            // e.status возвращает информацию об ошибке
//            WordResponse.getDefaultInstance()
//        }
//    }
//}
//
//// Метод для закрытия канала
//fun shutdown() {
//    channel.shutdown()
//}
//}
//
//// Пример использования клиента в вашем коде
//// Создание и использование клиента
//fun main() {
//    val channel = ManagedChannelBuilder.forAddress("your.server.com", 50051).usePlaintext().build()
//    val client = WordClient(channel)
//
//    val word = "ваше слово"
//    val response = runBlocking { client.sendWord(word) }
//
//    // Обработка ответа от сервера
//    // response содержит полученный ответ
//    println("Ответ от сервера: ${response.message}")
//
//    client.shutdown()
//}
