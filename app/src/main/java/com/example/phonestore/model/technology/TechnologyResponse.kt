package com.example.phonestore.model.technology

import com.google.gson.annotations.SerializedName

class TechnologyResponse(
    @SerializedName("thong_so_ky_thuat")
    var technology: Technology,
    @SerializedName("thong_tin_khac")
    var dateRelease: DateRelease
)