package com.example.phonestore.view.cart

import android.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.phonestore.extendsion.*
import com.example.phonestore.R
import com.example.phonestore.base.BaseFragment
import com.example.phonestore.databinding.FragmentDetailCartBinding
import com.example.phonestore.model.cart.DetailCart
import com.example.phonestore.model.ProductOrder
import com.example.phonestore.model.cart.Cart
import com.example.phonestore.model.cart.Discount
import com.example.phonestore.services.Constant
import com.example.phonestore.services.Constant.DISCOUNT
import com.example.phonestore.services.adapter.DetailProductAdapter
import com.example.phonestore.services.SwipeHelper
import com.example.phonestore.view.MainActivity
import com.example.phonestore.view.productDetail.FragmentDetailTechnology
import com.example.phonestore.viewmodel.CartViewModel
import com.google.android.material.snackbar.Snackbar
import com.jpardogo.android.googleprogressbar.library.FoldingCirclesDrawable
import kotlinx.coroutines.isActive

class FragmentDetailCart: BaseFragment() {
    private var bindingDetailCart: FragmentDetailCartBinding? = null
    private val detailCartViewModel: CartViewModel by activityViewModels()
    private var detailCartAdapter: DetailProductAdapter<Cart>? = null
    private var listProduct: ArrayList<Cart>? = arrayListOf()
    private var listProductChoose: ArrayList<ProductOrder>? = arrayListOf()
    private var totalMoney: Int? = 0
    private var hasDiscount: Int  = 0
    private var totalMoneyPrevious: Int? = 0
    override fun setBinding(inflater: LayoutInflater, container: ViewGroup?): View? {
        bindingDetailCart = FragmentDetailCartBinding.inflate(inflater, container, false)
        return bindingDetailCart?.root
    }

    override fun setUI() {
        initRecyclerView()
        detailCartViewModel.getMyCart()
        bindingDetailCart?.rvMyCart?.isNestedScrollingEnabled = false
    }

