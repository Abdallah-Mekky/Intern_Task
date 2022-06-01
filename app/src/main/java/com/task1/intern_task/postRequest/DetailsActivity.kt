package com.task1.intern_task.postRequest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputLayout
import com.task1.intern_task.headers.HeadersAdapter
import com.task1.intern_task.R
import com.task1.intern_task.headers.HeadersItem
import java.util.concurrent.Executors

class DetailsActivity : AppCompatActivity() {

    companion object {
        var requestBody: String? = null
    }

    lateinit var addBody: Button
    lateinit var postRequest: Button
    var bodyList = mutableListOf<HeadersItem>()
    lateinit var requestBodyLayout: TextInputLayout
    var service = Executors.newSingleThreadExecutor()
    var handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        initViews()

    }

    private fun initViews() {


        addBody = findViewById(R.id.addBody)
        postRequest = findViewById(R.id.POSTButton)
        requestBodyLayout = findViewById(R.id.requestBody_Layout)

        addBody.setOnClickListener {
            requestBody = '"' + requestBodyLayout.editText?.text.toString() + '"'
            Log.e("request Body", "" + requestBody)

            var fullURL:String? =   intent.getStringExtra("URL")

            service.execute {

                handler.post { PostRequest.sendPostRequest(fullURL!!, requestBody!!) }


            }
        }

    }
}
