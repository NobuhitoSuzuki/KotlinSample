package jp.co.sgnet.mediaPicker.internal.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v4.content.FileProvider
import android.support.v4.os.EnvironmentCompat
import jp.co.sgnet.mediaPicker.internal.entity.CaptureStrategy
import java.io.File
import java.io.IOException
import java.lang.ref.WeakReference
import java.text.SimpleDateFormat
import java.util.*

class MediaStoreCompat {
    private var context: WeakReference<Activity>
    private var fragment: WeakReference<Fragment>?
    private var captureStrategy: CaptureStrategy? = null
    private var currentPhotoUri: Uri? = null
    private var currentPhotoPath: String? = null

    companion object {
        fun hasCameraFeature(context: Context): Boolean {
            val pm = context.applicationContext.packageManager
            return pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)
        }
    }

    constructor(activity: Activity) : this(activity, null)

    constructor(activity: Activity, fragment: Fragment?) {
        context = WeakReference(activity)
        this.fragment = if (fragment == null) {
            null
        } else {
            WeakReference(fragment)
        }
    }

    fun setCaptureStrategy(strategy: CaptureStrategy) {
        captureStrategy = strategy
    }

    fun dispatchCaptureIntent(context: Context, requestCode: Int) {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(context.packageManager) != null) {
            var photoFile: File? = null
            try {
                photoFile = createImageFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            photoFile?.let {file ->
                currentPhotoPath = file.absolutePath
                currentPhotoUri = FileProvider.getUriForFile(context, captureStrategy!!.authority, file)

                intent.putExtra(MediaStore.EXTRA_OUTPUT, currentPhotoUri)
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    val resInfoList = context.packageManager
                        .queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
                    for (resolveInfo in resInfoList) {
                        val packageName = resolveInfo.activityInfo.packageName
                        context.grantUriPermission(
                            packageName, currentPhotoUri,
                            Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
                        )
                    }
                }
                fragment?.also { fragment->
                    fragment.get()?.startActivityForResult(intent, requestCode)
                } ?: kotlin.run {
                    this.context.get()?.startActivityForResult(intent, requestCode)

                }
            }
        }
    }

    private fun createImageFile(): File? {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
            .format(Date())
        val imageFileName = String.format("JPEG_%s.jpg", timeStamp)
        val storageDir: File
        if (captureStrategy?.isPublic!!) {
            storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)

            if (!storageDir.exists()) storageDir.mkdirs()
        } else {
            storageDir = this.context.get()?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        }

        // Avoid joining path components manually
        val tempFile = File(storageDir, imageFileName)

        // Handle the situation that user's external storage is not ready
        if (Environment.MEDIA_MOUNTED != EnvironmentCompat.getStorageState(tempFile)) {
            return null
        }
        return tempFile
    }

    fun getCurrentPhotoUri(): Uri? = currentPhotoUri
    fun getCurrentPhotoPath(): String? = currentPhotoPath
}