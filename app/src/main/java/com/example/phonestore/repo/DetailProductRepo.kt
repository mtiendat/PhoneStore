package com.example.phonestore.repo

import com.example.phonestore.model.*
import com.example.phonestore.services.APIRequest
import com.example.phonestore.services.APIServices
import com.example.phonestore.services.Constant
import okhttp3.MultipartBody

class DetailProductRepo {
    fun callSlideshowProduct(id: Int?, onSuccess: (ArrayList<String?>?) -> Unit, onError: (String?)->Unit){
        APIRequest.callRequest(
            call = APIServices.getInstance()?.getSlideshowProduct(id),
            onSuccess = {onSuccess.invoke(it?.listImageSlideshow)},
            onError = {onError.invoke(it)}
        )
    }

    fun callDetailProduct(id: Int? = 0, onSuccess: (DetailProduct?) -> Unit, onError: (String?)->Unit){
        APIRequest.callRequest(
            call = APIServices.getInstance()?.getDetailProduct(id, Constant.idUser),
            onSuccess = {results -> onSuccess.invoke(results?.detailProduct)},
            onError = {message -> onError.invoke(message)}
        )
    }
    fun callProductByColor(idCate: Int? = 0, color: String? = "", storage: String? = "", onSuccess: (ArrayList<ProductInfo?>?) -> Unit, onError: (String?)->Unit){
        APIRequest.callRequest(
            call = APIServices.getInstance()?.getProductByColor(idCate, color, storage, Constant.idUser),
            onSuccess = {results -> onSuccess.invoke(results?.listProduct)},
            onError = {message -> onError.invoke(message)}
        )
    }
    fun callRelatedProduct(idCate: Int? = 0, onSuccess: (ArrayList<ProductInfo?>?) -> Unit, onError: (String?)->Unit){
        APIRequest.callRequest(
            call = APIServices.getInstance()?.getRelatedProduct(idCate),
            onSuccess = {results -> onSuccess.invoke(results?.listProduct)},
            onError = {message -> onError.invoke(message)}
        )
    }
    fun callCompareProduct(price: Int?, idCate: Int?, onSuccess: (ArrayList<ProductInfo?>?) -> Unit, onError: (String?)->Unit){
        APIRequest.callRequest(
            call = APIServices.getInstance()?.getCompareProduct(idCate, price),
            onSuccess = {results -> onSuccess.invoke(results?.listProduct)},
            onError = {message -> onError.invoke(message)}
        )
    }
    fun callListProductNotComment(listID: ArrayList<Int>?, onSuccess: (ArrayList<ProductInfo?>?) -> Unit, onError: (String?)->Unit){
        APIRequest.callRequest(
            call = APIServices.getInstance()?.getListProductNotComment(ParamListID(listID)),
            onSuccess = {results -> onSuccess.invoke(results?.listProduct)},
            onError = {message -> onError.invoke(message)}
        )
    }
    fun callCheckComment(idProduct: Int?, onSuccess: (CheckCommentResponse?) -> Unit, onError: (String?)->Unit){
        APIRequest.callRequest(
            call = APIServices.getInstance()?.checkComment(Constant.idUser, idProduct),
            onSuccess = {results -> onSuccess.invoke(results)},
            onError = {message -> onError.invoke(message)}
        )
    }
    fun callListComment(idProduct: Int? = 0, onSuccess: (CommentResponse?) -> Unit, onError: (String?)->Unit){
        APIRequest.callRequest(
            call = APIServices.getInstance()?.getListComment(idProduct, Constant.idUser),
            onSuccess = {results -> onSuccess.invoke(results)},
            onError = {message -> onError.invoke(message)}
        )
    }
    fun callListReply(idComment: Int? = 0, onSuccess: (ReplyResponse?) -> Unit, onError: (String?)->Unit){
        APIRequest.callRequest(
            call = APIServices.getInstance()?.getListReply(idComment),
            onSuccess = {results -> onSuccess.invoke(results)},
            onError = {message -> onError.invoke(message)}
        )
    }
    fun postComment(comment: Comment, onSuccess: (PostCommentResponse?) -> Unit, onError: (String?)->Unit){
        APIRequest.callRequest(
            call = APIServices.getInstance()?.postComment(comment),
            onSuccess = {results -> onSuccess.invoke(results)},
            onError = {message -> onError.invoke(message)}
        )
    }
    fun updateComment(idComment: Int?, comment: Comment, onSuccess: (PostCommentResponse?) -> Unit, onError: (String?)->Unit){
        APIRequest.callRequest(
            call = APIServices.getInstance()?.updateComment(idComment, comment),
            onSuccess = {results -> onSuccess.invoke(results)},
            onError = {message -> onError.invoke(message)}
        )
    }
    fun deleteComment(idComment: Int?, onSuccess: (ReplyResponse?) -> Unit, onError: (String?)->Unit){
        APIRequest.callRequest(
            call = APIServices.getInstance()?.deleteComment(idComment),
            onSuccess = {results -> onSuccess.invoke(results)},
            onError = {message -> onError.invoke(message)}
        )
    }
    fun postImageComment(idComment: Int?, image: ArrayList<MultipartBody.Part>, onSuccess: (Boolean?) -> Unit, onError: (String?)->Unit){
        APIRequest.callRequest(
            call = APIServices.getInstance()?.postImageComment(idComment, image),
            onSuccess = {results -> onSuccess.invoke(results?.status)},
            onError = {message -> onError.invoke(message)}
        )
    }
    fun updateImageNewComment(idComment: Int?, imageNew: ArrayList<MultipartBody.Part>? = arrayListOf(), onSuccess: (Boolean?) -> Unit, onError: (String?)->Unit){
        APIRequest.callRequest(
            call = APIServices.getInstance()?.updateImageNewComment(idComment, imageNew),
            onSuccess = {results -> onSuccess.invoke(results?.status)},
            onError = {message -> onError.invoke(message)}
        )
    }
    fun updateImageOldComment(idComment: Int?, imageOld: ArrayList<Int>? = arrayListOf(), onSuccess: (Boolean?) -> Unit, onError: (String?)->Unit){
        APIRequest.callRequest(
            call = APIServices.getInstance()?.updateImageOldComment(idComment, UpdateImageModel(listID = imageOld)),
            onSuccess = {results -> onSuccess.invoke(results?.status)},
            onError = {message -> onError.invoke(message)}
        )
    }
    fun postReply(reply: Reply, onSuccess: (Boolean?) -> Unit, onError: (String?)->Unit){
        APIRequest.callRequest(
            call = APIServices.getInstance()?.postReply(reply),
            onSuccess = {results -> onSuccess.invoke(results?.status)},
            onError = {message -> onError.invoke(message)}
        )
    }
    fun postLike(idComment: Int?, onSuccess: (Boolean?) -> Unit, onError: (String?)->Unit){
        APIRequest.callRequest(
            call = APIServices.getInstance()?.postLike(idComment, Constant.idUser),
            onSuccess = {results -> onSuccess.invoke(results?.status)},
            onError = {message -> onError.invoke(message)}
        )
    }
    fun deleteLike(idComment: Int?, onSuccess: (Boolean?) -> Unit, onError: (String?)->Unit){
        APIRequest.callRequest(
            call = APIServices.getInstance()?.deleteLike(idComment, Constant.idUser),
            onSuccess = {results -> onSuccess.invoke(results?.status)},
            onError = {message -> onError.invoke(message)}
        )
    }
    fun addToWishList(idProduct: Int?, onSuccess: (WishListResponse?) -> Unit, onError: (String?)->Unit){
        APIRequest.callRequest(
            call = APIServices.getInstance()?.addToWishList(Constant.idUser, idProduct),
            onSuccess = {results -> onSuccess.invoke(results)},
            onError = {message -> onError.invoke(message)}
        )
    }
    fun deleteWishList(idProduct: Int?, onSuccess: (WishListResponse?) -> Unit, onError: (String?)->Unit){
        APIRequest.callRequest(
            call = APIServices.getInstance()?.deleteWishList(Constant.idUser, idProduct),
            onSuccess = {results -> onSuccess.invoke(results)},
            onError = {message -> onError.invoke(message)}
        )
    }
    fun wishList(onSuccess: (WishListResponse?) -> Unit, onError: (String?)->Unit){
        APIRequest.callRequest(
            call = APIServices.getInstance()?.wishList(Constant.idUser),
            onSuccess = {results -> onSuccess.invoke(results)},
            onError = {message -> onError.invoke(message)}
        )
    }
}