package org.acme

import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntity
import java.time.Instant
import java.util.*
import javax.persistence.Entity

@Entity
class Third: PanacheEntity() {
    lateinit var correlationId: UUID
    lateinit var ctime: Instant
}
