package com.example.phonestore.view.order

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.drawable.Drawable
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
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.phonestore.R
import com.example.phonestore.base.BaseFragment
import com.example.phonestore.databinding.FragmentSuccessOrderBinding
import com.example.phonestore.services.Constant
import com.example.phonestore.view.MainActivity
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
                .listener(object: RequestListener<GifDrawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<GifDrawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }

                    override fun onResourceReady(
                        resource: GifDrawable?,
                        model: Any?,
                        target: Target<GifDrawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        resource?.setLoopCount(1)
                        return false
                    }


                })
                .into(bindingSuccessOrder.ivSuccess)
        bindingSuccessOrder.btnContinue.setOnClickListener {
            view?.findNavController()?.navigate(FragmentSuccessOrderDirections.actionFragmentSuccessOrderToFragmentHome())
        }
        getData()
        sendNotification()
    }
    private fun getData(){
        cartViewModel.getTotalProduct()
        cartViewModel.getTotalNotification()
    }
    override fun setViewModel() {
        cartViewModel = ViewModelProvider(requireActivity()).get(CartViewModel::class.java)
    }

    override fun setObserve() {
        val totalNotificationObserver = Observer<Int?>{
            if(it>0) {
                MainActivity.bottomNav?.getOrCreateBadge(R.id.fragmentNotification)?.number = it
            }
        }
        cartViewModel.totalNotification.observe(requireActivity(), totalNotificationObserver)
        cartViewModel.totalProduct.observe(requireActivity(), {
            context?.let { it1 -> MainActivity.icon?.let { it2 ->
                MainActivity.setBadgeCount(
                    it1,
                    icon = it2,
                    it.toString()
                )
            } }
        })
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
            getNotificationBuilder()?.let { this?.notify(Constant.NOTIFICATION_ID, it.build()) }
        }
    }
    private fun getNotificationBuilder(): NotificationCompat.Builder? {
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