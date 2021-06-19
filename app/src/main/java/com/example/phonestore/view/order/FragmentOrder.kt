package com.example.phonestore.view.order

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.phonestore.R
import com.example.phonestore.base.BaseFragment
import com.example.phonestore.databinding.FragmentOrderBinding
import com.example.phonestore.extendsion.enabled
import com.example.phonestore.extendsion.gone
import com.example.phonestore.extendsion.toVND
import com.example.phonestore.extendsion.visible
import com.example.phonestore.model.ProductOrder
import com.example.phonestore.model.order.Address
import com.example.phonestore.model.order.AddressStore
import com.example.phonestore.model.payment.ZaloPayCreateOrderParam
import com.example.phonestore.services.Constant
import com.example.phonestore.services.Constant.ADDRESS
import com.example.phonestore.services.Constant.PAYMENT
import com.example.phonestore.services.Constant.SHIPPING
import com.example.phonestore.services.adapter.DetailProductAdapter
import com.example.phonestore.services.payment.ZaloPayHelper
import com.example.phonestore.view.MainActivity
import com.example.phonestore.viewmodel.OrderViewModel
import com.google.android.material.snackbar.Snackbar
import okhttp3.FormBody
import okhttp3.RequestBody
import vn.zalopay.sdk.ZaloPayError
import vn.zalopay.sdk.ZaloPaySDK
import vn.zalopay.sdk.listeners.PayOrderListener
import java.util.*

class FragmentOrder: BaseFragment() {
    private var bindingOrderBinding: FragmentOrderBinding? = null
    private var listProductOrder: ArrayList<ProductOrder>? = null
    private var orderAdapter: DetailProductAdapter<ProductOrder>? = null
    private var totalMoney: Int = 0
    private var idOrder:  Int? = 0
    private var discount: Int? = 0
    private var orderViewModel: OrderViewModel? = null
    private var isPaymentZaloPay: Boolean = false
    override fun setBinding(inflater: LayoutInflater, container: ViewGroup?): View? {
        bindingOrderBinding = FragmentOrderBinding.inflate(inflater, container, false)
        return bindingOrderBinding?.root
    }

    override fun setViewModel() {
        orderViewModel = ViewModelProvider(this@FragmentOrder).get(OrderViewModel::class.java)
    }

    override fun setObserve() {
        findNavController().currentBackStackEntry?.savedStateHandle?.apply {
            getLiveData<Address>(ADDRESS).observe(viewLifecycleOwner) {
                updateAddress(it)
            }
            getLiveData<AddressStore>(SHIPPING).observe(viewLifecycleOwner) {
                when(it){
                    null -> {
                        bindingOrderBinding?.tvShip?.text = "Giao hàng nhanh"
                        bindingOrderBinding?.tvFeeShip?.visible()
                    }
                    else -> {
                        bindingOrderBinding?.tvShip?.text = "Tại cửa hàng"
                        bindingOrderBinding?.tvInfoReceive?.text = "Đ/c: ${it.address}"
                        bindingOrderBinding?.tvFeeShip?.gone()
                    }
                }
            }
            getLiveData<Int>(PAYMENT).observe(viewLifecycleOwner) {
                bindingOrderBinding?.tvPayment?.text = when(it){
                    1 -> "Thanh toán khi nhận hàng"
                    2 -> "Thanh toán bằng Zalo Pay"
                    else -> ""
                }
            }
        }
        orderViewModel?.resultOrder?.observe(viewLifecycleOwner, {
            if(it==true){
                view?.findNavController()?.navigate(R.id.action_fragmentOrder_to_fragmentSuccessOrder)
            }
        })
        orderViewModel?.result?.observe(viewLifecycleOwner, {
            if(it==true){
                view?.let { it1 -> Snackbar.make(it1, Constant.SUCCESS_CANCEL, Snackbar.LENGTH_SHORT).show() }
                view?.findNavController()?.apply {
                    popBackStack(R.id.fragmentFollowOrder, true)
                    navigate(R.id.fragmentFollowOrder)
                }
            }
        })
        orderViewModel?.tokenZaloPayOrder?.observe(viewLifecycleOwner, { token ->
            activity?.let {
                    it -> ZaloPaySDK.getInstance().payOrder(it, token, "demozpdk://app", object : PayOrderListener{
                override fun onPaymentSucceeded(p0: String?, p1: String?, p2: String?) {
                        Toast.makeText(context, "thanh cong", Toast.LENGTH_SHORT).show()
                }

                override fun onPaymentCanceled(p0: String?, p1: String?) {
                    Toast.makeText(context, "cancel", Toast.LENGTH_SHORT).show()
                }

                override fun onPaymentError(p0: ZaloPayError?, p1: String?, p2: String?) {
                    Toast.makeText(context, "that bai", Toast.LENGTH_SHORT).show()
                }

            })
                isPaymentZaloPay = true
            }
        })

    }

