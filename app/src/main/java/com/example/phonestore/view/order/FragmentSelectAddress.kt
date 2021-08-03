package com.example.phonestore.view.order

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.phonestore.R
import com.example.phonestore.base.BaseFragment
import com.example.phonestore.databinding.FragmentAddressSelectionBinding
import com.example.phonestore.extendsion.AppEvent
import com.example.phonestore.extendsion.gone
import com.example.phonestore.extendsion.visible
import com.example.phonestore.model.order.Address
import com.example.phonestore.services.Constant
import com.example.phonestore.services.Constant.ADDRESS
import com.example.phonestore.services.widget.SwipeHelper
import com.example.phonestore.services.adapter.SelectAddressAdapter
import com.example.phonestore.viewmodel.AddressViewModel
import com.google.android.material.snackbar.Snackbar

class FragmentSelectAddress: BaseFragment() {
    private var bindingSelectAddress: FragmentAddressSelectionBinding? = null
    private lateinit var adapter: SelectAddressAdapter
    private val viewModel: AddressViewModel by activityViewModels()
    private var listAddress: ArrayList<Address>? = null
    private var isDelete: Boolean = false
    private var position: Int = -1
    override fun setBinding(inflater: LayoutInflater, container: ViewGroup?): View? {
        bindingSelectAddress = FragmentAddressSelectionBinding.inflate(inflater, container, false)
        return bindingSelectAddress?.root
    }

    override fun setUI() {
        adapter = SelectAddressAdapter()
        adapter.itemClick = {
            NavHostFragment.findNavController(this).apply {
                previousBackStackEntry?.savedStateHandle?.set(ADDRESS, it)
                popBackStack()
            }
        }
        adapter.editClick = {
            findNavController().navigate(R.id.action_fragmentSelectAddress_to_fragmentEditAddress, bundleOf("address" to it, "isCreateOrEdit" to true))
        }
        bindingSelectAddress?.rvAddressSelection?.layoutManager = LinearLayoutManager(context)
        bindingSelectAddress?.rvAddressSelection?.adapter = adapter
        val itemTouchHelper =   bindingSelectAddress?.rvAddressSelection?.let {
            ItemTouchHelper(object : SwipeHelper(it) {
                override fun instantiateUnderlayButton(position: Int): List<UnderlayButton> {
                    val deleteButton = deleteButton(position)
                    return listOf(deleteButton)
                }
            })
        }
        itemTouchHelper?.attachToRecyclerView(bindingSelectAddress?.rvAddressSelection)
        viewModel.getListMyAddress()


    }

    override fun setEvents() {
        bindingSelectAddress?.btnAddNew?.setOnClickListener {
            if(listAddress?.size==0){
                findNavController().navigate(R.id.action_fragmentSelectAddress_to_fragmentEditAddress,
                    bundleOf("caseNew" to true ))
            } else  findNavController().navigate(R.id.action_fragmentSelectAddress_to_fragmentEditAddress)
        }
    }

    override fun setObserve() {
        viewModel.listMyAddress.observe(viewLifecycleOwner, {
            adapter.submitList(it)
            listAddress = it
            bindingSelectAddress?.btnAddNew?.visible()
            bindingSelectAddress?.progressBar3?.gone()
        })
        viewModel.message.observe(viewLifecycleOwner, {
            view?.let { it1 ->
                if (it != null) {
                    Snackbar.make(it1, it, Snackbar.LENGTH_SHORT).show()
                }
            }
            if(isDelete){
                AppEvent.notifyClosePopUp()
                val item = listAddress?.get(position)?.id
                listAddress?.removeIf{n ->n.id == item}
                adapter.notifyDataSetChanged()
            }

        })

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
        context?.let { alertDelete(it, listAddress?.get(position)?.id, position) }
    }

    private fun alertDelete(context: Context, id: Int?, position: Int){
        val builder = AlertDialog.Builder(context)
        builder.setMessage(Constant.QUESTION_DELETE_ADDRESS)
        builder.setTitle(Constant.NOTIFICATION)
        builder.setCancelable(false)
        builder.setPositiveButton(Constant.YES) { _, _ ->
            isDelete = true
            this.position = position
           viewModel.deleteMyAddress(id)
            AppEvent.notifyShowPopUp()
        }
        builder.setNegativeButton(Constant.NO) { dialog, _ ->
            dialog.cancel()
            adapter.notifyDataSetChanged()
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
}