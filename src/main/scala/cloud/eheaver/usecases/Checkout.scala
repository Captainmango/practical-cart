package cloud.eheaver.usecases

import cats.MonadThrow
import cats.data.NonEmptyList
import cats.implicits.catsSyntaxApplicativeError
import cats.implicits.toFlatMapOps
import cats.syntax.all._
import cats.syntax.all.catsSyntaxMonadError
import cloud.eheaver.domain.carts.CartItem
import cloud.eheaver.domain.carts.EmptyCartError
import cloud.eheaver.domain.carts.ShoppingCartAlg
import cloud.eheaver.domain.orders.OrderError
import cloud.eheaver.domain.orders.OrderId
import cloud.eheaver.domain.orders.OrdersAlg
import cloud.eheaver.domain.payments.Card
import cloud.eheaver.domain.payments.Payment
import cloud.eheaver.domain.payments.PaymentError
import cloud.eheaver.domain.payments.PaymentId
import cloud.eheaver.domain.payments.PaymentsClientAlg
import cloud.eheaver.domain.users.UserId
import cloud.eheaver.domain.utils.Background
import cloud.eheaver.domain.utils.Retriable.Orders
import cloud.eheaver.domain.utils.Retriable.Payments
import cloud.eheaver.domain.utils.Retry
import cloud.eheaver.domain.utils.retryPolicy
import org.typelevel.log4cats.Logger
import squants.market.Money

import scala.concurrent.duration._

final case class Checkout[F[_]: {MonadThrow, Logger, Retry, Background}](
    payments: PaymentsClientAlg[F],
    cart: ShoppingCartAlg[F],
    orders: OrdersAlg[F]
) {
  def process(userId: UserId, card: Card): F[OrderId] = {
    for {
      c   <- cart.get(userId)
      its <- ensureNonEmpty(c.items)
      pid <- processPayment(Payment(userId, c.total, card))
      oid <- placeOrder(userId, pid, its, c.total)
      _   <- cart.delete(userId)
    } yield oid
  }

  private def ensureNonEmpty[A](xs: List[A]): F[NonEmptyList[A]] = {
    MonadThrow[F].fromOption(
      NonEmptyList.fromList(xs),
      EmptyCartError
    )
  }

  private def processPayment(in: Payment): F[PaymentId] = {
    Retry[F]
      .retry(retryPolicy, Payments)(payments.process(in))
      .adaptError { case e =>
        PaymentError(
          Option(e.getMessage).getOrElse("Unknown")
        )
      }
  }

  private def placeOrder(
      userId: UserId,
      paymentId: PaymentId,
      items: NonEmptyList[CartItem],
      total: Money
  ): F[OrderId] = {
    def action(
        userId: UserId,
        paymentId: PaymentId,
        items: NonEmptyList[CartItem],
        total: Money
    ): F[OrderId] = Retry[F]
      .retry(retryPolicy, Orders)(
        orders.place(userId, paymentId, items, total)
      )
      .adaptError { case e =>
        OrderError(e.getMessage)
      }

    def backgroundJob(fa: F[OrderId]): F[OrderId] = fa.onError { case _ =>
      Logger[F].error(
        s"Failed to create order for payment: ${paymentId.toString}"
      ) *>
        Background[F].schedule(backgroundJob(fa), 1.hour)
    }

    backgroundJob(action(userId, paymentId, items, total))
  }
}
