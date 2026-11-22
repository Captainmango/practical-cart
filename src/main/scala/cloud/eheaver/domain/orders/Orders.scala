package cloud.eheaver.domain.orders

import cats.data.NonEmptyList
import cloud.eheaver.domain.carts.CartItem
import cloud.eheaver.domain.payments.PaymentId
import cloud.eheaver.domain.users.UserId
import squants.market.Money

import java.util.UUID
import scala.util.control.NoStackTrace

opaque type OrderId = UUID

trait OrdersAlg[F[_]] {
  def place(
      userId: UserId,
      paymentId: PaymentId,
      items: NonEmptyList[CartItem],
      total: Money
  ): F[OrderId]
}

case class OrderError(msg: String) extends NoStackTrace
