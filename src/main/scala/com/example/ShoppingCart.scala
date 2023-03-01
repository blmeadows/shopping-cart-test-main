package com.example

final case class ShoppingCartTotals(subTotal: BigDecimal, tax: BigDecimal, total: BigDecimal)
final case class ShoppingCartItemTotal(numItems: Int)
final case class ShoppingCart(items: Map[ProductInfo, ShoppingCartItemTotal], totals: ShoppingCartTotals)
