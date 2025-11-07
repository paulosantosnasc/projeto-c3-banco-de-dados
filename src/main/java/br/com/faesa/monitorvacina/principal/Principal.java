package br.com.faesa.monitorvacina.principal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

// Importa TODOS os controllers
import br.com.faesa.monitorvacina.controller.IController;
import br.com.faesa.monitorvacina.controller.UsuarioController;
import br.com.faesa.monitorvacina.controller.VacinaController;
import br.com.faesa.monitorvacina.controller.RegistroEfeitoAdversoController;
import br.com.faesa.monitorvacina.controller.RelatorioController;

// Importa TODOS os models
import br.com.faesa.monitorvacina.model.Usuario;
import br.com.faesa.monitorvacina.model.Vacina;
import br.com.faesa.monitorvacina.model.RegistroEfeitoAdverso;

// Importa o ConexaoMongoDB (para fechar no final) e o SplashScreen
import br.com.faesa.monitorvacina.conexion.ConexaoMongoDB;
import br.com.faesa.monitorvacina.utils.SplashScreen;


public class Principal {
    
    // Declara os controllers e o Scanner como estáticos
    // para que os métodos auxiliares possam usá-los.
    private static Scanner teclado = new Scanner(System.in);
    private static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private static IController usuarioController = new UsuarioController();
    private static IController vacinaController = new VacinaController();
    private static IController registroController = new RegistroEfeitoAdversoController();
    private static RelatorioController relatorioController = new RelatorioController();

    public static void main(String[] args) {
        
        // 1. Mostra a tela de início (já atualizada para MongoDB)
        SplashScreen splash = new SplashScreen();
        splash.exibir();

        // 2. Loop do menu principal
        while (true) {
            
            // Este é o Menu CORRETO do Edital C3 (Item 6.a)
            System.out.println("\n--- MENU PRINCIPAL (MongoDB) ---");
            System.out.println("1 - Relatórios");
            System.out.println("2 - Inserir Documento");
            System.out.println("3 - Remover Documento");
            System.out.println("4 - Atualizar Documento");
            System.out.println("5 - Sair");
            System.out.print("Escolha uma opção: ");

            int opcao = lerInteiro(); // Usamos um leitor de inteiro seguro

            switch (opcao) {
                case 1: // RELATÓRIOS
                    menuRelatorios();
                    break;
                case 2: // INSERIR
                    menuInserir();
                    break;
                case 3: // REMOVER
                    menuRemover();
                    break;
                case 4: // ATUALIZAR
                    menuAtualizar();
                    break;
                case 5: // SAIR
                    System.out.println("Tchau! Saindo do sistema...");
                    teclado.close(); // Fecha o scanner
                    ConexaoMongoDB.fecharConexao(); // Fecha a conexão com o MongoDB
                    System.exit(0); // Encerra o programa
                    break;
                default:
                    System.out.println("!! Opção inválida, digite um número do menu !!");
                    break;
            }
        }
    }

    // --- 1. SUBMENU DE RELATÓRIOS (Item 7.a) ---
    private static void menuRelatorios() {
        System.out.println("\n--- SUBMENU DE RELATÓRIOS ---");
        System.out.println("1 - Listar Efeitos Adversos por Usuário (JOIN)");
        System.out.println("2 - Contagem de Efeitos por Vacina (GROUP BY)");
        System.out.print("Escolha um relatório: ");
        
        int opcaoRelatorio = lerInteiro();
        
        if (opcaoRelatorio == 1) {
            relatorioController.gerarRelatorioRegistrosPorUsuario();
        } else if (opcaoRelatorio == 2) {
            relatorioController.gerarRelatorioContagemPorVacina();
        } else {
            System.out.println("!! Opção de relatório inválida !!");
        }
    }

    // --- 2. SUBMENU DE INSERÇÃO (Item 7.b) ---
    private static void menuInserir() {
        System.out.println("\n--- INSERIR NOVO DOCUMENTO ---");
        System.out.println("1 - Inserir Usuário");
        System.out.println("2 - Inserir Vacina");
        System.out.println("3 - Inserir Registro de Efeito Adverso");
        System.out.println("9 - Voltar ao Menu Principal");
        System.out.print("Escolha uma opção: ");

        int opcao = lerInteiro();
        switch (opcao) {
            case 1:
                inserirUsuario();
                break;
            case 2:
                inserirVacina();
                break;
            case 3:
                inserirRegistro();
                break;
            case 9:
                System.out.println("... voltando ao menu principal.");
                break;
            default:
                System.out.println("!! Opção inválida !!");
                break;
        }
    }
    
