package com.bsro.util.encryption;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class EncryptionUtil {
	Log log = LogFactory.getLog(EncryptionUtil.class);
	
	private static final String AES_128_BIT_SYMMETRIC_KEY_ENCRYPTION_PREFIX = "s01";

	private static final String AES_256_BIT_SYMMETRIC_KEY_ENCRYPTION_PREFIX = "s02";
	
	private static final String CONFIG_PROP_KEY_LENGTH_IN_BITS = "keyLength";
	private static final String CONFIG_PROP_ALGORITHM = "algorithm";
	private static final String CONFIG_PROP_TRANSFORMATION = "transformation";
	private static final String CONFIG_PROP_HEX_KEY = "hexKey";
	private static final String CONFIG_PROP_INITIALIZATION_VECTOR_LENGTH_IN_BYTES = "initializationVectorLength";
	private static final String CONFIG_PROP_CHARACTER_ENCODING = "characterEncoding";
	
	private static final String ALGORITHM_AES = "AES";
	private static final String TRANSFORMATION_AES_CBC_PKCS5PADDING = "AES/CBC/PKCS5Padding";
	
	private static final String HEX_DIGITS = "0123456789abcdef";
	private static final String RANDOM_ALGORITHM = "SHA1PRNG";
	
	private static final Map<String, Map<String, Object>> ENCRYPTION_SCHEME_CONFIG_MAP;
	
	static {
		// We prefix our encrypted strings with a string that tells us how we're meant to decrypt them
		// We use this string as a key to a map with configuration information for a given encryption scheme
		ENCRYPTION_SCHEME_CONFIG_MAP = new HashMap<String, Map<String, Object>>();
		
		Map<String, Object> AES_128_BIT_SYMMETRIC_KEY_ENCRYPTION_CONFIG = new HashMap<String, Object>();
		// We will want to raise this to 256 for s02. We will do this on the new site, as it requires changing Java's security configuration to allow unlimited key sizes
		AES_128_BIT_SYMMETRIC_KEY_ENCRYPTION_CONFIG.put(CONFIG_PROP_KEY_LENGTH_IN_BITS, 128);
		AES_128_BIT_SYMMETRIC_KEY_ENCRYPTION_CONFIG.put(CONFIG_PROP_ALGORITHM, ALGORITHM_AES);
		AES_128_BIT_SYMMETRIC_KEY_ENCRYPTION_CONFIG.put(CONFIG_PROP_TRANSFORMATION, TRANSFORMATION_AES_CBC_PKCS5PADDING);
		 // This is our key, which must not be lost or we won't be able to decrypt ids
		AES_128_BIT_SYMMETRIC_KEY_ENCRYPTION_CONFIG.put(CONFIG_PROP_HEX_KEY, "81bc3c5e6874f4e023d00aa15e2479d7");
		AES_128_BIT_SYMMETRIC_KEY_ENCRYPTION_CONFIG.put(CONFIG_PROP_INITIALIZATION_VECTOR_LENGTH_IN_BYTES, 16);
		AES_128_BIT_SYMMETRIC_KEY_ENCRYPTION_CONFIG.put(CONFIG_PROP_CHARACTER_ENCODING, "UTF-8");
		
		ENCRYPTION_SCHEME_CONFIG_MAP.put(AES_128_BIT_SYMMETRIC_KEY_ENCRYPTION_PREFIX, AES_128_BIT_SYMMETRIC_KEY_ENCRYPTION_CONFIG);
	
		Map<String, Object> AES_256_BIT_SYMMETRIC_KEY_ENCRYPTION_CONFIG = new HashMap<String, Object>();
		// This requires changing Java's security configuration to allow unlimited key sizes
		AES_256_BIT_SYMMETRIC_KEY_ENCRYPTION_CONFIG.put(CONFIG_PROP_KEY_LENGTH_IN_BITS, 256);
		AES_256_BIT_SYMMETRIC_KEY_ENCRYPTION_CONFIG.put(CONFIG_PROP_ALGORITHM, ALGORITHM_AES);
		AES_256_BIT_SYMMETRIC_KEY_ENCRYPTION_CONFIG.put(CONFIG_PROP_TRANSFORMATION, TRANSFORMATION_AES_CBC_PKCS5PADDING);
		 // This is our key, which must not be lost or we won't be able to decrypt ids
		AES_256_BIT_SYMMETRIC_KEY_ENCRYPTION_CONFIG.put(CONFIG_PROP_HEX_KEY, "8d76cfaa2f5cb79d4a65b534a8dc6b96172baff1fd06f91919a2308911834321");
		AES_256_BIT_SYMMETRIC_KEY_ENCRYPTION_CONFIG.put(CONFIG_PROP_INITIALIZATION_VECTOR_LENGTH_IN_BYTES, 16);
		AES_256_BIT_SYMMETRIC_KEY_ENCRYPTION_CONFIG.put(CONFIG_PROP_CHARACTER_ENCODING, "UTF-8");
		
		ENCRYPTION_SCHEME_CONFIG_MAP.put(AES_256_BIT_SYMMETRIC_KEY_ENCRYPTION_PREFIX, AES_256_BIT_SYMMETRIC_KEY_ENCRYPTION_CONFIG);
	}

	private static String getAlgorithm(String encryptionSchemePrefix) {
		return (String)ENCRYPTION_SCHEME_CONFIG_MAP.get(encryptionSchemePrefix).get(CONFIG_PROP_ALGORITHM);
	}
	
	private static Integer getKeyLengthInBits(String encryptionSchemePrefix) {
		return (Integer)ENCRYPTION_SCHEME_CONFIG_MAP.get(encryptionSchemePrefix).get(CONFIG_PROP_KEY_LENGTH_IN_BITS);
	}
	
	private static String getTransformation(String encryptionSchemePrefix) {
		return (String)ENCRYPTION_SCHEME_CONFIG_MAP.get(encryptionSchemePrefix).get(CONFIG_PROP_TRANSFORMATION);
	}
	
	private static byte[] getKeyBytes(String encryptionSchemePrefix) {
		return convertHexStringToBytes((String)ENCRYPTION_SCHEME_CONFIG_MAP.get(encryptionSchemePrefix).get(CONFIG_PROP_HEX_KEY));
	}
	
	private static Integer getInitializationVectorLengthInBytes(String encryptionSchemePrefix) {
		return (Integer)ENCRYPTION_SCHEME_CONFIG_MAP.get(encryptionSchemePrefix).get(CONFIG_PROP_INITIALIZATION_VECTOR_LENGTH_IN_BYTES);
	}
	
	private static String getCharacterEncoding(String encryptionSchemePrefix) {
		return (String)ENCRYPTION_SCHEME_CONFIG_MAP.get(encryptionSchemePrefix).get(CONFIG_PROP_CHARACTER_ENCODING);
	}
	
	@SuppressWarnings("unused")
	/**
	 * Generates a key for the given encryption scheme, and returns it as a hex string.
	 * 
	 * @param encryptionSchemePrefix
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	private static String generateKey(String encryptionSchemePrefix) throws NoSuchAlgorithmException {
		// This is here in case we ever need it.
		KeyGenerator keyGenerator = KeyGenerator.getInstance(getAlgorithm(encryptionSchemePrefix));
		keyGenerator.init(getKeyLengthInBits(encryptionSchemePrefix));
		SecretKey generatedKey = keyGenerator.generateKey(); 
		return convertBytesToHexString(generatedKey.getEncoded());
	}

	/**
	 * Generates an initialization vector for a given encryption scheme.
	 * 
	 * @param encryptionSchemePrefix
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
    private static byte[] generateInitializationVector(String encryptionSchemePrefix) throws NoSuchAlgorithmException {
        SecureRandom random = SecureRandom.getInstance(RANDOM_ALGORITHM);
        byte[] initializationVector = new byte[getInitializationVectorLengthInBytes(encryptionSchemePrefix)];
        random.nextBytes(initializationVector);
        return initializationVector;
    }
    
    /**
     * v02 of our symmetric key encryption implementation. It returns the encrypted bytes in hex string format, prepended with a prefix to indicate the encryption used.
     * 
     * @param stringToEncrypt
     * @return
     * @throws GeneralSecurityException
     * @throws UnsupportedEncodingException
     */
	public static String encryptWithSymmetricKeyEncryptionV02(String stringToEncrypt) throws GeneralSecurityException, UnsupportedEncodingException {
		return encryptWithSymmetricKeyEncryption(stringToEncrypt, AES_256_BIT_SYMMETRIC_KEY_ENCRYPTION_PREFIX);		
	}
	
    /**
     * v01 of our symmetric key encryption implementation. It returns the encrypted bytes in hex string format, prepended with a prefix to indicate the encryption used.
     * Future implementations can be distinguished with method names ending in "v02," "v03," etc.
     * Each encryption scheme preprends the returned string with a given prefix that tells us which scheme we used.
     * 
     * @param stringToEncrypt
     * @return
     * @throws GeneralSecurityException
     * @throws UnsupportedEncodingException
     */
	public static String encryptWithSymmetricKeyEncryptionV01(String stringToEncrypt) throws GeneralSecurityException, UnsupportedEncodingException {
		return encryptWithSymmetricKeyEncryption(stringToEncrypt, AES_128_BIT_SYMMETRIC_KEY_ENCRYPTION_PREFIX);		
	}
	
	private static String encryptWithSymmetricKeyEncryption(String stringToEncrypt, String encryptionSchemePrefix) throws GeneralSecurityException, UnsupportedEncodingException {
		String algorithm = getAlgorithm(encryptionSchemePrefix);
		String characterEncoding = getCharacterEncoding(encryptionSchemePrefix);
		String transformation = getTransformation(encryptionSchemePrefix);
		
		// As this is symmetric-key encryption, we only have one standard key.
		byte [] key = getKeyBytes(encryptionSchemePrefix);
		SecretKeySpec secretKeySpec = new SecretKeySpec(key, algorithm);
		
		// We randomly generate an initialization vector so the same string doesn't encrypt the same way each time.
		byte[] initializationVector = generateInitializationVector(encryptionSchemePrefix);
		IvParameterSpec initializationVectorSpec = new IvParameterSpec(initializationVector);
		
		// The "transformation" indicates padding and such.
		Cipher encryptionCipher = Cipher.getInstance(transformation);
		
		// Initialize the cipher with the key and the initialization vector
		encryptionCipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, initializationVectorSpec);
		
		// Actually do the encryption
		byte[] encryptedBytes = encryptionCipher.doFinal(stringToEncrypt.getBytes(characterEncoding));
		
		// Prepend the encryption scheme prefix and the initialization vector so we know how to decrypt it.
		String encryptedString = encryptionSchemePrefix+convertBytesToHexString(initializationVector)+convertBytesToHexString(encryptedBytes);
		
		return encryptedString;
	}

	public static String decryptWithSymmetricKeyEncryption(String stringToDecrypt) throws GeneralSecurityException, UnsupportedEncodingException {
		if (stringToDecrypt == null) {
			throw new IllegalArgumentException("Null strings may not be decrypted");
		}
		
		String encryptionSchemePrefix = null;
		
		// Check for a recognized prefix. This will tell us how it was encrypted.
		for (String key : ENCRYPTION_SCHEME_CONFIG_MAP.keySet()) {
			if (stringToDecrypt.startsWith(key)) {
				encryptionSchemePrefix = key;
				break;
			}
		}
		
		if (encryptionSchemePrefix == null) {
			throw new IllegalArgumentException("String ("+stringToDecrypt+") does not conform to any known encryption scheme");
		}
		
		// Remove the encryption scheme prefix from the string we want to decrypt.
		stringToDecrypt = stringToDecrypt.substring(encryptionSchemePrefix.length());
			
		String algorithm = getAlgorithm(encryptionSchemePrefix);
		String characterEncoding = getCharacterEncoding(encryptionSchemePrefix);
		String transformation = getTransformation(encryptionSchemePrefix);
		
		// As this is symmetric-key encryption, we only have one standard key.
		byte [] key = getKeyBytes(encryptionSchemePrefix);
		SecretKeySpec secretKeySpec = new SecretKeySpec(key, algorithm);
		
		// The "transformation" indicates padding and such.
		Cipher decryptCipher = Cipher.getInstance(transformation);
		
		// Convert from hex string back to bytes
		byte[] bytesToDecrypt = convertHexStringToBytes(stringToDecrypt);
		
		// The first N bytes contains initializationVector, so pull those out.
		int initializationVectorLengthInBytes = getInitializationVectorLengthInBytes(encryptionSchemePrefix);
		
		byte[] initializationVector = new byte[initializationVectorLengthInBytes];
		
		for (int i = 0; i < initializationVectorLengthInBytes; i++) {
			initializationVector[i] = bytesToDecrypt[i];
		}
		
		IvParameterSpec ivspec = new IvParameterSpec(initializationVector);
		
		// The remaining bytes are the actual content we care about.
		byte[] encryptedContent = new byte[bytesToDecrypt.length-initializationVectorLengthInBytes];
		for (int i = initializationVectorLengthInBytes; i < bytesToDecrypt.length; i++) {
			encryptedContent[i-initializationVectorLengthInBytes] = bytesToDecrypt[i];
		}
		
		// Initialize the cipher with the key and the initialization vector
		decryptCipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivspec);
		
		// Actually do the encryption
		byte[] decrypted = decryptCipher.doFinal(encryptedContent);
		
		// Return as a string
		return new String(decrypted, characterEncoding);
	}

	private static String convertBytesToHexString(byte[] data, int length) {
		StringBuffer  buf = new StringBuffer();

		for (int i = 0; i != length; i++) {
			int v = data[i] & 0xff;
			buf.append(HEX_DIGITS.charAt(v >> 4));
			buf.append(HEX_DIGITS.charAt(v & 0xf));
		}

		return buf.toString();
	}

	private static String convertBytesToHexString(byte[] data) {
		return convertBytesToHexString(data, data.length);
	}

	private static byte[] convertHexStringToBytes(String s) {
		byte[] b = new byte[s.length() / 2];
		for (int i = 0; i < b.length; i++) {
			int index = i * 2;
			int v = Integer.parseInt(s.substring(index, index + 2), 16);
			b[i] = (byte)v;
		}
		return b;
	}
    
}
