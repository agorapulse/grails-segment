package grails.plugin.segment

import grails.util.Holders

import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

class IntercomHashCodec {

    static encode = { str ->
        if (secretKey) {
            Mac mac = setupMac(secretKey)
            return mac.doFinal(str.bytes).encodeHex().toString()
        } else {
            return str
        }
    }

    static decode = { str ->
        return ""
    }

    static String getSecretKey() {
        return Holders.config.grails.plugin.segmentio.intercomSecretKey
    }

    private static Mac setupMac(secretKey) {
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.bytes, "HmacSHA256")
        Mac hmacSHA256 = Mac.getInstance("HmacSHA256")
        hmacSHA256.init(secretKeySpec)
        return hmacSHA256
    }
}