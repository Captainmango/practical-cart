package cloud.eheaver.domain.users

import java.util.UUID

opaque type UserId            = UUID
opaque type Username          = String
opaque type EncryptedPassword = String
opaque type JwtToken          = String
opaque type Password          = String

case class User(userId: UserId, username: Username)
case class UserWithPassword(
    id: UserId,
    username: Username,
    password: EncryptedPassword
)

trait UsersAlg[F[_]] {
  def find(username: Username): F[Option[UserWithPassword]]
  def create(username: Username, password: EncryptedPassword): F[UserId]
}

trait AuthAlg[F[_]] {
  def login(username: Username, password: Password): F[JwtToken]
  def logout(token: JwtToken, username: Username): F[Unit]
}
