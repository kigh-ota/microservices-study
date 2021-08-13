package example

import io.micronaut.data.annotation.GeneratedValue
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity
import io.micronaut.data.annotation.TypeDef
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.DataType
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.CrudRepository
import java.time.LocalDateTime
import java.util.*

@JdbcRepository(dialect = Dialect.MYSQL)
interface SecondRepository : CrudRepository<Second, Long> {
}

@MappedEntity(value = "Second")
data class Second(@field:Id @field:GeneratedValue val id: Long,
                  val uuid: UUID,
                  val firstUuid: UUID,
                  val ctime: LocalDateTime)
