package cnlamb7758.pdf.pkcs12;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.util.Enumeration;


/**
 * <p>Title: PKCS12与JKS格式证书库转换工具</p>
 * <p>Description: 该工具可以把JKS和PKCS12格式的证书库相互转换</p>
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: </p>
 *
 * @author BrokenStone(wdmsyf@yahoo.com)
 * @version 1.0
 */

public class KeyStoreConv {
    /**
     * 从PKCS12格式转换为JKS格式
     * @param srcFile String PKCS12格式的证书库
     * @param srcPasswd String PKCS12格式的证书库密码
     * @param destFile String JKS格式的证书库
     * @param destPasswd String  JKS格式的证书库密码
     */
    public void PKCS12ToJKS(String srcFile, String srcPasswd, String destFile, String destPasswd){
        try {
            KeyStore inputKeyStore = KeyStore.getInstance("PKCS12");
            FileInputStream fis = new FileInputStream(srcFile);
            char[] srcPwd = null, destPwd = null;

            if ((srcPasswd == null) || srcPasswd.trim().equals("")) {
                srcPwd = null;
            } else {
                srcPwd = srcPasswd.toCharArray();
            }

            if ((destPasswd == null) || destPasswd.trim().equals("")) {
                destPwd = null;
            } else {
                destPwd = destPasswd.toCharArray();
            }

            inputKeyStore.load(fis, srcPwd);
            fis.close();

            KeyStore outputKeyStore = KeyStore.getInstance("JKS");
            outputKeyStore.load(null, destPwd);
            Enumeration enums = inputKeyStore.aliases();

            while (enums.hasMoreElements()) {
                String keyAlias = (String) enums.nextElement();
                System.out.println("alias=[" + keyAlias + "]");
                if (inputKeyStore.isKeyEntry(keyAlias)) {
                    Key key = inputKeyStore.getKey(keyAlias, srcPwd);
                    Certificate[] certChain = inputKeyStore.getCertificateChain(keyAlias);
                    outputKeyStore.setKeyEntry(keyAlias, key, destPwd, certChain);
                }
            }

            FileOutputStream out = new FileOutputStream(destFile);
            outputKeyStore.store(out, destPwd);
            out.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 从JKS格式转换为PKCS12格式
     * @param srcFile String JKS格式证书库
     * @param srcPasswd String JKS格式证书库密码
     * @param destFile String PKCS12格式证书库
     * @param destPasswd String PKCS12格式证书库密码
     */
    public void JSKToPKCS12(String srcFile, String srcPasswd, String destFile, String destPasswd){
        try {
            KeyStore inputKeyStore = KeyStore.getInstance("JKS");
            FileInputStream fis = new FileInputStream(srcFile);
            char[] srcPwd = null, destPwd = null;

            if ((srcPasswd == null) || srcPasswd.trim().equals("")) {
                srcPwd = null;
            } else {
                srcPwd = srcPasswd.toCharArray();
            }
            if ((destPasswd == null) || destPasswd.trim().equals("")) {
                destPwd = null;
            } else {
                destPwd = destPasswd.toCharArray();
            }

            inputKeyStore.load(fis, srcPwd);
            fis.close();

            KeyStore outputKeyStore = KeyStore.getInstance("PKCS12");

            Enumeration enums = inputKeyStore.aliases();

            while (enums.hasMoreElements()) {
                String keyAlias = (String) enums.nextElement();
                System.out.println("alias=[" + keyAlias + "]");

                outputKeyStore.load(null, destPwd );
                if (inputKeyStore.isKeyEntry(keyAlias)) {
                    Key key = inputKeyStore.getKey(keyAlias, srcPwd);
                    Certificate[] certChain = inputKeyStore.getCertificateChain(keyAlias);
                    outputKeyStore.setKeyEntry(keyAlias, key, destPwd, certChain);
                }
                String fName ="lamb7758";
                boolean ispfx = destFile.contains(".pfx");
                if(ispfx){
                    fName =destFile.substring(0, destFile.indexOf(".pfx"));
                    fName += "_" + keyAlias + ".pfx";
                }
                boolean isp12 = destFile.contains(".p12");
                if(isp12){
                    fName = destFile.substring(0, destFile.indexOf(".p12"));
                    fName += "_" + keyAlias + ".p12";
                }
                FileOutputStream out = new FileOutputStream(fName);
                outputKeyStore.store(out, destPwd);
                out.close();
                outputKeyStore.deleteEntry(keyAlias);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        KeyStoreConv c = new KeyStoreConv();
//        c.PKCS12ToJKS("C:\\lamb.p12","123456","C:\\lamb.jks","123456");
        c.JSKToPKCS12("C:\\lamb.jks","123456","C:\\jks2lamb.p12","123456");
//        String flag = "P2J";
//        if(args.length<5) {
//            System.out.println("用法：");
//            System.out.println("    KeyStoreConv <转换标志> <源证书库文件名> <源证书库密码> <目标证书库文件名> <目标证书库密码>");
//            System.out.println("    转换标志: P2J -- 从PKCS12转换为JKS格式");
//            System.out.println("             J2P -- 从JKS转换为PKCS12格式");
//            System.out.println("      注意：　1、如果从JKS转换为PKCS12且源JKS中有多个密钥对或证书，则每个密钥对或证书单独保存为一个文件。");
//        }else{
//            flag = args[0].toUpperCase();
//            if (!(flag.equals("P2J") || flag.equals("J2P"))) flag = "P2J";
//
//
//            if (flag.equals("P2J")) {
////                c.PKCS12ToJKS(args[1], args[2], args[3], args[4]);
//                c.PKCS12ToJKS("C:\\lamb.p12","123456","C:\\lamb.jks","123456");
//            } else {
//                c.JSKToPKCS12(args[1], args[2], args[3], args[4]);
//            }
//        }
    }
}