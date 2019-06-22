package cnlamb7758.pdf.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.List;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Pdfutil {


    /**
     * //1、生成一个PDF
     * @throws FileNotFoundException
     * @throws DocumentException
     */
    public static void createPDF() throws FileNotFoundException, DocumentException {
        //Step 1—Create a Document.
        Document document = new Document();
        //Step 2—Get a PdfWriter instance.
        PdfWriter.getInstance(document, new FileOutputStream( "C:/createSamplePDF.pdf"));
        //Step 3—Open the Document.
        document.open();
        //Step 4—Add content.
        document.add(new Paragraph("Hello World"));
        //Step 5—Close the Document.
        document.close();
    }

    /**
     * 2、页面大小,页面背景色,页边空白,Title,Author,Subject,Keyword
     * @throws FileNotFoundException
     * @throws DocumentException
     */
    public static void setPDF() throws FileNotFoundException, DocumentException {
        //页面大小
        Rectangle rect = new Rectangle(PageSize.B5.rotate());
        //页面背景色
        rect.setBackgroundColor(BaseColor.ORANGE);

        Document doc = new Document(rect);

        PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream( "C:/setSamplePDF.pdf"));

        //PDF版本(默认1.4)
        writer.setPdfVersion(PdfWriter.PDF_VERSION_1_2);

        //文档属性
        doc.addTitle("Title@sample");
        doc.addAuthor("Author@rensanning");
        doc.addSubject("Subject@iText sample");
        doc.addKeywords("Keywords@iText");
        doc.addCreator("Creator@iText");

        //页边空白
        doc.setMargins(10, 20, 30, 40);

        doc.open();
        doc.add(new Paragraph("Hello World"));
        doc.close();
    }

    /**
     * 2、页面大小,页面背景色,页边空白,Title,Author,Subject,Keyword
     * @throws FileNotFoundException
     * @throws DocumentException
     */
    public static void setPassword() throws FileNotFoundException, DocumentException {
        Document doc = new Document();
        PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream( "C:/setPassword.pdf"));
        // Hello 只读     World  所有权限
        writer.setEncryption("Hello".getBytes(), "World".getBytes(),
                PdfWriter.ALLOW_SCREENREADERS,
                PdfWriter.STANDARD_ENCRYPTION_128);
        doc.open();
        doc.add(new Paragraph("Hello World"));
        doc.close();
    }

    /**
     * 添加水印(背景图)
     * @throws FileNotFoundException
     * @throws DocumentException
     */
    public static void setWatermark() throws IOException, DocumentException {
        //图片水印
        PdfReader reader = new PdfReader("C:\\bao.pdf");
        PdfStamper stamp = new PdfStamper(reader, new FileOutputStream("C:\\setWatermark2.pdf"));

        Image img = Image.getInstance("C:\\test.png");
        img.setAbsolutePosition(200, 400);
        PdfContentByte under = stamp.getUnderContent(1);
        under.addImage(img);

        //文字水印
        PdfContentByte over = stamp.getOverContent(2);
        over.beginText();
        BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI,
                BaseFont.EMBEDDED);
        over.setFontAndSize(bf, 18);
        over.setTextMatrix(30, 30);
        over.showTextAligned(Element.ALIGN_LEFT, "lamb7758", 230, 430, 45);
        over.endText();

        //背景图
        Image img2 = Image.getInstance("c:\\login.png");
        img2.setAbsolutePosition(100, 100);
        PdfContentByte under2 = stamp.getUnderContent(3);
        under2.addImage(img2);

        stamp.close();
        reader.close();

    }

    /**
     * 插入Chunk, Phrase, Paragraph, List
     * @throws Exception
     * @throws DocumentException
     */
    public static void addConetent() throws IOException, DocumentException {
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream( "C:/addConetent.pdf"));
        document.open();
        //Chunk对象: a String, a Font, and some attributes
        document.add(new Chunk("China1"));
        document.add(new Chunk(" "));
        Font font = new Font(Font.FontFamily.HELVETICA, 6, Font.BOLD, BaseColor.WHITE);
        Chunk id = new Chunk("chinese1", font);
        id.setBackground(BaseColor.BLACK, 1f, 0.5f, 1f, 1.5f);
        id.setTextRise(6);
        document.add(id);
        document.add(Chunk.NEWLINE);

        document.add(new Chunk("Japan2"));
        document.add(new Chunk(" "));
        Font font2 = new Font(Font.FontFamily.HELVETICA, 6, Font.BOLD, BaseColor.WHITE);
        Chunk id2 = new Chunk("japanese2", font2);
        id2.setLineHeight(20f);
        id2.setBackground(BaseColor.BLACK, 1f, 0.5f, 1f, 1.5f);
        id2.setTextRise(6);
        id2.setUnderline(0.2f, -2f);
        document.add(id2);
        document.add(Chunk.NEWLINE);

        //Phrase对象: a List of Chunks with leading
        document.newPage();
        document.add(new Phrase("Phrase page3"));

        Phrase director = new Phrase();
        Chunk name = new Chunk("China3");
        name.setUnderline(0.2f, -2f);
        director.add(name);
        director.add(new Chunk(","));
        director.add(new Chunk(" "));
        director.add(new Chunk("chinese3"));
        director.setLeading(24);
        document.add(director);

        Phrase director2 = new Phrase();
        Chunk name2 = new Chunk("Japan4");
        name2.setUnderline(0.2f, -2f);
        director2.add(name2);
        director2.add(new Chunk(","));
        director2.add(new Chunk(" "));
        director2.add(new Chunk("japanese4"));
        director2.setLeading(24);
        document.add(director2);

        //Paragraph对象: a Phrase with extra properties and a newline
        document.newPage();
        document.add(new Paragraph("Paragraph page5"));

        Paragraph info = new Paragraph();
        info.add(new Chunk("China5 "));
        info.add(new Chunk("chinese5"));
        info.add(Chunk.NEWLINE);
        info.add(new Phrase("Japan "));
        info.add(new Phrase("japanese"));
        document.add(info);

        //List对象: a sequence of Paragraphs called ListItem
        document.newPage();
        List list = new List(List.ORDERED);
        for (int i = 0; i < 10; i++) {
            ListItem item = new ListItem(String.format("%s: %d movies",
                    "country" + (i + 1), (i + 1) * 100), new Font(
                    Font.FontFamily.HELVETICA, 6, Font.BOLD, BaseColor.WHITE));
            List movielist = new List(List.ORDERED, List.ALPHABETICAL);
            movielist.setLowercase(List.LOWERCASE);
            for (int j = 0; j < 5; j++) {
                ListItem movieitem = new ListItem("Title" + (j + 1));
                List directorlist = new List(List.UNORDERED);
                for (int k = 0; k < 3; k++) {
                    directorlist.add(String.format("%s, %s", "Name1" + (k + 1),
                            "Name2" + (k + 1)));
                }
                movieitem.add(directorlist);
                movielist.add(movieitem);
            }
            item.add(movielist);
            list.add(item);
        }
        document.add(list);

        document.close();
    }
    /**
     * 插入Anchor, Image, Chapter, Section
     * @throws Exception
     * @throws DocumentException
     */
    public static void addConetent2() throws IOException, DocumentException {
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("C:/addConetent2.pdf"));
        document.open();
        //Anchor对象: internal and external links
        Paragraph country = new Paragraph();
        Anchor dest = new Anchor("lamb7758", new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, BaseColor.BLUE));
        dest.setName("CN");
        dest.setReference("https://www.lamb7758.cn");//external
        country.add(dest);
        country.add(String.format(": %d sites", 10000));
        document.add(country);

        document.newPage();
        Anchor toUS = new Anchor("Go to first page.", new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, BaseColor.BLUE));
        toUS.setReference("#CN");//internal
        document.add(toUS);

        //Image对象
        document.newPage();
        Image img = Image.getInstance("C:/test.png");
        img.setAlignment(Image.LEFT | Image.TEXTWRAP);
        img.setBorder(Image.BOX);
        img.setBorderWidth(10);
        img.setBorderColor(BaseColor.WHITE);
        img.scaleToFit(1000, 72);//大小
        img.setRotationDegrees(-30);//旋转
        document.add(img);

