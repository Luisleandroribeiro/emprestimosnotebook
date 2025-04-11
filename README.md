Resumo do Projeto de Empréstimos de Notebooks

O projeto de Empréstimos de Notebooks é uma aplicação web voltada para o gerenciamento de empréstimos de notebooks em uma instituição, como uma biblioteca ou setor de TI de uma universidade. O sistema permite o registro de novos empréstimos, o acompanhamento do status dos empréstimos e a devolução dos notebooks de forma simples e eficiente.

Funcionalidades Principais:

Registro de Empréstimos:

O sistema permite que os usuários registrem novos empréstimos de notebooks para os alunos, associando cada notebook a um QR Code exclusivo do aluno.

Cada empréstimo possui informações como o código de barras do notebook e a data de retirada.

Acompanhamento de Empréstimos:

É possível visualizar a lista de todos os empréstimos realizados, com informações detalhadas como o código do aluno (QR Code), código de barras do notebook, data de retirada, e o status do empréstimo (Em aberto ou Devolvido).

A tabela de empréstimos exibe a data de devolução se o notebook já foi devolvido. Caso contrário, a data de devolução será marcada como "Não devolvido".

Devolução de Notebooks:

O sistema permite registrar a devolução de notebooks através do código de barras do notebook. A data de devolução é então registrada e o status do empréstimo é alterado para "Devolvido".

Tecnologias e Stack Utilizada:
Backend:

Java: A aplicação foi desenvolvida utilizando a linguagem de programação Java.

Spring Boot: Utilizado para construir a API RESTful que gerencia os empréstimos, cadastro de alunos, e notebooks. O Spring Boot oferece facilidade no desenvolvimento e configuração da aplicação.

JPA (Jakarta Persistence API): Para o mapeamento objeto-relacional (ORM), permitindo a comunicação com o banco de dados e o gerenciamento de entidades como Aluno, Notebook e Emprestimo.

H2 Database (ou outro banco relacional): Um banco de dados relacional foi utilizado para armazenar as informações sobre os empréstimos, alunos e notebooks. O H2 é ideal para desenvolvimento e testes, mas pode ser substituído por bancos de dados como MySQL ou PostgreSQL em produção.

Lombok: Biblioteca que simplifica o código ao gerar automaticamente métodos como getters, setters, toString(), entre outros, utilizando anotações como @Data, @Builder, e @NoArgsConstructor.

Frontend:

HTML & Thymeleaf: O frontend foi desenvolvido utilizando HTML5 e Thymeleaf, um mecanismo de templates do Spring que permite a renderização dinâmica do conteúdo no lado do servidor. Thymeleaf foi utilizado para gerar páginas HTML dinâmicas, como a tabela de empréstimos e os formulários de registro de novos empréstimos e devoluções.
