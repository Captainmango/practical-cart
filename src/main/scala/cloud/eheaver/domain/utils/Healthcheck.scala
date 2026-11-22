package cloud.eheaver.domain.utils

import io.circe.{Decoder, Encoder}
import monocle.Iso

enum Status {
  case Okay, Unreachable
}

object Status {
  val _Bool: Iso[Status, Boolean] =
    Iso[Status, Boolean] {
      case Status.Okay        => true
      case Status.Unreachable => false
    }(if _ then Okay else Unreachable)

  given Encoder[Status] =
    Encoder.forProduct1[Status, String]("status")(_.toString)
  given Decoder[Status] = Decoder.instance(_.get[String]("status")).emap { s =>
    Status.values.find(_.toString == s).toRight(s"Unknown status: $s")
  }
}

case class RedisStatus(value: Status)
object RedisStatus {
  given Encoder[RedisStatus] = Encoder[Status].contramap(_.value)
  given Decoder[RedisStatus] = Decoder[Status].map(RedisStatus(_))
}

case class PostgresStatus(value: Status)
object PostgresStatus {
  given Encoder[PostgresStatus] = Encoder[Status].contramap(_.value)
  given Decoder[PostgresStatus] = Decoder[Status].map(PostgresStatus(_))
}

trait Healthcheck[F[_]] {
  def status: F[Status]
}
