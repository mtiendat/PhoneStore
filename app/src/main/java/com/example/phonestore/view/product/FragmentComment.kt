package com.example.phonestore.view.product

import android.Manifest
import android.Manifest.permission.*
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.os.ParcelFileDescriptor
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.phonestore.R
import com.example.phonestore.base.BaseFragment
import com.example.phonestore.databinding.FragmentCommentBinding
import com.example.phonestore.extendsion.AppEvent
import com.example.phonestore.extendsion.gone
import com.example.phonestore.extendsion.visible
import com.example.phonestore.model.Attachment
import com.example.phonestore.model.Comment
import com.example.phonestore.services.Constant
import com.example.phonestore.services.FileUtils
import com.example.phonestore.services.ImageUtil
import com.example.phonestore.services.adapter.ImageCommentAdapter
import com.example.phonestore.services.adapter.ProductNotCommentAdapter
import com.example.phonestore.viewmodel.DetailProductViewModel
import com.google.android.material.snackbar.Snackbar
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import pub.devrel.easypermissions.EasyPermissions
import java.io.*
import java.text.SimpleDateFormat
import java.util.*


class FragmentComment: BaseFragment() {
    private lateinit var binding: FragmentCommentBinding
    private var detailViewModel: DetailProductViewModel? = null
    private var listID: ArrayList<Int>? = arrayListOf()
    private var i: Int = 0
    private var resultsLauncherPickImageGallery: ActivityResultLauncher<Intent>? = null
    private var resultsLauncherTakeAPicture: ActivityResultLauncher<Intent>? = null
    private var inputPFD: ParcelFileDescriptor? = null
    private lateinit var adapter: ProductNotCommentAdapter
    private lateinit var imageAdapter: ImageCommentAdapter
    private var isEdit:Boolean? = false
    private var listPath: ArrayList<String>? = arrayListOf()
    private var listPathOfEdit: ArrayList<String>? = arrayListOf()
    private var listDelete: ArrayList<Int>? = arrayListOf()
    private var pathImage: String? =""
    private var comment: Comment? = null
    private var listUpLoad: ArrayList<MultipartBody.Part> = arrayListOf()
    private var resultsLauncherPreviewPhoto: ActivityResultLauncher<Intent>? = null
    private val requestMultiplePermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (permissions[CAMERA] == true && permissions[READ_EXTERNAL_STORAGE] == true) {
                if(listPath?.size == 3){
                    view?.let { it1 -> Snackbar.make(it1, "Số hình ảnh được thêm đã tối đa", Snackbar.LENGTH_SHORT).show() }
                }else findNavController().navigate(R.id.action_fragmentComment_to_bottomSheetAvatar)
            } else {
                binding.btnAddAttachment.performClick()
            }
        }
    override fun setBinding(inflater: LayoutInflater, container: ViewGroup?): View? {
        binding = FragmentCommentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setViewModel() {
        detailViewModel = ViewModelProvider(this@FragmentComment).get(DetailProductViewModel::class.java)
    }

    override fun setObserve() {
        findNavController().currentBackStackEntry?.savedStateHandle?.apply {
            getLiveData<Int>("keyChangeAvatar").observe(viewLifecycleOwner) {
                    if (it == 1) {
                        pickImageFromGallery()
                    } else capturePhoto()
            }
        }
        detailViewModel?.listProductNotComment?.observe(viewLifecycleOwner, {
            adapter.setItems(it)
        })
        detailViewModel?.statusComment?.observe(viewLifecycleOwner, {
            if(it.status == true){
                    if(isEdit == false){
                        if(imageAdapter.listPath?.size  == 0){
                            AppEvent.notifyClosePopUp()
                            view?.let { it1 -> Snackbar.make(it1, Constant.SUCCESS_VOTED, Snackbar.LENGTH_SHORT).show() }
                            findNavController().popBackStack()
                        }
                        listPath?.forEach { path ->
                            listUpLoad.add(submitAttachment(path))
                        }
                        detailViewModel?.postImageComment(it.id, listUpLoad)
                    }else{
                        if(listPathOfEdit?.size?:0 >0){
                            listPathOfEdit?.forEach { path ->
                                listUpLoad.add(submitAttachment(path))
                            }
                            detailViewModel?.updateImageNewComment(it.id, listUpLoad)
                        }
                        detailViewModel?.updateImageOldComment(it.id, listDelete)

                    }
            }else AppEvent.notifyClosePopUp()
        })
        detailViewModel?.statusUpImage?.observe(viewLifecycleOwner, {
            if(it==false){
                Toast.makeText(context, "Có lỗi xảy ra", Toast.LENGTH_SHORT).show()
            }else {
                AppEvent.notifyClosePopUp()
                view?.let { it1 -> Snackbar.make(it1, Constant.SUCCESS_VOTED, Snackbar.LENGTH_SHORT).show() }
                findNavController().popBackStack()
            }
        })
        resultsLauncherPreviewPhoto = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if(result.resultCode == Activity.RESULT_OK){
                val data = result?.data?.getParcelableArrayListExtra<Attachment>("data")
                val delete = result?.data?.getParcelableArrayListExtra<Attachment>("delete")
                val listPath = arrayListOf<String>()
                data?.forEach {
                    listPath.add(it.attachment?:"")
                }
                delete?.forEach {
                    it.id?.let { it1 -> listDelete?.add(it1) }
                }
                imageAdapter.setItems(listPath)
            }
        }

    }
    override fun setUI() {
        getImageFromGallery()
        takeImageFromCamera()
        listID = arguments?.getIntegerArrayList("listId")
        isEdit = arguments?.getBoolean("isEdit", false)
        comment = arguments?.getParcelable("comment")
        adapter = ProductNotCommentAdapter()
        adapter.itemClick = {
            if(listID?.contains(it) == true){
                listID?.remove(it)
            }else listID?.add(it)
        }
        if(isEdit == true){
            setViewEdit(comment)
            comment?.listAttachment?.forEach {
                listPath?.add(it.attachment?:"")
            }
            imageAdapter = ImageCommentAdapter(listPath)
            imageAdapter.itemClick = {
                resultsLauncherPreviewPhoto?.launch(ActivityPreviewPhoto.intentFor(context, comment?.listAttachment, position = it,isEditorCreate =  true))
            }
        }else{
            binding.rvListProductNotComment.layoutManager = LinearLayoutManager(context)
            binding.rvListProductNotComment.adapter = adapter
            imageAdapter = ImageCommentAdapter(listPath)
            imageAdapter.itemClick = {
                val listAttachment = arrayListOf<Attachment>()
                listPath?.forEach {
                    listAttachment.add(Attachment(attachment = it))
                }
                resultsLauncherPreviewPhoto?.launch(ActivityPreviewPhoto.intentFor(context, listAttachment,position = it,isEditorCreate =  true))
            }
            detailViewModel?.getListProductNotComment(listID)
            listID?.clear()
        }
        binding.rvImage.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvImage.adapter = imageAdapter


    }

    override fun setEvents() {
        binding.btnSendVote.setOnClickListener {
            if(checkLengthContent()){
                AppEvent.notifyShowPopUp()
                if(isEdit==true){
                    detailViewModel?.updateComment(comment?.id, Comment(
                        idUser = Constant.idUser,
                        content = binding.edtVote.text.toString(),
                        vote = binding.rbVote.rating.toInt()
                    ))
                }else{
                    detailViewModel?.postComment(Comment(
                        idUser = Constant.idUser,
                        listID = listID,
                        content = binding.edtVote.text.toString(),
                        vote = binding.rbVote.rating.toInt()
                    ))
                }
            }
        }
        binding.btnAddAttachment.setOnClickListener {
            context?.let {
                if (!EasyPermissions.hasPermissions(it,
                        Manifest.permission.READ_EXTERNAL_STORAGE) || !EasyPermissions.hasPermissions(it,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) || !EasyPermissions.hasPermissions(it,
                        Manifest.permission.CAMERA)) {

                    requestMultiplePermissions.launch(
                        arrayOf(
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA
                        )
                    )
                } else {
                    if(listPath?.size == 3){
                        view?.let { it1 -> Snackbar.make(it1, "Số hình ảnh được thêm đã tối đa", Snackbar.LENGTH_SHORT).show() }
                    }else findNavController().navigate(R.id.action_fragmentChangeMyInfo_to_bottomSheetAvatar)


                }
            }

        }
    }
    private fun getImageFromGallery(){
        resultsLauncherPickImageGallery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if(result.resultCode == Activity.RESULT_OK){
                result.data?.data.also { returnUri ->
                    if(isEdit==true){
                        listPathOfEdit?.add(ImageUtil.compressImage(requireContext(), FileUtils().getPath(requireContext(), returnUri!!)?:""))
                        listPath?.add(ImageUtil.compressImage(requireContext(), FileUtils().getPath(requireContext(), returnUri!!)?:""))
                    }else listPath?.add(ImageUtil.compressImage(requireContext(), FileUtils().getPath(requireContext(), returnUri!!)?:""))
                    imageAdapter.notifyItemChanged(listPath?.size?:0)
                }
            }
        }
    }
    private fun takeImageFromCamera(){
        resultsLauncherTakeAPicture = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if(result.resultCode == Activity.RESULT_OK) {
                if(isEdit == true){
                    listPathOfEdit?.add(ImageUtil.compressImage(requireContext(), pathImage?:""))
                    listPath?.add(ImageUtil.compressImage(requireContext(), pathImage?:""))
                }else  listPath?.add(ImageUtil.compressImage(requireContext(), pathImage?:""))
                imageAdapter.notifyItemChanged(listPath?.size?:0)

            }
        }
    }
    private fun pickImageFromGallery(){
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        resultsLauncherPickImageGallery?.launch(Intent.createChooser(intent, "Select Image"))
    }
    private fun capturePhoto(){
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(requireContext().packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        requireContext(),
                        "com.example.phonestore",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    resultsLauncherTakeAPicture?.launch(takePictureIntent)
                }
            }
        }

    }
    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            pathImage = absolutePath
        }
    }
    private fun submitAttachment(path: String): MultipartBody.Part {
        val file = File(path)
        val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        i++
        return MultipartBody.Part.createFormData(
            "image_$i",
            "image_$i.jpg",
            requestBody
        )

    }
    private fun checkLengthContent(): Boolean {
        if(binding.edtVote.text.length > 250){
            binding.tv250.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
            return false
        }
        if(binding.rbVote.rating == 0f){
            binding.tvPleaseVote.visible()
            return false
        }
        return true
    }
    private fun setViewEdit(comment: Comment?){
        binding.groupProduct.gone()
        binding.tvTitleVote.text = "Đánh giá của bạn:"
        binding.btnSendVote.text = "Sửa"
        binding.edtVote.setText(comment?.content?:"")
        binding.rbVote.rating = comment?.vote?.toFloat()?:0f
    }
}