package classes;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

// Classe que controla os cadastros e os arquivos do sistema
public class Academia implements InterfaceAcademia {

    // [ENCAPSULAMENTO] As listas ficam privadas para serem alteradas pelos metodos da academia
    private ArrayList<Pessoa> pessoas;
    private ArrayList<Treino> treinos;
    private ArrayList<Pagamento> pagamentos;
    private String pastaDados;

    public Academia(String pastaDados) {
        if (pastaDados == null || pastaDados.isBlank()) {
            throw new IllegalArgumentException("Pasta de dados invalida.");
        }

        this.pastaDados = pastaDados;
        pessoas = new ArrayList<Pessoa>();
        treinos = new ArrayList<Treino>();
        pagamentos = new ArrayList<Pagamento>();
    }

    public void inserir(Pessoa pessoa) {
        if (pessoa == null) {
            throw new IllegalArgumentException("Pessoa invalida.");
        }
        if (pesquisar(pessoa.getCodigo()) != null) {
            throw new IllegalArgumentException("Codigo ja cadastrado.");
        }
        pessoas.add(pessoa);
    }

    // Impede remover pessoas que ainda estao ligadas a treinos ou pagamentos
    public void remover(Pessoa pessoa) {
        if (pessoa == null || pesquisar(pessoa.getCodigo()) != pessoa) {
            throw new IllegalArgumentException("Pessoa nao cadastrada.");
        }

        for (Treino treino : treinos) {
            if (treino.getAluno() == pessoa || treino.getInstrutor() == pessoa) {
                throw new IllegalArgumentException("Remova primeiro o treino " + treino.getIdTreino());
            }
        }

        for (Pagamento pagamento : pagamentos) {
            if (pagamento.getAluno() == pessoa) {
                throw new IllegalArgumentException("Remova primeiro o pagamento " + pagamento.getIdPagamento());
            }
        }

        pessoas.remove(pessoa);
    }

    public void atualizar(Pessoa novaPessoa) {
        if (novaPessoa == null) {
            throw new IllegalArgumentException("Pessoa invalida.");
        }

        Pessoa pessoa = pesquisar(novaPessoa.getCodigo());
        if (pessoa == null || pessoa.getClass() != novaPessoa.getClass()) {
            throw new IllegalArgumentException("Pessoa nao encontrada ou tipo diferente.");
        }

        if (pessoa instanceof Aluno) {
            Aluno aluno = (Aluno) pessoa;
            Aluno alunoNovo = (Aluno) novaPessoa;
            if (aluno.getMatricula() != alunoNovo.getMatricula()) {
                throw new IllegalArgumentException("A matricula nao pode ser alterada.");
            }
        }

        // Atualiza o mesmo objeto para nao perder os vinculos com treinos e pagamentos
        pessoa.setNome(novaPessoa.getNome());
        pessoa.setCpf(novaPessoa.getCpf());
        pessoa.setTelefone(novaPessoa.getTelefone());
        pessoa.setEmail(novaPessoa.getEmail());

        if (pessoa instanceof Aluno) {
            Aluno aluno = (Aluno) pessoa;
            Aluno alunoNovo = (Aluno) novaPessoa;
            aluno.setDataMatricula(alunoNovo.getDataMatricula());
            aluno.setStatus(alunoNovo.getStatus());
        } else {
            Instrutor instrutor = (Instrutor) pessoa;
            Instrutor instrutorNovo = (Instrutor) novaPessoa;
            instrutor.setCref(instrutorNovo.getCref());
            instrutor.setEspecialidade(instrutorNovo.getEspecialidade());
        }
    }

    public Pessoa pesquisar(int codigo) {
        for (Pessoa pessoa : pessoas) {
            if (pessoa.getCodigo() == codigo) {
                return pessoa;
            }
        }
        return null;
    }

    // Retorna uma copia para a lista original nao ser alterada fora da academia
    public ArrayList<Pessoa> pesquisar() {
        return new ArrayList<Pessoa>(pessoas);
    }

