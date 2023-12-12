package io.micro.core.robot.qq

import io.quarkus.logging.LoggingFilter
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.net.URI
import java.util.logging.Filter
import java.util.logging.LogRecord


/**
 *@author Augenstern
 *@since 2023/11/21
 */
@LoggingFilter(name = "qr-filter")
class QRLogFilter(private val qqRobotManager: QQRobotManager) : Filter {

    companion object {
        private const val TAG = "net.mamoe.mirai.Bot"

        private val QR = Regex("\\[QRCodeLogin] 将会显示二维码图片，若看不清图片，请查看文件 (.*)")

        private val QQ = Regex("mirai-qrcode-(\\w*)")

    }

    override fun isLoggable(record: LogRecord): Boolean {
        callQRCode(record)
        return true
    }

    /**
     * 从日志中获取扫码登录的二维码图片文件地址，提取为byte array供登录使用
     * @param record 日志记录
     * @see java.util.logging.LogRecord
     */
    private fun callQRCode(record: LogRecord) {
        // 查找含有二维码文件地址日志
        if (record.loggerName == TAG) {
            val message = record.message
            val qrPath = findFirst(message, QR)
            val qqNumber = findFirst(message, QQ)
            if (qrPath != null && qqNumber != null) {
                // 读取二维码图片
                val reader = FileInputStream(File(URI.create(qrPath).path))
                val bufferedReader = BufferedInputStream(reader)
                val byteArray = bufferedReader.readBytes()
                val robot = qqRobotManager.getRobot(qqNumber) as QQRobot?
                // 发布二维码开始事件
                robot?.loggingInListener(QQRobot.QRCodeStartEvent(byteArray))
            }
        }
    }

    /**
     * 获取匹配正则表达式的第一个值
     * @param msg 需要匹配的字符串
     * @param regex 正则表达式
     * @return 匹配值
     */
    private fun findFirst(msg: String, regex: Regex): String? {
        return regex.find(msg)?.groupValues?.stream()?.skip(1)?.findFirst()?.orElse(null)
    }
}