    // --- 3. SUBMENU DE REMOÇÃO (Item 7.c) ---
    private static void menuRemover() {
        System.out.println("\n--- REMOVER DOCUMENTO ---");
        System.out.println("1 - Remover Usuário");
        System.out.println("2 - Remover Vacina");
        System.out.println("3 - Remover Registro de Efeito Adverso");
        System.out.println("9 - Voltar ao Menu Principal");
        System.out.print("Escolha uma opção: ");

        int opcao = lerInteiro();
        switch (opcao) {
            case 1:
                removerUsuario();
                break;
            case 2:
                removerVacina();
                break;
            case 3:
                removerRegistro();
                break;
            case 9:
                System.out.println("... voltando ao menu principal.");
                break;
            default:
                System.out.println("!! Opção inválida !!");
                break;
        }
    }

    // --- 4. SUBMENU DE ATUALIZAÇÃO (Item 7.d) ---
    private static void menuAtualizar() {
        System.out.println("\n--- ATUALIZAR DOCUMENTO ---");
        System.out.println("1 - Atualizar Usuário");
        System.out.println("2 - Atualizar Vacina");
        System.out.println("3 - Atualizar Registro de Efeito Adverso");
        System.out.println("9 - Voltar ao Menu Principal");
        System.out.print("Escolha uma opção: ");

        int opcao = lerInteiro();
        switch (opcao) {
            case 1:
                atualizarUsuario();
                break;
            case 2:
                atualizarVacina();
                break;
            case 3:
                atualizarRegistro();
                break;
            case 9:
                System.out.println("... voltando ao menu principal.");
                break;
            default:
                System.out.println("!! Opção inválida !!");
                break;
        }
    }

    // --- MÉTODOS AUXILIARES (INSERIR) ---

    private static void inserirUsuario() {
        System.out.println("\n-- Inserindo Novo Usuário --");
        Usuario novoUsuario = new Usuario();
        
        System.out.print("Digite o nome: ");
        novoUsuario.setNome(teclado.nextLine());
        System.out.print("Digite o CPF: ");
        novoUsuario.setCpf(teclado.nextLine());
        novoUsuario.setDataNascimento(lerData("Digite a data de nascimento (dd/mm/aaaa): "));
        System.out.print("Digite o email: ");
        novoUsuario.setEmail(teclado.nextLine());
        System.out.print("Digite a senha: ");
        novoUsuario.setSenha(teclado.nextLine());
        
        usuarioController.inserir(novoUsuario);
    }

    private static void inserirVacina() {
        System.out.println("\n-- Inserindo Nova Vacina --");
        Vacina novaVacina = new Vacina();
        
        System.out.print("Digite o nome da vacina: ");
        novaVacina.setNome(teclado.nextLine());
        System.out.print("Digite o fabricante: ");
        novaVacina.setFabricante(teclado.nextLine());
        
        vacinaController.inserir(novaVacina);
    }

    private static void inserirRegistro() {
        System.out.println("\n-- Inserindo Novo Registro de Efeito Adverso --");
        
        // 1. Lista os usuários para o usuário poder escolher o ID
        System.out.println("... Listando Usuários disponíveis ...");
        List<Object> usuarios = listarTodos(usuarioController);
        if (usuarios.isEmpty()) {
            System.out.println("!! ERRO: Impossível criar registro. Não há usuários cadastrados !!");
            return;
        }
        
        // 2. Lista as vacinas para o usuário poder escolher o ID
        System.out.println("\n... Listando Vacinas disponíveis ...");
        List<Object> vacinas = listarTodos(vacinaController);
        if (vacinas.isEmpty()) {
            System.out.println("!! ERRO: Impossível criar registro. Não há vacinas cadastrados !!");
            return;
        }

        RegistroEfeitoAdverso novoRegistro = new RegistroEfeitoAdverso();
        
        System.out.print("\nDigite o ID do Usuário que tomou a vacina: ");
        novoRegistro.setIdUsuario(lerInteiro());
        
        System.out.print("Digite o ID da Vacina aplicada: ");
        novoRegistro.setIdVacina(lerInteiro());
        
        novoRegistro.setDataVacinacao(lerData("Digite a data da vacinação (dd/mm/aaaa): "));
        System.out.print("Digite a descrição do efeito adverso: ");
        novoRegistro.setDescricaoUsuario(teclado.nextLine());
        
        // Seta a data do registro como "agora"
        novoRegistro.setDataRegistro(LocalDateTime.now());
        
        registroController.inserir(novoRegistro);
    }

    // --- MÉTODOS AUXILIARES (REMOVER) ---

