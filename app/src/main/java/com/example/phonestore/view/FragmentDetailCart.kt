package com.example.phonestore.view

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.phonestore.extendsion.*
import com.example.phonestore.R
import com.example.phonestore.base.BaseFragment
import com.example.phonestore.databinding.FragmentDetailCartBinding
import com.example.phonestore.model.DetailCart
import com.example.phonestore.model.ProductOrder
import com.example.phonestore.services.Constant
import com.example.phonestore.services.DetailProductAdapter
import com.example.phonestore.services.SwipeHelper
import com.example.phonestore.viewmodel.CartViewModel
import com.google.android.material.snackbar.Snackbar
import com.jpardogo.android.googleprogressbar.library.FoldingCirclesDrawable

class FragmentDetailCart: BaseFragment() {
    private var bindingDetailCart: FragmentDetailCartBinding? = null
    private var detailCartViewModel: CartViewModel? = null
    private var detailCartAdapter: DetailProductAdapter<DetailCart>? = null
    private var listProduct: ArrayList<DetailCart>? = arrayListOf()
    private var listProductChoose: ArrayList<ProductOrder>? = arrayListOf()
    private var totalMoney: Int = 0
    private var flag: Int = 0
    override fun setBinding(inflater: LayoutInflater, container: ViewGroup?): View? {
        bindingDetailCart = FragmentDetailCartBinding.inflate(inflater, container, false)
        return bindingDetailCart?.root
    }

    override fun setUI() {
        bindingDetailCart?.progressBarDetailCart?.setIndeterminateDrawableTiled(
                FoldingCirclesDrawable.Builder(context).colors(resources.getIntArray(
                        R.array.google_colors)).build())
        initRecyclerView()
        if(flag==0) {
            detailCartViewModel?.getMyCart()
            flag++
        }
        bindingDetailCart?.btnOrder?.enabled()
        bindingDetailCart?.btnOrder?.setOnClickListener {
            val item = bundleOf("listProduct" to listProductChoose)
            it.findNavController().navigate(R.id.action_fragmentDetailCart_to_fragmentOrder, item)
        }


    }

    override fun setViewModel() {
        detailCartViewModel = ViewModelProvider(this@FragmentDetailCart).get(CartViewModel::class.java)
    }

    override fun setObserve() {
        val deleteItemObserve = Observer<Boolean>{
            if(it){
                view?.let { it1 -> Snackbar.make(it1, Constant.DELETED_PRODUCT, Snackbar.LENGTH_SHORT).show()
                    detailCartViewModel?.getTotalProduct()
                    detailCartViewModel?.getMyCart()
                }
            }
        }
        detailCartViewModel?.resultDeleteItem?.observe(viewLifecycleOwner, deleteItemObserve)
        val totalProductObserver = Observer<Int?>{
            context?.let { it1 -> MainActivity.icon?.let { it2 -> MainActivity.setBadgeCount(it1, icon = it2, it.toString())
                }
            }
        }
        detailCartViewModel?.totalProduct?.observe(viewLifecycleOwner, totalProductObserver)
        val listProductObserve = Observer<ArrayList<DetailCart>?>{
            if(it===null||it.size==0){//nếu k có sản phẩm nào trong giỏ hàng
                bindingDetailCart?.rvMyCart?.gone()
                bindingDetailCart?.tvNoProductInCart?.visible()
                bindingDetailCart?.progressBarDetailCart?.gone()

            }else {
                bindingDetailCart?.rvMyCart?.visible()
                listProduct?.addAll(it)
                detailCartAdapter?.setItems(it)
                bindingDetailCart?.tvNoProductInCart?.gone()
                bindingDetailCart?.progressBarDetailCart?.gone()
            }
        }
        detailCartViewModel?.listProduct?.observe(viewLifecycleOwner, listProductObserve)
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
        detailCartAdapter?.clickCheckBox = { total, state, product, qty ->

            if (total == 0 && !state && totalMoney == 0) { // loại đc trườg hợp check rồi, sau đó bỏ check nhưg tổng tiền = 0 thì nó sẽ trừ tiếp -> âm
                totalMoney = 0
            }
            if (state) {
                totalMoney += total
                val productOrder = ProductOrder(product, qty, totalMoney)
                listProductChoose?.add(productOrder)//thêm sp vào ds chờ. Để put qua order
            } else {
                totalMoney -= total
                val productOrder = ProductOrder(product, qty, totalMoney)

                listProductChoose?.removeIf { n ->(n.product?.id == productOrder.product?.id) }//remove dùng removeIf để tránh ngoại lệ ConcurrentModificationException và "main" java.lang.IndexOutOfBoundsException
            }
            if(totalMoney>0){
                bindingDetailCart?.btnOrder?.unEnabled()
            }else{
                bindingDetailCart?.btnOrder?.enabled()
            }
            bindingDetailCart?.tvTotalMoney?.text = totalMoney.toVND()
        }
        // set + -
        detailCartAdapter?.clickMaxMin = { price, state ->

            if (state) {
                totalMoney += price ?: 0
            } else totalMoney -= price ?: 0
            if(listProductChoose?.size!! > 0) {
                listProductChoose?.get(listProductChoose?.size!! - 1)?.total = totalMoney
            }
            bindingDetailCart?.tvTotalMoney?.text = totalMoney.toVND()
            if(totalMoney>0){
                bindingDetailCart?.btnOrder?.unEnabled()
            }else{
                bindingDetailCart?.btnOrder?.enabled()
            }
        }
        detailCartAdapter?.updateProductInList = { idProduct, qty ->
            if (listProduct != null) {
                for (i in listProduct!!) {
                    if (i.id == idProduct) {
                        i.qty = qty //Cập nhật lại số lượng
                    }
                }

            }
        }
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
                    detailCartViewModel?.deleteItem(i.idProduct)
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        totalMoney = 0
        listProductChoose = arrayListOf()
    }


}