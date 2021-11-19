package com.example.phonestore.view.order

import android.annotation.SuppressLint
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
import com.example.phonestore.model.ParamListHasQty
import com.example.phonestore.model.ParamTwoInt
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
import java.util.regex.Pattern
import kotlin.collections.ArrayList

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
    private var address: Address? = null
    private var fromNotificaiton = false
    private var click = false
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
                address = it
                updateAddress(it)
               
            }
            getLiveData<AddressStore>(SHIPPING).observe(viewLifecycleOwner) {
                when(it){
                    null -> {
                        bindingOrderBinding?.tvShip?.text = "Giao hàng tận nơi"
                        bindingOrderBinding?.tvFeeShip?.visible()
                        isAtStore = false
                        address?.let { it1 -> updateAddress(it1) }
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

        }
        orderViewModel?.resultOrder?.observe(viewLifecycleOwner, {
            if(it==true){
                AppEvent.notifyClosePopUp()
                view?.findNavController()?.navigate(R.id.action_fragmentOrder_to_fragmentSuccessOrder)
                cartViewModel.flag = 0
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
            if(!isPaymentZaloPay){
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
             }
                isPaymentZaloPay = true
            }

        })
        orderViewModel?.addressDefault?.observe(viewLifecycleOwner, {
            if(it?.id == -1){
                if(bindingOrderBinding?.tvOrderPhoneUser?.text.isNullOrEmpty()) bindingOrderBinding?.tvOrderPhoneUser?.setText("Nhấn để thêm địa chỉ")
                bindingOrderBinding?.tvChangeAddress?.gone()
            }else{
                if(isFragmentFollowOrder == false && !isAtStore&& address == null) {
                    bindingOrderBinding?.tvOrderAddress?.text =
                        "${it?.address}, ${it?.ward}, ${it?.district}, ${it?.city}"
                    bindingOrderBinding?.tvOrderNameUser?.text = it?.name
                    bindingOrderBinding?.tvOrderPhoneUser?.setText(it?.phone)
                    address = it
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
        orderViewModel?.listCheckProductInStore?.observe(viewLifecycleOwner, {
            if(it?.check?.listIDDownOne?.size== 0&& it?.check?.listIDNonExist?.size==0){
                orderViewModel?.createOrder(newOrder())
            }else{
                AppEvent.notifyClosePopUp()
                for(i in it?.check?.listIDNonExist!!){
                var i = 0
                listProductOrder?.forEach { product ->
                    if(id.toInt()== product.product?.idProduct){
                        product.product?.isAvailable = false
                        orderAdapter?.notifyItemChanged(i)
                    }
                    i++
                }
                i=0
            }
            for(id in it.check?.listIDDownOne!!){
                var i = 0
                listProductOrder?.forEach { product ->
                    if(id.toInt()== product.product?.idProduct){
                        product.product?.isQtyAvailable = false
                        orderAdapter?.notifyItemChanged(i)
                    }
                    i++
                }
                i=0
            }
            }
        })
        orderViewModel?.detailOrder?.observe(viewLifecycleOwner, {
            infoOrder = it.order
            followOrdeR(it.order?.state)
            totalMoneyPre = totalMoney
            bindingOrderBinding?.rvOrderProduct?.isNestedScrollingEnabled = false
            initRecyclerView(it.listProduct)
            setOnClickListener()
            setInfoOrder()
        })
    }

    override fun setUI() {
        (activity as MainActivity).supportActionBar?.title = "Kiểm tra đơn hàng"
        isFragmentFollowOrder = arguments?.getBoolean("key", false)
        val state  = arguments?.getString("state")
        listProductOrder = arguments?.getParcelableArrayList("listProduct")
        idOrder = arguments?.getInt("idOrder")
        voucher = arguments?.getParcelable("voucher")
        infoOrder = arguments?.getParcelable("info")
        fromNotificaiton = arguments?.getBoolean("FROMNOTIFICATION", false) == true
        if(fromNotificaiton){
            orderViewModel?.getListProductOrder(idOrder)
        }else{
            followOrdeR(state)
            totalMoneyPre = totalMoney
            bindingOrderBinding?.rvOrderProduct?.isNestedScrollingEnabled = false
            initRecyclerView()
            setOnClickListener()
            setInfoOrder()
        }



    }
    private fun followOrdeR(state : String?){
        if(isFragmentFollowOrder == true){
            bindingOrderBinding?.btnDiscountOption?.isEnabled = false
            bindingOrderBinding?.btnOrderShippingOption?.isEnabled = false
            bindingOrderBinding?.btnPaymentOption?.isEnabled = false
            bindingOrderBinding?.tvChangeAddress?.gone()
            if(state == Constant.CANCEL){
                bindingOrderBinding?.btnCancelOrder?.visible()
                context?.let { ContextCompat.getColor(it, R.color.dray) }?.let { bindingOrderBinding?.btnCancelOrder?.setBackgroundColor(it) }
                bindingOrderBinding?.btnCancelOrder?.text = Constant.CANCEL
                bindingOrderBinding?.btnCancelOrder?.disable()
            }else if(state == Constant.RECEIVED){
                bindingOrderBinding?.btnCancelOrder?.visible()
            }
            bindingOrderBinding?.ctrlOrder?.gone()
            totalMoney = infoOrder?.totalMoney
            bindingOrderBinding?.tvStateOrder?.text = state
            when(state){
                Constant.RECEIVED -> {
                    bindingOrderBinding?.tvStateInfo?.text = "Đơn hàng đã được tiếp nhận, chúng tôi sẽ sớm liên hệ với bạn."
                }
                Constant.CONFIRMED -> {
                    bindingOrderBinding?.tvStateInfo?.text = "Chúng tôi sẽ sớm giao hàng đến bạn."
                }
                Constant.DELIVERED -> {
                    bindingOrderBinding?.tvStateInfo?.text = "Kiện hàng đã được giao thành công đến bạn!"
                    bindingOrderBinding?.tvStateOrder?.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
                }
            }
        }else {
            totalMoney = arguments?.getInt("totalMoney", 0)
            if(address==null) orderViewModel?.getMyAddress()
            bindingOrderBinding?.ctrlState?.gone()
        }

    }
    private fun setOnClickListener(){
        bindingOrderBinding?.btnOrderFinish?.setOnClickListener {
            if(checkProductInOrder()) {
                AppEvent.notifyShowPopUp()
                if(bindingOrderBinding?.tvPayment?.text?.equals(getString(R.string.payment_cash)) == true){
                    val listID = arrayListOf<ParamTwoInt>()
                    listProductOrder?.forEach { product ->
                        product.product?.idProduct?.let {
                                it1 -> listID.add(ParamTwoInt(it1, product.qty?:0))
                        }
                    }
                    val param = ParamListHasQty(listID = listID)
                    orderViewModel?.checkProductInStore(address?.id,"delivery", param)

                }else{
                    isPaymentZaloPay = false
                    createOrder()
                }
            }
        }

        bindingOrderBinding?.btnCancelOrder?.setOnClickListener {
            alertCancel()
        }
        bindingOrderBinding?.tvChangeAddress?.setOnClickListener {
            click = true
            it.findNavController().navigate(R.id.action_fragmentOrder_to_fragmentSelectAddress)
        }
        bindingOrderBinding?.ctrlAddress?.setOnClickListener {
            click = true
            if(isFragmentFollowOrder==false) it.findNavController().navigate(R.id.action_fragmentOrder_to_fragmentSelectAddress)
        }
        bindingOrderBinding?.btnOrderShippingOption?.setOnClickListener {
            click = true
            it.findNavController().navigate(R.id.action_fragmentOrder_to_fragmentShippingOption, bundleOf("listProductOrder" to listProductOrder))
        }
        bindingOrderBinding?.btnPaymentOption?.setOnClickListener {
            click = true
            it.findNavController().navigate(R.id.action_fragmentOrder_to_fragmentPaymentOption)
        }
        bindingOrderBinding?.btnDiscountOption?.setOnClickListener {
            click = true
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
            bindingOrderBinding?.tvOrderNameUser?.text = infoOrder?.name
            bindingOrderBinding?.tvOrderPhoneUser?.setText(infoOrder?.phone)
            if(infoOrder?.shippingOption.equals("Nhận hàng tại cửa hàng")){
                bindingOrderBinding?.tvInfoReceive?.text = "Đ/c: ${infoOrder?.address}"
                bindingOrderBinding?.tvOrderAddressTitle?.text = "Thông tin nhận hàng"
            }
            bindingOrderBinding?.tvFeeShip?.gone()
            bindingOrderBinding?.tvOrderAddress?.text = infoOrder?.address
        }
        closePopup()
    }

    private fun updateAddress(address: Address){
        bindingOrderBinding?.ctrlAddress?.isEnabled = true
        bindingOrderBinding?.tvChangeAddress?.visible()
        bindingOrderBinding?.tvOrderAddressTitle?.text = "Địa chỉ giao hàng"
        bindingOrderBinding?.tvOrderAddress?.text = "${address?.address}, ${address?.ward}, ${address?.district}, ${address?.city}"
        bindingOrderBinding?.tvOrderNameUser?.text = address.name
        bindingOrderBinding?.tvOrderAddress?.post {
            bindingOrderBinding?.tvOrderPhoneUser?.setText(address.phone)
            bindingOrderBinding?.tvOrderPhoneUser?.isFocusable = false
        }

    }
    private fun updateAddress(){
        bindingOrderBinding?.ctrlAddress?.isEnabled = false
        bindingOrderBinding?.tvChangeAddress?.gone()
        bindingOrderBinding?.tvOrderAddressTitle?.text = "Thông tin nhận hàng"
        bindingOrderBinding?.tvOrderNameUser?.text = Constant.user?.name
        bindingOrderBinding?.tvOrderAddress?.post {
            if (Constant.user?.phone == null) {
                bindingOrderBinding?.tvOrderPhoneUser?.setText("Thêm số điện thoại của bạn")
                bindingOrderBinding?.tvOrderPhoneUser?.isFocusable = true
                bindingOrderBinding?.tvOrderPhoneUser?.isFocusableInTouchMode = true

            } else bindingOrderBinding?.tvOrderPhoneUser?.setText(Constant.user?.phone)
        }
        bindingOrderBinding?.tvOrderAddress?.text = ""
    }

    private fun initRecyclerView(listProOrder: ArrayList<ProductOrder>? = arrayListOf()){
        if(fromNotificaiton){
            orderAdapter = DetailProductAdapter(listProOrder)
        }else  orderAdapter = DetailProductAdapter(listProductOrder)

        orderAdapter?.deleteItemCart = { it, position->
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
    private fun createOrder(){
        click = true
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
                activity?.let { FragmentDialog.newInstance(it, "Thông báo", "Vui lòng xóa sản phẩm không hợp lệ trước khi đặt hàng", "OK") }
                return false
            }else if(it.product?.isQtyAvailable == false){
                activity?.let { FragmentDialog.newInstance(it, "Thông báo", "Vui lòng giảm số lượng sản phẩm trước khi đặt hàng", "OK") }
                return false
            }else if(bindingOrderBinding?.tvOrderPhoneUser?.text.toString().contains( "Nhấn để thêm địa chỉ của bạn")){
                activity?.let { FragmentDialog.newInstance(it, "Thông báo", "Vui lòng chọn địa chỉ trước khi đặt hàng", "OK") }
                return false
            }else if(!Pattern.compile("^(\\+84|0)+([3|5|7|8|9])+([0-9]{8})\$").matcher(bindingOrderBinding?.tvOrderPhoneUser?.text!!).matches()){
                activity?.let { FragmentDialog.newInstance(it, "Thông báo", "Vui lòng thêm số điện thoại hợp lê ", "OK") }
                return false
            }
        }
        return true
    }
    override fun onResume() {
        super.onResume()
//        if(isPaymentZaloPay){
//            findNavController().navigate(R.id.action_fragmentOrder_to_fragmentSuccessOrder)
//            orderViewModel?.createOrder(newOrder())
//        }
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
                totalMoney = totalMoney,
                listProduct = listProductFinish
            )
        } else {
            order = Order(
                idVoucher = voucher?.id,
                idAccountAddress = address?.id,
                paymentMethod = bindingOrderBinding?.tvPayment?.text.toString(),
                shippingOption = bindingOrderBinding?.tvShip?.text.toString(),
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
        cartViewModel.flag = 1
        if(!click){
            orderViewModel?.updateInQueue(Constant.idUser, "delete")
        }else click = false

    }

    private fun closePopup() {
        bindingOrderBinding?.pb?.popup?.gone()
    }
    private fun onShowPopup() {
        bindingOrderBinding?.pb?.popup?.visible()
    }
}