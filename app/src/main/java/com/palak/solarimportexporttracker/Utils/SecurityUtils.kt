package com.palak.solarimportexporttracker.Utils

import android.util.Base64
import androidx.annotation.Nullable
import java.lang.Exception
import java.nio.charset.Charset
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class SecurityUtils {

    companion object{

        val secretKey : String? = ""
        val secretAlgo = "AES"
        val AES_TRANS_MODE = "AES/CBC/PKCS5Padding"

        fun encrypt(dataToEncrypt : String): String? {

            if(secretKey == null) return null

            try{
                val cipher : Cipher = Cipher.getInstance(AES_TRANS_MODE)
                cipher.init(Cipher.ENCRYPT_MODE, SecretKeySpec(secretKey.toByteArray(), secretAlgo),
                    SecureRandom())

                val encryptedByteArray = cipher.doFinal(dataToEncrypt.toByteArray(Charsets.UTF_8))

                val iv = cipher.iv

                val combinedPayload = ByteArray(iv.size + encryptedByteArray.size)

                System.arraycopy(iv,0,combinedPayload,0,iv.size)
                System.arraycopy(encryptedByteArray,0,combinedPayload,iv.size,encryptedByteArray.size)

                return Base64.encodeToString(combinedPayload,Base64.DEFAULT)
            }
            catch (e : Exception){
                e.printStackTrace()
            }

            return null
        }

        @Nullable
        fun decrypt(dataToDecrypt : String) : String?{

            if(secretKey == null) return null

            try{
                val encryptedPayload = Base64.decode(dataToDecrypt,Base64.DEFAULT)
                val iv = ByteArray(16)
                val encryptedBytes = ByteArray(encryptedPayload.size - iv.size)

                System.arraycopy(encryptedPayload,0,iv,0,16)
                System.arraycopy(encryptedPayload,iv.size,encryptedBytes,0,encryptedBytes.size)

                val cipher : Cipher = Cipher.getInstance(AES_TRANS_MODE)
                cipher.init(Cipher.DECRYPT_MODE, SecretKeySpec(secretKey.toByteArray(), secretAlgo),
                    IvParameterSpec(iv))

                return String(cipher.doFinal(encryptedBytes))
            }
            catch (e : Exception){
                e.printStackTrace()
            }

            return null
        }
    }
}