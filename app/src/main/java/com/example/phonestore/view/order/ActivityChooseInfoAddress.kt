package com.example.phonestore.view.order

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.phonestore.base.BaseActivity
import com.example.phonestore.databinding.ActivityChooseInfoAddressBinding
import com.example.phonestore.extendsion.AppEvent
import com.example.phonestore.extendsion.gone
import com.example.phonestore.services.Constant
import com.example.phonestore.services.adapter.InfoAddressAdapter
import com.example.phonestore.viewmodel.AddressViewModel

class ActivityChooseInfoAddress: BaseActivity() {
    companion object{
        fun intentFor(context: Context, id: Int, isDistrict: Boolean = false): Intent =
            Intent(context, ActivityChooseInfoAddress::class.java).putExtra("id", id).putExtra("isDistrict", isDistrict)
    }
    private lateinit var binding: ActivityChooseInfoAddressBinding
    private var adapter: InfoAddressAdapter  = InfoAddressAdapter()
    private var viewModel: AddressViewModel? = null
    private var id: Int?= null
    private var isDistrict: Boolean = false
    override fun setBinding() {
        binding = ActivityChooseInfoAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)
        id = intent.getIntExtra("id", -1)
        isDistrict = intent.getBooleanExtra("isDistrict", false)
    }

    override fun setViewModel() {
        viewModel = ViewModelProvider(this@ActivityChooseInfoAddress).get(AddressViewModel::class.java)
    }

    override fun setObserve() {
        viewModel?.listLocation?.observe(this, {
            adapter.setItems(it)
            binding.progressBar2.gone()
        })
    }

    override fun setUI() {
        adapter.itemClick = {
            if(id == -1){
                val resultIntent = Intent()
                resultIntent.putExtra("data", it).putExtra("is", "city")
                setResult(RESULT_OK, resultIntent)
                finish()
            }else {
                if(isDistrict){
                    val resultIntent = Intent()
                    resultIntent.putExtra("data", it).putExtra("is", "district")
                    setResult(RESULT_OK, resultIntent)
                    finish()
                }else {
                    val resultIntent = Intent()
                    resultIntent.putExtra("data", it).putExtra("is", "ward")
                    setResult(RESULT_OK, resultIntent)
                    finish()
                }

            }

        }
        binding.rvAddress.layoutManager = LinearLayoutManager(this)
        binding.rvAddress.adapter = adapter
        if(id == -1){
            viewModel?.getCity()
        }else {
            if(isDistrict){
                viewModel?.getDistrict(id!!)
            }else viewModel?.getWard(id!!)
        }

    }

    override fun setToolBar() {
        if(id == -1){
            binding.toolbarAddress.toolbar.title = Constant.CITY
        }else {
            if(isDistrict){
                binding.toolbarAddress.toolbar.title = Constant.DISTRICT
            }else   binding.toolbarAddress.toolbar.title = Constant.WARD

        }
        setSupportActionBar( binding.toolbarAddress.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}