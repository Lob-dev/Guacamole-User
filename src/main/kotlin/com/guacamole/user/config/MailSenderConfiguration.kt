package com.guacamole.user.config

import com.guacamole.user.config.properties.MailSenderProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl
import java.util.*

@Configuration
class MailSenderConfiguration {

    @Bean
    protected fun javaMailSender(mailSenderProperty: MailSenderProperty): JavaMailSender {
        val mailSender = JavaMailSenderImpl()
        mailSender.host = mailSenderProperty.host
        mailSender.port = mailSenderProperty.port
        mailSender.username = mailSenderProperty.username
        mailSender.password = mailSenderProperty.password

        val props: Properties = mailSender.javaMailProperties
        props["mail.transport.protocol"] = "smtp"
        props["mail.smtp.auth"] = true
        props["mail.smtp.starttls.enable"] = true
        props["mail.debug"] = "true"
        return mailSender
    }
}
