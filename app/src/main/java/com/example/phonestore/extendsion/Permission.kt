package com.example.phonestore.extendsion

import android.Manifest
import android.app.Activity
import android.content.Context
import androidx.core.app.ActivityCompat
import pub.devrel.easypermissions.EasyPermissions

class Permission {
    fun Context.hasReadStoragePermission(): Boolean {
        return EasyPermissions.hasPermissions(this,
            Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    fun Context.hasWriteStoragePermission(): Boolean {
        return EasyPermissions.hasPermissions(this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    fun Context.hasReadWriteStoragePermission(): Boolean {
        return EasyPermissions.hasPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                && EasyPermissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    fun Context.hasCameraPermission(): Boolean {
        return EasyPermissions.hasPermissions(this,
            Manifest.permission.CAMERA)
    }

    fun Context.requestReadAndWriteStoragePermission(requestCode: Int) {
        ActivityCompat.requestPermissions(
            this as Activity,
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            ),
            requestCode
        )
    }
}