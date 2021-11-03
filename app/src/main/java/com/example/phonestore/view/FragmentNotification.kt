package com.example.phonestore.view

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.phonestore.R
import com.example.phonestore.base.BaseFragment
import com.example.phonestore.databinding.FragmentNotificationBinding
import com.example.phonestore.extendsion.AppEvent
import com.example.phonestore.extendsion.gone
import com.example.phonestore.extendsion.visible
import com.example.phonestore.model.Notification
import com.example.phonestore.services.Constant
import com.example.phonestore.services.adapter.DetailProductAdapter
import com.example.phonestore.services.adapter.NotificationAdapter
import com.example.phonestore.services.widget.SwipeHelper
import com.example.phonestore.viewmodel.UserViewModel
import com.google.android.material.snackbar.Snackbar

class FragmentNotification: BaseFragment() {
    private var bindingNotification: FragmentNotificationBinding? = null
    private var notificationViewModel: UserViewModel? = null
    private lateinit var notificationAdapter: NotificationAdapter
    private var listNotification: ArrayList<Notification>? = arrayListOf()
    private var isDelete: Boolean = false
    private var position: Int = -1
    override fun setBinding(inflater: LayoutInflater, container: ViewGroup?): View? {
        bindingNotification = FragmentNotificationBinding.inflate(inflater, container, false)
        bindingNotification?.pb?.popup?.gone()
        return bindingNotification?.root
    }

    override fun setViewModel() {
        notificationViewModel = ViewModelProvider(this@FragmentNotification).get(UserViewModel::class.java)
    }

    override fun setObserve() {
        notificationViewModel?.status?.observe(viewLifecycleOwner, {
            if(it){
                bindingNotification?.pb?.popup?.gone()
            }
        })
        notificationViewModel?.listNotification?.observe(viewLifecycleOwner, {
            it?.let {
                listNotification?.addAll(it)
                notificationAdapter?.notifyDataSetChanged()
            }
        })
        notificationViewModel?.notificationResponse?.observe(viewLifecycleOwner, {
            if(it?.status == true) {
                view?.let { it1 ->
                    Snackbar.make(
                        it1,
                        it.messages.toString(),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
                if(isDelete){
                    AppEvent.notifyClosePopUp()
                    val item = listNotification?.get(position)?.id
                    listNotification?.removeIf{n ->n.id == item}
                    notificationAdapter?.notifyDataSetChanged()
                }
                updateBadgeNotification()
            }
        })
    }

    override fun setUI() {
        initRecyclerView()
        notificationViewModel?.getNotification()
        notificationAdapter?.updateNotification = {
            bindingNotification?.pb?.popup?.visible()
            notificationViewModel?.updateNotification(it)
            updateBadgeNotification()
        }
    }
    private fun updateBadgeNotification(){
        val num = MainActivity.bottomNav?.getOrCreateBadge(R.id.fragmentNotification)?.number
        MainActivity.bottomNav?.getOrCreateBadge(R.id.fragmentNotification)?.number = num?.minus(1) ?:0
        if(MainActivity.bottomNav?.getOrCreateBadge(R.id.fragmentNotification)?.number==0){
            MainActivity.bottomNav?.removeBadge(R.id.fragmentNotification)
        }
    }
    fun initRecyclerView(){
        notificationAdapter = NotificationAdapter(listNotification)
        bindingNotification?.rvNotification?.adapter = notificationAdapter
        bindingNotification?.rvNotification?.layoutManager = LinearLayoutManager(context)
        bindingNotification?.rvNotification?.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        val itemTouchHelper = ItemTouchHelper(object : SwipeHelper(bindingNotification?.rvNotification!!) {
            override fun instantiateUnderlayButton(position: Int): List<UnderlayButton> {
                val deleteButton = deleteButton()
                return listOf(deleteButton)
            }
        })
        itemTouchHelper.attachToRecyclerView(bindingNotification?.rvNotification)
    }
    private fun deleteButton() : SwipeHelper.UnderlayButton{
        return SwipeHelper.UnderlayButton(
            this.requireContext(),
            "Xóa",
            14.0f,
            android.R.color.holo_red_light,
            this::handle,
            )
    }
    private fun handle(position: Int){
        context?.let { alertDelete(it, position = position) }
    }
    private fun alertDelete(context: Context, position: Int){
        val builder = AlertDialog.Builder(context)
        builder.setMessage(Constant.QUESTION_DELETE)
        builder.setTitle(Constant.NOTIFICATION)
        builder.setCancelable(false)
        builder.setPositiveButton(Constant.YES) { _, _ ->
            isDelete = true
            this.position = position
            AppEvent.notifyShowPopUp()
            notificationViewModel?.deleteNotification(listNotification?.get(position)?.id)
        }
        builder.setNegativeButton(Constant.NO) { dialog, _ ->
            dialog.cancel()
            notificationAdapter?.notifyDataSetChanged()
        }
        val alertDialog = builder.create()
        alertDialog.setOnShowListener {
            context.let {alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(
                ContextCompat.getColor(it, R.color.blue))}
            context.let {alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(
                ContextCompat.getColor(it, R.color.blue))}
        }

        alertDialog.show()
    }
}