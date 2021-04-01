package internal

import io.reactivex.rxjava3.core.Single

import com.google.cloud.kms.v1.CryptoKeyName

import com.google.cloud.kms.v1.KeyManagementServiceClient
import com.google.protobuf.ByteString

import java.util.*


class KeyEncryptionService {

    fun encrypt(request: EncryptionRequest): Single<String>{
        return Single.fromCallable {
            val projectId = "faith-master"
            val locationId = "global"
            val keyRingId = "Ivan"
            val keyId = "Fotonovela"
            encryptSymmetric(projectId, locationId, keyRingId, keyId, request.plaintext)
        }
    }

    fun encryptSymmetric(
        projectId: String?, locationId: String?, keyRingId: String?, keyId: String?, plaintext: String?
    ): String {
        // Initialize client that will be used to send requests. This client only
        // needs to be created once, and can be reused for multiple requests. After
        // completing all of your requests, call the "close" method on the client to
        // safely clean up any remaining background resources.
        val kmsc = KeyManagementServiceClient.create()
        val cryptoKeyName = CryptoKeyName.of(projectId, locationId, keyRingId, keyId)
        val encodedString = Base64.getEncoder().encode(plaintext?.encodeToByteArray())
        val encryptResponse = kmsc.encrypt(cryptoKeyName, ByteString.copyFrom(encodedString))
        val response = encryptResponse.ciphertext.toByteArray()
        val byteArray = Base64.getEncoder().encode(response)
        kmsc.shutdown()
        return String(byteArray)
    }

    fun decrypt(request: DecryptionRequest): Single<String>{
        return Single.fromCallable {
            val projectId = "faith-master"
            val locationId = "global"
            val keyRingId = "Ivan"
            val keyId = "Fotonovela"
            decryptSymmetric(projectId, locationId, keyRingId, keyId, request.ciphertext)
        }
    }
    fun decryptSymmetric(
        projectId: String?, locationId: String?, keyRingId: String?, keyId: String?, plaintext: String?
    ):String {
        // Initialize client that will be used to send requests. This client only
        // needs to be created once, and can be reused for multiple requests. After
        // completing all of your requests, call the "close" method on the client to
        // safely clean up any remaining background resources.
        val decodedText = Base64.getDecoder().decode(plaintext)
        val kmsc = KeyManagementServiceClient.create()
        val cryptoKeyName = CryptoKeyName.of(projectId,locationId,keyRingId,keyId)
        val decryptResponse = kmsc.decrypt(cryptoKeyName, ByteString.copyFrom(decodedText))
        val result = decryptResponse.plaintext.toStringUtf8()
        kmsc.shutdown()
        return String(Base64.getDecoder().decode(result.toString()))
    }
}

data class EncryptionRequest(val plaintext: String)

data class DecryptionRequest(val ciphertext: String)
