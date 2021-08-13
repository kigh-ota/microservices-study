- Run MySQL

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
