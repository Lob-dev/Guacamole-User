package com.guacamole.user.domain.mail.outbox

import com.guacamole.user.domain.mail.outbox.model.MailType
import com.guacamole.user.domain.mail.outbox.model.OutboxStatus
import java.time.LocalDateTime
import java.time.ZoneId
import javax.persistence.*

/**
 * @property payload
 * @sample { "user_id": 1, "email": "test@hgoogle.com", "auth_code": "sampleCode" }
 * @param user_id : User Identity
 * @param email : User Email
 * @param auth_code : Generated Token By AuthorizationCodeManager
 */
@Entity
@Table(
    name = "mail_outbox",
    indexes = [
        Index(
            name = "IDX_TRANSACTION_AT_OUTBOX_STATUS_MAIL_TYPE",
            columnList = "transactionAt, outboxStatus, mailType",
        )]
)
class MailOutbox(
    @Enumerated(EnumType.STRING)
    var outboxStatus: OutboxStatus,
    @Enumerated(EnumType.STRING)
    val mailType: MailType,
    val transactionAt: LocalDateTime,
    val payload: String,
    var publishAt: LocalDateTime? = null,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
) {

    fun complete(publishAt: LocalDateTime) {
        this.publishAt = publishAt
        this.outboxStatus = OutboxStatus.PUBLISH
    }

    companion object {
        fun create(payload: String, mailType: MailType): MailOutbox = MailOutbox(
            outboxStatus = OutboxStatus.CREATE,
            mailType = mailType,
            transactionAt = LocalDateTime.now(ZoneId.of("Asia/Seoul")),
            payload = payload,
        )
    }
}
