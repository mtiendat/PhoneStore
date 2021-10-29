package com.example.phonestore.view.cart

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.phonestore.R
import com.example.phonestore.databinding.FragmentMyVoucherBinding
import com.example.phonestore.extendsion.gone
import com.example.phonestore.extendsion.visible
import com.example.phonestore.services.Constant
import com.example.phonestore.services.adapter.SelectDiscountAdapter
import com.example.phonestore.viewmodel.CartViewModel
import com.google.android.material.snackbar.Snackbar


class FragmentMyVoucher: DialogFragment() {

    private lateinit var bindingMyVoucher: FragmentMyVoucherBinding
    private val cartViewModel: CartViewModel by activityViewModels()
    private lateinit var adapter: SelectDiscountAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindingMyVoucher = FragmentMyVoucherBinding.inflate(inflater, container, false)
        return bindingMyVoucher.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViewModel()
        setUI()
    }

    private fun setUI() {
        bindingMyVoucher.btnCloseDialog.setOnClickListener {
            dismiss()
        }
        adapter = SelectDiscountAdapter()
        adapter.itemClick  = {
            cartViewModel.voucher.value = it
            dismiss()
        }
        adapter.deleteItem = {
            context?.let { it1 -> alertDelete(it1, it) }
        }
        //bindingMyVoucher.rvMyDiscount.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        bindingMyVoucher.rvMyDiscount.layoutManager = LinearLayoutManager(context)
        bindingMyVoucher.rvMyDiscount.adapter = adapter
        cartViewModel.getMyVoucher()
    }

    private fun setViewModel(){
        cartViewModel.listMyVoucher.observe(viewLifecycleOwner, {
            if(it?.size == 0){
                bindingMyVoucher.groupVoucher.gone()
                bindingMyVoucher.groupNoVoucher.visible()
            }else {
                adapter.submitList(it)
                bindingMyVoucher.groupVoucher.visible()
            }
            bindingMyVoucher.pbMyVoucher.gone()
        })
        cartViewModel.deleteVoucher.observe(viewLifecycleOwner, {
            if(it!=null){
                if(it.status ==true){
                    cartViewModel.getMyVoucher()
                }
                view?.let {it1 -> Snackbar.make(it1, it?.message?:"", Snackbar.LENGTH_SHORT).show()}
            }
        })
    }
    override fun onStart() {
        super.onStart()
        //dialog full screen
        val window: Window? = dialog?.window
        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.MATCH_PARENT
        window?.setLayout(width, height)
    }

    private fun alertDelete(context: Context, id: Int?){
        val builder = AlertDialog.Builder(context)
        builder.setMessage(Constant.QUESTION_DELETE_VOUCHER)
        builder.setTitle(Constant.NOTIFICATION)
        builder.setCancelable(false)
        builder.setPositiveButton(Constant.YES) { _, _ ->
            cartViewModel.deleteVoucher(id)
        }
        builder.setNegativeButton(Constant.NO) { dialog, _ ->
            dialog.cancel()
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
        cartViewModel.deleteVoucher.value = null
    }
}