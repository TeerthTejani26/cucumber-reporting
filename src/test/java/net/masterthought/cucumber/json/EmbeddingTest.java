package net.masterthought.cucumber.json;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assumptions.assumeThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 * @author <a href="https://github.com/ghostcity">Stefan Gasterstädt</a>
 */
public class EmbeddingTest {

    private static final String NO_DECODING = null;

    public static Iterable<Object[]> data() {
        return asList(new Object[][] {
            createTestCase("application/ecmascript", "console.log('Hello world');", NO_DECODING, ".es"),
            createTestCase("application/gzip", "c29tZSBkYXRh", "some data", "embedding_-1003041823.gz"),
            createTestCase("application/javascript", "alert('Hello World!');", NO_DECODING, ".js"),
            createTestCase("application/json", "c29tZSBkYXRh", "some data", "embedding_-1003041823.json"),
            createTestCase("application/pdf", "c29tZSBkYXRh", "some data", "embedding_-1003041823.pdf"),
            createTestCase("application/vnd.ms-excel", "c29tZSBkYXRh", "some data", "embedding_-1003041823.xls"),
            createTestCase("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "c29tZSBkYXRh", "some data", "embedding_-1003041823.xlsx"),
            createTestCase("application/vnd.tcpdump.pcap", "c29tZSBkYXRh", "some data", "embedding_-1003041823.unknown"),
            createTestCase("application/xml", "c29tZSBkYXRh", "some data", "embedding_-1003041823.xml"),
            createTestCase("application/xslt+xml", "<xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" />", NO_DECODING, ".xslt"),
            createTestCase("application/x-bzip2", "c29tZSBkYXRh", "some data", "embedding_-1003041823.bz2"),
            createTestCase("application/x-tar", "c29tZSBkYXRh", "some data", "embedding_-1003041823.tar"),
            createTestCase("application/zip", "c29tZSBkYXRh", "some data", "embedding_-1003041823.zip"),
            createTestCase("image/bmp", "c29tZSBkYXRh", "some data", "embedding_-1003041823.bmp"),
            createTestCase("image/gif", "c29tZSBkYXRh", "some data", "embedding_-1003041823.gif"),
            createTestCase("image/jpeg", "c29tZSBkYXRh", "some data", "embedding_-1003041823.jpeg"),
            createTestCase("image/png", "c29tZSBkYXRh", "some data", "embedding_-1003041823.png"),
            createTestCase("image/svg", "c29tZSBkYXRh", "some data", "embedding_-1003041823.svg"),
            createTestCase("image/svg+xml", "c29tZSBkYXRh", "some data", "embedding_-1003041823.svg"),
            createTestCase("image/url", "c29tZSBkYXRh", "some data", "embedding_-1003041823.image"),
            createTestCase("js", "c29tZSBkYXRh", "some data", "embedding_-1003041823.unknown"),
            createTestCase("mime/type", "your data", NO_DECODING, ".type"),
            createTestCase("mime/type", "ZnVuY3Rpb24gbG9nZ2VyKG1lc3NhZ2UpIHsgIH0=", "function logger(message) {  }", ".type"),
            createTestCase("my mime TYPE", "abc", NO_DECODING, ".unknown"),
            createTestCase("text/html", "<html />", NO_DECODING, ".html"),
            createTestCase("text/html; charset=UTF-8", "c29tZSBkYXRh", "some data", "embedding_-1003041823.html"),
            createTestCase("text/php", "echo 'Hello World!';", NO_DECODING, ".php"),
            createTestCase("text/plain", "c29tZSBkYXRh", "some data", "embedding_-1003041823.txt"),
            createTestCase("text/xml", "c29tZSBkYXRh", "some data", "embedding_-1003041823.xml"),
            createTestCase("video/mp4", "c29tZSBkYXRh", "some data", "embedding_-1003041823.mp4"),
        });
    }
    
    private static Object[] createTestCase(String mimeType, String data, String decodedData, String fileName) {
        return new Object[] { mimeType, data, decodedData, fileName };
    }
    
    public String mimeType;
    public String data;
    public String decodedData;
    public String fileName;

    @MethodSource("data")
    @ParameterizedTest(name = "\"{0}\" with \"{1}\"")
    void getMimeType_ReturnsMimeType(String mimeType, String data, String decodedData, String fileName) {
        initEmbeddingTest(mimeType, data, decodedData, fileName);
        // given
        Embedding embedding = new Embedding(this.mimeType, this.data);

        // when
        String actualMimeType = embedding.getMimeType();

        // then
        assertThat(actualMimeType).isEqualTo(this.mimeType);
    }

    @MethodSource("data")
    @ParameterizedTest(name = "\"{0}\" with \"{1}\"")
    void getData_ReturnsContent(String mimeType, String data, String decodedData, String fileName) {
        initEmbeddingTest(mimeType, data, decodedData, fileName);
        // given
        Embedding embedding = new Embedding(this.mimeType, this.data);

        // when
        String actualContent = embedding.getData();

        // then
        assertThat(actualContent).isEqualTo(data);
    }

    @MethodSource("data")
    @ParameterizedTest(name = "\"{0}\" with \"{1}\"")
    void getDecodedData_ReturnsDecodedContent(String mimeType, String data, String decodedData, String fileName) {
        initEmbeddingTest(mimeType, data, decodedData, fileName);
        assumeThat(this.decodedData).isNotEqualTo(NO_DECODING);
        
        // given
        Embedding embedding = new Embedding(this.mimeType, this.data);

        // when
        String actualDecodedContent = embedding.getDecodedData();

        // then
        assertThat(actualDecodedContent).isEqualTo(this.decodedData);
    }

    @MethodSource("data")
    @ParameterizedTest(name = "\"{0}\" with \"{1}\"")
    void getFileName_ReturnsFileName(String mimeType, String data, String decodedData, String fileName) {
        initEmbeddingTest(mimeType, data, decodedData, fileName);
        assumeThat(this.fileName).matches("^[^\\.]+\\.[^\\.]+$");

        // given
        Embedding embedding = new Embedding(this.mimeType, this.data);

        // when
        String actualFileName = embedding.getFileName();

        // then
        assertThat(actualFileName).isEqualTo(this.fileName);
    }

    @MethodSource("data")
    @ParameterizedTest(name = "\"{0}\" with \"{1}\"")
    void getExtension_ReturnsFileExtension(String mimeType, String data, String decodedData, String fileName) {
        initEmbeddingTest(mimeType, data, decodedData, fileName);
        // given
        Embedding embedding = new Embedding(this.mimeType, this.data);

        // when
        String actualExtension = embedding.getExtension();

        // then
        assertThat(actualExtension).isEqualTo(this.fileName.split("\\.")[1]);
    }

    @MethodSource("data")
    @ParameterizedTest(name = "\"{0}\" with \"{1}\"")
    void getExtension_UsesExtensionFromNameWhenMIMETypeIsUnknown(String mimeType, String data, String decodedData, String fileName) {
        initEmbeddingTest(mimeType, data, decodedData, fileName);
        // Arrange
        mimeType = "unknown/mimetype";
        data = "c29tZSBkYXRh";
        String name = "example.docx";

        // Creating an embedding here with an unknown MIME type and a name containing a file extension
        Embedding embedding = new Embedding(mimeType, data, name);

        // Act
        String actualExtension = embedding.getExtension();

        // Assert
        assertThat(actualExtension).isEqualTo("docx");
    }

    @MethodSource("data")
    @ParameterizedTest(name = "\"{0}\" with \"{1}\"")
    void getName_ReturnsNull(String mimeType, String data, String decodedData, String fileName) {
        initEmbeddingTest(mimeType, data, decodedData, fileName);
        // given
        Embedding embedding = new Embedding(this.mimeType, this.data);

        // when
        String actualName = embedding.getName();

        // then
        assertThat(actualName).isNull();
    }

    public void initEmbeddingTest(String mimeType, String data, String decodedData, String fileName) {
        this.mimeType = mimeType;
        this.data = data;
        this.decodedData = decodedData;
        this.fileName = fileName;
    }

}
