package cloud.eheaver.domain.utils

import cats.effect.Temporal
import cats.syntax.semigroup.*
import cats.syntax.show.*
import cats.{Applicative, Show}
import org.typelevel.log4cats.Logger
import retry.RetryDetails.NextStep
import retry.RetryPolicies.*
import retry.{ResultHandler, RetryDetails, RetryPolicy, retryingOnErrors}

import scala.concurrent.duration.*

enum Retriable {
  case Orders, Payments
}

object Retriable {
  given Show[Retriable] = Show.show(_.toString.toLowerCase)
}

def retryPolicy[F[_]: Applicative]: RetryPolicy[F, Any] = {
  limitRetries[F](3) |+| exponentialBackoff[F](10.milliseconds)
}

trait Retry[F[_]] {
  def retry[A](
      policy: RetryPolicy[F, Throwable],
      retriable: Retriable
  )(
      fa: F[A]
  ): F[A]
}

object Retry {
  def apply[F[_]: {Retry}]: Retry[F] = summon[Retry[F]]

  given [F[_]: {Logger, Temporal}]: Retry[F] with
    def retry[A](
        policy: RetryPolicy[F, Throwable],
        retriable: Retriable
    )(
        fa: F[A]
    ): F[A] = {
      // Error handler helper
      def onError(e: Throwable, details: RetryDetails): F[Unit] =
        details.nextStepIfUnsuccessful match
          case NextStep.DelayAndRetry(_) =>
            Logger[F].error(
              s"Failed on ${retriable.show} with error ${e.toString}. We retried ${details.retriesSoFar} times."
            )
          case NextStep.GiveUp =>
            Logger[F].error(
              s"Giving up on ${retriable.show} with error ${e.toString} after ${details.retriesSoFar} retries."
            )

      retryingOnErrors(fa)(
        policy,
        ResultHandler.retryOnAllErrors[F, A](onError)
      )
    }
}
