package com.example.phonestore.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.phonestore.model.*
import com.example.phonestore.repo.ProductRepo

class ProductViewModel: ViewModel() {
    var listSlideshow: MutableLiveData<ArrayList<Slideshow>?> = MutableLiveData()
    var listProduct: MutableLiveData<ArrayList<ProductInfo>?> = MutableLiveData()
    var listFeaturedProduct: MutableLiveData<ArrayList<ProductInfo>?> = MutableLiveData()
    var listResultSearch: MutableLiveData<ArrayList<CateProductInfo>?> = MutableLiveData()
    var listSupplier: MutableLiveData<ArrayList<Supplier>?> = MutableLiveData()
    private var messageError:  MutableLiveData<String> = MutableLiveData()
    private var productRepo: ProductRepo = ProductRepo()
    fun getSlideShow(){
        if(listSlideshow.value?.size  == null) { //Nếu đã có dữ liệu thì không call api trong trường hợp back fragment
            productRepo.callSlideShow( this::onSuccessListSlideShow, this::onError)
        }
    }
    fun getListHotSaleProduct(){
        if(listProduct.value?.size  == null) { //Nếu đã có dữ liệu thì không call api trong trường hợp back fragment
            productRepo.callHotSaleProduct( this::onSuccessListHotSaleProduct, this::onError)
        }
    }
    fun getListSupplier(){
        if(listSupplier.value?.size  == null) {
            productRepo.callSupplier(this::onSuccessSupplier, this::onError)
        }
    }
    fun getFeaturedProduct(idSupplier: Int? = null){
        if(idSupplier == null) {
            if (listFeaturedProduct.value?.size == null ) {
                productRepo.callFeaturedProduct(this::onSuccessListCateProduct, this::onError)
            }
        }
        // else productRepo.getCateProductBySupplier(page, idSupplier, perPage, this::onSuccessListCateProduct, this::onError)
    }
//    fun getNewCateProductBySupplier(idSupplier: Int? = null){
//            if (listCateProduct.value?.size == null) {
//                productRepo.getNewCateProductBySupplier(idSupplier, this::onSuccessListCateProduct, this::onError)
//            }
//    }
//    fun getMoreListCateProduct(page: Int = 1, idSupplier: Int? = null, perPage: Int? =5){
//        if(idSupplier == null) {
//            if (listCateProduct.value?.size == null || page > 1) {
//                productRepo.callCateProduct(page, perPage, this::onSuccessListCateProduct, this::onError)
//            }
//        }else productRepo.getCateProductBySupplier(page, idSupplier, perPage, this::onSuccessListCateProduct, this::onError)
//    }
    fun searchName(q: String?){
        productRepo.searchName(q, this::onSuccessResultSearch, this::onError)
    }
    private fun onSuccessResultSearch(list: ArrayList<CateProductInfo>?){
        listResultSearch.value = list
    }
    private fun onSuccessListSlideShow(list:ArrayList<Slideshow>?){
        listSlideshow.value = list
    }
    private fun onSuccessListHotSaleProduct(list:ArrayList<ProductInfo>?){
        listProduct.value = list
    }
    private fun onSuccessListCateProduct(list: ArrayList<ProductInfo>?){
        this.listFeaturedProduct.value = list
    }


    private fun onSuccessSupplier(list: ArrayList<Supplier>?){
        this.listSupplier.value = list
    }
    private fun onError(e: String?){
        messageError.value = e
    }
}