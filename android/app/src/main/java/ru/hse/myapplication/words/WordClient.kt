package ru.hse.myapplication.words

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL


fun sendGetRequest() {
    val url = URL("http://84.201.143.191:8080")
    val connection = url.openConnection() as HttpURLConnection
    connection.requestMethod = "GET"

    val responseCode = connection.responseCode
    if (responseCode == HttpURLConnection.HTTP_OK) {
        val reader = BufferedReader(InputStreamReader(connection.inputStream))
        val response = reader.readLine()
        println("Response from server: $response")
        reader.close()
    } else {
        println("Error: HTTP $responseCode")
    }

    connection.disconnect()
}
