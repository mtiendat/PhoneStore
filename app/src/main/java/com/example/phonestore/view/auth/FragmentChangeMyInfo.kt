package com.example.phonestore.view.auth


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.phonestore.R
import com.example.phonestore.base.BaseFragment
import com.example.phonestore.databinding.FragmentChangeMyinfoBinding
import com.example.phonestore.services.Constant
import com.example.phonestore.viewmodel.UserViewModel
import com.google.android.material.snackbar.Snackbar

class FragmentChangeMyInfo: BaseFragment() {
    private var bindingChangeMyInfo: FragmentChangeMyinfoBinding? = null
    private var flag = 0
    private var data: String? =""
    private var userViewModel: UserViewModel? = null
    override fun setBinding(inflater: LayoutInflater, container: ViewGroup?): View? {
        bindingChangeMyInfo = FragmentChangeMyinfoBinding.inflate(inflater, container, false)
        return bindingChangeMyInfo?.root
    }

    override fun setUI() {
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>("key")?.observe(viewLifecycleOwner){
            data = it
            when(flag){
                1->{
                    userViewModel?.changeInfoUser(it, null, null)
                }
                2->{
                    userViewModel?.changeInfoUser(null, it, null)
                }
                3->{
                    userViewModel?.changeInfoUser(null, null, it)
                }
            } //Nhận dữ liệu từ bottomsheet trả về
        }
        bindingChangeMyInfo?.tvChangeName?.text = Constant.user?.name
        bindingChangeMyInfo?.tvChangePhone?.text = Constant.user?.phone
        setOnClickListener()

    }
    fun setOnClickListener(){
        bindingChangeMyInfo?.btnChangeName?.setOnClickListener {
            flag = 1
            it.findNavController().navigate(R.id.action_fragmentChangeMyInfo_to_bottomSheet, bundleOf("data" to Constant.user?.name, "title" to bindingChangeMyInfo?.btnChangeName?.text))
        }
        bindingChangeMyInfo?.btnChangePhone?.setOnClickListener {
            flag = 2
            it.findNavController().navigate(R.id.action_fragmentChangeMyInfo_to_bottomSheet, bundleOf("data" to Constant.user?.phone, "title" to bindingChangeMyInfo?.btnChangePhone?.text))
        }
        bindingChangeMyInfo?.btnChangeAddress?.setOnClickListener {
            flag = 3
            //it.findNavController().navigate(R.id.action_fragmentChangeMyInfo_to_bottomSheet, bundleOf("data" to Constant.user?.address, "title" to bindingChangeMyInfo?.btnChangeAddress?.text))
        }
    }
    override fun setViewModel() {
        userViewModel = ViewModelProvider(this@FragmentChangeMyInfo).get(UserViewModel::class.java)
    }
    override fun setObserve() {
        val changeObserve = Observer<Boolean>{
            if(it){
                when(flag){
                    1->bindingChangeMyInfo?.tvChangeName?.text = data
                    2->bindingChangeMyInfo?.tvChangePhone?.text = data
                    //3->Constant.user?.address = data
                }

            }else view?.let { it1 -> Snackbar.make(it1, "Lỗi, vui lòng thử lại sau...", Snackbar.LENGTH_SHORT).show() }
        }
        userViewModel?.status?.observe(viewLifecycleOwner, changeObserve)
    }

}