    // Confere se o aluno e o instrutor ja estao cadastrados antes de adicionar o treino
    public void adicionarTreino(Treino treino) {
        if (treino == null) {
            throw new IllegalArgumentException("Treino invalido.");
        }
        if (pesquisarTreino(treino.getIdTreino()) != null) {
            throw new IllegalArgumentException("ID do treino ja cadastrado.");
        }
        if (treino.getAluno() == null || treino.getInstrutor() == null) {
            throw new IllegalArgumentException("Aluno ou instrutor invalido.");
        }
        if (pesquisar(treino.getAluno().getCodigo()) != treino.getAluno()
                || pesquisar(treino.getInstrutor().getCodigo()) != treino.getInstrutor()) {
            throw new IllegalArgumentException("Aluno ou instrutor nao cadastrado.");
        }

        treinos.add(treino);
        treino.getAluno().adicionarTreino(treino);
    }

    public void removerTreino(Treino treino) {
        if (treino == null || !treinos.remove(treino)) {
            throw new IllegalArgumentException("Treino nao cadastrado.");
        }
        treino.getAluno().removerTreino(treino);
    }

    public Treino pesquisarTreino(int id) {
        for (Treino treino : treinos) {
            if (treino.getIdTreino() == id) {
                return treino;
            }
        }
        return null;
    }

    public ArrayList<Treino> listarTreinos() {
        return new ArrayList<Treino>(treinos);
    }

    public void registrarPagamento(Pagamento pagamento) {
        if (pagamento == null) {
            throw new IllegalArgumentException("Pagamento invalido.");
        }
        if (pesquisarPagamento(pagamento.getIdPagamento()) != null) {
            throw new IllegalArgumentException("ID do pagamento ja cadastrado.");
        }
        if (pagamento.getAluno() == null
                || pesquisar(pagamento.getAluno().getCodigo()) != pagamento.getAluno()) {
            throw new IllegalArgumentException("Aluno do pagamento nao cadastrado.");
        }
        pagamentos.add(pagamento);
    }

    public ArrayList<Pagamento> consultarPagamentos() {
        return new ArrayList<Pagamento>(pagamentos);
    }

    public Pagamento pesquisarPagamento(int id) {
        for (Pagamento pagamento : pagamentos) {
            if (pagamento.getIdPagamento() == id) {
                return pagamento;
            }
        }
        return null;
    }

    public void removerPagamento(Pagamento pagamento) {
        if (pagamento == null || !pagamentos.remove(pagamento)) {
            throw new IllegalArgumentException("Pagamento nao cadastrado.");
        }
    }

