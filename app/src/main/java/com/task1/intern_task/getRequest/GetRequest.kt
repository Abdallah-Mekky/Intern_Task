package com.task1.intern_task.getRequest

import android.net.Uri
import android.util.Log
import com.task1.intern_task.KeyValuePair
import com.task1.intern_task.main.MainActivity
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.util.ArrayList

class GetRequest() {

    companion object {

        var parametrs: ArrayList<KeyValuePair> = ArrayList<KeyValuePair>()
        var headers: ArrayList<KeyValuePair> = ArrayList<KeyValuePair>()
        var args: Set<String> = mutableSetOf()
        var response: String = ""
        var error: String = ""
        var uriE: Uri? = null

        fun sendGetRequest(fullURL: String) {

            try {
                val mURL = URL(fullURL)
                var uri = Uri.parse(fullURL)

                uriE = uri
                val server = uri.authority
                val path = uri.path
                val protocol = uri.scheme
                args = uri.queryParameterNames


                var connection = mURL.openConnection() as HttpURLConnection


                connection.requestMethod = "GET"

                for (i in MainActivity.HeadersList) {
//               connection.setRequestProperty(i.key,i.value)
                    Log.e("headers", "" + i)
                }

                // setRequestProperty("Content-Type", "application/json")
                response = connection.responseCode.toString()
                Log.e("URL", "" + connection.url)
                Log.e("Response Code", "" + connection.responseCode)

                BufferedReader(InputStreamReader(connection.inputStream)).use {
                    val response = StringBuffer()

                    var inputLine = it.readLine()
                    while (inputLine != null) {
                        response.append(inputLine)
                        inputLine = it.readLine()
                    }
                    it.close()
                    Log.e("Response", "" + response)
                }

            } catch (e: IOException) {
                e.printStackTrace();
                error = e.toString()
                Log.e("Error", "" + e)
            }

        }

        fun addQueryParameterautomatically() {
            var combinedParams = ""
            if (!parametrs.isEmpty()) {
                combinedParams += "?"
                for (p in parametrs) {
                    val paramKeys = URLEncoder.encode(p.key, "UTF-8")
                    val paramValues = URLEncoder.encode(p.value, "UTF-8")

                    combinedParams += if (combinedParams.length > 1) {
                        "&$paramKeys=$paramValues"

                    } else {
                        "$paramKeys=$paramValues"
                    }
                }
            }
        }
    }


}