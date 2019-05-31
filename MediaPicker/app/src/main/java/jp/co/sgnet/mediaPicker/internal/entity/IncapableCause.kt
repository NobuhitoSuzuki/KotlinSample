package jp.co.sgnet.mediaPicker.internal.entity

import android.content.Context
import android.support.v4.app.FragmentActivity
import android.widget.Toast
import jp.co.sgnet.mediaPicker.ui.widget.IncapableDialog

class IncapableCause {
    enum class Form {
        TOAST, DIALOG, NONE
    }
    private var form: Form = Form.TOAST
    private var title: String? = null
    private var message: String? = null

    constructor(form: Form, title: String?, message: String) {
        this.form = form
        this.title = title
        this.message = message
    }

    constructor(title: String?, message: String) {
        this.title = title
        this.message = message
    }

    constructor(form: Form, message: String) {
        this.form = form
        this.message = message
    }

    constructor(title: String?) {
        this.title = title
    }

    // TODO: imp
    companion object {
        @JvmStatic
        fun handleCause(context: Context, cause: IncapableCause?) {
            cause?.let {
                when (cause.form) {
                    Form.NONE -> {
                    }
                    Form.DIALOG -> {
                        val incapableDialog = IncapableDialog.newInstance(cause.title, cause.message)
                        incapableDialog.show(
                            (context as FragmentActivity).supportFragmentManager,
                            IncapableDialog.toString()
                        )
                    }
                    Form.TOAST -> Toast.makeText(context, cause.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}