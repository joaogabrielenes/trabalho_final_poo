package classes;

// [HERANÇA] Aproveita os dados comuns de Pessoa para evitar repeticao de codigo
public class Instrutor extends Pessoa {

    private String cref;
    private String especialidade;

    public Instrutor(int codigo, String nome, String cpf, String telefone, String email, String cref, String especialidade) {
        super(codigo, nome, cpf, telefone, email);
        setCref(cref);
        setEspecialidade(especialidade);
    }

    // Identifica o instrutor na hora de salvar os dados no TXT
    @Override
    public String getTipo() {
        return "Instrutor";
    }

    // [POLIMORFISMO] Permite exibir os dados especificos do Instrutor usando uma referencia Pessoa
    @Override
    public void exibirDados() {
        super.exibirDados();
        System.out.println("CREF: " + cref
                + "\nEspecialidade: " + especialidade);
    }

    public String getCref() {
        return cref;
    }

    // [ENCAPSULAMENTO] Impede cadastrar um CREF vazio ou com caracteres que atrapalham o TXT
    public void setCref(String cref) {
        if (cref == null || cref.isBlank() || cref.contains(";")) {
            throw new IllegalArgumentException("O CREF é obrigatório.");
        }
        this.cref = cref;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    // Não permite cadastrar um instrutor sem especialidade
    public void setEspecialidade(String especialidade) {
        if (especialidade == null || especialidade.isBlank() || especialidade.contains(";")) {
            throw new IllegalArgumentException("A especialidade é obrigatória.");
        }
        this.especialidade = especialidade;
    }

}