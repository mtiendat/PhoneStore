package com.example.phonestore.view.productDetail

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.phonestore.R
import com.example.phonestore.base.BaseFragment
import com.example.phonestore.databinding.FragmentReplyCommentBinding
import com.example.phonestore.extendsion.AppEvent
import com.example.phonestore.extendsion.gone
import com.example.phonestore.model.Attachment
import com.example.phonestore.model.Comment
import com.example.phonestore.model.Reply
import com.example.phonestore.services.Constant
import com.example.phonestore.services.adapter.ImageCommentAdapter
import com.example.phonestore.services.adapter.ReplyAdapter
import com.example.phonestore.viewmodel.DetailProductViewModel

import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*

class FragmentReply: BaseFragment() {
    private lateinit var binding: FragmentReplyCommentBinding
    private lateinit var adapter: ReplyAdapter
    private lateinit var adapterImage: ImageCommentAdapter
    private lateinit var viewModel: DetailProductViewModel
    private var resultsLauncherPreviewPhoto: ActivityResultLauncher<Intent>? = null
    private var comment: Comment? = null
    override fun setBinding(inflater: LayoutInflater, container: ViewGroup?): View? {
        binding = FragmentReplyCommentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setUI() {
        comment = arguments?.getParcelable("comment")
        binding.shimmerLayoutComment.startShimmer()
        setInfoComment()
        adapter = ReplyAdapter(comment?.listReply)
        binding.rvReply.layoutManager = LinearLayoutManager(context)
        binding.rvReply.adapter = adapter
        val listPath = arrayListOf<String>()
        comment?.listAttachment?.forEach {
            listPath.add(it.attachment ?:"")
        }
        adapterImage = ImageCommentAdapter(listPath)
        adapterImage.itemClick = {
            resultsLauncherPreviewPhoto?.launch(ActivityPreviewPhoto.intentFor(requireContext(), listAttachment = comment?.listAttachment, false, it))
        }
        binding.rvChildImage.adapter = adapterImage
        binding.rvChildImage.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.shimmerLayoutComment.stopShimmer()
        binding.shimmerLayoutComment.gone()
        if(listPath.size == 0)  binding.rvChildImage.gone()
    }

    override fun setViewModel() {
        viewModel = ViewModelProvider(this@FragmentReply).get(DetailProductViewModel::class.java)
        resultsLauncherPreviewPhoto = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if(result.resultCode == Activity.RESULT_OK){
                val data = result?.data?.getParcelableArrayListExtra<Attachment>("data")
                val listPath = arrayListOf<String>()
                data?.forEach {
                    listPath.add(it.attachment?:"")
                }
                adapterImage.setItems(listPath)
            }
        }
    }

    override fun setEvents() {
        binding.control.btnSendVote.setOnClickListener {
            if(checkLength()){
                AppEvent.notifyShowPopUp()
                viewModel.postReply(
                    Reply(
                        idUser = Constant.idUser,
                        idComment = comment?.id!!,
                        content = binding.control.edtContent.text.toString()
                    ))
            }

        }
    }

    override fun setObserve() {
        viewModel.statusReply?.observe(viewLifecycleOwner, {
            if(it == true){
                view?.let { it1 -> Snackbar.make(it1, Constant.SUCCESS_REPLY, Snackbar.LENGTH_SHORT).show() }
                val time = Calendar.getInstance().time
                val formatter = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())
                val currentDate = formatter.format(time)
                comment?.listReply?.add(Reply(
                    idUser = Constant.idUser,
                    idComment = comment?.id!!,
                    content = binding.control.edtContent.text.toString(),
                    name = Constant.user?.name.toString(),
                    avatar = Constant.user?.avatar.toString(),
                    date = currentDate
                ))
                adapter.notifyItemInserted(comment?.listReply?.size?.minus(1)?:0)
                comment?.totalReply = comment?.totalReply?.plus(1)?:0
                binding.btnTotalReply.text = "${comment?.totalReply} lượt trả lời"
                binding.control.edtContent.text?.clear()
                binding.control.textInputReplyComment.clearFocus()
                AppEvent.notifyClosePopUp()
            }
        })
    }
    private fun setInfoComment(){
        binding.tvContent.text = comment?.content
        binding.tvName.text = comment?.name
        binding.tvDate.text = comment?.date
        binding.btnTotalLike.text = "${comment?.like} lượt thích"
        binding.btnTotalReply.text = "${comment?.totalReply} lượt trả lời"
        binding.rbComment.rating = comment?.vote?.toFloat()?:0f
        context?.let {
            Glide.with(it)
                .load(comment?.avatar)
                .error(R.drawable.noimage)
                .into(binding.ivAvatarComment)
            Glide.with(it)
                .load(Constant.user?.avatar)
                .error(R.drawable.noimage)
                .into(binding.control.ivAvatarComment)
        }

    }
    private fun checkLength(): Boolean{
        return if(binding.control.edtContent.length()>250){
            binding.control.textInputReplyComment.error = "Số ký tự tối đa 250"
            false
        }else true

    }
}