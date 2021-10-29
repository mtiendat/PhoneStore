package com.example.phonestore.view.auth

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.ParcelFileDescriptor
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.phonestore.base.BaseActivity
import com.example.phonestore.databinding.ActivityCropPhotoBinding
import com.example.phonestore.services.Constant
import com.example.phonestore.services.Constant.CROP_PHOTO_BY_CAMERA
import com.example.phonestore.services.Constant.CROP_PHOTO_BY_GALLERY
import com.example.phonestore.viewmodel.UserViewModel
import com.facebook.appevents.AppEvent
import com.takusemba.cropme.OnCropListener
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.*


class ActivityCropPhoto: BaseActivity() {

    private lateinit var binding: ActivityCropPhotoBinding
    private var bitmap: Bitmap? = null
    private var uri : Uri? = null
    private var inputPFD: ParcelFileDescriptor? = null
    private var userViewModel: UserViewModel? = null
    private lateinit var  profilePic: MultipartBody.Part
    override fun setBinding() {
        binding = ActivityCropPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bitmap = intent.getParcelableExtra(CROP_PHOTO_BY_CAMERA)
        uri = intent.getParcelableExtra(CROP_PHOTO_BY_GALLERY)
        if(bitmap != null) bitmap?.let { binding.cropView.setBitmap(it)} else uri?.let { binding.cropView.setUri(it) }


        binding.cropView.addOnCropListener(object : OnCropListener {
            override fun onSuccess(bitmap: Bitmap) {
                    val file = bitmapToFile(bitmap)
                    val inputStream = FileInputStream(file)
                    val byte = getBytes(inputStream)
                    val requestBody: RequestBody = byte!!.toRequestBody("multipart/form-data".toMediaTypeOrNull(), 0, byte.size)
                    profilePic = MultipartBody.Part.createFormData(
                        "image",
                        "image.jpg",
                        requestBody
                    )
                com.example.phonestore.extendsion.AppEvent.notifyShowPopUp()
                userViewModel?.changeAvatar(profilePic)
            }

            override fun onFailure(e: Exception) {

            }
        })
        binding.ibDone.setOnClickListener {
            if(binding.cropView.isOffFrame()){
                Toast.makeText(this, "Vui lòng di chuyển để có ảnh đầy đủ" , Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }else binding.cropView.crop()
        }
    }

    override fun setViewModel() {
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        userViewModel?.loginResponse?.observe(this, {
            Toast.makeText(this, it?.messages.toString(), Toast.LENGTH_SHORT).show()
            if(it?.status == true){
                Constant.user = it.user
                setResult(RESULT_OK)
                com.example.phonestore.extendsion.AppEvent.notifyClosePopUp()
                finish()
            }
        })
    }
    private fun getBytes(input: InputStream): ByteArray?{
        val byteBuff = ByteArrayOutputStream()
        val bufSize = 1024
        val buff = ByteArray(bufSize)
        var len: Int
        while (input.read(buff).also { len = it } != -1){
            byteBuff.write(buff, 0, len)
        }

        return byteBuff.toByteArray()
    }

    private fun bitmapToFile(bitmap: Bitmap): File?{
        var file: File? = null
        return try {
            file = File(this.dataDir.toString() + File.separator + "image")
            file.createNewFile()

            //Convert bitmap to byte array
            val bos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 50, bos)
            val bitmapData = bos.toByteArray()

            //write the bytes in file
            val fos = FileOutputStream(file)
            fos.write(bitmapData)
            fos.flush()
            fos.close()
            file
        } catch (e: Exception) {
            e.printStackTrace()
            file // it will return null
        }
    }
}