//Chapter, Section对象（目录）
        document.newPage();
        Paragraph title = new Paragraph("Title");
        Chapter chapter = new Chapter(title, 1);

        title = new Paragraph("Section A");
        Section section = chapter.addSection(title);
        section.setBookmarkTitle("bmk");
        section.setIndentation(30);
        section.setBookmarkOpen(false);
        section.setNumberStyle(
                Section.NUMBERSTYLE_DOTTED_WITHOUT_FINAL_DOT);

        Section subsection = section.addSection(new Paragraph("Sub Section A"));
        subsection.setIndentationLeft(20);
        subsection.setNumberDepth(1);

        document.add(chapter);
        document.close();
    }
    /**
     * Header, Footer
     * @throws Exception
     * @throws DocumentException
     */
    public static void headerAndFooter() throws IOException, DocumentException {
        Document doc = new Document();
        PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream( "C:\\setHeaderFooter.pdf"));
        doc.open();

        writer.setPageEvent(new PdfPageEventHelper() {

            public void onEndPage(PdfWriter writer, Document document) {

                PdfContentByte cb = writer.getDirectContent();
                cb.saveState();

                cb.beginText();
                BaseFont bf = null;
                try {
                    bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.EMBEDDED);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                cb.setFontAndSize(bf, 10);

                //Header
                float x = document.top(-20);

                //左
                cb.showTextAligned(PdfContentByte.ALIGN_LEFT,
                        "H-Left",
                        document.left(), x, 0);
                //中
                cb.showTextAligned(PdfContentByte.ALIGN_CENTER,
                        writer.getPageNumber()+ " page",
                        (document.right() + document.left())/2,
                        x, 0);
                //右
                cb.showTextAligned(PdfContentByte.ALIGN_RIGHT,
                        "H-Right",
                        document.right(), x, 0);

                //Footer
                float y = document.bottom(-20);

                //左
                cb.showTextAligned(PdfContentByte.ALIGN_LEFT,
                        "F-Left",
                        document.left(), y, 0);
                //中
                cb.showTextAligned(PdfContentByte.ALIGN_CENTER,
                        writer.getPageNumber()+" page",
                        (document.right() + document.left())/2,
                        y, 0);
                //右
                cb.showTextAligned(PdfContentByte.ALIGN_RIGHT,
                        "F-Right",
                        document.right(), y, 0);

                cb.endText();

                cb.restoreState();
            }
        });

        doc.open();
        doc.add(new Paragraph("1 page"));
        doc.newPage();
        doc.add(new Paragraph("2 page"));
        doc.newPage();
        doc.add(new Paragraph("3 page"));
        doc.newPage();
        doc.add(new Paragraph("4 page"));

        doc.close();
    }

    /**
     * //左右文字
     * @throws FileNotFoundException
     * @throws DocumentException
     */
    public static void showTextAligned() throws FileNotFoundException, DocumentException {
        //Step 1—Create a Document.
        Document document = new Document();
        //Step 2—Get a PdfWriter instance.
        PdfWriter writer =   PdfWriter.getInstance(document, new FileOutputStream( "C:/showTextAligned.pdf"));

        //Step 3—Open the Document.
        document.open();
        PdfContentByte canvas = writer.getDirectContent();

        Phrase phrase1 = new Phrase("This is a test!left");
        Phrase phrase2 = new Phrase("This is a test!right");
        Phrase phrase3 = new Phrase("This is a test!center");
        ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, phrase1, 100, 0, 0);
        ColumnText.showTextAligned(canvas, Element.ALIGN_RIGHT, phrase2, 100, 536, 0);
        ColumnText.showTextAligned(canvas, Element.ALIGN_CENTER, phrase3, 100, 572, 0);
        document.close();
    }

    /**
     * //幻灯片放映
     * @throws FileNotFoundException
     * @throws DocumentException
     */
    public static void setTransition() throws FileNotFoundException, DocumentException {
        Document doc = new Document();
        PdfWriter writer =   PdfWriter.getInstance(doc, new FileOutputStream( "C:/setTransition.pdf"));

        writer.setPdfVersion(PdfWriter.VERSION_1_5);

        writer.setViewerPreferences(PdfWriter.PageModeFullScreen);//全屏
        writer.setPageEvent(new PdfPageEventHelper() {
            public void onStartPage(PdfWriter writer, Document document) {
                writer.setTransition(new PdfTransition(PdfTransition.DISSOLVE, 3));
                writer.setDuration(5);//间隔时间
            }
        });

        doc.open();
        doc.add(new Paragraph("1 page"));
        doc.newPage();
        doc.add(new Paragraph("2 page"));
        doc.newPage();
        doc.add(new Paragraph("3 page"));
        doc.newPage();
        doc.add(new Paragraph("4 page"));
        doc.newPage();
        doc.add(new Paragraph("5 page"));
        doc.close();
    }
    /**
     * //压缩PDF到Zip
     * @throws FileNotFoundException
     * @throws DocumentException
     */
    public static void zippdf() throws Exception, DocumentException {
        ZipOutputStream zip = new ZipOutputStream(new FileOutputStream( "C:\\zipPDF.zip"));
        for (int i = 1; i <= 3; i++) {
            ZipEntry entry = new ZipEntry("hello_" + i + ".pdf");
            zip.putNextEntry(entry);
            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, zip);
            writer.setCloseStream(false);
            document.open();
            document.add(new Paragraph("Hello " + i));
            document.close();
            zip.closeEntry();
        }
        zip.close();
    } /**
     * //压缩PDF到Zip
     * @throws FileNotFoundException
     * @throws DocumentException
     */
    public static void Annotation() throws Exception, DocumentException {
        Document doc = new Document();
        PdfWriter writer =   PdfWriter.getInstance(doc, new FileOutputStream( "C:/Annotation.pdf"));

        writer.setLinearPageMode();

        doc.open();
        doc.add(new Paragraph("1 page"));
        doc.add(new Annotation("Title", "This is a annotation!"));





        String myString = "https://www.lamb7758.cn";
//        myString = "LaMB";

        Barcode128 code128 = new Barcode128();
        code128.setCode(myString.trim());
        code128.setCodeType(Barcode128.CODE128);
//        code128.setFont(null);   //去除条码下面的字
        BaseFont  bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.EMBEDDED);
