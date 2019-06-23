package cnlamb7758.pdf.certificate;

import java.io.FileInputStream;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

public class Test {
    public static void main(String[] args) throws Exception {
        CertificateFactory factory=CertificateFactory.getInstance("X.509");
//        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        FileInputStream in=new FileInputStream("C:\\app.cer");
        Certificate certificate=factory.generateCertificate(in);
        System.out.println(  certificate.getPublicKey().getAlgorithm());
    }
}
