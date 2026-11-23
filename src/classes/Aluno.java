package classes;

import java.util.ArrayList;

// [HERANÇA] Aproveita os dados comuns de Pessoa para evitar repeticao de codigo
public class Aluno extends Pessoa {

    private int matricula;
    private String dataMatricula;
    private String status;
    private ArrayList<Treino> treinos;

    public Aluno(int codigo, String nome, String cpf, String telefone, String email, int matricula, String dataMatricula, String status) {
        super(codigo, nome, cpf, telefone, email);
        setMatricula(matricula);
        setDataMatricula(dataMatricula);
        setStatus(status);
        treinos = new ArrayList<Treino>();
    }

    // Identifica o tipo de pessoa na hora de salvar no TXT
    @Override
    public String getTipo() {
        return "Aluno";
    }

    // [POLIMORFISMO] Permite exibir os dados especificos do Aluno usando uma referencia Pessoa
    @Override
    public void exibirDados() {
        super.exibirDados();
        System.out.println("Matrícula: " + matricula
                + "\nData da matrícula: " + dataMatricula
                + "\nStatus: " + status);
    }

    public int getMatricula() {
        return matricula;
    }

    // [ENCAPSULAMENTO] Não permite cadastrar uma matricula invalida
    public void setMatricula(int matricula) {
        if (matricula <= 0) {
            throw new IllegalArgumentException("A matrícula deve ser positiva.");
        }
        this.matricula = matricula;
    }

    public String getDataMatricula() {
        return dataMatricula;
    }

    // Impede o uso de ponto e virgula porque ele separa os dados no TXT
    public void setDataMatricula(String dataMatricula) {
        if (dataMatricula == null || dataMatricula.isBlank() || dataMatricula.contains(";")) {
            throw new IllegalArgumentException("A data da matrícula é obrigatória.");
        }
        this.dataMatricula = dataMatricula;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        if (status == null || status.isBlank() || status.contains(";")) {
            throw new IllegalArgumentException("O status é obrigatório.");
        }
        this.status = status;
    }

    // Guarda o treino na lista do aluno para poder consultar depois
    public void adicionarTreino(Treino treino) {
        if (treino == null) {
            throw new IllegalArgumentException("O treino é obrigatório.");
        }
        treinos.add(treino);
    }

    public void removerTreino(Treino treino) {
        treinos.remove(treino);
    }

    // Usa a lista do aluno para mostrar apenas os treinos dele
    public void consultarTreino() {
        if (treinos.isEmpty()) {
            System.out.println("Aluno sem treinos cadastrados.");
        } else {
            for (Treino treino : treinos) {
                treino.exibirTreino();
            }
        }
    }


}