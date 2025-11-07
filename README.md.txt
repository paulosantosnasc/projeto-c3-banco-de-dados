💉 Sistema de Monitoramento de Efeitos Adversos (C3 - MongoDB)

Este projeto é a entrega da C3 para a disciplina de Banco de Dados, focado na migração de um sistema JavaMySQL (C2) para uma arquitetura JavaMongoDB (C3).

O sistema simula um portal onde usuários podem cadastrar efeitos adversos sentidos após tomarem vacinas, permitindo a geração de relatórios complexos sobre esses dados.

👥 Equipe

Paulo Henrique do Nascimento

Luis Felipe Andrade

Murillo Daré

Eduardo Gobbi

Pedro henrique pires

👨‍🏫 Disciplina

Matéria Banco de Dados

Professor Howard Roatti

✨ Funcionalidades (C3)

Banco de Dados NoSQL O sistema foi 100% migrado do MySQL para o MongoDB.

CRUD Completo (Item 7) Funcionalidades de Inserir, Atualizar e Remover para todas as 3 entidades do sistema

Usuario

Vacina

RegistroEfeitoAdverso

Script de Migração (Item 4) Um script (Migracao.java) foi criado para ler todos os dados do banco relacional (C2) e inseri-los corretamente no banco não-relacional (C3).

Relatórios Complexos (Item 7.a)

Relatório com Junção ($lookup) Lista os efeitos adversos, trazendo o nome do usuário e o nome da vacina (simulando um JOIN triplo).

Relatório com Agrupamento ($project$size) Lista todas as vacinas e conta quantos registros de efeitos adversos cada uma possui (simulando um LEFT JOIN + GROUP BY + COUNT).

Verificação de Integridade (Item 7.c.5.i) O sistema impede que um Usuario ou uma Vacina sejam removidos se existirem Registros (filhos) associados a eles, protegendo a integridade dos dados.

Splash Screen (Item 6.b) Tela de início que exibe os nomes dos componentes e a contagem de documentos em cada coleção do MongoDB.

Menu Dinâmico (Item 6.a) Menu principal em loop que segue a estrutura exigida pelo edital.

🛠️ Tecnologias Utilizadas

Java 1.8+

Apache Maven Para gerenciamento das dependências (MySQL e MongoDB drivers).

MongoDB Banco de dados NoSQL principal.

MySQL Usado apenas como fonte de dados para o script de migração.

⚠️ Pré-requisitos

Para executar este projeto, você DEVE ter os seguintes serviços instalados e rodando

Java JDK 1.8 (ou superior).

Apache Maven.

Servidor MongoDB Rodando localmente na porta padrão (mongodblocalhost27017).

Servidor MySQL Rodando localmente (localhost3306) e com o banco monitor_vacina_db da C2 (com dados) populado. (Obrigatório apenas para o Passo 1 da execução).

🚀 Como Executar o Projeto (Linux  Windows  macOS)

Este projeto é gerenciado pelo Maven, então a execução é feita pelo terminal.

# 1. Clone o repositório
git clone URL_DO_SEU_REPOSITORIO_AQUI

# 2. Entre na pasta do projeto
cd monitor-vacina-app

# 3. Compile o projeto e baixe as dependências
mvn clean install


Passo 1 Rodar o Script de Migração (Item 4 do Edital)

IMPORTANTE Este passo só precisa ser executado UMA VEZ. Ele copia os dados do seu MySQL (C2) para o MongoDB (C3).

(Garanta que seu MySQL e seu MongoDB estão ambos LIGADOS)

# No terminal, execute o script de migração
mvn execjava -Dexec.mainClass=br.com.faesa.monitorvacina.principal.Migracao


Você deverá ver no console as mensagens Conectou no MySQL..., Conectou no MongoDB..., Migrando tabela USUARIO..., e MIGRAÇÃO CONCLUÍDA COM SUCESSO!.

Passo 2 Rodar o Programa Principal (C3)

Após a migração, você pode rodar o sistema principal, que agora usa 100% o MongoDB.

(Garanta que seu MongoDB está LIGADO)

# No terminal, execute a classe Principal
mvn execjava -Dexec.mainClass=br.com.faesa.monitorvacina.principal.Principal


O Splash Screen aparecerá, seguido do menu principal, e o sistema estará pronto para uso.

🗂️ Estrutura de Pastas (Simplificada)

srcmainjavabrcomfaesamonitorvacina

conexion Contém as classes ConexaoMySQL.java e ConexaoMongoDB.java.

controller Contém os Controllers (IController, UsuarioController, VacinaController, etc.) que são o cérebro da aplicação.

model Contém as classes molde (Pessoa, Usuario, Vacina, etc.).

principal Contém o Principal.java (menu principal) e o Migracao.java (script de migração).

utils Contém o SplashScreen.java.

pom.xml O arquivo de configuração do Maven com as dependências.