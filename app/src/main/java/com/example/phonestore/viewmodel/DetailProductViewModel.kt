package com.example.phonestore.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.phonestore.extendsion.toVND
import com.example.phonestore.model.*
import com.example.phonestore.model.technology.TechnologyResponse
import com.example.phonestore.repo.DetailProductRepo
import com.example.phonestore.repo.TakeTechnologyRepo
import okhttp3.MultipartBody

class DetailProductViewModel: ViewModel() {
    var detailProduct: MutableLiveData<DetailProduct?>? = MutableLiveData()
    var relatedProduct: MutableLiveData<ArrayList<ProductInfo?>>? = MutableLiveData()
    var listProductNotComment: MutableLiveData<ArrayList<ProductInfo?>>? = MutableLiveData()
    var compareProduct: MutableLiveData<ArrayList<ProductInfo?>>? = MutableLiveData()
    var listComment: MutableLiveData<ArrayList<Comment>?>? = MutableLiveData()
    var listReply: MutableLiveData<ArrayList<Reply?>>? = MutableLiveData()
    var listColor: MutableLiveData<ArrayList<String?>?>? = MutableLiveData()
    var listStorage: MutableLiveData<ArrayList<String?>?>? = MutableLiveData()
    var listImageSlideshow: MutableLiveData<ArrayList<String?>?> = MutableLiveData()
    var statusComment:  MutableLiveData<PostCommentResponse>? = MutableLiveData()
    var statusDeleteComment:  MutableLiveData<ReplyResponse>? = MutableLiveData()
    var statusLike:  MutableLiveData<Boolean?>? = MutableLiveData()
    var statusReply:  MutableLiveData<Boolean?>? = MutableLiveData()
    var statusUpImage:  MutableLiveData<Boolean?>? = MutableLiveData()
    var statusAddToWishList:  MutableLiveData<Boolean?>? = MutableLiveData()
    var statusDeleteToWishList:  MutableLiveData<Boolean?>? = MutableLiveData()
    var wishListResponse:  MutableLiveData<WishListResponse?>? = MutableLiveData()
    var bought:  MutableLiveData<Boolean>? = MutableLiveData()
    var color: MutableLiveData<String>? = MutableLiveData()
    var priceOld: MutableLiveData<Int>? = MutableLiveData()
    var priceNew: MutableLiveData<Int>? = MutableLiveData()
    var checkComment: MutableLiveData<CheckCommentResponse?> = MutableLiveData()
    var idProduct: MutableLiveData<Int>? = MutableLiveData()
    var wish: MutableLiveData<Boolean>? = MutableLiveData()
    var technologyResponse: MutableLiveData<TechnologyResponse?>? = MutableLiveData()
    var technologyCompareResponse: MutableLiveData<TechnologyResponse>? = MutableLiveData()
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
    fun checkComment(idProduct: Int? = 0){
        detailProductRepo.callCheckComment(idProduct, this::onSuccessCheckComment, this::onError)
    }
    fun getListComment(idProduct: Int? = 0){
        detailProductRepo.callListComment(idProduct, this::onSuccessListComment, this::onError)
    }
    fun getListReply(idComment: Int? = 0){
        detailProductRepo.callListReply(idComment, this::onSuccessListReply, this::onError)
    }
    fun getListProductNotComment(listID: ArrayList<Int>?){
        detailProductRepo.callListProductNotComment(listID, this::onSuccessListProductNotComment, this::onError)
    }
    fun postComment(comment: Comment){
        detailProductRepo.postComment(comment, this::onSuccessComment, this::onError)
    }
    fun updateComment(idComment: Int?, comment: Comment){
        detailProductRepo.updateComment(idComment, comment, this::onSuccessComment, this::onError)
    }
    fun deleteComment(idComment: Int?){
        detailProductRepo.deleteComment(idComment, this::onSuccessDeleteComment, this::onError)
    }
    fun postImageComment(idComment: Int?, image: ArrayList<MultipartBody.Part>){
        detailProductRepo.postImageComment(idComment, image, this::onSuccessUpImage, this::onError)
    }
    fun updateImageNewComment(idComment: Int?, imageNew: ArrayList<MultipartBody.Part>? = arrayListOf()){
        detailProductRepo.updateImageNewComment(idComment, imageNew, this::onSuccessUpImage, this::onError)
    }
    fun updateImageOldComment(idComment: Int?, imageOld: ArrayList<Int>?= arrayListOf()){
        detailProductRepo.updateImageOldComment(idComment, imageOld, this::onSuccessUpImage, this::onError)
    }
    fun postReply(reply: Reply){
        detailProductRepo.postReply(reply, this::onSuccessReply, this::onError)
    }
    fun postLike(idComment: Int?){
        detailProductRepo.postLike(idComment, this::onSuccessLike, this::onError)
    }
    fun deleteLike(idComment: Int?){
        detailProductRepo.deleteLike(idComment, this::onSuccessLike, this::onError)
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
    fun getTechnologyCompare(path: String){
        technologyRepo.getTechnology(
                path = path,
                onSuccess = this::onSuccessTechnologyCompare,
                onError = this::onError
        )
    }
    fun addToWishList(idProduct: Int? = 0){
        detailProductRepo.addToWishList(idProduct, this::onSuccessWishListAdd, this::onError)
    }
    fun deleteToWishList(idProduct: Int? = 0){
        detailProductRepo.deleteWishList(idProduct, this::onSuccessWishListDelete, this::onError)
    }
    fun wishList(){
        detailProductRepo.wishList(this::onSuccessWishList, this::onError)
    }
    private fun onSuccessImageSlideShow(listImage: ArrayList<String?>?){
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
    private fun onSuccessTechnologyCompare(tech: TechnologyResponse?){
        technologyCompareResponse?.value = tech
    }

    private fun onSuccessProductByColor(product: ArrayList<ProductInfo?>?){
      if(product?.size?:-1 >0) {
          this.color?.value = product?.get(0)?.img
          this.priceNew?.value = (product?.get(0)?.price?.minus((product[0]?.price?.times(product[0]?.discount?:0f))?:0f))?.toInt()
          this.priceOld?.value = product?.get(0)?.price
          this.idProduct?.value = product?.get(0)?.id
          this.wish?.value = product?.get(0)?.wish
      }

    }
    private fun onSuccessListRelatedProduct(listRelated: ArrayList<ProductInfo?>?){
        this.relatedProduct?.value = listRelated
    }
    private fun onSuccessListCompareProduct(listCompare: ArrayList<ProductInfo?>?){
        this.compareProduct?.value = listCompare
    }
    private fun onSuccessListComment(r: CommentResponse?){
        this.listComment?.value = r?.listComment
        bought?.value = r?.status
    }
    private fun onSuccessListReply(r: ReplyResponse?){
        this.listReply?.value = r?.listReply
    }
    private fun onSuccessComment(p: PostCommentResponse?){
        statusComment?.value = p
    }
    private fun onSuccessDeleteComment(r: ReplyResponse?){
        statusDeleteComment?.value = r
    }
    private fun onSuccessUpImage(s: Boolean?){
        statusUpImage?.value = s
    }
    private fun onSuccessReply(s: Boolean?){
        statusReply?.value = s
    }
    private fun onSuccessLike(s: Boolean?){
        statusLike?.value = s
    }
    private fun onSuccessWishListDelete(d: WishListResponse?){
        statusDeleteToWishList?.value = d?.status
    }
    private fun onSuccessWishListAdd(a: WishListResponse?){
        statusAddToWishList?.value = a?.status
    }
    private fun onSuccessWishList(l: WishListResponse?){
        wishListResponse?.value = l
    }
    private fun onSuccessListProductNotComment(s: ArrayList<ProductInfo?>?){
        listProductNotComment?.value = s
    }
    private fun onSuccessCheckComment(c: CheckCommentResponse?){
        checkComment.value = c
    }
    private fun onError(e: String?){
        messageError?.value = e
    }
}