    // Salva os cadastros em arquivos TXT para nao perder os dados ao sair
    public void salvarDados() throws IOException {
        File pasta = new File(pastaDados);
        if (!pasta.exists() && !pasta.mkdirs()) {
            throw new IOException("Nao foi possivel criar a pasta de dados.");
        }

        // [POLIMORFISMO] getTipo identifica se a pessoa e aluno ou instrutor
        try (BufferedWriter arquivo = new BufferedWriter(new FileWriter(new File(pasta, "pessoas.txt")))) {
            for (Pessoa pessoa : pessoas) {
                String linha = pessoa.getTipo() + ";" + pessoa.getCodigo() + ";" + pessoa.getNome()
                        + ";" + pessoa.getCpf() + ";" + pessoa.getTelefone() + ";" + pessoa.getEmail();

                if (pessoa instanceof Aluno) {
                    Aluno aluno = (Aluno) pessoa;
                    linha += ";" + aluno.getMatricula() + ";" + aluno.getDataMatricula()
                            + ";" + aluno.getStatus();
                } else {
                    Instrutor instrutor = (Instrutor) pessoa;
                    linha += ";" + instrutor.getCref() + ";" + instrutor.getEspecialidade();
                }
                arquivo.write(linha);
                arquivo.newLine();
            }
        }

        // Guarda o tipo do treino para conseguir carregar a classe certa depois
        try (BufferedWriter arquivo = new BufferedWriter(new FileWriter(new File(pasta, "treinos.txt")))) {
            for (Treino treino : treinos) {
                String tipo = "Geral";
                String grupo = "";
                if (treino instanceof TreinoMusculacao) {
                    tipo = "Musculacao";
                    grupo = ((TreinoMusculacao) treino).getGrupoMuscular();
                }
                arquivo.write(tipo + ";" + treino.getIdTreino() + ";" + treino.getObjetivo()
                        + ";" + treino.getAluno().getCodigo() + ";" + treino.getInstrutor().getCodigo()
                        + ";" + grupo);
                arquivo.newLine();
            }
        }

        // Salva o ID do treino junto do exercicio para manter a ligacao entre eles
        try (BufferedWriter arquivo = new BufferedWriter(new FileWriter(new File(pasta, "exercicios.txt")))) {
            for (Treino treino : treinos) {
                for (Exercicio exercicio : treino.getExercicios()) {
                    arquivo.write(treino.getIdTreino() + ";" + exercicio.getNome() + ";"
                            + exercicio.getSeries() + ";" + exercicio.getRepeticoes() + ";"
                            + exercicio.getCarga());
                    arquivo.newLine();
                }
            }
        }

        // Salva o codigo do aluno para saber de quem e cada pagamento
        try (BufferedWriter arquivo = new BufferedWriter(new FileWriter(new File(pasta, "pagamentos.txt")))) {
            for (Pagamento pagamento : pagamentos) {
                arquivo.write(pagamento.getIdPagamento() + ";" + pagamento.getValor() + ";"
                        + pagamento.getData() + ";" + pagamento.getStatus() + ";"
                        + pagamento.getAluno().getCodigo());
                arquivo.newLine();
            }
        }
    }

    // Carrega os dados dos TXT quando o programa inicia
    public void carregarDados() throws IOException {
        File pasta = new File(pastaDados);
        File pessoasTxt = new File(pasta, "pessoas.txt");
        File treinosTxt = new File(pasta, "treinos.txt");
        File exerciciosTxt = new File(pasta, "exercicios.txt");
        File pagamentosTxt = new File(pasta, "pagamentos.txt");

        int quantidade = 0;
        if (pessoasTxt.exists()) quantidade++;
        if (treinosTxt.exists()) quantidade++;
        if (exerciciosTxt.exists()) quantidade++;
        if (pagamentosTxt.exists()) quantidade++;

        if (quantidade == 0) {
            return;
        }
        // Se faltar algum arquivo, evita carregar dados incompletos
        if (quantidade != 4) {
            throw new IOException("Os quatro arquivos TXT devem existir juntos.");
        }

        pessoas.clear();
        treinos.clear();
        pagamentos.clear();

        try {
            // Carrega as pessoas primeiro porque treinos e pagamentos dependem delas
            carregarPessoas(pessoasTxt);
            carregarTreinos(treinosTxt);
            carregarExercicios(exerciciosTxt);
            carregarPagamentos(pagamentosTxt);
        } catch (IllegalArgumentException erro) {
            throw new IOException("Dados invalidos: " + erro.getMessage(), erro);
        }
    }

    // Recria os alunos e instrutores usando as informacoes do arquivo
    private void carregarPessoas(File arquivoTxt) throws IOException {
        try (BufferedReader arquivo = new BufferedReader(new FileReader(arquivoTxt))) {
            String linha;
            while ((linha = arquivo.readLine()) != null) {
                if (linha.isBlank()) {
                    continue;
                }
                String[] partes = linha.split(";", -1);
                if (partes[0].equals("Aluno") && partes.length == 9) {
                    Aluno aluno = new Aluno(Integer.parseInt(partes[1]), partes[2], partes[3],
                            partes[4], partes[5], Integer.parseInt(partes[6]), partes[7], partes[8]);
                    inserir(aluno);
                } else if (partes[0].equals("Instrutor") && partes.length == 8) {
                    Instrutor instrutor = new Instrutor(Integer.parseInt(partes[1]), partes[2], partes[3],
                            partes[4], partes[5], partes[6], partes[7]);
                    inserir(instrutor);
                } else {
                    throw new IllegalArgumentException("Linha invalida em pessoas.txt.");
                }
            }
        }
    }

