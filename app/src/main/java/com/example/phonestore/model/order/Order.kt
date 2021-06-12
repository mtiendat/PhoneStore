package com.example.phonestore.model.order

import com.example.phonestore.model.cart.DetailCart

class Order(
        var totalMoney: Int? = 0,
        var listProduct: ArrayList<DetailCart> = arrayListOf()
)