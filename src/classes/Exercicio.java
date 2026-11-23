package classes;

public class Exercicio {

    private String nome;
    private int series;
    private int repeticoes;
    private double carga;

    public Exercicio(String nome, int series, int repeticoes, double carga) {
        setNome(nome);
        setSeries(series);
        setRepeticoes(repeticoes);
        setCarga(carga);
    }

    public String getNome() {
        return nome;
    }

    // [ENCAPSULAMENTO] Garante que o exercicio tenha um nome e evita problemas ao salvar no TXT
    public void setNome(String nome) {
        if (nome == null || nome.isBlank() || nome.contains(";")) {
            throw new IllegalArgumentException(
                "Nome do exercício inválido."
            );
        }
        this.nome = nome;
    }

    public int getSeries() {
        return series;
    }

    // Não permite cadastrar um exercicio com zero series ou menos
    public void setSeries(int series) {
        if (series <= 0) {
            throw new IllegalArgumentException(
                "O número de séries deve ser positivo."
            );
        }
        this.series = series;
    }

    public int getRepeticoes() {
        return repeticoes;
    }

    // Evita cadastrar exercicios com quantidades de repeticoes impossiveis
    public void setRepeticoes(int repeticoes) {
        if (repeticoes <= 0) {
            throw new IllegalArgumentException(
                "O número de repetições deve ser positivo."
            );
        }
        this.repeticoes = repeticoes;
    }

    public double getCarga() {
        return carga;
    }

    // Permite exercicios sem peso, mas não aceita cargas negativas ou invalidas
    public void setCarga(double carga) {
        if (carga < 0 || Double.isNaN(carga) || Double.isInfinite(carga)) {
            throw new IllegalArgumentException(
                "A carga deve ser zero ou um número positivo."
            );
        }
        this.carga = carga;
    }

    public void exibirExercicio() {
        System.out.println("Exercício: " + nome
                + "\nSéries: " + series
                + "\nRepetições: " + repeticoes
                + "\nCarga: " + carga + " kg");
    }
}