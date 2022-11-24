package com.guacamole.user.domain.mail

import mu.KLogger
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service


@Service
class MailService(
    private val mailSender: JavaMailSender,
    @Value("\${mail.auth-url-template}")
    private val authUrlTemplate: String,
) {
    private val logger: KLogger = KotlinLogging.logger { }

    fun sendAuthorizeEmail(email: String, userId: Long, authorizeCode: String) {
        val authorizeUrl = authUrlTemplate.format(userId, authorizeCode)
        val message = buildAuthorizeMailTemplate(email, authorizeUrl)

        try {
            mailSender.send(message)
        } catch (exception: Exception) {
            logger.info { "MailService.sendAuthorizeEmail throw exception ${exception.localizedMessage}" }
            throw RuntimeException("send mail failed ${exception.localizedMessage}")
        }
    }

    private fun buildAuthorizeMailTemplate(
        email: String,
        authorizeUrl: String
    ): SimpleMailMessage = SimpleMailMessage().apply {
        setFrom("noreply@guacamole.com")
        setTo(email)
        setSubject("과카몰리 - 인증 메일 발송")
        setText("""<p> 링크 : <a href="$authorizeUrl">인증 하기</a> </p>""")
    }
}
