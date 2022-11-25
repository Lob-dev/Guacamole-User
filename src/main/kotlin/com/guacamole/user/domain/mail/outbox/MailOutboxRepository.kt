package com.guacamole.user.domain.mail.outbox

import com.guacamole.user.domain.mail.outbox.model.MailType
import com.guacamole.user.domain.mail.outbox.model.OutboxStatus
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface MailOutboxRepository : JpaRepository<MailOutbox, Long> {

    fun findFirstByTransactionAtLessThanAndOutboxStatusAndMailType(
        transactionAt: LocalDateTime,
        outboxStatus: OutboxStatus,
        mailType: MailType,
    ) : MailOutbox?
}
