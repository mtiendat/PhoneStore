package com.example.phonestore.services

import com.example.phonestore.model.User

object Constant {
    const val URL = "http://192.168.1.174:8000/api/"
    const val KEY_API_YOUTUBE= "AIzaSyDrW8Kp1XXdwddwzkg43LJ3MtJTs8jzuqk"
    var TOKEN: String? = ""
    var idUser: Int  = 0
    var user: User? = null
    const val VIEW_HOTSALE_PRODUCT = 0
    const val VIEW_LOGO_SUPPLIER = 1
    const val VIEW_CATEPRODUCT = 2
    const val VIEW_SHIMMER = 3
    const val VIEW_MYCART = 4
    const val VIEW_PRODUCT_ORDER = 5
    const val VIEW_MY_ORDER = 6
    const val VIEW_SEARCH_NAME = 7
    const val TITLE_COLOR = "M.Sắc"
    const val TITLE_STORAGE = "D.Lượng"
    const val PLEASE_CHOOSE_COLOR = "Bạn chưa chọn màu"
    const val PLEASE_CHOOSE_STORAGE ="Bạn chưa chọn dung lượng"
    const val RECEIVED = "Đã tiếp nhận"
    const val TRANSPORTED = "Đang giao hàng"
    const val DELIVERED = "Đã giao hàng"
    const val CANCEL = "Đã hủy"

}