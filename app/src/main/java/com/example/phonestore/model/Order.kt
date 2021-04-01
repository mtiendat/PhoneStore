package com.example.phonestore.model

class Order(
        var totalMoney: Int? = 0,
        var listProduct: ArrayList<DetailCart> = arrayListOf()
)