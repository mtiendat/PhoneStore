package com.example.phonestore.view

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.example.phonestore.R
import com.example.phonestore.base.BaseFragment
import com.example.phonestore.databinding.FragmentSuccessOrderBinding
import com.example.phonestore.services.Constant
import com.example.phonestore.viewmodel.CartViewModel


class FragmentSuccessOrder: BaseFragment() {
    private lateinit var bindingSuccessOrder: FragmentSuccessOrderBinding
    private lateinit var cartViewModel: CartViewModel
    private lateinit var notificationManager: NotificationManager
    override fun setBinding(inflater: LayoutInflater, container: ViewGroup?): View {
        bindingSuccessOrder = FragmentSuccessOrderBinding.inflate(inflater, container, false)
        return bindingSuccessOrder.root
    }

    override fun setUI() {
        createNotificationChanel()
        Glide.with(this)
                .asGif()
                .load(R.drawable.success)
                .into(bindingSuccessOrder.ivSuccess)
        bindingSuccessOrder.btnContinue.setOnClickListener {
            view?.findNavController()?.navigate(FragmentSuccessOrderDirections.actionFragmentSuccessOrderToFragmentHome())
        }
        cartViewModel.getTotalProduct()
        cartViewModel.getTotalNotification()
    }

    override fun setViewModel() {
        sendNotification()
        cartViewModel = ViewModelProvider(requireActivity()).get(CartViewModel::class.java)
    }

    override fun setObserve() {
        val totalNotificationObserver = Observer<Int?>{
            if(it>0) {
                MainActivity.bottomNav?.getOrCreateBadge(R.id.fragmentNotification)?.number = it
            }
        }
        cartViewModel.totalNotification.observe(requireActivity(), totalNotificationObserver)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                view?.findNavController()?.navigate(FragmentSuccessOrderDirections.actionFragmentSuccessOrderToFragmentHome())
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }
    private fun createNotificationChanel(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(Constant.CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            notificationManager = activity?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
    private fun sendNotification(){
        with(context?.let { NotificationManagerCompat.from(it) }) {
            // notificationId is a unique int for each notification that you must define
            getNotificationBuilder()?.let { this?.notify(Constant.NOTIFICATION_ID, it.build()) }
        }
    }
    private fun getNotificationBuilder(): NotificationCompat.Builder? {
        //val notificationIntent = Intent(this, MainActivity::class.java)
        //val notificationPendingIntent = PendingIntent.getActivity(this, com.example.ungdungdocbao.Activity.DangKy.NOTIFICATION_ID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        return context?.let {
            NotificationCompat.Builder(it, Constant.CHANNEL_ID)
                    .setContentTitle("Đã tiếp nhận đơn hàng")
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentText("Cám ơn bạn đã đặt hàng")
                    .setAutoCancel(true)
                    .setStyle(NotificationCompat.BigTextStyle())
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
        }
    }
}