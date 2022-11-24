package com.guacamole.user.domain.mail.outbox

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.guacamole.user.common.TransactionHandler
import com.guacamole.user.domain.mail.MailService
import com.guacamole.user.domain.mail.outbox.model.AuthorizationMailProperty
import com.guacamole.user.domain.mail.outbox.model.MailType
import com.guacamole.user.domain.mail.outbox.model.OutboxStatus
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.concurrent.TimeUnit

@Component
class MailOutboxRelay(
    private val mailOutboxRepository: MailOutboxRepository,
    private val mailService: MailService,
    private val transactionHandler: TransactionHandler,
    private val objectMapper: ObjectMapper,
) {

    fun publish(mailOutbox: MailOutbox) {
        mailOutboxRepository.save(mailOutbox)
    }

    @Scheduled(fixedDelay = 100, timeUnit = TimeUnit.MILLISECONDS)
    fun onRelay() {
        val publishAt = LocalDateTime.now(ZoneId.of("Asia/Seoul"))
        transactionHandler.runInTransaction {
            val mailOutbox =
                mailOutboxRepository.findByTransactionAtLessThanAndOutboxStatusAndMailType(
                    publishAt,
                    OutboxStatus.CREATE,
                    MailType.AUTHORIZATION,
                ) ?: return@runInTransaction

            with(objectMapper.readValue<AuthorizationMailProperty>(mailOutbox.payload)) {
                mailService.sendAuthorizeEmail(this.email, this.userId, this.authorizeCode)
            }
            mailOutbox.complete(publishAt)
        }
    }
}