//        Font fontsize8 = new Font(bf,8,Font.BOLD);
        code128.setFont(bf);   //去除条码下面的字

        PdfContentByte cb = writer.getDirectContent();
        Image code128Image = code128.createImageWithBarcode(cb, null, null);

        code128Image.setAbsolutePosition(10,700);
        code128Image.scalePercent(125);
        doc.add(code128Image);

        BarcodeQRCode qrcode = new BarcodeQRCode(myString.trim(), 1, 1, null);
        Image qrcodeImage = qrcode.getImage();
        qrcodeImage.setAbsolutePosition(10,600);
        qrcodeImage.scalePercent(200);
        doc.add(qrcodeImage);






        doc.newPage();
        doc.add(new Paragraph("2 page"));
        Chunk chunk = new Chunk("\u00a03333333");
        chunk.setAnnotation(PdfAnnotation.createText(writer, null, "Title", "yi----This is a another annotation!", false, "Comment"));
        doc.add(chunk);

        //添加附件
        doc.newPage();
        doc.add(new Paragraph("3 page"));
        Chunk chunk2 = new Chunk("\u00a0\u00a0");
        PdfAnnotation annotation = PdfAnnotation.createFileAttachment(
                writer, null, "Title", null,
                "C://test.png",
                "img.png");
        annotation.put(PdfName.NAME,  new PdfString("4444444"));
        chunk2.setAnnotation(annotation);
        doc.add(chunk2);




        doc.close();
    }

    public static void main(String[] args) throws Exception, DocumentException {
//        createPDF();
//        setPDF();
        setPassword();
//        setWatermark();
//        addConetent2();
//        headerAndFooter();
//        showTextAligned();
//        setTransition();
//        zippdf();
//        Annotation();


    }


}
