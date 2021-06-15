package com.example.phonestore.view.auth

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.example.phonestore.base.BaseActivity
import com.example.phonestore.databinding.ActivitySignUpInputPhoneBinding
import com.example.phonestore.extendsion.AppEvent
import com.example.phonestore.services.Constant
import com.example.phonestore.viewmodel.UserViewModel
import java.util.regex.Pattern

class ActivitySignUpInputPhone: BaseActivity() {
    companion object{
        fun intentFor(context: Context): Intent =
            Intent(context, ActivitySignUpInputPhone::class.java)
    }
    private lateinit var binding: ActivitySignUpInputPhoneBinding
    private var viewModel: UserViewModel? = null
    override fun setBinding() {
        binding = ActivitySignUpInputPhoneBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun setUI() {
        binding.btnSignUpPhone.setOnClickListener {
            if (Pattern.compile("^(\\+84|0)+([3|5|7|8|9])+([0-9]{8})\$").matcher(binding.edtInputPhone.text!!).matches()){
                AppEvent.notifyShowPopUp()
                viewModel?.checkNumberPhone(binding.edtInputPhone.text.toString())
            }else binding.textInputPhone.error = Constant.PHONE_INVALID
        }
        binding.edtInputPhone.addTextChangedListener{
            binding.textInputPhone.error = null
        }
    }

    override fun setViewModel() {
        viewModel = ViewModelProvider(this@ActivitySignUpInputPhone).get(UserViewModel::class.java)
    }
    override fun setObserve() {
        viewModel?.loginResponse?.observe(this@ActivitySignUpInputPhone, {
            AppEvent.notifyClosePopUp()
        if(it?.status == true){
            startActivity(ActivitySignUpVerify.intentFor(this, binding.edtInputPhone.text.toString()))
        }else binding.tvLoginFail.text = it.messages
        })
    }
    override fun setToolBar() {
        binding.toolbarSignUpInputPhone.toolbar.title = "Đăng kí tài khoản"
        setSupportActionBar(binding.toolbarSignUpInputPhone.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }


}