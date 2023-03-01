package com.example

import cats.effect.{IO, Ref}

trait ShoppingCartService {
  def getShoppingCart: IO[ShoppingCart]
  def addToShoppingCart(productName: String, total: Int): IO[Unit]
}

class LiveShoppingCartService(
                               cartRef: Ref[IO, ShoppingCart],
                               productInfoService: ProductInfoService
                             ) extends ShoppingCartService {
  val taxRate = 0.125

  override def getShoppingCart: IO[ShoppingCart] =
    for {
      shoppingCart <- cartRef.get
    } yield shoppingCart

  override def addToShoppingCart(productName: String, total: Int): IO[Unit] =
    for {
      productInfo <- productInfoService.getProductInfo(productName)
      _ <- cartRef.update(cart => updateItems(cart, productInfo, total))
      _ <- cartRef.update(cart => updateTotals(cart))
    } yield ()

  def updateItems(cart: ShoppingCart, productInfo: ProductInfo, total: Int): ShoppingCart =
    cart.copy(items = cart.items.updatedWith(productInfo) {
      case Some(itemTotal) => Some(ShoppingCartItemTotal(itemTotal.numItems + total))
      case None => Some(ShoppingCartItemTotal(total))
    })

  def updateTotals(cart: ShoppingCart): ShoppingCart = {
    val subTotal = cart.items.foldLeft(BigDecimal(0).setScale(2, BigDecimal.RoundingMode.HALF_UP))((acc, f) => acc + (f._1.price * f._2.numItems))
    val tax = (subTotal * taxRate).setScale(2, BigDecimal.RoundingMode.HALF_UP)
    val total = (subTotal + tax).setScale(2, BigDecimal.RoundingMode.HALF_UP)
    cart.copy(totals = cart.totals.copy(subTotal = subTotal, tax = tax, total = total))
  }
}

object LiveShoppingCartService {
  def make(productInfoService: ProductInfoService): IO[ShoppingCartService] =
    Ref.of[IO, ShoppingCart](ShoppingCart(Map.empty, ShoppingCartTotals(0,0,0))).map(new LiveShoppingCartService(_, productInfoService))
}
