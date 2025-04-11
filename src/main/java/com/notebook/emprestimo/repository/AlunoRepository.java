package com.notebook.emprestimo.repository;

import com.notebook.emprestimo.model.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AlunoRepository extends JpaRepository<Aluno, Long> {
    Optional<Aluno> findByQrCode(String qrCode);
    Optional<Aluno> findByMatricula(String matricula);
}
