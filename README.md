# practical-cart

An example application built from the [ce3.g8 template](https://github.com/typelevel/ce3.g8).

## Run application

```shell
sbt run
```

## Run tests

```shell
sbt test
```

## sbt-tpolecat

This template uses the `sbt-tpolecat` sbt plugin to set Scala compiler options to recommended defaults. If you want to change these defaults or find out about the different modes the plugin can operate in you can find out [here](https://github.com/typelevel/sbt-tpolecat/).

## Endpoints

• GET /brands
• POST /brands
• GET /categories
• POST /categories
• GET /items
• GET /items?brand={name}
• POST /items
• PUT /items
• GET /cart
• POST /cart
• PUT /cart
• DELETE /cart/{itemId}
• GET /orders
• GET /orders/{orderId}
• POST /checkout
• POST /auth/users
• POST /auth/login
• POST /auth/logout