package cloud.eheaver.domain.brands

import java.util.UUID

opaque type BrandId   = UUID
opaque type BrandName = String

case class Brand(id: BrandId, name: BrandName)

trait BrandsAlg[F[_]] {
  def findAll: F[List[Brand]]
  def create(name: Brand): F[BrandId]
}
