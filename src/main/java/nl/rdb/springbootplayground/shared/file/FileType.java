package nl.rdb.springbootplayground.shared.file;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

import org.apache.tika.Tika;

import com.google.common.collect.Sets;

public enum FileType {
    PDF(Set.of(new byte[] { 0x25, 0x50, 0x44, 0x46, 0x2D })),
    PNG(Set.of(new byte[] { (byte) 0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A })),
    JPG(Set.of(new byte[] { (byte) 0xFF, (byte) 0xD8, (byte) 0xFF, (byte) 0xE0 })),
    JPEG(Set.of(
            new byte[] { (byte) 0xFF, (byte) 0xD8, (byte) 0xFF, (byte) 0xDB },
            new byte[] { (byte) 0xFF, (byte) 0xD8, (byte) 0xFF, (byte) 0xE0, 0x00, 0x10, 0x4A, 0x46, 0x49, 0x46, 0x00, 0x01 },
            new byte[] { (byte) 0xFF, (byte) 0xD8, (byte) 0xFF, (byte) 0xEE },
            new byte[] { (byte) 0xFF, (byte) 0xD8, (byte) 0xFF, (byte) 0xE1 })),
    CSV,
    TXT(Set.of(
            new byte[] { 0x00, 0x01, 0x00, 0x00, 0x00 },
            new byte[] { (byte) 0xFF, (byte) 0xFE },
            new byte[] { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF },
            new byte[] { (byte) 0xFE, (byte) 0xFF },
            new byte[] { 0x00, 0x00, (byte) 0xFE, (byte) 0xFF },
            new byte[] { 0x0E, (byte) 0xFE, (byte) 0xFF })),
    ZIP(
            Set.of(
                    new byte[] { 0x50, 0x4B, 0x03, 0x04 },
                    new byte[] { 0x50, 0x4B, 0x05, 0x06 },
                    new byte[] { 0x50, 0x4B, 0x07, 0x08 }
            ),
            Set.of("application/zip",
                    "application/x-zip",
                    "application/x-zip-compressed",
                    "application/octet-stream",
                    "application/zip-compressed",
                    "multipart/x-zip"
            )
    ),
    DOCX(ZIP.magicValues),
    XLSX(ZIP.magicValues),
    PPTX(ZIP.magicValues),
    EXE_COM(Set.of(new byte[] { 0x4D, 0x5A })),
    MSI(Set.of(new byte[] { (byte) 0xD0, (byte) 0xCF, 0x11, (byte) 0xE0, (byte) 0xA1, (byte) 0xB1, 0x1A, (byte) 0xE1 }));

    private static final Tika tika = new Tika();

    private final Set<byte[]> magicValues;
    private Set<String> contentTypesOverride;

    FileType() {
        this.magicValues = Collections.emptySet();
    }

    FileType(Set<byte[]> magicValues) {
        this.magicValues = magicValues;
    }

    FileType(Set<byte[]> magicValues, Set<String> contentTypes) {
        this.magicValues = magicValues;
        this.contentTypesOverride = contentTypes;
    }

    public Set<String> getContentTypes() {
        return Objects.requireNonNullElseGet(this.contentTypesOverride, () -> Sets.newHashSet(tika.detect(getExtension())));
    }

    public String getExtension() {
        return ".%s".formatted(name().toLowerCase());
    }

    public boolean matches(final byte[] magicBytes) {
        return this.magicValues.stream().anyMatch(bytes -> {
            if (magicBytes.length < bytes.length) {
                return false;
            }
            for (var index = 0; index < bytes.length; index++) {
                if (magicBytes[index] != bytes[index]) {
                    return false;
                }
            }
            return true;
        });
    }
}
