package com.example.phonestore.view.cart

import android.app.AlertDialog
import android.content.Context
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.phonestore.R
import com.example.phonestore.base.BaseFragment
import com.example.phonestore.databinding.FragmentDetailCartBinding
import com.example.phonestore.extendsion.*
import com.example.phonestore.model.ProductOrder
import com.example.phonestore.model.cart.Cart
import com.example.phonestore.model.cart.Voucher
import com.example.phonestore.services.Constant
import com.example.phonestore.services.adapter.DetailProductAdapter
import com.example.phonestore.services.widget.SwipeHelper
import com.example.phonestore.view.MainActivity
import com.example.phonestore.viewmodel.CartViewModel
import com.google.android.material.snackbar.Snackbar

class FragmentDetailCart: BaseFragment() {
    private var bindingDetailCart: FragmentDetailCartBinding? = null
    private val detailCartViewModel: CartViewModel by activityViewModels()
    private var detailCartAdapter: DetailProductAdapter<Cart>? = null
    private var listProduct: ArrayList<Cart>? = arrayListOf()
    private var listProductChoose: ArrayList<ProductOrder>? = arrayListOf()
    private var totalMoney: Int? = 0
    private var hasDiscount: Int  = 0
    private var voucher: Voucher?  = null
    private var totalMoneyPrevious: Int? = 0
    private var isDelete = false
    private var flag = 0
    private var click = false
    private var listIDs = arrayListOf<Int>()
    private var listProductWasDelete = arrayListOf<ProductOrder>()
    private var condition: Int = 0
    private var listIDsException = arrayListOf<Int>()
    override fun setBinding(inflater: LayoutInflater, container: ViewGroup?): View? {
        bindingDetailCart = FragmentDetailCartBinding.inflate(inflater, container, false)
        closePopup()
        return bindingDetailCart?.root
    }

    override fun setUI() {
        if(flag==0){
            bindingDetailCart?.progressBarDetailCart?.visible()
            detailCartViewModel.getMyCart()
            bindingDetailCart?.rvMyCart?.isNestedScrollingEnabled = false
        }else{
            listProductChoose?.clear()
            totalMoney = detailCartViewModel.totalMoney.value
                totalMoneyPrevious = totalMoney
                listProduct?.forEach { item ->
                    listProductChoose?.add(ProductOrder(item, item.qty))
                }
                bindingDetailCart?.tvTotalMoney?.text = totalMoney.toVND()
                bindingDetailCart?.tvTotalPreDetail?.text = totalMoney.toVND()
                if(isDelete){
                    updateTotalHasDiscount()
                }
                bindingDetailCart?.rvMyCart?.visible()
                bindingDetailCart?.tvNoProductInCart?.gone()
                bindingDetailCart?.progressBarDetailCart?.gone()
                bindingDetailCart?.ctrlBonus?.visible()
                bindingDetailCart?.ctrlOrder?.visible()
                enabledBtnOrder()
        }

        flag++
        initRecyclerView()

    }

    override fun setEvents() {
        bindingDetailCart?.btnOrder?.setOnClickListener {
            onShowPopup()
            click = true
            detailCartViewModel?.updateInQueue( "add")

            //listProductChoose?.clear()
        }
        bindingDetailCart?.btnChooseDiscount?.setOnClickListener {
            val dialog = FragmentMyVoucher()
            activity?.supportFragmentManager?.let { it1 -> dialog.show(it1, "Discount") }
        }
        bindingDetailCart?.ivClearDiscount?.setOnClickListener {
            bindingDetailCart?.tvTotalMoney?.text = totalMoneyPrevious.toVND()
            bindingDetailCart?.tvDiscountDetail?.text = "0đ"
            bindingDetailCart?.groupDiscount?.gone()
            bindingDetailCart?.btnChooseDiscount?.text = "Chọn mã giảm giá"
            totalMoney = totalMoneyPrevious
            hasDiscount = 0
        }
    }

    override fun setViewModel() {

    }

