package cloud.eheaver.domain.items

import cloud.eheaver.domain.brands.Brand
import cloud.eheaver.domain.categories.Category
import squants.market.Money

import java.util.UUID

opaque type ItemId          = UUID
opaque type ItemName        = String
opaque type ItemDescription = String

case class Item(
    id: ItemId,
    name: ItemName,
    description: ItemDescription,
    price: Money,
    brand: Brand,
    category: Category
)

case class CreateItem(
    name: ItemName,
    description: ItemDescription,
    price: Money,
    brand: Brand,
    category: Category
)

case class UpdateItem(
    id: ItemId,
    price: Money
)

trait ItemsAlg[F[_]] {
  def findAll: F[List[Item]]
  def findBy(brand: Brand): F[List[Item]]
  def findById(id: ItemId): F[Option[Item]]
  def create(item: CreateItem): F[ItemId]
  def updatedItem(item: UpdateItem): F[Unit]
}
