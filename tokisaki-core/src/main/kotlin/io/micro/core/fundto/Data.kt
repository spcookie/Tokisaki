package io.micro.core.fundto


/**
 *@author Augenstern
 *@since 2023/10/8
 */
data class Data(var type: MediaType = MediaType.None, var bytes: ByteArray = byteArrayOf()) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Data) return false

        if (type != other.type) return false
        if (!bytes.contentEquals(other.bytes)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = type.hashCode()
        result = 31 * result + bytes.contentHashCode()
        return result
    }
}