    private static void removerUsuario() {
        System.out.println("\n-- Removendo Usuário --");
        System.out.println("... Listando Usuários ...");
        List<Object> usuarios = listarTodos(usuarioController);
        if (usuarios.isEmpty()) {
            System.out.println("Nenhum usuário para remover.");
            return;
        }

        System.out.print("\nDigite o ID do Usuário que deseja remover: ");
        int idParaRemover = lerInteiro();
        
        // *** VERIFICAÇÃO DE INTEGRIDADE (Item 7.c.5.i) ***
        // Vamos checar se algum registro "filho" usa esse ID
        List<Object> registros = registroController.listarTodos();
        long countFilhos = registros.stream()
                                  .map(obj -> (RegistroEfeitoAdverso) obj)
                                  .filter(reg -> reg.getIdUsuario() == idParaRemover)
                                  .count();
        
        if (countFilhos > 0) {
            System.out.println("!! ERRO: Este usuário (ID: " + idParaRemover + ") não pode ser removido!");
            System.out.println("!! Motivo: Ele possui " + countFilhos + " registro(s) de efeitos adversos associados.");
            System.out.println("!! (Conforme Edital 7.c.5.i, a remoção foi bloqueada).");
            return; // Aborta a remoção
        }
        
        // Se chegou aqui, não tem filhos, pode remover.
        System.out.print("Tem certeza que deseja remover o usuário ID " + idParaRemover + "? (S/N): ");
        if (teclado.nextLine().equalsIgnoreCase("S")) {
            Usuario usuarioParaRemover = new Usuario();
            usuarioParaRemover.setIdUsuario(idParaRemover);
            usuarioController.remover(usuarioParaRemover);
        } else {
            System.out.println("... remoção cancelada.");
        }
    }

    private static void removerVacina() {
        System.out.println("\n-- Removendo Vacina --");
        System.out.println("... Listando Vacinas ...");
        List<Object> vacinas = listarTodos(vacinaController);
        if (vacinas.isEmpty()) {
            System.out.println("Nenhuma vacina para remover.");
            return;
        }

        System.out.print("\nDigite o ID da Vacina que deseja remover: ");
        int idParaRemover = lerInteiro();
        
        // *** VERIFICAÇÃO DE INTEGRIDADE (Item 7.c.5.i) ***
        List<Object> registros = registroController.listarTodos();
        long countFilhos = registros.stream()
                                  .map(obj -> (RegistroEfeitoAdverso) obj)
                                  .filter(reg -> reg.getIdVacina() == idParaRemover)
                                  .count();
        
        if (countFilhos > 0) {
            System.out.println("!! ERRO: Esta vacina (ID: " + idParaRemover + ") não pode ser removida!");
            System.out.println("!! Motivo: Ela possui " + countFilhos + " registro(s) de efeitos adversos associados.");
            System.out.println("!! (Conforme Edital 7.c.5.i, a remoção foi bloqueada).");
            return; // Aborta a remoção
        }

        System.out.print("Tem certeza que deseja remover a vacina ID " + idParaRemover + "? (S/N): ");
        if (teclado.nextLine().equalsIgnoreCase("S")) {
            Vacina vacinaParaRemover = new Vacina();
            vacinaParaRemover.setIdVacina(idParaRemover);
            vacinaController.remover(vacinaParaRemover);
        } else {
            System.out.println("... remoção cancelada.");
        }
    }

    private static void removerRegistro() {
        System.out.println("\n-- Removendo Registro de Efeito Adverso --");
        System.out.println("... Listando Registros ...");
        List<Object> registros = listarTodos(registroController);
        if (registros.isEmpty()) {
            System.out.println("Nenhum registro para remover.");
            return;
        }

        System.out.print("\nDigite o ID do Registro que deseja remover: ");
        int idParaRemover = lerInteiro();

        System.out.print("Tem certeza que deseja remover o registro ID " + idParaRemover + "? (S/N): ");
        if (teclado.nextLine().equalsIgnoreCase("S")) {
            RegistroEfeitoAdverso registroParaRemover = new RegistroEfeitoAdverso();
            registroParaRemover.setIdRegistro(idParaRemover);
            registroController.remover(registroParaRemover);
        } else {
            System.out.println("... remoção cancelada.");
        }
    }

    // --- MÉTODOS AUXILIARES (ATUALIZAR) ---

    private static void atualizarUsuario() {
        System.out.println("\n-- Atualizando Usuário --");
        System.out.println("... Listando Usuários ...");
        List<Object> usuarios = listarTodos(usuarioController);
        if (usuarios.isEmpty()) {
            System.out.println("Nenhum usuário para atualizar.");
            return;
        }
        
        System.out.print("\nDigite o ID do Usuário que deseja atualizar: ");
        int idParaAtualizar = lerInteiro();
        
        Usuario usuarioAtualizado = new Usuario();
        usuarioAtualizado.setIdUsuario(idParaAtualizar); // Coloca o ID no objeto
        
        // Pede TODOS os campos (Item 7.d.5)
        System.out.print("Digite o NOVO nome: ");
        usuarioAtualizado.setNome(teclado.nextLine());
        System.out.print("Digite o NOVO CPF: ");
        usuarioAtualizado.setCpf(teclado.nextLine());
        usuarioAtualizado.setDataNascimento(lerData("Digite a NOVA data de nascimento (dd/mm/aaaa): "));
        System.out.print("Digite o NOVO email: ");
        usuarioAtualizado.setEmail(teclado.nextLine());
        System.out.print("Digite a NOVA senha: ");
        usuarioAtualizado.setSenha(teclado.nextLine());
        
        usuarioController.atualizar(usuarioAtualizado);
        // (Item 7.d.7: Exibir registro atualizado - pulado por simplicidade,
        // pois exigiria um método "buscarPorId" em todos os controllers)
        System.out.println("... Usuário (ID: " + idParaAtualizar + ") atualizado (se existir).");
    }

