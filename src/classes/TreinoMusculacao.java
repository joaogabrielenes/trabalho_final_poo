package classes;

// [HERANÇA] Reaproveita os dados e métodos de Treino para evitar repeticao de codigo
public class TreinoMusculacao extends Treino {

    private String grupoMuscular;

    public TreinoMusculacao(int idTreino, String objetivo, Aluno aluno,Instrutor instrutor, String grupoMuscular) {
        super(idTreino, objetivo, aluno, instrutor);
        setGrupoMuscular(grupoMuscular);
    }

    public String getGrupoMuscular() {
        return grupoMuscular;
    }

    // [POLIMORFISMO] Permite exibir os dados especificos da musculacao usando uma referencia Treino
    @Override
    public void exibirTreino() {
        super.exibirTreino();
        System.out.println("Grupo muscular: " + grupoMuscular);
    }

    // [ENCAPSULAMENTO] Impede cadastrar um grupo muscular vazio ou com caracteres que atrapalham o TXT
    public void setGrupoMuscular(String grupoMuscular) {
        if (grupoMuscular == null
                || grupoMuscular.isBlank()
                || grupoMuscular.contains(";")) {
            throw new IllegalArgumentException(
                "O grupo muscular é inválido."
            );
        }

        this.grupoMuscular = grupoMuscular;
    }


}