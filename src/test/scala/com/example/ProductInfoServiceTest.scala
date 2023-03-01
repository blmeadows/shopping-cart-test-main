package com.example

import munit.ClientSuite
import cats.effect.IO
import io.circe.syntax.EncoderOps
import org.http4s.circe._
import org.http4s.client.Client

class ProductInfoServiceTest extends ClientSuite {

  val client: Client[IO] = Client.from {
    case GET -> Root / "mattjanks16" / "shopping-cart-test-data" / "main" / "cheerios.json" => Ok(ProductInfo("cheerios", 8.43).asJson)
    case GET -> Root / "mattjanks16" / "shopping-cart-test-data" / "main" / "cornflakes.json" => Ok(ProductInfo("cornflakes", 2.52).asJson)
    case GET -> Root / "mattjanks16" / "shopping-cart-test-data" / "main" / "frosties.json" => Ok(ProductInfo("frosties", 4.99).asJson)
    case GET -> Root / "mattjanks16" / "shopping-cart-test-data" / "main" / "shreddies.json" => Ok(ProductInfo("shreddies", 4.68).asJson)
    case GET -> Root / "mattjanks16" / "shopping-cart-test-data" / "main" / "weetabix.json" => Ok(ProductInfo("weetabix", 9.98).asJson)
  }

  val productInfoService: ProductInfoService = ProductInfoService(client)

  test("get product info for cheerios successfully") {
    for {
      productInfoResult <- productInfoService.getProductInfo("cheerios")
    } yield assert(productInfoResult == ProductInfo("cheerios", 8.43))
  }

  test("fail when product info doesn't exist") {
    val productInfoResult = productInfoService.getProductInfo("bad")
    interceptIO[ProductInfoError](productInfoResult)
  }
}
