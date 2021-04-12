package com.example.phonestore.view

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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.phonestore.R
import com.example.phonestore.base.BaseFragment
import com.example.phonestore.databinding.FragmentMeBinding
import com.example.phonestore.services.Constant
import com.example.phonestore.viewmodel.UserViewModel
import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.facebook.HttpMethod
import com.facebook.login.LoginManager
import com.google.android.material.snackbar.Snackbar
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.*

class FragmentMe: BaseFragment() {
    private lateinit var bindingMe: FragmentMeBinding
    private var userViewModel: UserViewModel? = null
    private lateinit var resultsLauncherPickImageGallery: ActivityResultLauncher<Intent>
    private lateinit var resultsLauncherTakeAPicture: ActivityResultLauncher<Intent>
    private lateinit var inputPFD: ParcelFileDescriptor
    override fun setBinding(inflater: LayoutInflater, container: ViewGroup?): View{
        bindingMe = FragmentMeBinding.inflate(inflater, container, false)
        return  bindingMe.root
    }

    override fun setUI() {
        changeAvatarFromGallery()
        changeAvatarFromCamera()
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Int>("key")?.observe(viewLifecycleOwner) {
            if(it==1){
                pickImageFromGallery()
            }else capturePhoto()
        }
        bindingMe.tvMyName.text = Constant.user?.name
        context?.let { setImg(Constant.user?.avatar, bindingMe.ivAvatar, it) }

        bindingMe.btnFollowOrder.setOnClickListener {
            it.findNavController().navigate(R.id.action_fragmentMe_to_fragmentFollowOrder)
        }
        bindingMe.btnLogout.setOnClickListener {
            disconnectFromFacebook()
            userViewModel?.postSignOut()
        }
        bindingMe.btnSettingAccount.setOnClickListener {
            it.findNavController().navigate(R.id.action_fragmentMe_to_fragmentChangeMyInfo)
        }
        bindingMe.btnHelper.setOnClickListener {
            it.findNavController().navigate(R.id.action_fragmentMe_to_fragmentHelper)
        }
        bindingMe.cvAvatar.setOnClickListener {
            if(Constant.user?.formality !="Facebook") {
                it.findNavController().navigate(R.id.action_fragmentMe_to_bottomSheetAvatar)
            }
        }

    }

    override fun setObserve() {
        val statusSignUpObserve = Observer<Boolean> {
            if (it) {
                val ref = context?.getSharedPreferences("saveAccount", Context.MODE_PRIVATE)
                ref?.edit()?.clear()?.apply()
                view?.let { it1 ->
                    Snackbar.make(it1, "Đăng xuất thành công", Snackbar.LENGTH_SHORT).show()
                    it1.findNavController().navigate(FragmentMeDirections.actionFragmentMeToActivityLogin())
                    activity?.finish()
                }
            }
        }
        userViewModel?.status?.observe(viewLifecycleOwner, statusSignUpObserve)
        val statusChangeAvatarObserver = Observer<Boolean> {
            if (it) {
                view?.let { it1 ->
                    Snackbar.make(it1, "Cập nhật thành công", Snackbar.LENGTH_SHORT).show()
                }
            }
        }
        userViewModel?.statusChangeAvatar?.observe(viewLifecycleOwner, statusChangeAvatarObserver)
    }

    override fun setViewModel() {
        userViewModel = ViewModelProvider(this@FragmentMe).get(UserViewModel::class.java)
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
                   val fileDescriptor = inputPFD.fileDescriptor
                   val inputStream = FileInputStream(fileDescriptor)
                   val byte = getBytes(inputStream)
                   val requestBody: RequestBody = byte!!.toRequestBody("multipart/form-data".toMediaTypeOrNull(), 0, byte.size)
                   val profilePic = MultipartBody.Part.createFormData(
                           "image",
                           "image.jpg",
                           requestBody
                   )
                   context?.let { setImg(returnUri.toString(), bindingMe.ivAvatar, it) }
                   userViewModel?.changeAvatar(profilePic)
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
                userViewModel?.changeAvatar(profilePic)
                bindingMe.ivAvatar.setImageBitmap(imageBitmap)
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
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos) // YOU can also save it in JPEG
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
        resultsLauncherPickImageGallery.launch(Intent.createChooser(intent, "Select Image"))
    }
    private fun capturePhoto(){
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        resultsLauncherTakeAPicture.launch(cameraIntent)
    }
    private fun setImg(img: String?, v: ImageView, context: Context){
        Glide.with(context)
                .load(img)
                .error(R.drawable.noimage)
                .into(v)
    }
    private fun disconnectFromFacebook() {
        if (AccessToken.getCurrentAccessToken() == null) {
            return  // already logged out
        }
        GraphRequest(
            AccessToken.getCurrentAccessToken(),
            "/me/permissions/",
            null,
            HttpMethod.DELETE
        ) {
            AccessToken.refreshCurrentAccessTokenAsync()
            LoginManager.getInstance().logOut()
        }.executeAsync()
    }
}