    override fun setEvents() {
        bindingDetailCart?.btnOrder?.setOnClickListener {
            val item = bundleOf("listProduct" to listProductChoose, "discount" to hasDiscount)
            it.findNavController().navigate(R.id.action_fragmentDetailCart_to_fragmentOrder, item)
        }
        bindingDetailCart?.btnChooseDiscount?.setOnClickListener {
            val dialog = FragmentMyDiscount()
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
        detailCartViewModel.resultDeleteItem.observe(viewLifecycleOwner,{
            if(it){
                view?.let { it1 -> Snackbar.make(it1, Constant.DELETED_PRODUCT, Snackbar.LENGTH_SHORT).show()
                    detailCartViewModel.getTotalProduct()
                    detailCartViewModel.getMyCart()
                }
            }
        })
        detailCartViewModel.totalProduct.observe(viewLifecycleOwner, {
            context?.let { it1 -> MainActivity.icon?.let { it2 ->
                MainActivity.setBadgeCount(it1, icon = it2, it.toString())
                }
            }
        })

        detailCartViewModel.listProduct.observe(viewLifecycleOwner, {
            if(it===null||it.size==0){//nếu k có sản phẩm nào trong giỏ hàng
                bindingDetailCart?.rvMyCart?.gone()
                bindingDetailCart?.tvNoProductInCart?.visible()
                bindingDetailCart?.progressBarDetailCart?.gone()
                bindingDetailCart?.ctrlBonus?.gone()
            }else {
                bindingDetailCart?.rvMyCart?.visible()
                bindingDetailCart?.tvNoProductInCart?.gone()
                bindingDetailCart?.progressBarDetailCart?.gone()
                bindingDetailCart?.ctrlBonus?.visible()
                totalMoney = detailCartViewModel.totalMoney.value
                listProduct?.addAll(it)
                it.forEach { item ->
                    listProductChoose?.add(ProductOrder(item, item.qty, totalMoney))
                }
                detailCartAdapter?.setItems(it)
                bindingDetailCart?.tvTotalMoney?.text = totalMoney.toVND()
                bindingDetailCart?.tvTotalPreDetail?.text = totalMoney.toVND()
            }
        })
        detailCartViewModel.discount.observe(viewLifecycleOwner, {
            totalMoneyPrevious = totalMoney
            hasDiscount = it.discount
            val priceDiscount = totalMoney?.div(it.discount)
            bindingDetailCart?.tvDiscount?.text = "-${it.discount}%"
            bindingDetailCart?.tvDiscountDetail?.text = priceDiscount.toVND()
            bindingDetailCart?.groupDiscount?.visible()
            bindingDetailCart?.btnChooseDiscount?.text =""
            totalMoney = (totalMoney?.minus((totalMoney!! / it.discount)))
            bindingDetailCart?.tvTotalMoney?.text = totalMoney.toVND()
        })

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

                val deleteButton = deleteButton()
                return listOf(deleteButton)
            }
        })
        }
        itemTouchHelper?.attachToRecyclerView(bindingDetailCart?.rvMyCart)
    }
    private fun featuresCart(){
        //set checkbox
//        detailCartAdapter?.clickCheckBox = { total, state, product, qty ->
//            if (total == 0 && !state && totalMoney == 0) { // loại đc trườg hợp check rồi, sau đó bỏ check nhưg tổng tiền = 0 thì nó sẽ trừ tiếp -> âm
//                totalMoney = 0
//            }
//            if (state) {
//                totalMoney += total
//                if(!checkExist(product.id)){
//                    val productOrder = ProductOrder(product, qty, totalMoney)
//                    if(total>0) {
//                        listProductChoose?.add(productOrder)//thêm sp vào ds chờ. Để put qua order
//                    }
//                }else{
//                    for (i in listProductChoose!!) {
//                            if (i.product?.id == product.id) {
//                                i.total = totalMoney //Cập nhật lại số lượng
//                            }
//                        }
//                }
//
//            } else {
//                totalMoney -= total
//                val productOrder = ProductOrder(product, qty, totalMoney)
//
//                listProductChoose?.removeIf { n ->(n.product?.id == productOrder.product?.id) }//remove dùng removeIf để tránh ngoại lệ ConcurrentModificationException và "main" java.lang.IndexOutOfBoundsException
//            }
//            if(totalMoney>0){
//                bindingDetailCart?.btnOrder?.unEnabled()
//            }else{
//                bindingDetailCart?.btnOrder?.enabled()
//            }
//            bindingDetailCart?.tvTotalMoney?.text = totalMoney.toVND()
//        }
        detailCartAdapter?.clickCheckBox = { total, state, product, qty ->
            if(hasDiscount?:0 >0){
                totalMoney = totalMoneyPrevious
            }
            if (total == 0 && !state && totalMoney == 0) { // loại đc trườg hợp check rồi, sau đó bỏ check nhưg tổng tiền = 0 thì nó sẽ trừ tiếp -> âm
                totalMoney = 0
            }
            if (state) {
                totalMoney = totalMoney?.plus(total)
                if(!checkExist(product.id)){
                    val productOrder = ProductOrder(product, qty, totalMoney)
                    if(total>0) {
                        listProductChoose?.add(productOrder)//thêm sp vào ds chờ. Để put qua order
                    }
                }else{
                    for (i in listProductChoose!!) {
                        if (i.product?.id == product.id) {
                            i.total = totalMoney //Cập nhật lại số lượng
                        }
                    }
                }

            } else {
                totalMoney = totalMoney?.minus(total)
                val productOrder = ProductOrder(product, qty, totalMoney)
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
                            }
                        }
                    } else listProductChoose?.removeIf { n -> n.product?.id == product?.id }//số lượng về 0 thì xóa sp khỏi ds đã chọn
                }else {
                    val productOrder = ProductOrder(product, qty, product?.price)
                    listProductChoose?.add(productOrder)
                }

            }
        }
        detailCartAdapter?.updateItemCart = { id, it ->
            if(it == true){
                detailCartViewModel?.updateItem(id, "plus")
            }else detailCartViewModel?.updateItem(id, "min")
        }
        detailCartAdapter?.deleteItemCart = {
            detailCartViewModel?.deleteItem(it)
            bindingDetailCart?.progressBarDetailCart?.visible()
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
            for (i in listProduct!!) {
                if (listProduct?.get(position) == i){
                    if(position== listProduct?.size?.minus(1) ?: -1){
                        bindingDetailCart?.tvTotalMoney?.text = "0"
                        bindingDetailCart?.tvTotalPreDetail?.text = "0"
                        bindingDetailCart?.btnOrder?.enabled()
                    }
                    detailCartViewModel?.deleteItem(i.idProduct)
                }
            }
    }

    private fun updateTotalHasDiscount(){
        if(totalMoney?:0 >0){
            bindingDetailCart?.btnOrder?.unEnabled()
        }else{
            totalMoney = 0
            bindingDetailCart?.btnOrder?.enabled()
        }
        if(hasDiscount > 0){
            val priceDiscount = totalMoney?.div(hasDiscount)
            bindingDetailCart?.tvTotalPreDetail?.text = totalMoney.toVND()
            bindingDetailCart?.tvDiscountDetail?.text = priceDiscount.toVND()
            bindingDetailCart?.tvTotalMoney?.text = (totalMoney?.minus(priceDiscount!!)).toVND()
            totalMoneyPrevious = totalMoney
        }else{
            bindingDetailCart?.tvTotalMoney?.text = totalMoney.toVND()
            bindingDetailCart?.tvTotalPreDetail?.text = totalMoney.toVND()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        activity?.viewModelStore?.clear()
    }
}