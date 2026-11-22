package cloud.eheaver.domain.utils

import cats.effect.Temporal
import cats.effect.std.Supervisor

import scala.concurrent.duration.FiniteDuration
import cats.syntax.all.*

trait Background[F[_]] {
  def schedule[A](
      fa: F[A],
      duration: FiniteDuration
  ): F[Unit]
}

object Background {
  def apply[F[_]: Background]: Background[F] = summon[Background[F]]
  given [F[_]](using sup: Supervisor[F], tmp: Temporal[F]): Background[F] with {
    def schedule[A](
        fa: F[A],
        duration: FiniteDuration
    ): F[Unit] = {
      sup.supervise(tmp.sleep(duration) *> fa).void
    }
  }
}
