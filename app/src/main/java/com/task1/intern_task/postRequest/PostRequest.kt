package com.task1.intern_task.postRequest

import android.util.Log
import com.task1.intern_task.getRequest.GetRequest
import com.task1.intern_task.KeyValuePair
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.util.ArrayList

class PostRequest {

    companion object {

        var headers: ArrayList<KeyValuePair> = ArrayList<KeyValuePair>()

        fun sendPostRequest(fullURL: String, body: String) {


            try {


                val mURL = URL(fullURL)
                var connection = mURL.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.doOutput = true

                connection.setRequestProperty("Content-Type", "application/json")
                connection.setRequestProperty("Accept", "application/json")


                val out = BufferedWriter(OutputStreamWriter(connection.getOutputStream()))
                out.write(body)
                out.close()

                GetRequest.response = connection.responseCode.toString()
                Log.e("URL", "" + connection.url)
                Log.e("Response Code", "" + connection.responseCode)


                BufferedReader(InputStreamReader(connection.inputStream)).use {
                    val response = StringBuffer()

                    var inputLine = it.readLine()
                    while (inputLine != null) {
                        response.append(inputLine)
                        inputLine = it.readLine()
                    }

                    Log.e("Response", "" + response)
                }

            } catch (e: IOException) {
                e.printStackTrace();
                Log.e("Error", "" + e)
            }

        }
    }
}