package com.example.phonestore.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.phonestore.model.CateProductInfo
import com.example.phonestore.model.ProductInfo
import com.example.phonestore.model.Vote
import com.example.phonestore.model.VoteResponse
import com.example.phonestore.repo.DetailProductRepo

class DetailProductViewModel: ViewModel() {
    var cateProductByID: MutableLiveData<CateProductInfo>? = MutableLiveData()
    var relatedProduct: MutableLiveData<ArrayList<CateProductInfo>>? = MutableLiveData()
    var messageSuccess:  MutableLiveData<Boolean>? = MutableLiveData()
    var bought:  MutableLiveData<Boolean>? = MutableLiveData()
    private var messageError:  MutableLiveData<String>? = MutableLiveData()
    var color: MutableLiveData<String>? = MutableLiveData()
    var priceOld: MutableLiveData<Int>? = MutableLiveData()
    var priceNew: MutableLiveData<Int>? = MutableLiveData()
    var listColor: MutableLiveData<List<String>>? = MutableLiveData()
    var listStorage: MutableLiveData<List<String>>? = MutableLiveData()
    var listVote: MutableLiveData<ArrayList<Vote>>? = MutableLiveData()
    var idProduct: MutableLiveData<Int>? = MutableLiveData()
    private var detailProductRepo = DetailProductRepo()
    fun getListCateProductByID(id: Int? = 0){
        detailProductRepo.callCateProductByID(id, this::onSuccessListCateProductByID, this::onError)
    }
    fun getColorOrStorageProduct(idCate: Int?, color: String, storage: String){
        detailProductRepo.callProductByColor(idCate, color, storage, this::onSuccessProductByColor, this::onError)
    }
    fun getRelatedProduct(idCate: Int? = 0){
        detailProductRepo.callRelatedProduct(idCate, this::onSuccessListRelatedProduct, this::onError)
    }
    fun getListVote(idCate: Int? = 0, all: Boolean? = false){
        detailProductRepo.callListVote(idCate, all, this::onSuccessListVote, this::onError)
    }
    fun postVote(idCate: Int? = 0, vote: Vote){
        detailProductRepo.postVote(idCate, vote, this::onSuccess, this::onError)
    }
    private fun onSuccessListCateProductByID(cate: CateProductInfo?){
        cateProductByID?.value = cate
        listColor?.value = cate?.listColor
        listStorage?.value = cate?.listStorage

    }
    private fun onSuccessProductByColor(product: ArrayList<ProductInfo>?){
      if(product?.size!! >0) {
          this.color?.value = product[0].img
          this.priceNew?.value = product[0].priceNew
          this.priceOld?.value = product[0].priceOld
          this.idProduct?.value =product[0].id
      }

    }
    private fun onSuccessListRelatedProduct(listRelated: ArrayList<CateProductInfo>?){
        this.relatedProduct?.value = listRelated
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