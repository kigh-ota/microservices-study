package org.acme

import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntity
import java.time.Instant
import javax.persistence.Entity

@Entity
class Third : PanacheEntity() {
    lateinit var correlationId: String
    lateinit var ctime: Instant
}
