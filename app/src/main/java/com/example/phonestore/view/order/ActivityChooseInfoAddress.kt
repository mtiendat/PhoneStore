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
        fun intentFor(context: Context, id: String? = "", isDistrict: Boolean = false): Intent =
            Intent(context, ActivityChooseInfoAddress::class.java).putExtra("id", id).putExtra("isDistrict", isDistrict)
    }
    private lateinit var binding: ActivityChooseInfoAddressBinding
    private var adapter: InfoAddressAdapter  = InfoAddressAdapter()
    private var viewModel: AddressViewModel? = null
    private var id: String? = ""
    private var isDistrict: Boolean = false
    override fun setBinding() {
        binding = ActivityChooseInfoAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)
        id = intent.getStringExtra("id")
        isDistrict = intent.getBooleanExtra("isDistrict", false)
    }

    override fun setViewModel() {
        viewModel = ViewModelProvider(this@ActivityChooseInfoAddress).get(AddressViewModel::class.java)
    }

    override fun setObserve() {
        viewModel?.listCity?.observe(this, {
            adapter.setItems(it)
            binding.progressBar2.gone()
        })
    }

    override fun setUI() {
        adapter.itemClick = {
            if(id.equals("")){
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
        if(id.equals("")){
            viewModel?.getCity()
        }else {
            if(isDistrict){
                viewModel?.getDistrict(id)
            }else viewModel?.getWard(id)
        }

    }

    override fun setToolBar() {
        if(id.equals("")){
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