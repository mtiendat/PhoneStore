package com.example.phonestore.repo

import android.graphics.Color
import com.example.phonestore.model.*
import com.example.phonestore.services.APIRequest
import com.example.phonestore.services.APIServices
import com.example.phonestore.services.Constant

class ProductRepo {
    fun callSlideShow( onSuccess: (ArrayList<Slideshow>?) -> Unit, onError: (String?)->Unit){
        APIRequest.callRequest(
            call = APIServices.getInstance()?.getSlideshow(),
            onSuccess = {results -> results?.listSlideshow?.let { onSuccess.invoke(it) } },
            onError = {message -> onError.invoke(message)}
        )
    }
    fun callHotSaleProduct(onSuccess: (ArrayList<ProductInfo?>?) -> Unit, onError: (String?)->Unit){
        APIRequest.callRequest(
            call = APIServices.getInstance()?.getTopProduct(),
            onSuccess = {results -> results?.listProduct?.let { onSuccess.invoke(it) } },
            onError = {message -> onError.invoke(message)}
        )
    }
    fun callSupplier(onSuccess: (ArrayList<Supplier?>?) -> Unit, onError: (String?)->Unit){
        APIRequest.callRequest(
            call = APIServices.getInstance()?.getSupplier(),
            onSuccess = {results -> results?.listSupplier?.let { onSuccess.invoke(it) } },
            onError = {message -> onError.invoke(message)}
        )
    }
    fun callFeaturedProduct(onSuccess: (ArrayList<ProductInfo?>?) -> Unit, onError: (String?)->Unit){
        APIRequest.callRequest(
            call = APIServices.getInstance()?.getFeaturedProduct(),
            onSuccess = {results -> results?.listProduct?.let { onSuccess.invoke(it) } },
            onError = {message -> onError.invoke(message)}
        )
    }

    fun callCheckQtyProductInWareHouse(color: String?, storage: String?, onSuccess: (QtyResponse) -> Unit, onError: (String?)->Unit){
        APIRequest.callRequest(
            call = APIServices.getInstance()?.checkQtyProductInWareHouse(color, storage),
            onSuccess = {results -> results?.let { onSuccess.invoke(it) } },
            onError = {message -> onError.invoke(message)}
        )
    }
    fun callCheckQtyProductByColorStorage(id: Int, color: String?, storage: String?, onSuccess: (ProductInfoCart?) -> Unit, onError: (String?)->Unit){
        APIRequest.callRequest(
            call = APIServices.getInstance()?.checkQtyProductByColorStorage(id, color, storage),
            onSuccess = {results -> results?.product?.let { onSuccess.invoke(it) } },
            onError = {message -> onError.invoke(message)}
        )
    }
    fun searchName(q: String? ="", onSuccess: (ArrayList<ProductInfo?>?) -> Unit, onError: (String?) -> Unit){
        APIRequest.callRequest(
            call = APIServices.getInstance()?.searchName(q),
            onSuccess = {results ->results?.listSearch?.let {onSuccess.invoke(it) } },
            onError = {message -> onError.invoke(message)}
        )
    }

    fun callInfoProduct(image: String?, storage: String?, onSuccess: (ProductInfo) -> Unit, onError: (String?)->Unit){
        APIRequest.callRequest(
            call = APIServices.getInstance()?.getInfoProduct(storage, image),
            onSuccess = {results -> results?.product?.let { onSuccess.invoke(it) } },
            onError = {message -> onError.invoke(message)}
        )
    }

    fun checkWarranty(imei: String?, onSuccess: (WarrantyResponse?) -> Unit, onError: (String?) -> Unit){
        APIRequest.callRequest(
            call = APIServices.getInstance()?.checkWarranty(imei),
            onSuccess = {results ->results?.let {onSuccess.invoke(it)} },
            onError = {message -> onError.invoke(message)}
        )
    }
}