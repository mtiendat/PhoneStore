package com.example.phonestore.view.order

import android.app.AlertDialog
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.phonestore.R
import com.example.phonestore.base.BaseFragment
import com.example.phonestore.databinding.FragmentOrderBinding
import com.example.phonestore.extendsion.*
import com.example.phonestore.model.CheckProductID
import com.example.phonestore.model.ProductOrder
import com.example.phonestore.model.cart.Cart
import com.example.phonestore.model.cart.Voucher
import com.example.phonestore.model.order.Address
import com.example.phonestore.model.order.AddressStore
import com.example.phonestore.model.order.Order
import com.example.phonestore.model.payment.ZaloPayCreateOrderParam
import com.example.phonestore.services.Constant
import com.example.phonestore.services.Constant.ADDRESS
import com.example.phonestore.services.Constant.LISTCHECK
import com.example.phonestore.services.Constant.PAYMENT
import com.example.phonestore.services.Constant.SHIPPING
import com.example.phonestore.services.adapter.DetailProductAdapter
import com.example.phonestore.services.payment.ZaloPayHelper
import com.example.phonestore.view.MainActivity
import com.example.phonestore.view.cart.FragmentDialog
import com.example.phonestore.view.cart.FragmentMyVoucher
import com.example.phonestore.viewmodel.CartViewModel
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
    private var totalMoney: Int? = 0
    private var totalMoneyPre: Int? = 0
    private var idOrder:  Int? = 0
    private var flag = 0
    private var voucher: Voucher? = null
    private var infoOrder: Order? = null
    private var orderViewModel: OrderViewModel? = null
    private val cartViewModel: CartViewModel by activityViewModels()
    private var isPaymentZaloPay: Boolean = false
    private var isAtStore = false
    private var isFragmentFollowOrder: Boolean? = false
    private var idItem: Int? = null
    override fun setBinding(inflater: LayoutInflater, container: ViewGroup?): View? {
        bindingOrderBinding = FragmentOrderBinding.inflate(inflater, container, false)
        return bindingOrderBinding?.root
    }

    override fun setViewModel() {
        orderViewModel = ViewModelProvider(this@FragmentOrder).get(OrderViewModel::class.java)
    }

    override fun setObserve() {
        cartViewModel.voucher.value = null
        findNavController().currentBackStackEntry?.savedStateHandle?.apply {
            getLiveData<Address>(ADDRESS).observe(viewLifecycleOwner) {
                idItem = it.id
                updateAddress(it)
            }
            getLiveData<AddressStore>(SHIPPING).observe(viewLifecycleOwner) {
                when(it){
                    null -> {
                        bindingOrderBinding?.tvShip?.text = "Giao hàng tận nơi"
                        bindingOrderBinding?.tvFeeShip?.visible()
                    }
                    else -> {
                        isAtStore = true
                        bindingOrderBinding?.tvShip?.text = "Nhận hàng tại cửa hàng"
                        bindingOrderBinding?.tvInfoReceive?.text = "Đ/c: ${it.address}"
                        bindingOrderBinding?.tvFeeShip?.gone()
                        updateAddress()
                        idItem = it.id
                    }
                }
            }
            getLiveData<Int>(PAYMENT).observe(viewLifecycleOwner) {
                bindingOrderBinding?.tvPayment?.text = when(it){
                    1 -> getString(R.string.payment_cash)
                    2 -> getString(R.string.payment_zalopay)
                    else -> getString(R.string.payment_cash)
                }
            }
            getLiveData<CheckProductID>(LISTCHECK).observe(viewLifecycleOwner) {
                for(id in it.listIDNonExist!!){
                    var i = 0
                    listProductOrder?.forEach { product->
                        if(id.toInt()== product.product?.id){
                            product.product?.isAvailable = false
                            orderAdapter?.notifyItemChanged(i)
                        }
                        i++
                    }
                    i=0
                }
            }
        }
        orderViewModel?.resultOrder?.observe(viewLifecycleOwner, {
            if(it==true){
                AppEvent.notifyClosePopUp()
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
                    isPaymentZaloPay = true
                }

                override fun onPaymentCanceled(p0: String?, p1: String?) {
                    Toast.makeText(context, "cancel", Toast.LENGTH_SHORT).show()
                }

                override fun onPaymentError(p0: ZaloPayError?, p1: String?, p2: String?) {
                    Toast.makeText(context, "that bai", Toast.LENGTH_SHORT).show()
                }

            })

            }
        })
        orderViewModel?.addressDefault?.observe(viewLifecycleOwner, {
            if(it?.id == -1){
                bindingOrderBinding?.tvOrderPhoneUser?.text = "Nhấn để thêm địa chỉ của bạn"
                bindingOrderBinding?.tvChangeAddress?.gone()
            }else{
                if(flag == 0 ) {
                    bindingOrderBinding?.tvOrderAddress?.text =
                        "${it?.address}, ${it?.ward}, ${it?.district}, ${it?.city}"
                    bindingOrderBinding?.tvOrderNameUser?.text = it?.name
                    bindingOrderBinding?.tvOrderPhoneUser?.text = it?.phone
                }
            }

        })
        cartViewModel.voucher.observe(viewLifecycleOwner, {
            if(it!=null) {
                if (totalMoney!! >= it.condition!!) {
                    totalMoney = totalMoneyPre
                    val priceDiscount = (totalMoney!! * it.discount / 100)
                    bindingOrderBinding?.tvDiscountValue?.text = "-${it?.discount}%"
                    bindingOrderBinding?.tvOrderDiscountDetail?.text = priceDiscount.toVND()
                    bindingOrderBinding?.tvOrderTotalMoneyFinish?.text =
                        (totalMoney!! - priceDiscount).toVND()
                    totalMoney = (totalMoney!! - priceDiscount)
                    voucher = it
                } else {
                    activity?.let { it1 ->
                        FragmentDialog.newInstance(
                            it1,
                            "Thông báo",
                            Constant.WARNING_VOUCHER,
                            "Đóng"
                        )
                    }
                }
            }

        })

    }

    override fun setUI() {
        isFragmentFollowOrder = arguments?.getBoolean("key", false)
        val state  = arguments?.getString("state")
        listProductOrder = arguments?.getParcelableArrayList("listProduct")
        idOrder = arguments?.getInt("idOrder")
        voucher = arguments?.getParcelable("voucher")
        infoOrder = arguments?.getParcelable("info")
        if(isFragmentFollowOrder==true){
            bindingOrderBinding?.btnDiscountOption?.isEnabled = false
            bindingOrderBinding?.btnOrderShippingOption?.isEnabled = false
            bindingOrderBinding?.btnPaymentOption?.isEnabled = false
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
            totalMoney = infoOrder?.totalMoney
        }else totalMoney = arguments?.getInt("totalMoney", 0)
        totalMoneyPre = totalMoney
        bindingOrderBinding?.rvOrderProduct?.isNestedScrollingEnabled = false
        initRecyclerView()
        setOnClickListener()
        setInfoOrder()
        orderViewModel?.getMyAddress()

    }
    fun setOnClickListener(){
        bindingOrderBinding?.btnOrderFinish?.setOnClickListener {
            if(checkProductInOrder()) {
                AppEvent.notifyShowPopUp()
                if(bindingOrderBinding?.tvPayment?.text?.equals(getString(R.string.payment_cash)) == true){
                    orderViewModel?.createOrder(newOrder())
                }else{
                    createOrder()
                }
            }else activity?.let { FragmentDialog.newInstance(it, "Thông báo", "Vui lòng xóa sản phẩm không hợp lệ trước khi đặt hàng", "OK") }
        }

        bindingOrderBinding?.btnCancelOrder?.setOnClickListener {
            alertCancel()
        }
        bindingOrderBinding?.tvChangeAddress?.setOnClickListener {
            it.findNavController().navigate(R.id.action_fragmentOrder_to_fragmentSelectAddress)
        }
        bindingOrderBinding?.ctrlAddress?.setOnClickListener {
            if(isFragmentFollowOrder==false) it.findNavController().navigate(R.id.action_fragmentOrder_to_fragmentSelectAddress)
        }
        bindingOrderBinding?.btnOrderShippingOption?.setOnClickListener {
            it.findNavController().navigate(R.id.action_fragmentOrder_to_fragmentShippingOption, bundleOf("listProductOrder" to listProductOrder))
        }
        bindingOrderBinding?.btnPaymentOption?.setOnClickListener {
            it.findNavController().navigate(R.id.action_fragmentOrder_to_fragmentPaymentOption)
        }
        bindingOrderBinding?.btnDiscountOption?.setOnClickListener {
            val dialog = FragmentMyVoucher()
            activity?.supportFragmentManager?.let { it1 -> dialog.show(it1, "Discount") }
        }
        bindingOrderBinding?.btnContinue?.setOnClickListener {
            it?.findNavController()?.navigate(FragmentOrderDirections.actionFragmentOrderToFragmentHome())
        }
    }
    private fun setInfoOrder(){
        if(voucher?.discount?:-1 > 0){
            val totalPre = totalMoney!! * voucher?.discount!! /100
            bindingOrderBinding?.tvOrderDiscountDetail?.text = totalPre.toVND()
            bindingOrderBinding?.tvOrderTotalMoney?.text = totalMoney.toVND()
            bindingOrderBinding?.tvDiscountValue?.text = "-${voucher?.discount}%"
            bindingOrderBinding?.tvOrderTotalMoneyFinish?.text = totalMoney?.minus(totalPre).toVND()
            totalMoney = totalMoney?.minus(totalPre)

        }else{
            bindingOrderBinding?.tvOrderTotalMoney?.text = totalMoney.toVND()
            bindingOrderBinding?.tvOrderTotalMoneyFinish?.text = totalMoney.toVND()
        }
        if(isFragmentFollowOrder==true){
            if(infoOrder?.idVoucher == null){
                bindingOrderBinding?.tvDiscountValue?.text =""
                bindingOrderBinding?.tvOrderTotalMoney?.text = infoOrder?.totalMoney.toVND()
            }else{
                bindingOrderBinding?.tvDiscountValue?.text = "-${infoOrder?.idVoucher}%"
                val priceRoot = (((infoOrder?.totalMoney?.div((100- infoOrder?.idVoucher!!)))?.times(infoOrder?.idVoucher!!)))?.plus(infoOrder?.totalMoney!!)
                bindingOrderBinding?.tvOrderTotalMoney?.text = priceRoot.toVND()
                bindingOrderBinding?.tvOrderDiscountDetail?.text = (priceRoot?.div(infoOrder?.idVoucher!!)).toVND()
            }
            bindingOrderBinding?.tvPayment?.text = infoOrder?.paymentMethod.toString()
            bindingOrderBinding?.tvShip?.text = infoOrder?.shippingOption.toString()
            bindingOrderBinding?.tvOrderTotalDetailTitle?.visible()
            bindingOrderBinding?.tvOrderTotalDetail?.visible()
            bindingOrderBinding?.tvOrderTotalDetail?.text = totalMoney.toVND()
            if(infoOrder?.shippingOption.equals("Nhận hàng tại cửa hàng")){
                bindingOrderBinding?.tvInfoReceive?.text = "Đ/c: ${infoOrder?.address}"
            }
            bindingOrderBinding?.tvFeeShip?.gone()
        }

    }

    private fun updateAddress(address: Address){
        bindingOrderBinding?.ctrlAddress?.isEnabled = true
        bindingOrderBinding?.tvChangeAddress?.visible()
        bindingOrderBinding?.tvOrderAddressTitle?.text = "Địa chỉ giao hàng"
        bindingOrderBinding?.tvOrderAddress?.text = "${address?.address}, ${address?.ward}, ${address?.district}, ${address?.city}"
        bindingOrderBinding?.tvOrderNameUser?.text = address.name
        bindingOrderBinding?.tvOrderPhoneUser?.text = address.phone

    }
    private fun updateAddress(){
        bindingOrderBinding?.ctrlAddress?.isEnabled = false
        bindingOrderBinding?.tvChangeAddress?.gone()
        bindingOrderBinding?.tvOrderAddressTitle?.text = "Thông tin nhận hàng"
        bindingOrderBinding?.tvOrderNameUser?.text = Constant.user?.name
        bindingOrderBinding?.tvOrderPhoneUser?.text = Constant.user?.phone
        bindingOrderBinding?.tvOrderAddress?.text = ""
    }

    private fun initRecyclerView(){
        orderAdapter = DetailProductAdapter(listProductOrder)
        orderAdapter?.deleteItemCart = {
            AppEvent.notifyShowPopUp()
            cartViewModel.deleteItem(it)
            cartViewModel.flagDelete = 1
            listProductOrder?.removeIf{n ->n.product?.id == it}
            orderAdapter?.notifyDataSetChanged()
            cartViewModel.listProduct.value?.removeIf { n ->n?.id == it }
            Handler(Looper.getMainLooper()).postDelayed({
                AppEvent.notifyClosePopUp()
            },500)
            if(listProductOrder?.size ==0){
                bindingOrderBinding?.groupOrder?.gone()
                bindingOrderBinding?.groupNoneProduct?.visible()
                cartViewModel.getTotalProduct()
                cartViewModel.flag = 0
            }

        }
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
                    Amount = totalMoney.toString(),
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
    private fun checkProductInOrder(): Boolean{
        listProductOrder?.forEach {
            if(it.product?.isAvailable == false){
                return false
            }
        }
        return true
    }
    override fun onResume() {
        super.onResume()
        if(isPaymentZaloPay){
            findNavController().navigate(R.id.action_fragmentOrder_to_fragmentSuccessOrder)
            orderViewModel?.createOrder(newOrder())
        }
    }
    private fun newOrder(): Order {
        val listProductFinish: ArrayList<Cart> = arrayListOf()
        listProductOrder?.forEach {
            it.product?.let { it1 -> listProductFinish.add(it1) }
        }
        var order = Order()
        if (isAtStore) {
            order = Order(
                idVoucher = voucher?.id,
                idStore = idItem,
                paymentMethod = bindingOrderBinding?.tvPayment?.text.toString(),
                shippingOption = bindingOrderBinding?.tvShip?.text.toString(),
                address = bindingOrderBinding?.tvInfoReceive?.text.toString().replace(
                        "Đ/c:",
                        ""),
                totalMoney = totalMoney,
                listProduct = listProductFinish
            )
        } else {
            order = Order(
                idVoucher = voucher?.id,
                idAccountAddress = idItem,
                paymentMethod = bindingOrderBinding?.tvPayment?.text.toString(),
                shippingOption = bindingOrderBinding?.tvShip?.text.toString(),
                address =  bindingOrderBinding?.tvOrderAddress?.text.toString(),
                totalMoney = totalMoney,
                listProduct = listProductFinish
            )
        }
        return order
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cartViewModel.voucher.value = null
        cartViewModel.resultDeleteItem?.value = null
        flag = 1
    }

}