    private static void atualizarVacina() {
        System.out.println("\n-- Atualizando Vacina --");
        System.out.println("... Listando Vacinas ...");
        List<Object> vacinas = listarTodos(vacinaController);
        if (vacinas.isEmpty()) {
            System.out.println("Nenhuma vacina para atualizar.");
            return;
        }

        System.out.print("\nDigite o ID da Vacina que deseja atualizar: ");
        int idParaAtualizar = lerInteiro();
        
        Vacina vacinaAtualizada = new Vacina();
        vacinaAtualizada.setIdVacina(idParaAtualizar);
        
        System.out.print("Digite o NOVO nome da vacina: ");
        vacinaAtualizada.setNome(teclado.nextLine());
        System.out.print("Digite o NOVO fabricante: ");
        vacinaAtualizada.setFabricante(teclado.nextLine());
        
        vacinaController.atualizar(vacinaAtualizada);
        System.out.println("... Vacina (ID: " + idParaAtualizar + ") atualizada (se existir).");
    }

    private static void atualizarRegistro() {
        System.out.println("\n-- Atualizando Registro --");
        System.out.println("... Listando Registros ...");
        List<Object> registros = listarTodos(registroController);
        if (registros.isEmpty()) {
            System.out.println("Nenhum registro para atualizar.");
            return;
        }

        System.out.print("\nDigite o ID do Registro que deseja atualizar: ");
        int idParaAtualizar = lerInteiro();
        
        // Lista usuários e vacinas para o usuário saber quais IDs escolher
        System.out.println("... Listando Usuários disponíveis ...");
        listarTodos(usuarioController);
        System.out.println("\n... Listando Vacinas disponíveis ...");
        listarTodos(vacinaController);

        RegistroEfeitoAdverso registroAtualizado = new RegistroEfeitoAdverso();
        registroAtualizado.setIdRegistro(idParaAtualizar);
        
        System.out.print("\nDigite o NOVO ID do Usuário: ");
        registroAtualizado.setIdUsuario(lerInteiro());
        System.out.print("Digite o NOVO ID da Vacina: ");
        registroAtualizado.setIdVacina(lerInteiro());
        registroAtualizado.setDataVacinacao(lerData("Digite a NOVA data da vacinação (dd/mm/aaaa): "));
        System.out.print("Digite a NOVA descrição do efeito adverso: ");
        registroAtualizado.setDescricaoUsuario(teclado.nextLine());
        registroAtualizado.setDataRegistro(LocalDateTime.now()); // Atualiza data do registro
        
        registroController.atualizar(registroAtualizado);
        System.out.println("... Registro (ID: " + idParaAtualizar + ") atualizado (se existir).");
    }


    // --- MÉTODOS AUXILIARES (UTILITÁRIOS) ---

    /**
     * Um método genérico para listar todos os objetos de qualquer controller
     * (Item 7.c.3 e 7.d.3)
     */
    private static List<Object> listarTodos(IController controller) {
        List<Object> lista = controller.listarTodos();
        
        if (lista.isEmpty()) {
            System.out.println("(Nenhum item encontrado)");
        } else {
            for(Object obj : lista) {
                // Usa o método toString() que definimos nos Models
                System.out.println(obj.toString());
            }
        }
        return lista;
    }

    /**
     * Um leitor de Inteiro "seguro" que impede o programa de quebrar
     * se o usuário digitar "abc" em vez de "123".
     */
    private static int lerInteiro() {
        while (true) {
            try {
                int i = Integer.parseInt(teclado.nextLine());
                return i;
            } catch (NumberFormatException e) {
                System.out.print("!! Valor inválido. Digite um número inteiro: ");
            }
        }
    }

    /**
     * Um leitor de Data "seguro" que força o formato dd/mm/aaaa.
     */
    private static LocalDate lerData(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return LocalDate.parse(teclado.nextLine(), dtf);
            } catch (DateTimeParseException e) {
                System.out.println("!! Data inválida. Use o formato dd/mm/aaaa.");
            }
        }
    }
}