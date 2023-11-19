package com.barack.securebanksystem.service.Impl;

import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlException;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class WordDocumentGenerator {

    public static void main(String[] args) {
        try {
            generateWordDocument("D:\\Spring-Boot\\Reactive-Programming\\secure-bank-system\\src\\file\\bank_statement.docx", "D:\\Spring-Boot\\Reactive-Programming\\secure-bank-system\\src\\file\\output.docx");
        } catch (IOException | XmlException e) {
            e.printStackTrace();
        }
    }

    public static void generateWordDocument(String templatePath, String outputPath) throws IOException, XmlException {
        try (FileInputStream fis = new FileInputStream(templatePath);
             XWPFDocument document = new XWPFDocument(fis)) {

            // Replace placeholders with actual values
            Map<String, String> placeholders = new HashMap<>();
            placeholders.put("${customerName}", "John Doe");
            placeholders.put("${startDate}", "2023-01-01");
            placeholders.put("${endDate}", "2023-01-31");
            placeholders.put("${address}", "6427 NW Edmonton, Alberta");

            replacePlaceholders(document, placeholders);

            // Format the first table in the document
            formatFirstTable(document);

            // Set page layout properties
            setPageLayout(document);

            // Save the modified document
            try (FileOutputStream fos = new FileOutputStream(outputPath)) {
                document.write(fos);
            }
        }
    }

    private static void replacePlaceholders(XWPFDocument document, Map<String, String> placeholders) {
        for (XWPFParagraph paragraph : document.getParagraphs()) {
            for (Map.Entry<String, String> entry : placeholders.entrySet()) {
                String placeholder = entry.getKey();
                String value = entry.getValue();
                replaceText(paragraph, placeholder, value);
            }
        }
    }

    private static void replaceText(XWPFParagraph paragraph, String find, String replace) {
        for (XWPFRun run : paragraph.getRuns()) {
            String text = run.getText(0);
            if (text != null && text.contains(find)) {
                int start = text.indexOf(find);
                int end = start + find.length();
                run.setText(text.substring(0, start) + replace + text.substring(end), 0);
            }
        }
    }


    private static void formatFirstTable(XWPFDocument document) {
        // Assume the first table needs special formatting
        XWPFTable firstTable = document.getTables().get(0);
        formatTable(firstTable);
    }

    private static void formatTable(XWPFTable table) {
        CTTblPr tblPr = table.getCTTbl().addNewTblPr();
        CTTblWidth tblWidth = tblPr.addNewTblW();
        tblWidth.setType(STTblWidth.DXA);
        tblWidth.setW(BigInteger.valueOf(5000));  // Set width in twentieths of a point

        for (XWPFTableRow row : table.getRows()) {
            for (XWPFTableCell cell : row.getTableCells()) {
                CTTcPr cellPr = cell.getCTTc().addNewTcPr();
                // Set additional cell properties as needed
            }
        }
    }

    private static void setPageLayout(XWPFDocument document) {
        CTDocument1 doc = document.getDocument();
        CTBody body = doc.getBody();
        CTSectPr sectPr = body.getSectPr();
        if (sectPr == null) {
            sectPr = body.addNewSectPr();
        }

        // Set page size and orientation
        CTPageSz pageSize = sectPr.isSetPgSz() ? sectPr.getPgSz() : sectPr.addNewPgSz();
        pageSize.setW(BigInteger.valueOf(12240));  // Page width in twentieths of a point
        pageSize.setH(BigInteger.valueOf(15840));  // Page height in twentieths of a point
        sectPr.getPgSz().setOrient(STPageOrientation.LANDSCAPE);  // Set landscape orientation

        // Set page margins
        CTPageMar pageMar = sectPr.isSetPgMar() ? sectPr.getPgMar() : sectPr.addNewPgMar();
        pageMar.setTop(BigInteger.valueOf(1440));  // 1 inch margin at the top
        pageMar.setBottom(BigInteger.valueOf(1440));  // 1 inch margin at the bottom
        pageMar.setLeft(BigInteger.valueOf(1440));  // 1 inch margin at the left
        pageMar.setRight(BigInteger.valueOf(1440));  // 1 inch margin at the right
    }
}

