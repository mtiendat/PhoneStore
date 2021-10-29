package com.example.phonestore.view.product

import android.content.Context
import android.content.Intent
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.phonestore.R
import com.example.phonestore.base.BaseActivity
import com.example.phonestore.databinding.ActivityCompareProductBinding
import com.example.phonestore.extendsion.gone
import com.example.phonestore.extendsion.visible
import com.example.phonestore.model.technology.Name
import com.example.phonestore.model.technology.Quality
import com.example.phonestore.model.technology.Technology
import com.example.phonestore.services.Constant

class ActivityCompareProduct: BaseActivity() {
    private lateinit var binding: ActivityCompareProductBinding
    private var technology1: Technology? = null
    private var technology2: Technology? = null
    private var iv1: String? =""
    private var iv2: String? =""
    companion object{
        fun intentFor(context: Context?, iv1: String?,  iv2: String?, technology1: Technology?, technology2: Technology?): Intent =
            Intent(context, ActivityCompareProduct::class.java).apply {
                putExtra("iv1", iv1)
                putExtra("iv2", iv2)
                putExtra("technology1", technology1)
                putExtra("technology2", technology2)
            }
    }
    override fun setBinding() {
        binding = ActivityCompareProductBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun setToolBar() {
        binding.toolbarCompare.toolbar.title = Constant.COMPARE
        setSupportActionBar( binding.toolbarCompare.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }
    override fun setUI() {
        iv1 = intent?.getStringExtra("iv1")
        iv2 = intent?.getStringExtra("iv2")
        technology1 =  intent?.getParcelableExtra("technology1")
        technology2 =  intent?.getParcelableExtra("technology2")
        setImage(iv1, binding.ivProduct1)
        setImage(iv2, binding.ivProduct2)
        setData()
    }

    private fun setImage(url: String?, imageView: ImageView){
            Glide.with(this)
                .load(url)
                .error(R.drawable.noimage)
                .into(imageView)
    }
    private fun setData(){
        binding.tvTechDesignDetail1.text = technology1?.design?.design
        binding.tvTechMaterialDetail1.text = technology1?.design?.material
        binding.tvTechSizeDetail1.text = technology1?.design?.size
        binding.tvTechWeightDetail1.text = technology1?.design?.weight
        binding.tvTechScreenDetail1.text = technology1?.screen?.technology
        binding.tvTechScreenPixelsDetail1.text = technology1?.screen?.pixel
        binding.tvTechScreenSizeDetail1.text = technology1?.screen?.size
        binding.tvTechScreenHzDetail1.text
        binding.tvTechScreenLightDetail1.text
        binding.tvTechScreenGlassDetail1.text = technology1?.screen?.glass
        binding.tvTechRearCameraPixelsDetail1.text = technology1?.rearCamera?.pixel
        binding.tvTechRearCameraVideoDetail1.text = convertListQualityToString(technology1?.rearCamera?.video)
        binding.tvTechRearCameraFlashDetail1.text = technology1?.rearCamera?.flash
        binding.tvTechRearCameraFeatureDetail1.text= convertListNameToString(technology1?.rearCamera?.feature)
        binding.tvTechFrontCameraPixelsDetail1.text = technology1?.frontCamera?.pixel
        binding.tvTechFrontCameraFeatureDetail1.text = technology1?.frontCamera?.pixel
        binding.tvTechFrontCameraFeatureDetail1.text = convertListNameToString(technology1?.frontCamera?.feature)
        binding.tvTechOSDetail1.text = technology1?.os_cpu?.name
        binding.tvTechOSCPUDetail1.text = technology1?.os_cpu?.cpu
        binding.tvTechOSCPUSpeedDetail1.text = technology1?.os_cpu?.speed
        binding.tvTechGPUDetail1.text = technology1?.os_cpu?.gpu
        binding.tvTechRamDetail1.text = technology1?.storage?.ram
        binding.tvTechStorageDetail1.text = technology1?.storage?.internal
        binding.tvTechStorageCanUseDetail1.text = technology1?.storage?.hasUse
        binding.tvTechMobileNetworkDetail1.text = technology1?.network?.mobileNetwork
        binding.tvTechSimDetail1.text = technology1?.network?.sim
        binding.tvTechWifiDetail1.text = convertListNameToString(technology1?.network?.wifi)
        binding.tvTechGPSDetail1.text= convertListNameToString(technology1?.network?.gps)
        binding.tvTechBluetoothDetail1.text = convertListNameToString(technology1?.network?.bluetooth)
        binding.tvTechChargeDetail1.text = technology1?.network?.portCharge
        binding.tvTechJackDetail1.text = technology1?.network?.jackPhone
        binding.tvTechOtherNetworkDetail1.text = convertListNameToString(technology1?.network?.ortherNetwork)
        binding.tvTechBatteryDetail1.text = technology1?.battery?.capacity
        binding.tvTechBatteryCategoryDetail1.text = technology1?.battery?.type
        binding.tvTechBatteryTechnologyDetail1.text = convertListNameToString(technology1?.battery?.technology)
        binding.tvTechMaxChargeDetail1.text
        binding.tvTechSecurityDetail1.text = convertListNameToString(technology1?.utilities?.security)
        binding.tvTechFeatureSpecialDetail1.text = convertListNameToString(technology1?.utilities?.featureOther)
        binding.tvTechWaterProofDetail1.text
        binding.tvTechRecordDetail1.text = technology1?.utilities?.record
        binding.tvTechWatchMovieDetail1.text = convertListNameToString(technology1?.utilities?.movie)
        binding.tvTechMusicDetail1.text = convertListNameToString(technology1?.utilities?.music)
        binding.tvTechDateDetail1.text = technology1?.date?.date

        binding.tvTechDesignDetail2.text = technology2?.design?.design
        binding.tvTechMaterialDetail2.text = technology2?.design?.material
        binding.tvTechSizeDetail2.text = technology2?.design?.size
        binding.tvTechWeightDetail2.text = technology2?.design?.weight
        binding.tvTechScreenDetail2.text = technology2?.screen?.technology
        binding.tvTechScreenPixelsDetail2.text = technology2?.screen?.pixel
        binding.tvTechScreenSizeDetail2.text = technology2?.screen?.size
        binding.tvTechScreenHzDetail2.text
        binding.tvTechScreenLightDetail2.text
        binding.tvTechScreenGlassDetail2.text = technology2?.screen?.glass
        binding.tvTechRearCameraPixelsDetail2.text = technology2?.rearCamera?.pixel
        binding.tvTechRearCameraVideoDetail2.text = convertListQualityToString(technology2?.rearCamera?.video)
        binding.tvTechRearCameraFlashDetail2.text = technology2?.rearCamera?.flash
        binding.tvTechRearCameraFeatureDetail2.text= convertListNameToString(technology2?.rearCamera?.feature)
        binding.tvTechFrontCameraPixelsDetail2.text = technology2?.frontCamera?.pixel
        binding.tvTechFrontCameraFeatureDetail2.text = technology2?.frontCamera?.pixel
        binding.tvTechFrontCameraFeatureDetail2.text = convertListNameToString(technology2?.frontCamera?.feature)
        binding.tvTechOSDetail2.text = technology2?.os_cpu?.name
        binding.tvTechOSCPUDetail2.text = technology2?.os_cpu?.cpu
        binding.tvTechOSCPUSpeedDetail2.text = technology2?.os_cpu?.speed
        binding.tvTechGPUDetail2.text = technology2?.os_cpu?.gpu
        binding.tvTechRamDetail2.text = technology2?.storage?.ram
        binding.tvTechStorageDetail2.text = technology2?.storage?.internal
        binding.tvTechStorageCanUseDetail2.text = technology2?.storage?.hasUse
        binding.tvTechMobileNetworkDetail2.text = technology2?.network?.mobileNetwork
        binding.tvTechSimDetail2.text = technology2?.network?.sim
        binding.tvTechWifiDetail2.text = convertListNameToString(technology2?.network?.wifi)
        binding.tvTechGPSDetail2.text= convertListNameToString(technology2?.network?.gps)
        binding.tvTechBluetoothDetail2.text = convertListNameToString(technology2?.network?.bluetooth)
        binding.tvTechChargeDetail2.text = technology2?.network?.portCharge
        binding.tvTechJackDetail2.text = technology2?.network?.jackPhone
        binding.tvTechOtherNetworkDetail2.text = convertListNameToString(technology2?.network?.ortherNetwork)
        binding.tvTechBatteryDetail2.text = technology2?.battery?.capacity
        binding.tvTechBatteryCategoryDetail2.text = technology2?.battery?.type
        binding.tvTechBatteryTechnologyDetail2.text = convertListNameToString(technology2?.battery?.technology)
        binding.tvTechMaxChargeDetail2.text
        binding.tvTechSecurityDetail2.text = convertListNameToString(technology2?.utilities?.security)
        binding.tvTechFeatureSpecialDetail2.text = convertListNameToString(technology2?.utilities?.featureOther)
        binding.tvTechWaterProofDetail2.text
        binding.tvTechRecordDetail2.text = technology2?.utilities?.record
        binding.tvTechWatchMovieDetail2.text = convertListNameToString(technology2?.utilities?.movie)
        binding.tvTechMusicDetail2.text = convertListNameToString(technology2?.utilities?.music)
        binding.tvTechDateDetail2.text = technology2?.date?.date
        binding.pbCompare.gone()
        binding.lnCompare.visible()
    }
    private fun convertListNameToString(list: List<Name>?): String{
        var result: String =""
        list?.forEach {
            result += if(it == list[list.size-1]){
                "${it.name}"
            }else "${it.name}\n"
        }
        return result
    }
    private fun convertListQualityToString(list: List<Quality>?): String{
        var result: String =""
        list?.forEach {
            result += if(it == list[list.size-1]){
                "${it.quality}"
            }else "${it.quality}\n"
        }
        return result
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}