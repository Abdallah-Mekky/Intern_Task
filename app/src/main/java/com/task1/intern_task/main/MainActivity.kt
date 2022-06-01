package com.task1.intern_task.main

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.task1.intern_task.*
import com.task1.intern_task.base.BaseActivity
import com.task1.intern_task.getRequest.GetRequest
import com.task1.intern_task.headers.HeadersAdapter
import com.task1.intern_task.headers.HeadersItem
import com.task1.intern_task.postRequest.DetailsActivity
import java.lang.StringBuilder
import java.util.concurrent.Executors

class MainActivity : BaseActivity() {

    companion object {

        var HeadersList = mutableListOf<KeyValuePair>()

    }

    lateinit var headersRecyclerView: RecyclerView
    lateinit var headersAdapter: HeadersAdapter
    lateinit var addHeaders: Button
    lateinit var full_URL: TextInputLayout
    lateinit var getRequest: Button
    lateinit var postRequest: Button
    lateinit var url: String
    var deleteImage: ImageView? = null
    var keyEditText: TextInputEditText? = null
    var valueEditText: TextInputEditText? = null
    var dynamicList = mutableListOf<HeadersItem>()
    var service = Executors.newSingleThreadExecutor()
    var handler = Handler(Looper.getMainLooper())


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        if (checkForInternet(this)) {

            Toast.makeText(this, "Connected", Toast.LENGTH_LONG).show()

            initViews()

            addHeaders.setOnClickListener {
                addHeader()
            }

            deleteHeader()

            getRequest.setOnClickListener {

                if (valdiateURL()) {
                    url = full_URL.editText?.text.toString()
                    Log.e("copy url", url)
                    service.execute {

                        GetRequest.sendGetRequest(url)
                        handler.post {
                            showDialog(
                                dialogMessage(),
                                "OK",
                                { dialog, which -> dialog?.dismiss() },
                                "Get Response Details"
                            )
                        }
                    }
                }
            }

            postRequest.setOnClickListener {
                if (valdiateURL()) {
                    url = full_URL.editText?.text.toString()
                    Log.e("copy url", url)

                    service.execute {
                        var intent = Intent(this@MainActivity, DetailsActivity::class.java)
                        intent.putExtra("URL", url)
                        startActivity(intent)
                    }
                }
            }
        } else {
            ienternetDialog()
        }
    }


    fun checkForInternet(context: Context): Boolean {

        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else {

            @Suppress("DEPRECATION") val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }


    fun initViews() {
        headersRecyclerView = findViewById(R.id.headersRecyclerView)
        addHeaders = findViewById(R.id.add)
        full_URL = findViewById(R.id.url_Layout)
        getRequest = findViewById(R.id.GET)
        postRequest = findViewById(R.id.POST)
        headersAdapter = HeadersAdapter(null)
        headersRecyclerView.adapter = headersAdapter
        keyEditText = findViewById(R.id.key_Details)
        valueEditText = findViewById(R.id.value_Details)


    }

    fun addHeader() {
        dynamicList.add(HeadersItem("", "", R.drawable.ic_delete, R.drawable.ic_confirm))
        headersAdapter.reloadAdapter(dynamicList)
    }

    fun deleteHeader() {
        deleteImage = findViewById(R.id.deleteImage)
        headersAdapter.onDeleteImageClickListener = object : HeadersAdapter.OnItemClickListener {
            override fun onClick(
                position: Int,
                items: MutableList<HeadersItem>,
                itemView: HeadersItem
            ) {
                items.removeAt(position)
                headersAdapter.notifyItemRemoved(position)
                headersAdapter.notifyItemRangeChanged(position, items.size)
            }
        }
    }

    fun confirmHeader() {
        // keyEditText = findViewById(R.id.key_Details)
        //valueEditText = findViewById(R.id.value_Details)
        var key = keyEditText?.text.toString()

        var value = valueEditText?.text.toString()

        headersAdapter.onConfirmImageClickListener = object : HeadersAdapter.OnItemClickListener {
            override fun onClick(
                position: Int, items: MutableList<HeadersItem>, itemView: HeadersItem
            ) {

                dynamicList.get(position).key = keyEditText?.text.toString()
                Log.e("new key", key)
                dynamicList.get(position).value = valueEditText?.text.toString()
                Log.e("new value", value)
                Log.e("dynamic List", "" + dynamicList.get(0))
//                 headersAdapter.reloadAdapter(items)
            }
        }
    }

    fun valdiateURL(): Boolean {

        var isVaild: Boolean = true

        if (full_URL.editText?.text.toString().isBlank()) {

            full_URL.error = "Please Enter URL"
            isVaild = false
        } else {

            full_URL.error = null
        }
        return isVaild
    }

    fun error(): String {

        var tempText: String

        if (GetRequest.response == "200") {
            tempText = "No Error"
        } else {
            tempText = GetRequest.error
        }

        return tempText
    }

    fun printQueryParametres(): String {

        var queryParametres = StringBuilder()
        for (i in GetRequest.args) {
            Log.e("parametrs", i)
            queryParametres.append(i)
            Log.e("keys", "" + i)
            queryParametres.append(" = " + GetRequest.uriE?.getQueryParameter(i))
            queryParametres.append("\n")
            Log.e("Values", "" + GetRequest.uriE?.getQueryParameter(i))

        }

        return queryParametres.toString()
    }

    fun dialogMessage(): String {

        var stringBuilder = StringBuilder()

        stringBuilder.append("Response Code : " + GetRequest.response)
        stringBuilder.append("\n\n" + "Error : " + error())
        stringBuilder.append("\n\n" + "Request/Response Headers : ")
        stringBuilder.append("\n\n" + "Query Parametres : \n" + printQueryParametres())

        return stringBuilder.toString()
    }

    fun ienternetDialog() {

        showDialog(
            "Please Open Internet ", "OK",
            { dialog, which -> dialog?.dismiss() }, "Error NO Internet Connection"
        )
    }
}