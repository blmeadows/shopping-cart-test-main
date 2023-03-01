package com.example

import cats.effect.IO
import io.circe.syntax.EncoderOps
import org.http4s.circe._
import org.http4s.client.Client

class ShoppingCartServiceTest extends munit.ClientSuite {

  val client: Client[IO] = Client.from {
    case GET -> Root / "mattjanks16" / "shopping-cart-test-data" / "main" / "cheerios.json" => Ok(ProductInfo("cheerios", 8.43).asJson)
    case GET -> Root / "mattjanks16" / "shopping-cart-test-data" / "main" / "cornflakes.json" => Ok(ProductInfo("cornflakes", 2.52).asJson)
    case GET -> Root / "mattjanks16" / "shopping-cart-test-data" / "main" / "frosties.json" => Ok(ProductInfo("frosties", 4.99).asJson)
    case GET -> Root / "mattjanks16" / "shopping-cart-test-data" / "main" / "shreddies.json" => Ok(ProductInfo("shreddies", 4.68).asJson)
    case GET -> Root / "mattjanks16" / "shopping-cart-test-data" / "main" / "weetabix.json" => Ok(ProductInfo("weetabix", 9.98).asJson)
  }

  val productInfoService: ProductInfoService = ProductInfoService(client)

  test("get shopping cart successfully") {
    for {
      shoppingCartService <- LiveShoppingCartService.make(productInfoService)
      shoppingCart <- shoppingCartService.getShoppingCart
    } yield assert(shoppingCart == ShoppingCart(Map.empty, ShoppingCartTotals(0,0,0)))
  }

  test("add one item to shopping cart successfully") {
    for {
      shoppingCartService <- LiveShoppingCartService.make(productInfoService)
      _ <- shoppingCartService.addToShoppingCart("cheerios", 1)
      shoppingCart <- shoppingCartService.getShoppingCart
    } yield assert(shoppingCart == ShoppingCart(Map(ProductInfo("cheerios", 8.43) -> ShoppingCartItemTotal(1)), ShoppingCartTotals(8.43, 1.05, 9.48)))
  }

  test("add multiple items to shopping cart successfully") {
    for {
      shoppingCartService <- LiveShoppingCartService.make(productInfoService)
      _ <- shoppingCartService.addToShoppingCart("cornflakes", 2)
      _ <- shoppingCartService.addToShoppingCart("weetabix", 1)
      shoppingCart <- shoppingCartService.getShoppingCart
    } yield assert(shoppingCart == ShoppingCart(
      Map(
        ProductInfo("cornflakes", 2.52) -> ShoppingCartItemTotal(2),
        ProductInfo("weetabix", 9.98) -> ShoppingCartItemTotal(1)
      ),
      ShoppingCartTotals(15.02, 1.88, 16.90)
    ))
  }
}
