package cloud.eheaver.domain.carts

import cloud.eheaver.domain.items.ItemId
import cloud.eheaver.domain.users.UserId
import squants.market.Money

import scala.util.control.NoStackTrace

opaque type Quantity = Int
opaque type Contents = Map[ItemId, Quantity]

case class CartItem(itemId: ItemId, quantity: Quantity)
case class CartTotal(items: List[CartItem], total: Money)

trait ShoppingCartAlg[F[_]] {
  def add(
      userId: UserId,
      itemId: ItemId,
      quantity: Quantity
  ): F[Unit]
  def get(userId: UserId): F[CartTotal]
  def delete(userId: UserId): F[Unit]
  def removeItem(userId: UserId, itemId: ItemId): F[Unit]
  def update(userId: UserId, contents: Contents): F[Unit]
}

case object EmptyCartError extends NoStackTrace
