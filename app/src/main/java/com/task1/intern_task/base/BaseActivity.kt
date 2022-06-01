package com.task1.intern_task.base

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity

open class BaseActivity:AppCompatActivity() {

    fun showDialog(
        message: String? = null,
        posActionName: String? = null,
        posAction: DialogInterface.OnClickListener? = null,
        title:String?=null

    ) {

        var dialog = androidx.appcompat.app.AlertDialog.Builder(this)

        dialog.setMessage(message)
        dialog.setPositiveButton(posActionName, posAction)
        dialog.setTitle(title)
        dialog.show()
    }

}