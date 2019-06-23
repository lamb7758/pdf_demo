package cnlamb7758.pdf.certificate;

import org.springframework.util.Base64Utils;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.crypto.Cipher;





public class CertificateCoder {

    public static final String CERT_TYPE="X.509";



    /**
     * 获取私匙
     * @param keyStorePath
     * @param pwd
     * @param alias
     * @return PrivateKey 私匙
     * @throws Exception
     */
    private static PrivateKey getPrivateKey(String keyStorePath,String pwd,String alias) throws Exception{
        KeyStore ks=getKeyStore(keyStorePath, pwd);
        return (PrivateKey)ks.getKey(alias, pwd.toCharArray());

    }


    /**
     *
     * @param keyStorePath
     * @param pwd
     * @return keyStore 密匙库
     * @throws Exception
     */
    private static KeyStore getKeyStore(String keyStorePath,String pwd) throws Exception{
        KeyStore ks=KeyStore.getInstance(KeyStore.getDefaultType());
        FileInputStream in=new FileInputStream(keyStorePath);
        ks.load(in,pwd.toCharArray());
        in.close();
        return ks;
    }


    /**
     *
     * @param certificatePath
     * @return Certificate 证书
     * @throws Exception
     */
    private static Certificate getCertificate(String certificatePath) throws Exception{
        CertificateFactory factory=CertificateFactory.getInstance(CERT_TYPE);
        FileInputStream in=new FileInputStream(certificatePath);
        Certificate certificate=factory.generateCertificate(in);
        in.close();
        return certificate;

    }


    /**
     * 通过证书返回公匙
     * @param certificatePath
     * @return Publickey 返回公匙
     * @throws Exception
     */
    private static PublicKey getPublicKeyByCertificate(String certificatePath) throws Exception{
        Certificate certificate=getCertificate(certificatePath);
        return certificate.getPublicKey();
    }


    /**
     *
     * @param keyStorePath
     * @param alias
     * @param pwd
     * @return Certificate 证书
     * @throws Exception
     */
    private static Certificate getCertificate(String keyStorePath,String alias,String pwd) throws Exception{
        KeyStore ks=getKeyStore(keyStorePath, pwd);
        //获取证书
        return ks.getCertificate(alias);
    }


    /**
     * 私匙加密
     * @param data
     * @param keyStorePath
     * @param alias
     * @param pwd
     * @return byte[] 被私匙加密的数据
     * @throws Exception
     */
    public static byte[] encryptByPrivateKey(byte[] data,String keyStorePath,String alias,String pwd) throws Exception{
        PrivateKey privateKey=getPrivateKey(keyStorePath, pwd, alias);
        //对数据进行加密
        Cipher cipher=Cipher.getInstance(privateKey.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        return cipher.doFinal(data);

    }


    /**
     * 私匙解密
     * @param data
     * @param keyStorePath
     * @param alias
     * @param pwd
     * @return byte[] 私匙解密的数据
     * @throws Exception
     */
    public static byte[] decryptByPrivateKey(byte[] data,String keyStorePath,String alias,String pwd) throws Exception{
        PrivateKey privateKey=getPrivateKey(keyStorePath, pwd, alias);
        Cipher cipher=Cipher.getInstance(privateKey.getAlgorithm());
        cipher.init(cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(data);
    }


    /**
     * 公匙加密
     * @param data
     * @param cerPath
     * @return byte[] 被公匙加密的数据
     * @throws Exception
     */
    public static byte[] encryptByPublicKey(byte[] data,String cerPath) throws Exception{
        //获取公匙
        PublicKey publicKey=getPublicKeyByCertificate(cerPath);
        System.out.println(publicKey.getAlgorithm());
        Cipher cipher=Cipher.getInstance(publicKey.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(data);
    }

    /**
     * 公匙解密
     * @param data
     * @param cerPath
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPublicKey(byte[] data,String cerPath) throws Exception{
        PublicKey publicKey=getPublicKeyByCertificate(cerPath);
        Cipher cipher=Cipher.getInstance(publicKey.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        return cipher.doFinal(data);
    }

    /**
     * 签名
     * @param sign
     * @param keyStorePath
     * @param pwd
     * @param alias
     * @return
     * @throws Exception
     */
    public static byte[] sign(byte[] sign,String keyStorePath,String pwd,String alias) throws Exception{
        //获取证书
        X509Certificate x509=(X509Certificate)getCertificate(keyStorePath, alias, pwd);
        //构建签名,由证书指定签名算法
        Signature sa=Signature.getInstance(x509.getSigAlgName());
        //获取私匙
        PrivateKey privateKey=getPrivateKey(keyStorePath, pwd, alias);
        sa.initSign(privateKey);
        sa.update(sign);
        return sa.sign();
    }

    /**
     * 验证签名
     * @param data
     * @param sign
     * @param cerPath
     * @return
     * @throws Exception
     */
    public static boolean verify(byte[] data,byte[] sign,String cerPath) throws Exception{
        X509Certificate x509=(X509Certificate)getCertificate(cerPath);
        Signature sa=Signature.getInstance(x509.getSigAlgName());
        sa.initVerify(x509);
        sa.update(data);
        return sa.verify(sign);
    }


    public static void main(String[] args) throws Exception {
//        PrivateKey pk = getPrivateKey("C:\\lamb.p12", "123456", "cretkey");
//
//        System.out.println( pk.getAlgorithm());
//        System.out.println( pk.getFormat());
//        System.out.println( pk);
//
//        Certificate cer = getCertificate("C:\\lamb.p12");
//        PublicKey pubk = cer.getPublicKey();
//        System.out.println( pubk.getAlgorithm());

        byte[] sv = sign("123".getBytes("UTF-8"),"C:\\lamb.p12","123456","cretkey");
        String svs =  Base64Utils.encodeToString(sv);
        System.out.println(svs);
        byte[] aa = Base64Utils.decodeFromString(svs);

        boolean ver = verify("123".getBytes("UTF-8"), aa, "C:\\lamb.cer");
        System.out.println(ver);
    }
}

//nCdNrLh8YD0Z6Zdbo2n4sOaiT02w9SD69bu7FycYAGk5gI8W2UkxLg3mTzB3+CE1xk3GApbHrNYX0LWry6+6FyynotU1t4H7tRc1lCRHxrsf2uPDFUh6Jm36qi68ArW7GLkPFKWRcFcm/Svm+v2QUcQanSepv2TF/CySf456Uew=