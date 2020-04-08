package zy.pointer.j2easy.framework.endecrypt;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.codec.binary.Base64;

public class RSAUtils {

	private RSAUtils(){};
	
	/** 默认字符集 */
	public static final String CHARSET = "UTF-8";
	/***/
    public static final String RSA_ALGORITHM = "RSA";
    
    public static final String SIGN_ALGORITHMS = "sha256withRSA";  
    
    /**
     * 创建密钥对(RSA keys must be at least 512 bits long)
     * @return Map{
     *    "publicKey"  : "" ,
     *    "privateKey" : ""
     * }
     */
    public static Map<String, String> createKeys(int keySize){
    	//为RSA算法创建一个KeyPairGenerator对象 
    	KeyPairGenerator kpg; 
    	try{ 
    		kpg = KeyPairGenerator.getInstance(RSA_ALGORITHM); 
    	}catch(NoSuchAlgorithmException e){ 
    		throw new IllegalArgumentException("No such algorithm [" + RSA_ALGORITHM + "]"); 
    	}
    	//初始化KeyPairGenerator对象,密钥长度
        kpg.initialize(keySize);
        //生成密匙对
        KeyPair keyPair = kpg.generateKeyPair();
        //得到公钥
        Key publicKey = keyPair.getPublic();
        //得到私钥
        Key privateKey = keyPair.getPrivate();
        String publicKeyStr = Base64.encodeBase64String(publicKey.getEncoded());
        String privateKeyStr = Base64.encodeBase64String(privateKey.getEncoded());
        Map<String, String> result = new HashMap<>();
        result.put("publicKey", publicKeyStr);
        result.put("privateKey", privateKeyStr);
    	return result;
    }
    
    /**
     * 得到公钥
     * @param publicKey 密钥字符串（经过base64编码）
     * @throws Exception
     */ 
    private static RSAPublicKey getPublicKey(String publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException { 
    	//通过X509编码的Key指令获得公钥对象
    	KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM); 
    	X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(Base64.decodeBase64(publicKey)); 
    	RSAPublicKey key = (RSAPublicKey) keyFactory.generatePublic(x509KeySpec); 
    	return key; 
    }
    
