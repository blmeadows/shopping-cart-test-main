package com.example

import cats.effect.IO
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}
import org.http4s.circe.{jsonEncoderOf, jsonOf}
import org.http4s.{EntityDecoder, EntityEncoder}

final case class ProductInfo(title: String, price: BigDecimal)

object ProductInfo {
  implicit val decoder: Decoder[ProductInfo] = deriveDecoder[ProductInfo]
  implicit val encoder: Encoder[ProductInfo] = deriveEncoder[ProductInfo]
  implicit def entityDecoder: EntityDecoder[IO, ProductInfo] = jsonOf
  implicit def entityEncoder: EntityEncoder[IO, ProductInfo] = jsonEncoderOf
}

final case class ProductInfoError(e: Throwable) extends RuntimeException
