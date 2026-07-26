package com.ashwin.financetracker.finance_tracker_api.service;

import com.ashwin.financetracker.finance_tracker_api.entity.Transaction;
import com.ashwin.financetracker.finance_tracker_api.entity.User;
import com.ashwin.financetracker.finance_tracker_api.repository.TransactionRepository;
import com.ashwin.financetracker.finance_tracker_api.repository.UserRepository;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.opencsv.CSVWriter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.time.YearMonth;
import java.util.List;

@Service
public class ReportService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public ReportService(TransactionRepository transactionRepository, UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

    // --- SECURITY & DATA FETCHING ---
    private User getAuthenticatedUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private List<Transaction> getTransactionsForMonth(String monthYear) {
        User user = getAuthenticatedUser();
        YearMonth ym = YearMonth.parse(monthYear);
        // Reusing the exact method you already built for the Dashboard!
        return transactionRepository.findByUserIdAndTxnDateBetween(
                user.getId(), ym.atDay(1), ym.atEndOfMonth()
        );
    }

    // --- 1. CSV GENERATION ---
    public byte[] generateCsvReport(String monthYear) {
        List<Transaction> transactions = getTransactionsForMonth(monthYear);
        
        // This holds the file in memory instead of saving to disk
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try (CSVWriter writer = new CSVWriter(new OutputStreamWriter(out))) {
            // Write Header Row
            String[] header = {"Date", "Time", "Type", "Category", "Amount", "Note"};
            writer.writeNext(header);

            // Write Data Rows
            for (Transaction t : transactions) {
                String[] data = {
                        t.getTxnDate().toString(),
                        t.getTxnTime().toString(),
                        t.getType().name(),
                        t.getCategory().getName(),
                        t.getAmount().toString(),
                        t.getNote() != null ? t.getNote() : ""
                };
                writer.writeNext(data);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate CSV file");
        }

        return out.toByteArray();
    }

    // --- 2. PDF GENERATION ---
    public byte[] generatePdfReport(String monthYear) {
        List<Transaction> transactions = getTransactionsForMonth(monthYear);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        // OpenPDF uses a Document object to represent the page
        try (Document document = new Document()) {
            PdfWriter.getInstance(document, out);
            document.open();

            // Create a Title
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Paragraph title = new Paragraph("Transaction Report - " + monthYear, titleFont);
            title.setAlignment(Paragraph.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            // Create a Table with 6 columns
            PdfPTable table = new PdfPTable(6);
            table.setWidthPercentage(100);
            
            // Table Headers
            table.addCell("Date");
            table.addCell("Time");
            table.addCell("Type");
            table.addCell("Category");
            table.addCell("Amount");
            table.addCell("Note");

            // Fill Table Data
            for (Transaction t : transactions) {
                table.addCell(t.getTxnDate().toString());
                table.addCell(t.getTxnTime().toString());
                table.addCell(t.getType().name());
                table.addCell(t.getCategory().getName());
                table.addCell(t.getAmount().toString());
                table.addCell(t.getNote() != null ? t.getNote() : "");
            }

            document.add(table);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate PDF file");
        }

        return out.toByteArray();
    }
}