package com.example.phonestore.services

import com.example.phonestore.model.User

object Constant {
    const val URL = "http://192.168.1.176:8000/api/"
    const val KEY_API_YOUTUBE= "AIzaSyDrW8Kp1XXdwddwzkg43LJ3MtJTs8jzuqk"
    var TOKEN: String? = ""
    var idUser: Int  = 0
    var user: User? = null
    const val CHANNEL_ID = "successOrderNotification"
    const val NOTIFICATION_ID = 1
    const val VIEW_HOTSALE_PRODUCT = 0
    const val VIEW_LOGO_SUPPLIER = 1
    const val VIEW_CATEPRODUCT = 2
    const val VIEW_SHIMMER = 3
    const val VIEW_MYCART = 4
    const val VIEW_PRODUCT_ORDER = 5
    const val VIEW_MY_ORDER = 6
    const val VIEW_VOTE = 7
    const val REQUEST_ID = 7
    const val YES = "Có"
    const val NO = "Không"
    const val ALL ="Tất cả"
    const val RECEIVED = "Đã tiếp nhận"
    const val TRANSPORTED = "Đang giao hàng"
    const val DELIVERED = "Đã giao hàng"
    const val CANCEL = "Đã hủy"
    const val NOTIFICATION = "Thông báo"
    const val NEW_PRODUCT = "Sản phẩm mới"
    const val TITLE_COLOR = "M.Sắc"
    const val TITLE_STORAGE = "D.Lượng"
    const val PLEASE_CHOOSE_COLOR = "Bạn chưa chọn màu"
    const val PLEASE_CHOOSE_STORAGE ="Bạn chưa chọn dung lượng"
    const val PLEASE_CHOOSE_VOTE = "Bạn chưa chọn thang đánh giá"
    const val PLEASE_ACCEPT_PERMISSION = "Quyền Camera và Storage là cần thiết cho app hoạt động bình thường"
    const val SUCCESS_VOTED ="Đánh giá thành công"
    const val SUCCESS_ADD_TO_CART ="Thêm vào giỏ hàng thành công"
    const val SUCCESS_LOG_OUT ="Đăng xuất thành công"
    const val SUCCESS_UPDATE ="Cập nhật thành công"
    const val SUCCESS_CANCEL ="Hủy thành công"
    const val ERROR_VOTED ="Thất bại! Vui lòng thử lại sau..."

    const val CHANGE_PASSWORD_SUCCESS = "Đổi mật khẩu thành công"
    const val CONFIRM_PASSWORD_FAILURE = "Xác nhận password chưa đúng"
    const val FIND_ACCOUNT = "Tìm tài khoản"
    const val DELETED_PRODUCT = "Đã xóa sản phẩm"
    const val LOGIN_FAILURE = "Username or Password not valid"
    const val YOUTUBE_FAILURE = "Load trailer failure"
    const val VALIDATE_BUY_PRODUCT = "Bạn được mua tối đa 2 sản phẩm"
    const val VALIDATE_FULL_NAME = "Họ tên không được để trống"
    const val VALIDATE_EMAIL = "Email không được để trống"
    const val VALIDATE_PASSWORD = "Password không được để trống"
    const val VALIDATE_PHONE = "Sdt không được để trống"
    const val VALIDATE_ADDRESS = "Địa chỉ không được để trống"
    const val VALIDATE_VOTE = "Bạn chưa điền đánh giá"
    const val VALIDATE_CONFIRM_PASSWORD = "Xác nhận Password không được để trống"
    const val CONFIRM_PASSWORD_NOT_SAME = "Xác nhận password không đúng"
    const val VALIDATE_CHECKBOX_PRIVACY = "Vui lòng đồng ý với chính sách của chúng tôi"
    const val EMAIL_INVALID = "Email không đúng định dạng [xxx@xxxx.xx]"
    const val PHONE_INVALID = "Sdt không hợp lệ"
    const val QUESTION_CANCEL = "Bạn có chắc chắn muốn hủy không?"
}