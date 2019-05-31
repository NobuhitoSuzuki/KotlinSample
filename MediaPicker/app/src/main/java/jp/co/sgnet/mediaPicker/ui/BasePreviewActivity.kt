package jp.co.sgnet.mediaPicker.ui

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import jp.co.sgnet.mediaPicker.R

import kotlinx.android.synthetic.main.activity_base_preview.*

class BasePreviewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base_preview)
        setSupportActionBar(toolbar)
    }

}
