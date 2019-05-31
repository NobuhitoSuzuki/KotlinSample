package jp.co.sgnet.mediaPicker.ui.widget

import android.os.Bundle
import android.support.v4.app.DialogFragment

private const val ARG_TITLE = "title"
private const val ARG_MESSAGE = "message"

class IncapableDialog: DialogFragment() {
    companion object {
        fun newInstance(title: String?, message: String?): IncapableDialog =
            IncapableDialog().apply {
                arguments = Bundle().apply {
                    putString(ARG_TITLE, title)
                    putString(ARG_MESSAGE, message)
                }
            }
    }
}