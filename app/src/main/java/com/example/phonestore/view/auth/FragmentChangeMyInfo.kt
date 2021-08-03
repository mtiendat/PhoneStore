package com.example.phonestore.view.auth


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.ParcelFileDescriptor
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.phonestore.R
import com.example.phonestore.base.BaseFragment
import com.example.phonestore.databinding.FragmentChangeMyinfoBinding
import com.example.phonestore.extendsion.gone
import com.example.phonestore.extendsion.visible
import com.example.phonestore.services.Constant
import com.example.phonestore.viewmodel.UserViewModel
import com.google.android.material.snackbar.Snackbar
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.*

class FragmentChangeMyInfo: BaseFragment() {
    private lateinit var bindingChangeMyInfo: FragmentChangeMyinfoBinding
    private var isUpdateAvatar = false
    private var flag = 0
    private var data: String? =""
    private var userViewModel: UserViewModel? = null
    private var resultsLauncherPickImageGallery: ActivityResultLauncher<Intent>? = null
    private var resultsLauncherTakeAPicture: ActivityResultLauncher<Intent>? = null
    private var inputPFD: ParcelFileDescriptor? = null
    override fun setBinding(inflater: LayoutInflater, container: ViewGroup?): View? {
        bindingChangeMyInfo = FragmentChangeMyinfoBinding.inflate(inflater, container, false)
        return bindingChangeMyInfo.root
    }

    override fun setUI() {
        findNavController().currentBackStackEntry?.savedStateHandle?.apply {
            getLiveData<Int>("keyChangeAvatar").observe(viewLifecycleOwner) {
                if(flag==0) {
                    if (it == 1) {
                        pickImageFromGallery()
                        flag = 1
                    } else capturePhoto()
                }else flag = 0

            }
            getLiveData<String>("key").observe(viewLifecycleOwner){
                isUpdateAvatar = false
                data = it
                userViewModel?.changeInfoUser(it)
            }
        }
        context?.let { setImg(Constant.user?.avatar, bindingChangeMyInfo.ivAvatar, it) }
        bindingChangeMyInfo.tvChangeName.text = Constant.user?.name
        bindingChangeMyInfo.tvChangePhone.text = Constant.user?.phone
        changeAvatarFromGallery()
        changeAvatarFromCamera()
        setOnClickListener()

    }
    fun setOnClickListener(){
        bindingChangeMyInfo.btnChangeName.setOnClickListener {
            it.findNavController().navigate(R.id.action_fragmentChangeMyInfo_to_bottomSheet, bundleOf("data" to Constant.user?.name, "title" to bindingChangeMyInfo.btnChangeName.text))
        }
        bindingChangeMyInfo.cvAvatar.setOnClickListener {
            if(Constant.user?.formality == "normal") {
                it.findNavController().navigate(R.id.action_fragmentChangeMyInfo_to_bottomSheetAvatar)
            }
        }
        bindingChangeMyInfo.btnChangePassword.setOnClickListener {
            startActivity(context?.let { it1 -> ActivityForgotPassword.intentFor(it1, Constant.user?.phone, true) })
        }

    }
    override fun setViewModel() {
        userViewModel = ViewModelProvider(this@FragmentChangeMyInfo).get(UserViewModel::class.java)
    }
    override fun setObserve() {
        userViewModel?.loginResponse?.observe(viewLifecycleOwner, {
            if(it?.status == true){
                if(!isUpdateAvatar) {
                    bindingChangeMyInfo.tvChangeName.text = data
                    Constant.user?.name = data
                }else bindingChangeMyInfo.progressBarUploadImage.gone()
                view?.let { it1 -> Snackbar.make(it1, it?.messages.toString(), Snackbar.LENGTH_SHORT).show() }

            }else view?.let { it1 -> Snackbar.make(it1, it?.messages.toString(), Snackbar.LENGTH_SHORT).show() }
        })
    }
    private fun changeAvatarFromGallery(){
        resultsLauncherPickImageGallery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if(result.resultCode == Activity.RESULT_OK){
                result.data?.data.also { returnUri ->
                    inputPFD = try{
                        returnUri?.let {
                            context?.contentResolver?.openFileDescriptor(it, "r")
                        }!!
                    }catch (e: FileNotFoundException){
                        e.printStackTrace()
                        return@also
                    }
                    val fileDescriptor = inputPFD?.fileDescriptor
                    val inputStream = FileInputStream(fileDescriptor)
                    val byte = getBytes(inputStream)
                    val requestBody: RequestBody = byte!!.toRequestBody("multipart/form-data".toMediaTypeOrNull(), 0, byte.size)
                    val profilePic = MultipartBody.Part.createFormData(
                        "image",
                        "image.jpg",
                        requestBody
                    )
                    bindingChangeMyInfo.progressBarUploadImage.visible()
                    context?.let { setImg(returnUri.toString(), bindingChangeMyInfo.ivAvatar, it) }
                    userViewModel?.changeAvatar(profilePic)
                    isUpdateAvatar = true
                }
            }
        }
    }
    private fun changeAvatarFromCamera(){
        resultsLauncherTakeAPicture = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if(result.resultCode == Activity.RESULT_OK) {
                val imageBitmap = result.data?.extras?.get("data") as Bitmap
                val file = bitmapToFile(imageBitmap)
                val inputStream = FileInputStream(file)
                val byte = getBytes(inputStream)
                val requestBody: RequestBody = byte!!.toRequestBody("multipart/form-data".toMediaTypeOrNull(), 0, byte.size)
                val profilePic = MultipartBody.Part.createFormData(
                    "image",
                    "image.jpg",
                    requestBody
                )
                bindingChangeMyInfo.progressBarUploadImage.visible()
                userViewModel?.changeAvatar(profilePic)
                bindingChangeMyInfo.ivAvatar.setImageBitmap(imageBitmap)
                isUpdateAvatar = true
            }
        }
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
            file = File(context?.dataDir.toString() + File.separator + "image")
            file.createNewFile()

            //Convert bitmap to byte array
            val bos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos)
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
    private fun pickImageFromGallery(){
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        resultsLauncherPickImageGallery?.launch(Intent.createChooser(intent, "Select Image"))
    }
    private fun capturePhoto(){
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        resultsLauncherTakeAPicture?.launch(cameraIntent)
    }
    private fun setImg(img: String?, v: ImageView?, context: Context){
        if (v != null) {
            Glide.with(context)
                .load(img)
                .error(R.drawable.noimage)
                .into(v)
        }
    }

}