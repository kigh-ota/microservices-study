1. first-service ([Javalin](https://javalin.io/), [Exposed](https://github.com/JetBrains/Exposed))
   - inserts a row to `First` table
   - calls second-service
   - fails and rollbacks with low probability
2. second-service ([Micronaut](https://micronaut.io/))
   - inserts a row to `Second` table
   - fails and rollbacks with low probability

Invariant condition: `First` and `Second` tables have the same number of correspondong rows.

# Run MySQL

```console
docker run \
  -e MYSQL_ROOT_PASSWORD= \
  -e MYSQL_DATABASE=microservices-study \
  -e MYSQL_USER=user \
  -e MYSQL_PASSWORD=password \
  -e MYSQL_ALLOW_EMPTY_PASSWORD=yes \
  -p 3306:3306 \
  mysql:8
```
