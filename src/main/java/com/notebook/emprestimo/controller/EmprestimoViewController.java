package com.notebook.emprestimo.controller;

import com.notebook.emprestimo.model.Emprestimo;
import com.notebook.emprestimo.model.Notebook;
import com.notebook.emprestimo.service.AlunoService;
import com.notebook.emprestimo.service.EmprestimoService;
import com.notebook.emprestimo.service.NotebookService;
import com.notebook.emprestimo.service.ExcelExportService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class EmprestimoViewController {

    private final EmprestimoService emprestimoService;
    private final AlunoService alunoService;
    private final NotebookService notebookService;
    private final ExcelExportService excelExportService;

    // Página principal de empréstimos
    @GetMapping("/emprestimos")
    public String listarEmprestimos(Model model) {
        // Contador de notebooks emprestados (independente de devolução)
        long totalEmprestados = emprestimoService.contarTotalEmprestimos();  // Conta todos os empréstimos
        long totalPendentes = emprestimoService.contarEmprestimosPendentes(); // Conta os empréstimos pendentes de devolução

        // Passando os valores para a view
        model.addAttribute("totalEmprestados", totalEmprestados);
        model.addAttribute("totalPendentes", totalPendentes);

        // Lista de empréstimos dos últimos 7 dias
        model.addAttribute("emprestimos", emprestimoService.buscarEmprestimosPorPeriodo(
                java.time.LocalDateTime.now().minusDays(7), java.time.LocalDateTime.now()));

        return "emprestimos"; // Retorna para o template HTML
    }

    // Página para criar um novo empréstimo
    @GetMapping("/novo-emprestimo")
    public String novoEmprestimo() {
        return "novo-emprestimo"; // Retorna a página com o formulário
    }

    // Endpoint para processar o novo empréstimo
    @PostMapping("/emprestimos/novo")
    public String registrarNovoEmprestimo(@RequestParam String qrCodeAluno, @RequestParam String codigoNotebook, Model model) {
        try {
            // Buscar ou criar aluno
            var aluno = alunoService.buscarOuCriarAluno(qrCodeAluno);

            // Buscar ou criar notebook
            var notebook = notebookService.buscarOuCriarNotebook(codigoNotebook);

            // Verificar se o notebook está disponível
            if (!notebook.isDisponivel()) {
                model.addAttribute("error", "Notebook já está emprestado.");
                return "novo-emprestimo";  // Retorna o formulário com erro
            }

            // Registrar o empréstimo
            emprestimoService.registrarEmprestimo(aluno, notebook);
            notebookService.marcarComoIndisponivel(notebook);

            return "redirect:/emprestimos";  // Redireciona para a lista de empréstimos
        } catch (Exception e) {
            model.addAttribute("error", "Erro ao registrar o empréstimo: " + e.getMessage());
            return "novo-emprestimo";  // Retorna o formulário com erro
        }
    }

    // Endpoint para registrar a devolução de um notebook
    @PostMapping("/emprestimos/devolucao")
    public String registrarDevolucao(@RequestParam String codigoNotebook, Model model) {
        try {
            // Buscar o notebook pelo código
            Optional<Notebook> notebookOpt = notebookService.buscarPorCodigoBarra(codigoNotebook);

            // Verifica se o notebook foi encontrado
            if (notebookOpt.isEmpty()) {
                model.addAttribute("error", "Notebook não encontrado.");
                return "redirect:/emprestimos";
            }

            // Buscar o empréstimo ativo pelo notebook
            var emprestimo = emprestimoService.buscarEmprestimoAtivoPorNotebook(notebookOpt.get());

            // Verifica se o empréstimo foi encontrado
            if (emprestimo.isEmpty()) {
                model.addAttribute("error", "Notebook não está emprestado.");
                return "redirect:/emprestimos";
            }

            // Se o empréstimo existe, registra a devolução
            var emprestimoAtualizado = emprestimoService.registrarDevolucao(emprestimo.get());

            // Acessa o notebook do empréstimo atualizado
            var notebookDevolvido = emprestimoAtualizado.getNotebook();

            // Marca o notebook como disponível novamente
            notebookService.marcarComoDisponivel(notebookDevolvido);

            // Adiciona mensagem de sucesso
            model.addAttribute("success", "Devolução registrada com sucesso!");
            return "redirect:/emprestimos";  // Redireciona para a lista de empréstimos
        } catch (Exception e) {
            model.addAttribute("error", "Erro ao registrar devolução: " + e.getMessage());
            return "redirect:/emprestimos";  // Redireciona para a lista de empréstimos com erro
        }
    }

    // Endpoint para exportar os dados dos empréstimos para um arquivo Excel
    @GetMapping("/exportar-para-excel")
    @ResponseBody
    public ResponseEntity<byte[]> exportarParaExcel() {
        try {
            // Supondo que você tenha uma lista de empréstimos
            List<Emprestimo> emprestimos = emprestimoService.buscarTodosEmprestimos(); // Aqui, você precisa obter os empréstimos corretamente

            // Criando o arquivo Excel
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Emprestimos");

            // Criando a linha de cabeçalho
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Código do Aluno");
            headerRow.createCell(1).setCellValue("Notebook");
            headerRow.createCell(2).setCellValue("Data de Retirada");
            headerRow.createCell(3).setCellValue("Data de Devolução");
            headerRow.createCell(4).setCellValue("Status");

            // Preenchendo os dados
            int rowNum = 1;
            for (Emprestimo emprestimo : emprestimos) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(emprestimo.getAluno().getQrCode());
                row.createCell(1).setCellValue(emprestimo.getNotebook().getCodigoBarra());
                row.createCell(2).setCellValue(emprestimo.getDataRetirada().toString()); // Use formato de data
                row.createCell(3).setCellValue(emprestimo.getDataDevolucao() != null ? emprestimo.getDataDevolucao().toString() : "Não devolvido");
                row.createCell(4).setCellValue(emprestimo.getDataDevolucao() != null ? "Devolvido" : "Em aberto");
            }

            // Escrevendo os dados no byte array
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            workbook.write(byteArrayOutputStream);
            workbook.close();

            // Obter a data atual e formatá-la
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            String formattedDate = currentDate.format(formatter);

            // Definindo cabeçalhos para o download do arquivo
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=emprestimos_" + formattedDate + ".xlsx");

            return new ResponseEntity<>(byteArrayOutputStream.toByteArray(), headers, HttpStatus.OK);

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    // Endpoint para remover todos os empréstimos e notebooks
    @PostMapping("/remover-todos-dados")
    public String removerTodosDados(Model model) {
        try {
            // Remover todos os empréstimos e notebooks
            emprestimoService.removerTodosEmprestimos();
            emprestimoService.removerTodosNotebooks();  // Remover notebooks também, se necessário

            // Adicionar mensagem de sucesso
            model.addAttribute("success", "Todos os empréstimos e notebooks foram removidos com sucesso!");
        } catch (Exception e) {
            model.addAttribute("error", "Erro ao remover os dados: " + e.getMessage());
        }

        // Redirecionar de volta para a página de empréstimos
        return "redirect:/emprestimos";  // Mantém o usuário na página de empréstimos
    }

}
