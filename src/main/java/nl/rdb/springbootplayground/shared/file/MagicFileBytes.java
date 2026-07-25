package nl.rdb.springbootplayground.shared.file;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class MagicFileBytes {

    private MagicFileBytes() {
        throw new AssertionError("Called private constructor of util-class %s.".formatted(this.getClass().getName()));
    }

    private static final Map<String, FileType> EXPECTED_FILE_TYPE_BYTES = Map.of(
            "application/pdf", FileType.PDF,
            "image/png", FileType.PNG,
            "image/jpeg", FileType.JPEG,
            "application/vnd.openxmlformats-officedocument.presentationml.presentation", FileType.PPTX,
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document", FileType.DOCX,
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", FileType.XLSX,
            "application/zip", FileType.ZIP
    );

    private static byte[] readIdentifyingBytes(InputStream inputStream) throws IOException {
        byte[] bytes = new byte[12];

        int i = 0;
        while (i < 12 && inputStream.available() > 0) {
            bytes[i] = (byte) inputStream.read();
            i++;
        }

        return bytes;
    }
}
