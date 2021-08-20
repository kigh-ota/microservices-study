1. first-service ([Javalin](https://javalin.io/), [Exposed](https://github.com/JetBrains/Exposed))
    - generates a correlation id
    - inserts a row to `First` table
    - calls second-service
    - fails and rollbacks with low probability
2. second-service ([Micronaut](https://micronaut.io/))
    - inserts a row to `Second` table
    - fails and rollbacks with low probability
3. third-service ([Quarkus](https://quarkus.io/))

Invariant condition: Tables `First`, `Second` and `Third` have the same number of corresponding
rows.
