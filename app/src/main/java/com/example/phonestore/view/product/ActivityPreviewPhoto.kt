package com.example.phonestore.view.product

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.example.phonestore.base.BaseActivity
import com.example.phonestore.databinding.ActivityPreviewPhotoBinding
import com.example.phonestore.extendsion.visible
import com.example.phonestore.model.Attachment
import com.example.phonestore.services.Constant
import com.example.phonestore.services.adapter.PhotoPagerAdapter


class ActivityPreviewPhoto: BaseActivity() {
    companion object{
        fun intentFor(context: Context?, listAttachment: ArrayList<Attachment>?, isEditorCreate: Boolean = false, position: Int? = null): Intent =
            Intent(context, ActivityPreviewPhoto::class.java).apply {
                putExtra(Constant.KEY_URL, listAttachment)
                putExtra(Constant.IS_EDIT, isEditorCreate)
                putExtra(Constant.POSITION, position)
            }
    }

    private lateinit var binding: ActivityPreviewPhotoBinding
    private lateinit var adapter: PhotoPagerAdapter
    private var listAttachment: ArrayList<Attachment>? = arrayListOf()
    private var listDelete: ArrayList<Attachment>? = arrayListOf()
    private var isEditorCreate: Boolean? = false
    private var position: Int? = null
    override fun setBinding() {
        binding = ActivityPreviewPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        listAttachment = intent.getParcelableArrayListExtra(Constant.KEY_URL)
        isEditorCreate = intent.getBooleanExtra(Constant.IS_EDIT, false)
        position = intent.getIntExtra(Constant.POSITION, 0)
    }

    override fun setUI() {
        adapter = PhotoPagerAdapter()
        adapter.setAttachments(listAttachment)
        binding.vpImage.adapter = adapter
        if(position?:0 >0){
            binding.vpImage.currentItem = position?:0
        }
        setEvent()
        if(isEditorCreate == true){
            binding.btnDelete.visible()
        }
    }
    fun setEvent(){
        binding.btnNext.setOnClickListener {
            binding.vpImage.currentItem =  binding.vpImage.currentItem + 1
        }
        binding.btnPre.setOnClickListener {
            binding.vpImage.currentItem =  binding.vpImage.currentItem - 1
        }
        binding.btnDelete.setOnClickListener {
            listAttachment?.get(binding.vpImage.currentItem)?.let { it1 -> listDelete?.add(it1) }
            listAttachment?.removeAt(binding.vpImage.currentItem)
            adapter.notifyDataSetChanged()
            if(listAttachment?.size?:0==0){
                val resultIntent = Intent()
                resultIntent.putExtra("data", listAttachment).putExtra("delete", listDelete)
                setResult(AppCompatActivity.RESULT_OK, resultIntent)
                finish()
            }

        }
        binding.btnClose.setOnClickListener {
            if(isEditorCreate == true){
                val resultIntent = Intent()
                resultIntent.putExtra("data", listAttachment).putExtra("delete", listDelete)
                setResult(AppCompatActivity.RESULT_OK, resultIntent)
            }
            finish()
        }
    }

    override fun onBackPressed() {
        if(isEditorCreate == true) {
            val resultIntent = Intent()
            resultIntent.putExtra("data", listAttachment).putExtra("delete", listDelete)
            setResult(AppCompatActivity.RESULT_OK, resultIntent)
            finish()
        }else super.onBackPressed()
    }
}

