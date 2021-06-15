package com.example.phonestore.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.phonestore.extendsion.toVND
import com.example.phonestore.model.*
import com.example.phonestore.model.technology.TechnologyResponse
import com.example.phonestore.repo.DetailProductRepo
import com.example.phonestore.repo.TakeTechnologyRepo

class DetailProductViewModel: ViewModel() {
    var detailProduct: MutableLiveData<DetailProduct>? = MutableLiveData()
    var relatedProduct: MutableLiveData<ArrayList<ProductInfo>>? = MutableLiveData()
    var compareProduct: MutableLiveData<ArrayList<ProductInfo>>? = MutableLiveData()
    var listVote: MutableLiveData<ArrayList<Vote>>? = MutableLiveData()
    var listColor: MutableLiveData<List<String>>? = MutableLiveData()
    var listStorage: MutableLiveData<List<String>>? = MutableLiveData()
    var listImageSlideshow: MutableLiveData<ArrayList<String>>? = MutableLiveData()
    var messageSuccess:  MutableLiveData<Boolean>? = MutableLiveData()
    var bought:  MutableLiveData<Boolean>? = MutableLiveData()
    var color: MutableLiveData<String>? = MutableLiveData()
    var priceOld: MutableLiveData<Int>? = MutableLiveData()
    var priceNew: MutableLiveData<String>? = MutableLiveData()

    var idProduct: MutableLiveData<Int>? = MutableLiveData()
    var technologyResponse: MutableLiveData<TechnologyResponse>? = MutableLiveData()
    private var messageError:  MutableLiveData<String>? = MutableLiveData()
    private var detailProductRepo = DetailProductRepo()
    private var technologyRepo = TakeTechnologyRepo()
    fun getListImageSlideshow(id: Int?){
        if(listImageSlideshow?.value?.size  == null) {
            detailProductRepo.callSlideshowProduct(
                id,
                this::onSuccessImageSlideShow,
                this::onError
            )
        }
    }
    fun getDetailProduct(id: Int? = 0){
        if(detailProduct?.value  == null) {
            detailProductRepo.callDetailProduct(id, this::onSuccessDetailProduct, this::onError)
        }
    }
    fun getColorOrStorageProduct(idCate: Int?, color: String, storage: String){
        detailProductRepo.callProductByColor(idCate, color, storage, this::onSuccessProductByColor, this::onError)
    }
    fun getRelatedProduct(idCate: Int? = 0){
        if(relatedProduct?.value?.size  == null) {
            detailProductRepo.callRelatedProduct(
                idCate,
                this::onSuccessListRelatedProduct,
                this::onError
            )
        }
    }
    fun getCompareProduct(price: Int?, idCate: Int?){
        if(compareProduct?.value?.size  == null) {
            detailProductRepo.callCompareProduct(
                idCate,
                price,
                this::onSuccessListCompareProduct,
                this::onError
            )
        }
    }
    fun getListVote(idCate: Int? = 0, all: Boolean? = false){
        detailProductRepo.callListVote(idCate, all, this::onSuccessListVote, this::onError)
    }
    fun postVote(idCate: Int? = 0, vote: Vote){
        detailProductRepo.postVote(idCate, vote, this::onSuccess, this::onError)
    }
    fun getTechnology(path: String){
        if(technologyResponse?.value  == null) {
            technologyRepo.getTechnology(
                path = path,
                onSuccess = this::onSuccessTechnology,
                onError = this::onError
            )
        }
    }

    private fun onSuccessImageSlideShow(listImage: ArrayList<String>?){
        listImageSlideshow?.value = listImage
    }

    private fun onSuccessDetailProduct(cate: DetailProduct?){
        detailProduct?.value = cate
        listColor?.value = cate?.listColor
        listStorage?.value = cate?.listStorage
    }
    private fun onSuccessTechnology(tech: TechnologyResponse?){
        technologyResponse?.value = tech
    }

    private fun onSuccessProductByColor(product: ArrayList<ProductInfo>?){
      if(product?.size!! >0) {
          this.color?.value = product[0].img
          this.priceNew?.value = (product[0].price.minus((product[0].price * product[0].discount))).toInt().toVND()
          this.priceOld?.value = product[0].price
          this.idProduct?.value =product[0].id
      }

    }
    private fun onSuccessListRelatedProduct(listRelated: ArrayList<ProductInfo>?){
        this.relatedProduct?.value = listRelated
    }
    private fun onSuccessListCompareProduct(listCompare: ArrayList<ProductInfo>?){
        this.compareProduct?.value = listCompare
    }
    private fun onSuccessListVote(r: VoteResponse?){
        this.listVote?.value = r?.listVote
        bought?.value = r?.message
    }
    private fun onSuccess(s: Boolean?){
        messageSuccess?.value = s

    }
    private fun onError(e: String?){
        messageError?.value = e
    }
}