    /**
     * 得到私钥
     * @param privateKey 密钥字符串（经过base64编码）
     * @throws Exception
     */ 
    private static RSAPrivateKey getPrivateKey(String privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException { 
    	 //通过PKCS#8编码的Key指令获得私钥对象 
    	 KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM); 
    	 PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKey)); 
    	 RSAPrivateKey key = (RSAPrivateKey) keyFactory.generatePrivate(pkcs8KeySpec); 
    	 return key; 
    }
    
    private static byte[] rsaSplitCodec(Cipher cipher, int opmode, byte[] datas, int keySize) throws IOException, IllegalBlockSizeException, BadPaddingException{ 
    	int maxBlock = 0; 
    	if(opmode == Cipher.DECRYPT_MODE){ 
    		maxBlock = keySize / 8; 
    	}else{ 
    		maxBlock = keySize / 8 - 11; 
    	} 
    	ByteArrayOutputStream out = new ByteArrayOutputStream(); 
    	int offSet = 0; 
    	byte[] buff; 
    	int i = 0; 
    	
		while(datas.length > offSet){ 
			if(datas.length-offSet > maxBlock){ 
				buff = cipher.doFinal(datas, offSet, maxBlock); 
			}else{ 
				buff = cipher.doFinal(datas, offSet, datas.length-offSet); 
			} 
			out.write(buff, 0, buff.length); i++; offSet = i * maxBlock; 
		} 
    	
    	byte[] resultDatas = out.toByteArray(); 
    	out.close();
    	return resultDatas; 
    }
    
    public static String publicEncrypt(String data, String publicKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException, IOException, InvalidKeySpecException, IllegalBlockSizeException, BadPaddingException{ 
		Cipher cipher = Cipher.getInstance(RSA_ALGORITHM); 
		RSAPublicKey rsaPublicKey = getPublicKey(publicKey);
		cipher.init(Cipher.ENCRYPT_MODE, rsaPublicKey); 
		return Base64.encodeBase64String(
				rsaSplitCodec(cipher, 
						Cipher.ENCRYPT_MODE, 
						data.getBytes(CHARSET), 
						rsaPublicKey.getModulus().bitLength()
				)
		); 
    } 
    
    public static String privateDecrypt(String data, String privateKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException, IOException, InvalidKeySpecException, IllegalBlockSizeException, BadPaddingException{ 
		Cipher cipher = Cipher.getInstance(RSA_ALGORITHM); 
		RSAPrivateKey rsaPrivateKey = getPrivateKey(privateKey);
		cipher.init(Cipher.DECRYPT_MODE, rsaPrivateKey); 
		return new String(
				rsaSplitCodec(cipher, 
						Cipher.DECRYPT_MODE, 
						Base64.decodeBase64(data), 
						rsaPrivateKey.getModulus().bitLength()
				), 
				CHARSET
		); 
    } 
    
    /**
     * 私钥加密
     * @param data
     * @param privateKey
     * @return
     * @throws InvalidKeySpecException 
     * @throws BadPaddingException 
     * @throws IllegalBlockSizeException 
     */ 
    public static String privateEncrypt(String data, String privateKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException, IOException, InvalidKeySpecException, IllegalBlockSizeException, BadPaddingException{ 
		Cipher cipher = Cipher.getInstance(RSA_ALGORITHM); 
		RSAPrivateKey rsaPrivateKey = getPrivateKey(privateKey);
		cipher.init(Cipher.ENCRYPT_MODE, rsaPrivateKey); 
		return Base64.encodeBase64URLSafeString(
				rsaSplitCodec(cipher, 
						Cipher.ENCRYPT_MODE, 
						data.getBytes(CHARSET), 
						rsaPrivateKey.getModulus().bitLength()
				)
		); 
    } 
    
    /**
     * 公钥解密
     * @param data
     * @param publicKey
     * @return
     * @throws InvalidKeySpecException 
     * @throws BadPaddingException 
     * @throws IllegalBlockSizeException 
     */ 
    public static String publicDecrypt(String data, String publicKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException, IOException, InvalidKeySpecException, IllegalBlockSizeException, BadPaddingException{ 
		 Cipher cipher = Cipher.getInstance(RSA_ALGORITHM); 
		 RSAPublicKey rsaPublicKey = getPublicKey(publicKey);
		 cipher.init(Cipher.DECRYPT_MODE, rsaPublicKey); 
		 return new String(
				 rsaSplitCodec(cipher, 
						 Cipher.DECRYPT_MODE, 
						 Base64.decodeBase64(data), 
						 rsaPublicKey.getModulus().bitLength()
				), 
				CHARSET
		); 
    }
    
    public static String sign(String content, String privateKey, String encode)  {  
        try  {  
            PKCS8EncodedKeySpec priPKCS8    = new PKCS8EncodedKeySpec( Base64.decodeBase64(privateKey) );   
               
            KeyFactory keyf                 = KeyFactory.getInstance("RSA");  
            PrivateKey priKey               = keyf.generatePrivate(priPKCS8);  
   
            java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);  
   
            signature.initSign(priKey);  
            signature.update( content.getBytes(encode));  
   
            byte[] signed = signature.sign();  
            return Base64.encodeBase64String(signed);
        }  
        catch (Exception e)   
        {  
            e.printStackTrace();  
        }  
        return null;  
    }  
    
    public static String sign(String content, String privateKey)  
    {  
        try  
        {  
            PKCS8EncodedKeySpec priPKCS8    = new PKCS8EncodedKeySpec( Base64.decodeBase64(privateKey) );   
            KeyFactory keyf = KeyFactory.getInstance("RSA");  
            PrivateKey priKey = keyf.generatePrivate(priPKCS8);  
            java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);  
            signature.initSign(priKey);  
            signature.update( content.getBytes());  
            byte[] signed = signature.sign();  
            return Base64.encodeBase64String(signed);  
        }  
        catch (Exception e)   
        {  
            e.printStackTrace();  
        }  
        return null;  
    }  
    
    
    public static boolean doCheck(String content, String sign, String publicKey,String encode)  
    {  
        try  
        {  
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");  
            byte[] encodedKey = Base64.decodeBase64(publicKey);  
            PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));  
            java.security.Signature signature = java.security.Signature  
            .getInstance(SIGN_ALGORITHMS);  
            signature.initVerify(pubKey);  
            signature.update( content.getBytes(encode) );  
            boolean bverify = signature.verify( Base64.decodeBase64(sign) );  
            return bverify;  
        }   
        catch (Exception e)   
        {  
            e.printStackTrace();  
        }  
           
        return false;  
    }  
    
    public static boolean doCheck(String content, String sign, String publicKey)  
    {  
        try  
        {  
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");  
            byte[] encodedKey = Base64.decodeBase64(publicKey);  
            PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));  
   
            java.security.Signature signature = java.security.Signature  
            .getInstance(SIGN_ALGORITHMS);  
           
            signature.initVerify(pubKey);  
            signature.update( content.getBytes() );  
           
            return signature.verify( Base64.decodeBase64(sign) );

        }   
        catch (Exception e)   
        {
            e.printStackTrace();  
        }
           
        return false;  
    }  

}
