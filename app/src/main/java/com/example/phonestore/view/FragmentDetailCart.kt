package com.example.phonestore.view

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.phonestore.Extension.gone
import com.example.phonestore.Extension.toVND
import com.example.phonestore.Extension.visible
import com.example.phonestore.base.BaseFragment
import com.example.phonestore.databinding.FragmentDetailCartBinding
import com.example.phonestore.databinding.FragmentDetailProductBinding
import com.example.phonestore.model.DetailCart
import com.example.phonestore.services.DetailProductAdapter
import com.example.phonestore.services.SwipeHelper
import com.example.phonestore.viewmodel.CartViewModel
import com.google.android.material.snackbar.Snackbar
import okhttp3.internal.addHeaderLenient

class FragmentDetailCart: BaseFragment() {
    private lateinit var bindingDetailCart: FragmentDetailCartBinding
    private lateinit var detailCartViewModel: CartViewModel
    private var detailCartAdapter: DetailProductAdapter<DetailCart>? = null
    private var listProduct: ArrayList<DetailCart>? = arrayListOf()
    private var totalMoney: Int = 0
    override fun setBinding(inflater: LayoutInflater, container: ViewGroup?): View {
        bindingDetailCart = FragmentDetailCartBinding.inflate(inflater, container, false)
        return bindingDetailCart.root
    }

    override fun setUI() {
        initRecyclerView()
        detailCartViewModel.getMyCart()
        //set checkbox
        detailCartAdapter?.clickCheckBox = { total, state ->
            if (total == 0 && !state && totalMoney == 0) { // loại đc trườg hợp check rồi, sau đó bỏ check nhưg tổng tiền = 0 thì nó sẽ trừ tiếp -> âm
                totalMoney = 0
            }
            if (state) {
                totalMoney += total
            } else totalMoney -= total
            bindingDetailCart.tvTotalMoney.text = totalMoney.toVND()
        }
        // set + -
        detailCartAdapter?.clickMaxMin = { price, state ->
            if (state) {
                totalMoney += price ?: 0
            } else totalMoney -= price ?: 0
            bindingDetailCart.tvTotalMoney.text = totalMoney.toVND()
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

    override fun setViewModel() {
        detailCartViewModel = ViewModelProvider(this@FragmentDetailCart).get(CartViewModel::class.java)
    }

    override fun setObserve() {
        val deleteItemObserve = Observer<Boolean>{
            if(it){
                view?.let {
                    it1 -> Snackbar.make(it1, "Đã xóa sản phẩm", Snackbar.LENGTH_SHORT).show()
                    detailCartViewModel.getTotalProduct()
                    detailCartViewModel.getMyCart()
                }
            }
        }
        detailCartViewModel.resultDeleteItem.observe(viewLifecycleOwner, deleteItemObserve)
        val totalProductObserver = Observer<Int?>{
            context?.let {
                it1 -> MainActivity.icon?.let {
                it2 -> MainActivity.setBadgeCount(it1, icon = it2, it.toString())
                }
            }
        }
        detailCartViewModel.totalProduct.observe(viewLifecycleOwner, totalProductObserver)
        val listProductObserve = Observer<ArrayList<DetailCart>?>{
            listProduct?.addAll(it)
            detailCartAdapter?.setItems(it)
            if(it.size==0){
                bindingDetailCart.tvNoProductInCart.visible()

            }else {
                bindingDetailCart.tvNoProductInCart.gone()
            }
        }
        detailCartViewModel.listProduct.observe(viewLifecycleOwner, listProductObserve)
    }
    private fun initRecyclerView(){
        detailCartAdapter = DetailProductAdapter(listProduct)
        bindingDetailCart.rvMyCart.adapter = detailCartAdapter
        bindingDetailCart.rvMyCart.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        bindingDetailCart.rvMyCart.layoutManager = LinearLayoutManager(context)
        val itemTouchHelper = ItemTouchHelper(object : SwipeHelper(bindingDetailCart.rvMyCart){
            override fun instantiateUnderlayButton(position: Int): List<UnderlayButton> {
                var buttons = listOf<UnderlayButton>()
                val deleteButton = deleteButton(position)
                buttons = listOf(deleteButton)
                return buttons
        }})
        itemTouchHelper.attachToRecyclerView(bindingDetailCart.rvMyCart)
    }
    private fun deleteButton(position: Int) : SwipeHelper.UnderlayButton{
        return SwipeHelper.UnderlayButton(
                    this.requireContext(),
                    "Delete",
                    14.0f,
                    android.R.color.holo_red_light,
                    this::handle

            )
    }
    private fun handle(position: Int){
            for (i in listProduct!!) {
                if (listProduct?.get(position) == i){
                    detailCartViewModel.deleteItem(i.idProduct)
                }
            }
    }
}