package com.example.firebasegroupapp6

class OrderDataClass(
    var products: Map<String, CartDataClass>? = null,
   var Customerid:String? ="",
    var TotalOrderedProduct: Long? =0,
    var OrderDate:String?="",
    var TotalOrderCost:String?="") {
}