    override fun setObserve() {
        detailCartViewModel.resultDeleteItem?.observe(viewLifecycleOwner,{
            if(it!=null) {
                if (it && detailCartViewModel.flagDelete == 0) {
                    view?.let { it1 ->
                        Snackbar.make(it1, Constant.DELETED_PRODUCT, Snackbar.LENGTH_SHORT).show()
                    }
                    if(listProduct?.size==0) {
                        setViewNoneProduct()
                        detailCartViewModel.listProduct.value = null
                    }
                }
            }
        })
        detailCartViewModel.listProduct.observe(viewLifecycleOwner, {
            if(it?.size == 0){//nếu k
                    setViewNoneProduct()
            }else if(it?.size?:-1 >0) {
                context?.let { it1 ->
                    ContextCompat.getColor(
                        it1, R.color.red_light)
                }?.let { it2 -> bindingDetailCart?.btnOrder?.setBackgroundColor(it2) }

                totalMoney = detailCartViewModel.totalMoney.value
                totalMoneyPrevious = totalMoney
                if (it != null) {
                    listProduct?.addAll(it)
                    detailCartAdapter?.setItems(it)
                }
                it?.forEach { item ->
                    listProductChoose?.add(ProductOrder(item, item.qty))
                }
                bindingDetailCart?.tvTotalMoney?.text = totalMoney.toVND()
                bindingDetailCart?.tvTotalPreDetail?.text = totalMoney.toVND()
                if(isDelete){
                    updateTotalHasDiscount()
                }
                bindingDetailCart?.rvMyCart?.visible()
                bindingDetailCart?.tvNoProductInCart?.gone()
                bindingDetailCart?.progressBarDetailCart?.gone()
                bindingDetailCart?.ctrlBonus?.visible()
                bindingDetailCart?.ctrlOrder?.visible()
                enabledBtnOrder()
            }
        })
        detailCartViewModel.voucher.observe(viewLifecycleOwner, {
            if(it!=null){
                if(totalMoney!! >= it.condition!!){
                    voucher = it
                    totalMoneyPrevious = totalMoney
                    hasDiscount = it.discount
                    val priceDiscount = (totalMoney!!*it.discount /100)
                    bindingDetailCart?.tvDiscount?.text = "-${it.discount}%"
                    bindingDetailCart?.tvDiscountDetail?.text = priceDiscount.toVND()
                    bindingDetailCart?.groupDiscount?.visible()
                    bindingDetailCart?.btnChooseDiscount?.text =""
                    totalMoney = (totalMoney?.minus(priceDiscount))
                    bindingDetailCart?.tvTotalMoney?.text = totalMoney.toVND()
                    condition = it.condition!!
                }else{
                    activity?.let { it1 -> FragmentDialog.newInstance(it1, "Thông báo", Constant.WARNING_VOUCHER,"Đóng") }
                }
            }

        })
        detailCartViewModel.status.observe(viewLifecycleOwner, {
                if(click){
                    if(it){
                        if(listIDsException.isNotEmpty()){
                            detailCartViewModel.checkProductInQueue(listIDsException)
                        }else detailCartViewModel.checkProductInQueue(null)
                    }else activity?.let {
                            it1 -> FragmentDialog.newInstance(it1, "Thông báo", Constant.WARNING_ORDER,"OK")
                            closePopup()
                    }

                }
        })
        detailCartViewModel.responseCheckInQueue.observe(viewLifecycleOwner, {
            if(click){
                if(it.status == true){
//                    if(listIDs.isNotEmpty() && !checkNew(it.listIDs)){
//                        enableProduct(listIDs)
//                    }
                    closePopup()
                    //detailCartViewModel?.updateInQueue(Constant.idUser, "add")
                    android.os.Handler(Looper.getMainLooper()).postDelayed({
                        goToOrder()
                    }, 1000)
                }else {
                    if(checkNew(it.listIDs)){
                        click = true
                        detailCartViewModel.updateInQueue( "add")
                    }else{
                        disableProduct(it.listIDs)
                        listIDs = it.listIDs?: arrayListOf()
                        closePopup()
                        context?.let { it1 -> alertNotification(it1) }
                    }

                }
                click  = false
            }

        })

    }
    private fun checkNew(newListID: ArrayList<Int>?): Boolean{
        if(listIDs == newListID){
            return true
        }
        return false
    }
    private fun disableProduct(listIDs: ArrayList<Int>?){
        for(i in 0 until detailCartAdapter?.list?.size!!){
            listIDs?.forEach { id ->
                if(detailCartAdapter?.list?.get(i)?.idProduct ==  id) {
                    if(detailCartAdapter?.list?.get(i)?.qtyInWH!! > 0){
                        detailCartAdapter?.list?.get(i)?.qtyInWH = 0
                        detailCartAdapter?.notifyItemChanged(i)
                        listProductChoose?.forEach { n ->
                            if(n.product?.idProduct ==  id){
                                listProductWasDelete.add(n)
                            }
                        }

                    }
                }
            }
        }
    }
    private fun enableProduct(listIDs: ArrayList<Int>?){
        for(i in 0 until detailCartAdapter?.list?.size!!){
            listIDs?.forEach { id ->
                if(detailCartAdapter?.list?.get(i)?.idProduct ==  id) {
                    detailCartAdapter?.list?.get(i)?.qtyInWH = 99
                    detailCartAdapter?.notifyItemChanged(i)
                    listProductChoose?.addAll(listProductWasDelete.filter { n -> n.product?.idProduct == id })
                }
            }
        }
    }
    private fun goToOrder(){
        val item = bundleOf("listProduct" to listProductChoose, "voucher" to voucher, "totalMoney" to totalMoneyPrevious)
        NavHostFragment.findNavController(this).navigate(R.id.action_fragmentDetailCart_to_fragmentOrder, item)
    }
    private fun initRecyclerView(){
        detailCartAdapter = DetailProductAdapter(listProduct)
        featuresCart()
        bindingDetailCart?.rvMyCart?.adapter = detailCartAdapter
        bindingDetailCart?.rvMyCart?.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        bindingDetailCart?.rvMyCart?.layoutManager = LinearLayoutManager(context)
        val itemTouchHelper = bindingDetailCart?.rvMyCart?.let {
            ItemTouchHelper(object : SwipeHelper(it) {
                override fun instantiateUnderlayButton(position: Int): List<UnderlayButton> {
                    val deleteButton = deleteButton(position)
                    return listOf(deleteButton)
                }
            })
        }
        itemTouchHelper?.attachToRecyclerView(bindingDetailCart?.rvMyCart)
    }
    private fun featuresCart(){
        //set checkbox
        detailCartAdapter?.clickCheckBox = { total, state, product, qty ->
            if(hasDiscount?:0 >0){
                totalMoney = totalMoneyPrevious
            }
            if (total == 0 && !state && totalMoney == 0) { // loại đc trườg hợp check rồi, sau đó bỏ check nhưg tổng tiền = 0 thì nó sẽ trừ tiếp -> âm
                totalMoney = 0
            }
            if (state) {
                totalMoney = totalMoney?.plus(total)
                listIDsException.removeIf { n -> n == product.idProduct }
                if(!checkExist(product.id)){
                    val productOrder = ProductOrder(product, qty)
                    if(total>0) {
                        listProductChoose?.add(productOrder)//thêm sp vào ds chờ. Để put qua order
                    }
                }

            } else {
                totalMoney = totalMoney?.minus(total)
                val productOrder = ProductOrder(product, qty)
                product.idProduct?.let { listIDsException.add(it) }
                listProductChoose?.removeIf { n ->(n.product?.id == productOrder.product?.id) }//remove dùng removeIf để tránh ngoại lệ ConcurrentModificationException và "main" java.lang.IndexOutOfBoundsException
            }
            updateTotalHasDiscount()
        }
        // set + -
        detailCartAdapter?.clickMaxMin = { price, state ->
            if(hasDiscount?:0 >0){
                totalMoney = totalMoneyPrevious
            }
            if (state) {
                totalMoney = totalMoney?.plus(price ?: 0)
            } else totalMoney = totalMoney?.minus(price ?: 0)
//            if(listProductChoose?.size!! > 0) {
//                listProductChoose?.get(listProductChoose?.size!! - 1)?.total = totalMoney
//            }

            updateTotalHasDiscount()

        }
        detailCartAdapter?.updateProductInList = { product, qty ->
            if (listProduct != null) {
                if(checkExist(product?.id)) { //Ktra sp có trong list đã chọn chưa
                    if (qty != 0) {
                        for (i in listProductChoose!!) {
                            if (i.product?.id == product?.id) {
                                i.qty = qty //Cập nhật lại số lượng
                                i.product?.isQtyAvailable = true
                            }
                        }
                    } else listProductChoose?.removeIf { n -> n.product?.id == product?.id }//số lượng về 0 thì xóa sp khỏi ds đã chọn
                }else {
                    val productOrder = ProductOrder(product, qty)
                    listProductChoose?.add(productOrder)
                }

            }
        }
        detailCartAdapter?.updateItemCart = { id, it ->
            if(it == true){
                detailCartViewModel.updateItem(id, "plus")
                listProduct?.forEach {
                    if(it.idProduct == id){
                        it.qty = it.qty?.plus(1)
                    }
                }
            }else {
                detailCartViewModel.updateItem(id, "min")
                listProduct?.forEach {
                    if(it.idProduct == id){
                        it.qty = it.qty?.minus(1)
                    }
                }
            }
        }
        detailCartAdapter?.deleteItemCart = { id, position ->
            context?.let { alertDelete(it, id, position) }
        }
    }
    private fun checkExist(idProduct: Int?): Boolean{
        for (i in listProductChoose!!) {
            if (i.product?.id == idProduct) {
                return true
            }
        }
        return false
    }

