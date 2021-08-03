package com.example.phonestore.services

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.Environment
import androidx.exifinterface.media.ExifInterface
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

object ImageUtil {
    private const val MAX_WIDTH = 1024
    private const val MAX_HEIGHT = 1024
    private const val QUALITY = 70
    private const val FOLDER_NAME = "/ImageCompressor"

    private fun generateImageName() = SimpleDateFormat("yyyymmddhhmmssSSS", Locale.getDefault()).format(
        Date()
    ) + ".jpg"

    fun writeImageToDisk(image: ByteArray): String {
        val file = File(
            Environment.getExternalStorageDirectory().toString() + File.separator + Environment.DIRECTORY_PICTURES,
            System.currentTimeMillis().toString().plus(".jpg"))

        var output: FileOutputStream? = null
        try {
            output = FileOutputStream(file).apply {
                write(image)
            }
            return file.absolutePath
        } catch (e: IOException) {
        } finally {
            output?.let { it ->
                try {
                    it.close()
                } catch (e: IOException) {
                }
            }
        }
        return ""
    }


    fun compressImage(context: Context, imagePath: String): String {
        var cacheDir = context.externalCacheDir

        if (cacheDir == null)
            cacheDir = context.cacheDir

        val rootDir = cacheDir?.absolutePath + FOLDER_NAME
        val root = File(rootDir)

        if (!root.exists())
            root.mkdirs()

        var bitmap = decodeImageFromFiles(imagePath, MAX_WIDTH, MAX_HEIGHT)

        bitmap = rotateImageIfRequired(context, bitmap, imagePath)

        val compressed = File(root, generateImageName())

        val byteArrayOutputStream = ByteArrayOutputStream()

        bitmap?.compress(Bitmap.CompressFormat.JPEG, QUALITY, byteArrayOutputStream)

        val fileOutputStream = FileOutputStream(compressed)
        fileOutputStream.write(byteArrayOutputStream.toByteArray())
        fileOutputStream.flush()

        fileOutputStream.close()

        return compressed.absolutePath
    }

    private fun decodeImageFromFiles(path: String, width: Int, height: Int): Bitmap? {

        val scaleOptions = BitmapFactory.Options()
        scaleOptions.inJustDecodeBounds = true

        BitmapFactory.decodeFile(path, scaleOptions)

        var scale = 1
        while (scaleOptions.outWidth / scale / 2 >= width && scaleOptions.outHeight / scale / 2 >= height) {
            scale *= 2
        }
        val outOptions = BitmapFactory.Options()
        outOptions.inSampleSize = scale

        return BitmapFactory.decodeFile(path, outOptions)
    }

    fun deleteFolderImage(context: Context) {
        var cacheDir = context.externalCacheDir

        if (cacheDir == null)
            cacheDir = context.cacheDir

        val dir = File(cacheDir?.absolutePath + FOLDER_NAME)
        if (dir.exists() || dir.list().isNullOrEmpty())
            return

        for (child in dir.list())
            File(dir, child).delete()
    }


    @Throws(IOException::class)
    private fun rotateImageIfRequired(context: Context, img: Bitmap?, path: String): Bitmap? {

        val ei = ExifInterface(path)
        val orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)

        return when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(img, 90)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(img, 180)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(img, 270)
            else -> img
        }
    }

    private fun rotateImage(img: Bitmap?, degree: Int): Bitmap? {
        if (img != null) {
            val matrix = Matrix()
            matrix.postRotate(degree.toFloat())
            val rotatedImg = Bitmap.createBitmap(img, 0, 0, img.width, img.height, matrix, true)
            img.recycle()
            return rotatedImg
        }
        return null
    }
}