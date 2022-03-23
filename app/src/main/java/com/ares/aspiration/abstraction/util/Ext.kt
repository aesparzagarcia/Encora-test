package com.ares.aspiration.abstraction.util

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ares.aspiration.abstraction.util.Constants.ERROR_MESSAGE
import com.ares.aspiration.abstraction.util.Constants.GO_BACK
import com.xwray.groupie.Group
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder

inline fun <reified VM : ViewModel> FragmentActivity.viewModelProvider(
    factory: ViewModelProvider.Factory
) = ViewModelProvider(this, factory)[VM::class.java]

operator fun GroupAdapter<GroupieViewHolder>.plusAssign(element: Group) = this.add(element)

fun String.cleartext(): String {
    return if (this.isNotEmpty())
        this.replace("\"","").replace("[","").replace("]","")
    else this
}

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun Context.showDialogAlert(message: String, activity: Activity) {
    val dialogBuilder = AlertDialog.Builder(this)
    dialogBuilder.setMessage(message.cleartext())
        .setCancelable(false)
        .setNegativeButton(GO_BACK) { _, _ ->
            activity.finish()
        }

    val alert = dialogBuilder.create()
    alert.setTitle(ERROR_MESSAGE)
    alert.show()
}