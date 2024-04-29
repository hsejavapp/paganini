package ru.hse.myapplication.words

import androidx.compose.ui.text.toLowerCase
import com.squareup.okhttp.Response
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL


//fun sendGetRequest() {
//    val url = URL("http://84.201.143.191:80")
//    val connection = url.openConnection() as HttpURLConnection
//    connection.requestMethod = "GET"
//
//    val responseCode = connection.responseCode
//    if (responseCode == HttpURLConnection.HTTP_OK) {
//        val reader = BufferedReader(InputStreamReader(connection.inputStream))
//        val response = reader.readLine()
//        println("Response from server: $response")
//        reader.close()
//    } else {
//        println("Error: HTTP $responseCode")
//    }
//    connection.disconnect()
//}

fun getWord(urlStr: String = "http://84.201.143.191:80/new_word"): List<Char> {
    val url = URL(urlStr)
    val connection = url.openConnection() as HttpURLConnection
    connection.requestMethod = "GET"
    val responseCode = connection.responseCode
    var response: List<Char> = emptyList()
    if (responseCode == HttpURLConnection.HTTP_OK) {
        val reader = BufferedReader(InputStreamReader(connection.inputStream))
        response = reader.readLine().uppercase().toCharArray().toList()
        println("Response from server: $response")
        reader.close()
    } else {
        println("Error: HTTP $responseCode")
    }
    connection.disconnect()
    return response
}


fun getCollisions(word: StringBuffer, urlStr: String = "http://84.201.143.191:80/collisions/"): List<Int>? {
    val url = URL(urlStr + word.toString().lowercase())
    val connection = url.openConnection() as HttpURLConnection
    connection.requestMethod = "GET"
    val responseCode = connection.responseCode
    var collisionList: List<Int>? = null
    if (responseCode == HttpURLConnection.HTTP_OK) {
        val reader = BufferedReader(InputStreamReader(connection.inputStream))
        collisionList = reader.readLine().map { it.toString().toInt() }
        println("Response from server: $collisionList")
        reader.close()
    } else {
        println("Error: HTTP $responseCode")
    }
    connection.disconnect()
    return collisionList
}
