Sistema de Monitoramento de Efeitos Adversos (C3 - MongoDB)

Este projeto é a entrega da C3 para a disciplina de Banco de Dados, focado na migração de um sistema Java/MySQL (C2) para uma arquitetura Java/MongoDB (C3).

O sistema simula um portal onde usuários podem cadastrar efeitos adversos sentidos após tomarem vacinas, permitindo a geração de relatórios complexos sobre esses dados.

Vídeo Demonstrativo (Item 8.a.iv)
https://youtu.be/V2HuZVt-CSM
Clique aqui para assistir à demonstração no YouTube

Equipe

Paulo Henrique do Nascimento

Luis Felipe Andrade

Murillo Daré

Eduardo Gobbi

Pedro Henrique Pires

Disciplina

Matéria: Banco de Dados

Professor: Howard Roatti

Funcionalidades (C3)

Banco de Dados NoSQL: O sistema foi 100% migrado do MySQL para o MongoDB.

CRUD Completo (Item 7): Funcionalidades de Inserir, Atualizar e Remover para todas as 3 entidades do sistema:

Usuario

Vacina

RegistroEfeitoAdverso

Script de Migração (Item 4): Um script (Migracao.java) foi criado para ler todos os dados do banco relacional (C2) e inseri-los corretamente no banco não-relacional (C3).

Relatórios Complexos (Item 7.a):

Relatório com Junção ($lookup): Lista os efeitos adversos, trazendo o nome do usuário e o nome da vacina (simulando um JOIN triplo).

Relatório com Agrupamento ($project/$size): Lista todas as vacinas e conta quantos registros de efeitos adversos cada uma possui (simulando um LEFT JOIN + GROUP BY + COUNT).

Verificação de Integridade (Item 7.c.5.i): O sistema impede que um Usuario ou uma Vacina sejam removidos se existirem Registros ("filhos") associados a eles, protegendo a integridade dos dados.

Splash Screen (Item 6.b): Tela de início que exibe os nomes dos componentes e a contagem de documentos em cada coleção do MongoDB.

Menu Dinâmico (Item 6.a): Menu principal em loop que segue a estrutura exigida pelo edital.

Tecnologias Utilizadas

Java 1.8+

Apache Maven (Para gerenciamento das dependências)

MongoDB (Banco de dados NoSQL principal)

MySQL (Usado apenas como fonte de dados para o script de migração)

Como Executar do Zero (Ambiente Linux - Ubuntu)

Estas são as instruções completas para instalar e rodar o projeto em um ambiente Linux (Ubuntu/Debian) limpo, conforme solicitado no edital.

Passo 0: Instalar as Ferramentas (Java, Git, Maven, MySQL e MongoDB)

# 1. Atualize os pacotes do Linux
sudo apt update

# 2. Instale o Java (JDK), Git, Maven e o servidor MongoDB
sudo apt install openjdk-17-jdk git maven mongodb -y

# 3. Instale o servidor MySQL (necessário APENAS para o script de migração)
sudo apt install mysql-server -y

# 4. Inicie os serviços do MySQL e MongoDB
sudo systemctl start mysql
sudo systemctl start mongodb

# 5. (Opcional, se o MySQL pedir senha) Configure a senha do MySQL
# Se o MySQL não deixar o script de migração conectar, rode os comandos abaixo
# sudo mysql -u root
# ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'root';
# FLUSH PRIVILEGES;
# EXIT;


Passo 1: Clonar e Preparar o Projeto

# 1. Clone o repositório (substitua pela URL do seu GitHub)
# Ex: git clone [https://github.com/paulosantoonac/projeto-c3-banco-de-dados.git](https://github.com/paulosantoonac/projeto-c3-banco-de-dados.git)
git clone https://github.com/paulosantosnasc/projeto-c3-banco-de-dados.git

# 2. Entre na pasta do projeto
# (O nome da pasta pode ser 'monitor-vacina-app' ou o nome do seu repositório)
cd nome-da-pasta-do-projeto

# 3. Compile o projeto e baixe as dependências (drivers)
mvn clean install

# 4. (IMPORTANTE) Popule o banco MySQL da C2
# Você deve ter o seu banco 'monitor_vacina_db' da C2 pronto
# e com os 4 usuários que apareceram no vídeo.
# Ex: mysql -u root -p monitor_vacina_db < /caminho/para/seu/script_C2.sql


Passo 2: Rodar o Script de Migração (Item 4 do Edital)

Este passo só precisa ser executado UMA VEZ. Ele copia os dados do MySQL (C2) para o MongoDB (C3).

(Garanta que seu MySQL e seu MongoDB estão ambos LIGADOS)

# No terminal, execute o script de migração
mvn exec:java -Dexec.mainClass="br.com.faesa.monitorvacina.principal.Migracao"


Você deverá ver no console as mensagens "Conectou no MySQL...", "Conectou no MongoDB...", "Migrando tabela USUARIO...", e "MIGRAÇÃO CONCLUÍDA COM SUCESSO!".

Passo 3: Rodar o Programa Principal (C3)

Após a migração, o MySQL não é mais necessário. O sistema agora usa 100% o MongoDB.

(Garanta que seu MongoDB está LIGADO)

# No terminal, execute a classe Principal
mvn exec:java -Dexec.mainClass="br.com.faesa.monitorvacina.principal.Principal"


O Splash Screen aparecerá, seguido do menu principal, e o sistema estará pronto para uso.

Estrutura de Pastas (Simplificada)

src/main/java/br/com/faesa/monitorvacina/

conexion/: Contém ConexaoMySQL.java e ConexaoMongoDB.java.

controller/: Contém os "cérebros" (UsuarioController, VacinaController, etc.).

model/: Contém as classes "molde" (Usuario, Vacina, etc.).

principal/: Contém o Principal.java (menu) e Migracao.java (script).

utils/: Contém o SplashScreen.java.

pom.xml: O arquivo do Maven com as dependências.
