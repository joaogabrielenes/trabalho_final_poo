package classes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Principal {

    private static Scanner leitor = new Scanner(System.in);
    private static Academia academia = new Academia("dados");

    // Recupera os cadastros salvos para continuar usando os dados da ultima execucao
    public static void main(String[] args) {
        try {
            academia.carregarDados();
        } catch (IOException erro) {
            System.out.println("Erro ao carregar dados: " + erro.getMessage());
            return;
        }

        int opcao = -1;

        // Permite realizar varias operacoes sem precisar reiniciar o programa
        do {
            System.out.println("\n=== ACADEMIA EVOLUTION ===");
            System.out.println("1 - Cadastrar pessoa");
            System.out.println("2 - Listar pessoas");
            System.out.println("3 - Buscar pessoa");
            System.out.println("4 - Editar pessoa");
            System.out.println("5 - Remover pessoa");
            System.out.println("6 - Treinos e exercicios");
            System.out.println("7 - Pagamentos");
            System.out.println("8 - Consultar treinos de um aluno");
            System.out.println("0 - Sair");

            try {
                opcao = lerInteiro("Opcao: ");

                switch (opcao) {
                    case 1:
                        cadastrarPessoa();
                        break;
                    case 2:
                        listarPessoas();
                        break;
                    case 3:
                        buscarPessoa();
                        break;
                    case 4:
                        editarPessoa();
                        break;
                    case 5:
                        removerPessoa();
                        break;
                    case 6:
                        menuTreinos();
                        break;
                    case 7:
                        menuPagamentos();
                        break;
                    case 8:
                        Aluno aluno = buscarAluno(lerInteiro("Codigo do aluno: "));
                        aluno.consultarTreino();
                        break;
                    case 0:
                        academia.salvarDados();
                        System.out.println("Dados salvos. Ate logo!");
                        break;
                    default:
                        System.out.println("Opcao invalida.");
                }
            } catch (IllegalArgumentException erro) {
                System.out.println("Erro: " + erro.getMessage());
            } catch (IOException erro) {
                System.out.println("Erro no arquivo: " + erro.getMessage());
            }
        } while (opcao != 0);
    }

    // Permite cadastrar tipos diferentes de pessoas usando a mesma opcao do menu
    private static void cadastrarPessoa() throws IOException {
        int tipo = lerInteiro("1 - Aluno | 2 - Instrutor: ");
        if (tipo != 1 && tipo != 2) {
            System.out.println("Tipo invalido.");
            return;
        }

        int codigo = lerInteiro("Codigo: ");
        String nome = lerTexto("Nome: ");
        String cpf = lerTexto("CPF: ");
        String telefone = lerTexto("Telefone: ");
        String email = lerTexto("Email: ");

        Pessoa pessoa;
        if (tipo == 1) {
            int matricula = lerInteiro("Matricula: ");
            String data = lerTexto("Data da matricula: ");
            String status = lerTexto("Status: ");
            pessoa = new Aluno(codigo, nome, cpf, telefone, email, matricula, data, status);
        } else {
            String cref = lerTexto("CREF: ");
            String especialidade = lerTexto("Especialidade: ");
            pessoa = new Instrutor(codigo, nome, cpf, telefone, email, cref, especialidade);
        }

        academia.inserir(pessoa);
        academia.salvarDados();
        System.out.println("Pessoa cadastrada.");
    }

    // [POLIMORFISMO] Permite exibir Aluno e Instrutor usando a mesma referencia Pessoa
    private static void listarPessoas() {
        ArrayList<Pessoa> pessoas = academia.pesquisar();
        if (pessoas.isEmpty()) {
            System.out.println("Nenhuma pessoa cadastrada.");
        } else {
            for (Pessoa pessoa : pessoas) {
                pessoa.exibirDados();
                System.out.println();
            }
        }
    }

    private static void buscarPessoa() {
        Pessoa pessoa = obterPessoa(lerInteiro("Codigo: "));
        pessoa.exibirDados();
    }

    // Permite mudar apenas os campos que o usuario quiser
    private static void editarPessoa() throws IOException {
        Pessoa atual = obterPessoa(lerInteiro("Codigo: "));
        System.out.println("Deixe em branco para manter o valor atual.");

        String nome = lerOpcional("Nome", atual.getNome());
        String cpf = lerOpcional("CPF", atual.getCpf());
        String telefone = lerOpcional("Telefone", atual.getTelefone());
        String email = lerOpcional("Email", atual.getEmail());

        Pessoa novaPessoa;
        if (atual instanceof Aluno) {
            Aluno aluno = (Aluno) atual;
            String data = lerOpcional("Data da matricula", aluno.getDataMatricula());
            String status = lerOpcional("Status", aluno.getStatus());
            novaPessoa = new Aluno(aluno.getCodigo(), nome, cpf, telefone, email,
                    aluno.getMatricula(), data, status);
        } else {
            Instrutor instrutor = (Instrutor) atual;
            String cref = lerOpcional("CREF", instrutor.getCref());
            String especialidade = lerOpcional("Especialidade", instrutor.getEspecialidade());
            novaPessoa = new Instrutor(instrutor.getCodigo(), nome, cpf, telefone,
                    email, cref, especialidade);
        }

        academia.atualizar(novaPessoa);
        academia.salvarDados();
        System.out.println("Pessoa atualizada.");
    }

    // Deixa a verificacao de vinculos com a Academia para evitar remover pessoas ainda utilizadas
    private static void removerPessoa() throws IOException {
        Pessoa pessoa = obterPessoa(lerInteiro("Codigo: "));
        academia.remover(pessoa);
        academia.salvarDados();
        System.out.println("Pessoa removida.");
    }

    private static void menuTreinos() throws IOException {
        System.out.println("\n=== TREINOS ===");
        System.out.println("1 - Cadastrar treino");
        System.out.println("2 - Listar treinos");
        System.out.println("3 - Buscar treino");
        System.out.println("4 - Editar treino");
        System.out.println("5 - Remover treino");
        System.out.println("6 - Adicionar exercicio");
        System.out.println("7 - Remover exercicio");
        System.out.println("0 - Voltar");

        int opcao = lerInteiro("Opcao: ");
        switch (opcao) {
            case 1:
                cadastrarTreino();
                break;
            case 2:
                ArrayList<Treino> treinos = academia.listarTreinos();
                if (treinos.isEmpty()) {
                    System.out.println("Nenhum treino cadastrado.");
                } else {
                    for (Treino treino : treinos) {
                        treino.exibirTreino();
                        System.out.println();
                    }
                }
                break;
            case 3:
                obterTreino(lerInteiro("ID do treino: ")).exibirTreino();
                break;
            case 4:
                editarTreino();
                break;
            case 5:
                Treino treino = obterTreino(lerInteiro("ID do treino: "));
                academia.removerTreino(treino);
                academia.salvarDados();
                System.out.println("Treino removido.");
                break;
            case 6:
                adicionarExercicio();
                break;
            case 7:
                removerExercicio();
                break;
            case 0:
                break;
            default:
                System.out.println("Opcao invalida.");
        }
    }

    // Liga o treino a um aluno e a um instrutor ja cadastrados
    private static void cadastrarTreino() throws IOException {
        int tipo = lerInteiro("1 - Treino geral | 2 - Musculacao: ");
        if (tipo != 1 && tipo != 2) {
            System.out.println("Tipo invalido.");
            return;
        }

        int id = lerInteiro("ID do treino: ");
        String objetivo = lerTexto("Objetivo: ");
        Aluno aluno = buscarAluno(lerInteiro("Codigo do aluno: "));
        Instrutor instrutor = buscarInstrutor(lerInteiro("Codigo do instrutor: "));

        Treino treino;
        if (tipo == 1) {
            treino = new Treino(id, objetivo, aluno, instrutor);
        } else {
            String grupo = lerTexto("Grupo muscular: ");
            treino = new TreinoMusculacao(id, objetivo, aluno, instrutor, grupo);
        }

        academia.adicionarTreino(treino);
        academia.salvarDados();
        System.out.println("Treino cadastrado.");
    }

    private static void editarTreino() throws IOException {
        Treino treino = obterTreino(lerInteiro("ID do treino: "));
        String objetivo = lerOpcional("Objetivo", treino.getObjetivo());
        String grupo = null;

        if (treino instanceof TreinoMusculacao) {
            TreinoMusculacao musculacao = (TreinoMusculacao) treino;
            grupo = lerOpcional("Grupo muscular", musculacao.getGrupoMuscular());
        }

        treino.setObjetivo(objetivo);
        if (treino instanceof TreinoMusculacao) {
            TreinoMusculacao musculacao = (TreinoMusculacao) treino;
            musculacao.setGrupoMuscular(grupo);
        }
        academia.salvarDados();
        System.out.println("Treino atualizado.");
    }

    private static void adicionarExercicio() throws IOException {
        Treino treino = obterTreino(lerInteiro("ID do treino: "));
        String nome = lerTexto("Nome do exercicio: ");
        int series = lerInteiro("Series: ");
        int repeticoes = lerInteiro("Repeticoes: ");
        double carga = lerDouble("Carga: ");

        Exercicio exercicio = new Exercicio(nome, series, repeticoes, carga);
        treino.adicionarExercicio(exercicio);
        academia.salvarDados();
        System.out.println("Exercicio adicionado.");
    }

    // Mostra os exercicios com numeros para facilitar a escolha
    private static void removerExercicio() throws IOException {
        Treino treino = obterTreino(lerInteiro("ID do treino: "));
        ArrayList<Exercicio> exercicios = treino.getExercicios();

        if (exercicios.isEmpty()) {
            System.out.println("Esse treino nao possui exercicios.");
            return;
        }

        for (int i = 0; i < exercicios.size(); i++) {
            System.out.println("Exercicio " + (i + 1));
            exercicios.get(i).exibirExercicio();
        }

        int numero = lerInteiro("Numero do exercicio: ");
        if (numero < 1 || numero > exercicios.size()) {
            System.out.println("Numero invalido.");
            return;
        }

        treino.removerExercicio(exercicios.get(numero - 1));
        academia.salvarDados();
        System.out.println("Exercicio removido.");
    }

    private static void menuPagamentos() throws IOException {
        System.out.println("\n=== PAGAMENTOS ===");
        System.out.println("1 - Registrar pagamento");
        System.out.println("2 - Listar pagamentos");
        System.out.println("3 - Buscar pagamento");
        System.out.println("4 - Verificar status");
        System.out.println("5 - Remover pagamento");
        System.out.println("0 - Voltar");

        int opcao = lerInteiro("Opcao: ");
        switch (opcao) {
            case 1:
                int id = lerInteiro("ID do pagamento: ");
                double valor = lerDouble("Valor: ");
                String data = lerTexto("Data: ");
                String status = lerTexto("Status: ");
                Aluno aluno = buscarAluno(lerInteiro("Codigo do aluno: "));
                Pagamento pagamento = new Pagamento(id, valor, data, status, aluno);
                academia.registrarPagamento(pagamento);
                academia.salvarDados();
                System.out.println("Pagamento registrado.");
                break;
            case 2:
                ArrayList<Pagamento> pagamentos = academia.consultarPagamentos();
                if (pagamentos.isEmpty()) {
                    System.out.println("Nenhum pagamento cadastrado.");
                } else {
                    for (Pagamento p : pagamentos) {
                        p.exibirPagamento();
                        System.out.println();
                    }
                }
                break;
            case 3:
                obterPagamento(lerInteiro("ID do pagamento: ")).exibirPagamento();
                break;
            case 4:
                Pagamento p = obterPagamento(lerInteiro("ID do pagamento: "));
                System.out.println("Status: " + p.verificarStatus());
                break;
            case 5:
                Pagamento remover = obterPagamento(lerInteiro("ID do pagamento: "));
                academia.removerPagamento(remover);
                academia.salvarDados();
                System.out.println("Pagamento removido.");
                break;
            case 0:
                break;
            default:
                System.out.println("Opcao invalida.");
        }
    }

    private static Pessoa obterPessoa(int codigo) {
        Pessoa pessoa = academia.pesquisar(codigo);
        if (pessoa == null) {
            throw new IllegalArgumentException("Pessoa nao encontrada.");
        }
        return pessoa;
    }

    // Confere se o codigo encontrado pertence mesmo a um aluno
    private static Aluno buscarAluno(int codigo) {
        Pessoa pessoa = obterPessoa(codigo);
        if (!(pessoa instanceof Aluno)) {
            throw new IllegalArgumentException("Esse codigo nao pertence a um aluno.");
        }
        return (Aluno) pessoa;
    }

    // Evita usar um aluno no lugar de um instrutor
    private static Instrutor buscarInstrutor(int codigo) {
        Pessoa pessoa = obterPessoa(codigo);
        if (!(pessoa instanceof Instrutor)) {
            throw new IllegalArgumentException("Esse codigo nao pertence a um instrutor.");
        }
        return (Instrutor) pessoa;
    }

    private static Treino obterTreino(int id) {
        Treino treino = academia.pesquisarTreino(id);
        if (treino == null) {
            throw new IllegalArgumentException("Treino nao encontrado.");
        }
        return treino;
    }

    private static Pagamento obterPagamento(int id) {
        Pagamento pagamento = academia.pesquisarPagamento(id);
        if (pagamento == null) {
            throw new IllegalArgumentException("Pagamento nao encontrado.");
        }
        return pagamento;
    }

    private static String lerTexto(String pergunta) {
        System.out.print(pergunta);
        return leitor.nextLine();
    }

    // Permite editar apenas os campos desejados sem alterar os demais
    private static String lerOpcional(String campo, String atual) {
        String resposta = lerTexto(campo + " [" + atual + "]: ");
        if (resposta.isBlank()) {
            return atual;
        }
        return resposta;
    }

    // Evita que letras sejam aceitas onde precisa de numero
    private static int lerInteiro(String pergunta) {
        String resposta = lerTexto(pergunta);
        try {
            return Integer.parseInt(resposta.trim());
        } catch (NumberFormatException erro) {
            throw new IllegalArgumentException("Digite um numero inteiro.");
        }
    }

    // Aceita virgula ou ponto na hora de digitar valores
    private static double lerDouble(String pergunta) {
        String resposta = lerTexto(pergunta);
        try {
            return Double.parseDouble(resposta.trim().replace(',', '.'));
        } catch (NumberFormatException erro) {
            throw new IllegalArgumentException("Digite um numero valido.");
        }
    }
}