    private fun deleteButton(position: Int) : SwipeHelper.UnderlayButton {
        return SwipeHelper.UnderlayButton(
            requireContext(),
            "Xóa",
            14.0f,
            android.R.color.holo_red_light,
            this::handle)
    }

    private fun handle(position: Int){
        context?.let { alertDelete(it, listProduct?.get(position)?.id, position) }
    }

    private fun updateTotalHasDiscount(){
        if(totalMoney?:0 >0){
            enabledBtnOrder()
        }else{
            totalMoney = 0
            disabledBtnOrder()
        }
        if(hasDiscount > 0){
            val priceDiscount = (totalMoney!!*hasDiscount/100)
            bindingDetailCart?.tvTotalPreDetail?.text = totalMoney.toVND()
            bindingDetailCart?.tvDiscountDetail?.text = priceDiscount.toVND()
            bindingDetailCart?.tvTotalMoney?.text = (totalMoney?.minus(priceDiscount!!)).toVND()
            totalMoneyPrevious = totalMoney
            if(condition > totalMoney?.minus(priceDiscount)!!){
                activity?.let { it1 -> FragmentDialog.newInstance(it1, "Thông báo", Constant.WARNING_VOUCHER,"Đóng") }
                bindingDetailCart?.ivClearDiscount?.performClick()
                condition = 0
            }
        }else{
            bindingDetailCart?.tvTotalMoney?.text = totalMoney.toVND()
            bindingDetailCart?.tvTotalPreDetail?.text = totalMoney.toVND()
        }
    }
    private fun alertNotification(context: Context){
        val builder = AlertDialog.Builder(context, R.style.MyDialogTheme)
        builder.setMessage(getText(R.string.sold_out))
        builder.setTitle(Constant.NOTIFICATION)
        builder.setCancelable(false)
        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.cancel()
        }
        val alertDialog = builder.create()
        alertDialog.setOnShowListener {
            context.let {alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(
                ContextCompat.getColor(it, R.color.blue))}
        }
        alertDialog.show()
    }
    private fun alertDelete(context: Context, id: Int?, position: Int){
        val builder = AlertDialog.Builder(context, R.style.MyDialogTheme)
        builder.setMessage(Constant.QUESTION_DELETE)
        builder.setTitle(Constant.NOTIFICATION)
        builder.setCancelable(false)
        builder.setPositiveButton(Constant.YES) { _, _ ->
            isDelete = true

            if(hasDiscount==0){
                bindingDetailCart?.tvTotalMoney?.text = (totalMoney?.minus(listProduct?.get(position)?.price?.times(listProduct?.get(position)?.qty?:0)?:0)).toVND()
                bindingDetailCart?.tvTotalPreDetail?.text = (totalMoney?.minus(listProduct?.get(position)?.price?.times(listProduct?.get(position)?.qty?:0)?:0)).toVND()
            }else updateTotalHasDiscount()
            for (i in 0 until listProduct?.size!!) {
               if(listProduct?.get(i)?.id == id){
                    listProduct?.removeAt(i)
                    detailCartAdapter?.notifyItemRemoved(i)
                   break
                }
            }

            listProductChoose?.removeIf {n -> n.product?.id == id }
            listIDsException.removeIf { n -> n == id }
            context.let { it1 -> MainActivity.icon?.let { it2 ->
                MainActivity.setBadgeCount(it1, icon = it2, listProduct?.size.toString())
            }
                detailCartViewModel.deleteItem(id)
            }
        }
        builder.setNegativeButton(Constant.NO) { dialog, _ ->
            dialog.cancel()
            detailCartAdapter?.notifyDataSetChanged()
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

    override fun onDestroyView() {
        super.onDestroyView()
        detailCartViewModel.voucher.value = null
        detailCartViewModel.listProduct.value = null
        detailCartViewModel.resultDeleteItem?.value = null
        listIDsException.clear()
    }

    private fun setViewNoneProduct(){
        bindingDetailCart?.rvMyCart?.gone()
        bindingDetailCart?.tvNoProductInCart?.visible()
        bindingDetailCart?.progressBarDetailCart?.gone()
        bindingDetailCart?.ctrlBonus?.gone()
        disabledBtnOrder()
        bindingDetailCart?.tvTotalMoney?.text = "0đ"
    }

    private fun disabledBtnOrder(){
        bindingDetailCart?.btnOrder?.isEnabled = false
        bindingDetailCart?.btnOrder?.setBackgroundResource(R.color.dray)
    }
    private fun enabledBtnOrder(){
        bindingDetailCart?.btnOrder?.isEnabled = true
        bindingDetailCart?.btnOrder?.setBackgroundResource(R.color.red_light)
    }
    private fun closePopup() {
        bindingDetailCart?.pb?.popup?.gone()
    }
    private fun onShowPopup() {
        bindingDetailCart?.pb?.popup?.visible()
    }

}