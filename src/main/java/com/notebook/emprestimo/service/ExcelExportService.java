package com.notebook.emprestimo.service;

import com.notebook.emprestimo.model.Emprestimo;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ExcelExportService {

    public void exportToExcel(List<Emprestimo> emprestimos, String filePath) throws IOException {
        Workbook workbook = new XSSFWorkbook();  // Cria um arquivo Excel no formato .xlsx
        Sheet sheet = workbook.createSheet("Empréstimos");

        // Criar cabeçalhos
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Código do Aluno");
        headerRow.createCell(1).setCellValue("Notebook");
        headerRow.createCell(2).setCellValue("Data de Retirada");
        headerRow.createCell(3).setCellValue("Status");
        headerRow.createCell(4).setCellValue("Data de Devolução");

        // Preencher os dados dos empréstimos
        int rowNum = 1;
        for (Emprestimo emprestimo : emprestimos) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(emprestimo.getAluno().getQrCode());
            row.createCell(1).setCellValue(emprestimo.getNotebook().getCodigoBarra());
            row.createCell(2).setCellValue(emprestimo.getDataRetirada().toString());
            row.createCell(3).setCellValue(emprestimo.getDataDevolucao() != null ? "Devolvido" : "Em aberto");
            row.createCell(4).setCellValue(emprestimo.getDataDevolucao() != null ? emprestimo.getDataDevolucao().toString() : "Não devolvido");
        }

        // Ajusta a largura das colunas para o conteúdo
        for (int i = 0; i < 5; i++) {
            sheet.autoSizeColumn(i);
        }

        // Salvar o arquivo Excel
        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            workbook.write(fileOut);
        } finally {
            workbook.close();
        }
    }
}
