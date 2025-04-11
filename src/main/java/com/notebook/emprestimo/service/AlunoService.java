package com.notebook.emprestimo.service;

import com.notebook.emprestimo.model.Aluno;
import com.notebook.emprestimo.repository.AlunoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlunoService {

    private final AlunoRepository alunoRepository;

    // Busca ou cria um aluno baseado no QR Code
    public Aluno buscarOuCriarAluno(String qrCode) {
        var alunoOpt = alunoRepository.findByQrCode(qrCode);  // Tenta encontrar o aluno pelo QR Code
        if (alunoOpt.isEmpty()) {
            // Se o aluno não existir, cria ele automaticamente
            Aluno novoAluno = new Aluno();
            novoAluno.setQrCode(qrCode); // Define o QR Code no novo aluno
            alunoRepository.save(novoAluno); // Salva o novo aluno no banco de dados
            return novoAluno;
        }
        return alunoOpt.get();  // Retorna o aluno já existente
    }

    // Método salvar para simplificar o código no controller
    public Aluno salvar(Aluno aluno) {
        return alunoRepository.save(aluno);
    }
}
