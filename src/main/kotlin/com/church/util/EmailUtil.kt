package com.church.util

import jakarta.mail.internet.MimeMessage
import org.slf4j.LoggerFactory
import org.springframework.core.io.FileSystemResource
import org.springframework.mail.MailException
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.retry.annotation.Backoff
import org.springframework.retry.annotation.Retryable
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.thymeleaf.context.Context
import org.thymeleaf.spring6.SpringTemplateEngine
import java.io.File

@Service
class EmailUtil(
    private val mailSender: JavaMailSender,
    private val templateEngine: SpringTemplateEngine
) {

    private val logger = LoggerFactory.getLogger(EmailUtil::class.java)


    fun sendSimpleEmail(to: String, subject: String, text: String, from: String? = null) {
        try {
            val message = mailSender.createMimeMessage()
            val helper = MimeMessageHelper(message, false)
            helper.setTo(to)
            helper.setSubject(subject)
            helper.setText(text, false)
            from?.let { helper.setFrom(it) }

            mailSender.send(message)
            logger.info("Simple email sent to $to")
        } catch (ex: MailException) {
            logger.error("Failed to send email to $to", ex)
            throw ex
        }
    }

    @Retryable(
        value = [MailException::class],
        maxAttempts = 3,
        backoff = Backoff(delay = 3000)
    )
    fun sendHtmlEmail(to: String, subject: String, from: String? = null) {
        try {
            val message: MimeMessage = mailSender.createMimeMessage()
            val helper = MimeMessageHelper(message, true, "UTF-8")
            helper.setTo(to)
            helper.setSubject(subject)
            helper.setText(subject, true)
            from?.let { helper.setFrom(it) }

            mailSender.send(message)
        } catch (ex: Exception) {
            logger.error("Failed to send HTML email to $to", ex)
            throw ex
        }
    }


    fun sendEmailWithAttachment(to: String, subject: String, htmlBody: String, attachment: File, from: String? = null) {
        try {
            val message: MimeMessage = mailSender.createMimeMessage()
            val helper = MimeMessageHelper(message, true)
            helper.setTo(to)
            helper.setSubject(subject)
            helper.setText(htmlBody, true)
            from?.let { helper.setFrom(it) }

            val file = FileSystemResource(attachment)
            helper.addAttachment(attachment.name, file)
            mailSender.send(message)
            logger.info("Email with attachment sent to $to")
        } catch (ex: Exception) {
            logger.error("Failed to send email with attachment to $to", ex)
            throw ex
        }
    }



    @Async
    fun sendVerificationEmail(to: String, username: String, verificationToken: String) {
        try {
            val mimeMessage: MimeMessage = mailSender.createMimeMessage()
            val helper = MimeMessageHelper(mimeMessage, "utf-8")

            val context = Context()
            context.setVariables(
                mapOf(
                    "username" to username,
                    "token" to verificationToken
                )
            )
            templateEngine.process("email/welcome", context)

            helper.setTo(to)
            helper.setSubject("Verify Your Email")
            helper.setFrom("no-reply@yourapp.com")

            mailSender.send(mimeMessage)
            logger.info("Verification email sent to $to")
        } catch (e: Exception) {
            logger.error("Failed to send email to $to", e)
        }
    }
}
