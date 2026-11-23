package classes;

// [ABSTRAÇÃO] Reune os dados comuns de Aluno e Instrutor para evitar repeticao de codigo
public abstract class Pessoa {

    private int codigo;
    private String cpf;
    private String telefone;
    private String email;
    private String nome;


    public Pessoa(int codigo, String nome, String cpf, String telefone, String email) {
        setCodigo(codigo);
        setNome(nome);
        setCpf(cpf);
        setTelefone(telefone);
        setEmail(email);
    }

    // Evita repetir a exibicao dos dados comuns nas classes filhas
    public void exibirDados(){
        System.out.println("Codigo: " + codigo
            + "\nNome: " + nome
            + "\nCpf: " + cpf
            + "\nTelefone: " + telefone
            + "\nEmail: " + email
        );
    }

    // [ABSTRAÇÃO] Cada classe filha precisa informar se é Aluno ou Instrutor
    public abstract String getTipo();

    public int getCodigo() {
        return codigo;
    }

    // [ENCAPSULAMENTO] Impede cadastrar uma pessoa com código inválido
    public void setCodigo(int codigo){
        if (codigo <= 0){
            throw new IllegalArgumentException("O código deve ser positivo.");
        }
        this.codigo = codigo;
    }

    public String getCpf() {
        return cpf;
    }

    // Impede campos vazios e ponto e vírgula por causa do TXT
    public void setCpf(String cpf) {
        if (cpf == null || cpf.isBlank() || cpf.contains(";")) {
            throw new IllegalArgumentException("O CPF é obrigatório.");
        }
        this.cpf = cpf;
    }

    public String getTelefone() {
        return telefone;
    }

    // Aceita apenas números para manter o telefone no formato correto
    public void setTelefone(String telefone) {
        if (telefone == null || !telefone.matches("\\d{10,11}")) {
            throw new IllegalArgumentException(
                "O telefone deve conter apenas números e ter 10 ou 11 dígitos."
            );
        }
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    // Evita cadastrar emails vazios ou sem o formato minimo esperado
    public void setEmail(String email) {
        if (email == null || email.isBlank() || !email.contains("@") || email.contains(";")) {
            throw new IllegalArgumentException("E-mail inválido.");
        }
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    // Não permite cadastrar pessoas sem nome
    public void setNome(String nome) {
        if (nome == null || nome.isBlank() || nome.contains(";")) {
            throw new IllegalArgumentException("O nome é obrigatório.");
        }
        this.nome = nome;
    }

}