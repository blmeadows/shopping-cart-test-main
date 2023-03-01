package com.example

import cats.effect.IO
import cats.syntax.all._
import org.http4s.Method.GET
import org.http4s.Uri
import org.http4s.client.Client
import org.http4s.client.dsl.Http4sClientDsl

trait ProductInfoService {
  def getProductInfo(productName: String): IO[ProductInfo]
}

object ProductInfoService {
  def apply(client: Client[IO]): ProductInfoService =
    new ProductInfoService {
      val dsl: Http4sClientDsl[IO] = new Http4sClientDsl[IO] {}
      import dsl._

      override def getProductInfo(productName: String): IO[ProductInfo] = {
          for {
            uri <- IO.fromEither(Uri.fromString(s"https://raw.githubusercontent.com/mattjanks16/shopping-cart-test-data/main/$productName.json"))
            productInfo <- client.expect[ProductInfo](GET(uri)).adaptError { case t => ProductInfoError(t) }
          } yield productInfo
      }

    }
}