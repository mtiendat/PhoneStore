package com.example.phonestore.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.phonestore.R
import com.example.phonestore.base.BaseFragment
import com.example.phonestore.databinding.FragmentNotificationBinding
import com.example.phonestore.model.Notification
import com.example.phonestore.services.DetailProductAdapter
import com.example.phonestore.services.SwipeHelper
import com.example.phonestore.viewmodel.UserViewModel
import com.google.android.material.snackbar.Snackbar

class FragmentNotification: BaseFragment() {
    private var bindingNotification: FragmentNotificationBinding? = null
    private var notificationViewModel: UserViewModel? = null
    private var notificationAdapter: DetailProductAdapter<Notification>? = null
    private var listNotification: ArrayList<Notification>? = arrayListOf()
    override fun setBinding(inflater: LayoutInflater, container: ViewGroup?): View? {
        bindingNotification = FragmentNotificationBinding.inflate(inflater, container, false)
        return bindingNotification?.root
    }

    override fun setViewModel() {
        notificationViewModel = ViewModelProvider(this@FragmentNotification).get(UserViewModel::class.java)
    }

    override fun setObserve() {
        val notificationObserve = Observer<ArrayList<Notification>?>{
            listNotification?.addAll(it)
            notificationAdapter?.setItems(it)
        }
        notificationViewModel?.listNotification?.observe(viewLifecycleOwner, notificationObserve)
        val statusObserve = Observer<Boolean?>{
            if(it) {
                view?.let { it1 ->
                    Snackbar.make(
                        it1,
                        "Xóa thông báo thành công",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
                updateBadgeNotification()
            }
        }
        notificationViewModel?.status?.observe(viewLifecycleOwner, statusObserve)
    }

    override fun setUI() {
        initRecyclerView()
        notificationViewModel?.getNotification()
        notificationAdapter?.updateNotification = {
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
        notificationAdapter = DetailProductAdapter(listNotification)
        bindingNotification?.rvNotification?.adapter = notificationAdapter
        bindingNotification?.rvNotification?.layoutManager = LinearLayoutManager(context)
        bindingNotification?.rvNotification?.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        bindingNotification?.rvNotification?.layoutManager = LinearLayoutManager(context)
        val itemTouchHelper = ItemTouchHelper(object : SwipeHelper(bindingNotification?.rvNotification) {
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
            "Delete",
            14.0f,
            android.R.color.holo_red_light,
            this::handle,
            )
    }
    private fun handle(position: Int){
        for (i in listNotification!!) {
            if (listNotification?.get(position) == i){
                notificationViewModel?.deleteNotification(i.id)
                listNotification?.remove(listNotification?.get(position))
                notificationAdapter?.notifyDataSetChanged()
            }
        }
    }
}