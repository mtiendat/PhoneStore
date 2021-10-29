package com.example.phonestore.view.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.example.phonestore.databinding.FragmentDetailTechnologyBinding
import com.example.phonestore.model.technology.Name
import com.example.phonestore.model.technology.Quality
import com.example.phonestore.model.technology.Technology


class FragmentDetailTechnology: DialogFragment() {
    private var technology : Technology? = null
    private lateinit var binding: FragmentDetailTechnologyBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentDetailTechnologyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setData()
        binding.btnCloseDialog.setOnClickListener {
            dismiss()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        technology = arguments?.getParcelable("technology")
    }

    override fun onStart() {
        super.onStart()
        //dialog full screen
        val window: Window? = dialog?.window
        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.MATCH_PARENT
        window?.setLayout(width, height)
    }
    private fun setData(){
        binding.tvTechDesignDetail.text = technology?.design?.design
        binding.tvTechMaterialDetail.text = technology?.design?.material
        binding.tvTechSizeDetail.text = technology?.design?.size
        binding.tvTechWeightDetail.text = technology?.design?.weight
        binding.tvTechScreenDetail.text = technology?.screen?.technology
        binding.tvTechScreenPixelsDetail.text = technology?.screen?.pixel
        binding.tvTechScreenSizeDetail.text = technology?.screen?.size
        binding.tvTechScreenHzDetail.text
        binding.tvTechScreenLightDetail.text
        binding.tvTechScreenGlassDetail.text = technology?.screen?.glass
        binding.tvTechRearCameraPixelsDetail.text = technology?.rearCamera?.pixel
        binding.tvTechRearCameraVideoDetail.text = convertListQualityToString(technology?.rearCamera?.video)
        binding.tvTechRearCameraFlashDetail.text = technology?.rearCamera?.flash
        binding.tvTechRearCameraFeatureDetail.text= convertListNameToString(technology?.rearCamera?.feature)
        binding.tvTechFrontCameraPixelsDetail.text = technology?.frontCamera?.pixel
        binding.tvTechFrontCameraFeatureDetail.text = technology?.frontCamera?.pixel
        binding.tvTechFrontCameraFeatureDetail.text = convertListNameToString(technology?.frontCamera?.feature)
        binding.tvTechOSDetail.text = technology?.os_cpu?.name
        binding.tvTechOSCPUDetail.text = technology?.os_cpu?.cpu
        binding.tvTechOSCPUSpeedDetail.text = technology?.os_cpu?.speed
        binding.tvTechGPUDetail.text = technology?.os_cpu?.gpu
        binding.tvTechRamDetail.text = technology?.storage?.ram
        binding.tvTechStorageDetail.text = technology?.storage?.internal
        binding.tvTechStorageCanUseDetail.text = technology?.storage?.hasUse
        binding.tvTechMobileNetworkDetail.text = technology?.network?.mobileNetwork
        binding.tvTechSimDetail.text = technology?.network?.sim
        binding.tvTechWifiDetail.text = convertListNameToString(technology?.network?.wifi)
        binding.tvTechGPSDetail.text= convertListNameToString(technology?.network?.gps)
        binding.tvTechBluetoothDetail.text = convertListNameToString(technology?.network?.bluetooth)
        binding.tvTechChargeDetail.text = technology?.network?.portCharge
        binding.tvTechJackDetail.text = technology?.network?.jackPhone
        binding.tvTechOtherNetworkDetail.text = convertListNameToString(technology?.network?.ortherNetwork)
        binding.tvTechBatteryDetail.text = technology?.battery?.capacity
        binding.tvTechBatteryCategoryDetail.text = technology?.battery?.type
        binding.tvTechBatteryTechnologyDetail.text = convertListNameToString(technology?.battery?.technology)
        binding.tvTechMaxChargeDetail.text
        binding.tvTechSecurityDetail.text = convertListNameToString(technology?.utilities?.security)
        binding.tvTechFeatureSpecialDetail.text = convertListNameToString(technology?.utilities?.featureOther)
        binding.tvTechWaterProofDetail.text
        binding.tvTechRecordDetail.text = technology?.utilities?.record
        binding.tvTechWatchMovieDetail.text = convertListNameToString(technology?.utilities?.movie)
        binding.tvTechMusicDetail.text = convertListNameToString(technology?.utilities?.music)
        binding.tvTechDateDetail.text = technology?.date?.date
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
}