    // Procura o aluno e o instrutor antes de montar cada treino
    private void carregarTreinos(File arquivoTxt) throws IOException {
        try (BufferedReader arquivo = new BufferedReader(new FileReader(arquivoTxt))) {
            String linha;
            while ((linha = arquivo.readLine()) != null) {
                if (linha.isBlank()) {
                    continue;
                }
                String[] partes = linha.split(";", -1);
                if (partes.length != 6) {
                    throw new IllegalArgumentException("Linha invalida em treinos.txt.");
                }

                int id = Integer.parseInt(partes[1]);
                Pessoa aluno = pesquisar(Integer.parseInt(partes[3]));
                Pessoa instrutor = pesquisar(Integer.parseInt(partes[4]));
                if (!(aluno instanceof Aluno) || !(instrutor instanceof Instrutor)) {
                    throw new IllegalArgumentException("Aluno ou instrutor nao encontrado.");
                }

                Treino treino;
                if (partes[0].equals("Geral")) {
                    treino = new Treino(id, partes[2], (Aluno) aluno, (Instrutor) instrutor);
                } else if (partes[0].equals("Musculacao")) {
                    treino = new TreinoMusculacao(id, partes[2], (Aluno) aluno,
                            (Instrutor) instrutor, partes[5]);
                } else {
                    throw new IllegalArgumentException("Tipo de treino invalido.");
                }
                adicionarTreino(treino);
            }
        }
    }

    // Coloca cada exercicio de volta no treino correspondente
    private void carregarExercicios(File arquivoTxt) throws IOException {
        try (BufferedReader arquivo = new BufferedReader(new FileReader(arquivoTxt))) {
            String linha;
            while ((linha = arquivo.readLine()) != null) {
                if (linha.isBlank()) {
                    continue;
                }
                String[] partes = linha.split(";", -1);
                if (partes.length != 5) {
                    throw new IllegalArgumentException("Linha invalida em exercicios.txt.");
                }

                Treino treino = pesquisarTreino(Integer.parseInt(partes[0]));
                if (treino == null) {
                    throw new IllegalArgumentException("Treino do exercicio nao encontrado.");
                }
                Exercicio exercicio = new Exercicio(partes[1], Integer.parseInt(partes[2]),
                        Integer.parseInt(partes[3]), Double.parseDouble(partes[4]));
                treino.adicionarExercicio(exercicio);
            }
        }
    }

    // Liga cada pagamento ao aluno que foi salvo no TXT
    private void carregarPagamentos(File arquivoTxt) throws IOException {
        try (BufferedReader arquivo = new BufferedReader(new FileReader(arquivoTxt))) {
            String linha;
            while ((linha = arquivo.readLine()) != null) {
                if (linha.isBlank()) {
                    continue;
                }
                String[] partes = linha.split(";", -1);
                if (partes.length != 5) {
                    throw new IllegalArgumentException("Linha invalida em pagamentos.txt.");
                }

                Pessoa pessoa = pesquisar(Integer.parseInt(partes[4]));
                if (!(pessoa instanceof Aluno)) {
                    throw new IllegalArgumentException("Aluno do pagamento nao encontrado.");
                }
                Pagamento pagamento = new Pagamento(Integer.parseInt(partes[0]),
                        Double.parseDouble(partes[1]), partes[2], partes[3], (Aluno) pessoa);
                registrarPagamento(pagamento);
            }
        }
    }
}