    override fun setUI() {
        val isFragmentFollowOrder = arguments?.getBoolean("key")
        val state  = arguments?.getString("state")
        listProductOrder = arguments?.getParcelableArrayList("listProduct")
        idOrder = arguments?.getInt("idOrder")
        discount = arguments?.getInt("discount")
        if(isFragmentFollowOrder==true){
            bindingOrderBinding?.tvChangeAddress?.gone()
            if(state==Constant.CANCEL){
                bindingOrderBinding?.btnCancelOrder?.visible()
                context?.let { ContextCompat.getColor(it, R.color.dray) }?.let { bindingOrderBinding?.btnCancelOrder?.setBackgroundColor(it) }
                bindingOrderBinding?.btnCancelOrder?.text = Constant.CANCEL
                bindingOrderBinding?.btnCancelOrder?.enabled()
            }else if(state==Constant.RECEIVED){
                bindingOrderBinding?.btnCancelOrder?.visible()
            }
            bindingOrderBinding?.ctrlOrder?.gone()
        }
        totalMoney = listProductOrder?.size?.minus(1) ?: 0 //lấy tổng tiền ở ptu vị trí cuối

        bindingOrderBinding?.rvOrderProduct?.isNestedScrollingEnabled = false
        initRecyclerView()
        setOnClickListener()
        setInfoOrder()

    }
    fun setOnClickListener(){
        bindingOrderBinding?.btnOrderFinish?.setOnClickListener {
//            val listProductFinish: ArrayList<DetailCart> = arrayListOf()
//            for(i in listProductOrder!!){
//                i.product?.let { it1 -> listProductFinish.add(it1) }
//            }
//            val order = Order(listProductOrder?.get(totalMoney)?.total, listProductFinish)
//            orderViewModel?.order(order)
            createOrder()
        }

        bindingOrderBinding?.btnCancelOrder?.setOnClickListener {
            alertCancel()
        }
        bindingOrderBinding?.tvChangeAddress?.setOnClickListener {
            it.findNavController().navigate(R.id.action_fragmentOrder_to_fragmentSelectAddress)
        }
        bindingOrderBinding?.btnOrderShippingOption?.setOnClickListener {
            it.findNavController().navigate(R.id.action_fragmentOrder_to_fragmentShippingOption)
        }
        bindingOrderBinding?.btnPaymentOption?.setOnClickListener {
            it.findNavController().navigate(R.id.action_fragmentOrder_to_fragmentPaymentOption)
        }
    }
    private fun setInfoOrder(){
        //bindingOrderBinding?.tvOrderAddress?.text = Constant.user?.address
        bindingOrderBinding?.tvOrderNameUser?.text = Constant.user?.name
        bindingOrderBinding?.tvOrderPhoneUser?.text = Constant.user?.phone
        val totalMoneyPre = listProductOrder?.get(totalMoney)?.total?.div(discount!!)
        bindingOrderBinding?.tvOrderDiscountDetail?.text = totalMoneyPre.toVND()
        bindingOrderBinding?.tvDiscountValue?.text = "-${discount}%"
        bindingOrderBinding?.tvOrderTotalMoney?.text = listProductOrder?.get(totalMoney)?.total?.toVND()
        bindingOrderBinding?.tvOrderTotalMoneyFinish?.text = (listProductOrder?.get(totalMoney)?.total?.minus(totalMoneyPre!!)).toVND()

    }

    private fun updateAddress(address: Address){
        bindingOrderBinding?.tvOrderAddress?.text = address.address
        bindingOrderBinding?.tvOrderNameUser?.text = address.name
        bindingOrderBinding?.tvOrderPhoneUser?.text = address.phone
    }

    private fun initRecyclerView(){
        orderAdapter = DetailProductAdapter(listProductOrder)
        bindingOrderBinding?.rvOrderProduct?.adapter = orderAdapter
        bindingOrderBinding?.rvOrderProduct?.layoutManager = LinearLayoutManager(context)
    }

    private fun alertCancel(){
       val builder = AlertDialog.Builder(context)
        builder.setMessage(Constant.QUESTION_CANCEL)
        builder.setTitle(Constant.NOTIFICATION)
        builder.setCancelable(false)
        builder.setPositiveButton(Constant.YES) { dialog, _ ->
            orderViewModel?.cancelOrder(idOrder)
            dialog.cancel()
        }
        builder.setNegativeButton(Constant.NO) { dialog, _ ->
            dialog.cancel()
        }
        val alertDialog = builder.create()
        alertDialog.setOnShowListener {
            context?.let {alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(it, R.color.blue))}
            context?.let {alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(it, R.color.blue))}
        }

        alertDialog.show()
    }
    fun createOrder(){
        var zaloPayHelper = ZaloPayHelper()
        val appTime = Date().time
        val param = ZaloPayCreateOrderParam(
            AppId = java.lang.String.valueOf(Constant.APP_ID),
                    AppUser = "LD_Mobile",
                    AppTime = appTime.toString(),
                    Amount = listProductOrder?.get(totalMoney)?.total.toString(),
                    AppTransId = zaloPayHelper.getAppTransId(),
                    EmbedData = "{}",
                    Items = "[]",
                    BankCode = "zalopayapp",
                    Description = "Thanh toán cho đơn hàng #" + zaloPayHelper.getAppTransId()
        )
        val inputHMac = String.format("%s|%s|%s|%s|%s|%s|%s",
            param.AppId,
            param.AppTransId,
            param.AppUser,
            param.Amount,
            param.AppTime,
            param.EmbedData,
            param.Items)
        param.Mac = zaloPayHelper.getMac(Constant.MAC_KEY, inputHMac)
        val formBody: RequestBody = FormBody.Builder()
            .add("appid", param.AppId)
            .add("appuser", param.AppUser)
            .add("apptime", param.AppTime)
            .add("amount", param.Amount)
            .add("apptransid", param.AppTransId)
            .add("embeddata", param.EmbedData)
            .add("item", param.Items)
            .add("bankcode", param.BankCode)
            .add("description", param.Description)
            .add("mac", param.Mac)
            .build()
        orderViewModel?.getToken(formBody)
    }


    override fun onResume() {
        super.onResume()
        if(isPaymentZaloPay){
            findNavController().navigate(R.id.action_fragmentOrder_to_fragmentSuccessOrder)
        }
    }

}