package cloud.eheaver.domain.payments

import cloud.eheaver.domain.users.UserId
import squants.market.Money

import java.util.UUID

opaque type PaymentId = UUID

case class Card()
case class Payment(
    id: UserId,
    total: Money,
    card: Card
)

trait PaymentsClientAlg[F[_]] {
  def process(payment: Payment): F[PaymentId]
}
