package classes;

import java.util.ArrayList;

public class Treino {

    private int idTreino;
    private String objetivo;
    private Aluno aluno;
    private Instrutor instrutor;
    private ArrayList<Exercicio> exercicios;

    public Treino(int idTreino, String objetivo, Aluno aluno, Instrutor instrutor) {
        setIdTreino(idTreino);
        setObjetivo(objetivo);

        // Todo treino precisa ter um aluno e um instrutor
        if (aluno == null || instrutor == null) {
            throw new IllegalArgumentException(
                "Aluno e instrutor são obrigatórios."
            );
        }
        this.aluno = aluno;
        this.instrutor = instrutor;
        this.exercicios = new ArrayList<Exercicio>();
    }

    public int getIdTreino() {
        return idTreino;
    }

    // [ENCAPSULAMENTO] Garante que cada treino tenha um ID positivo para sua identificacao
    public void setIdTreino(int idTreino) {
        if (idTreino <= 0) {
            throw new IllegalArgumentException("O ID do treino deve ser positivo.");
        }
        this.idTreino = idTreino;
    }

    public String getObjetivo() {
        return objetivo;
    }

    // Impede campos vazios e ponto e vírgula por causa do TXT
    public void setObjetivo(String objetivo) {
        if (objetivo == null || objetivo.isBlank() || objetivo.contains(";")) {
            throw new IllegalArgumentException("Objetivo inválido.");
        }
        this.objetivo = objetivo;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public Instrutor getInstrutor() {
        return instrutor;
    }

    // [ENCAPSULAMENTO] Retorna uma cópia para não alterar a lista de exercícios diretamente
    public ArrayList<Exercicio> getExercicios() {
        return new ArrayList<Exercicio>(exercicios);
    }

    // Verifica se o exercício é válido antes de adicionar na lista
    public void adicionarExercicio(Exercicio exercicio) {
        if (exercicio == null) {
            throw new IllegalArgumentException("O exercício é obrigatório.");
        }
        exercicios.add(exercicio);
    }

    // Avisa quando tentam remover um exercício que não está no treino
    public void removerExercicio(Exercicio exercicio) {
        if (!exercicios.remove(exercicio)) {
            throw new IllegalArgumentException(
                "Exercício não encontrado neste treino."
            );
        }
    }

    public void exibirTreino() {
        System.out.println("Treino: " + idTreino
                + "\nObjetivo: " + objetivo
                + "\nAluno: " + aluno.getNome()
                + "\nInstrutor: " + instrutor.getNome());

        // Mostra os exercícios cadastrados ou avisa se a lista estiver vazia
        if (exercicios.isEmpty()) {
            System.out.println("Nenhum exercício cadastrado neste treino.");
        } else {
            for (Exercicio exercicio : exercicios) {
                exercicio.exibirExercicio();
            }
        }
    }
}