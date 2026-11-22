package cloud.eheaver.domain.categories

import java.util.UUID

opaque type CategoryId   = UUID
opaque type CategoryName = String

case class Category(
    id: CategoryId,
    name: CategoryName
)

trait CategoriesAlg[F[_]] {}
