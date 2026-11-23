package classes;

public class Pagamento {

    private int idPagamento;
    private double valor;
    private String data;
    private String status;
    private Aluno aluno;

    public Pagamento(int idPagamento, double valor, String data,String status, Aluno aluno) {
        // Impede cadastrar pagamentos com ID inválido
        if (idPagamento <= 0) {
            throw new IllegalArgumentException(
                "O ID do pagamento deve ser positivo."
            );
        }

        if (aluno == null) {
            throw new IllegalArgumentException("O aluno é obrigatório.");
        }

        this.idPagamento = idPagamento;
        this.aluno = aluno;
        setValor(valor);
        setData(data);
        setStatus(status);
    }

    public int getIdPagamento() {
        return idPagamento;
    }

    public double getValor() {
        return valor;
    }

    // [ENCAPSULAMENTO] Evita valores negativos, zerados ou indefinidos nos pagamentos
    public void setValor(double valor) {
        if (valor <= 0 || Double.isNaN(valor) || Double.isInfinite(valor)) {
            throw new IllegalArgumentException(
                "O valor do pagamento deve ser positivo."
            );
        }
        this.valor = valor;
    }

    public String getData() {
        return data;
    }

    // Impede datas vazias e ponto e virgula para evitar problemas ao salvar no TXT
    public void setData(String data) {
        if (data == null || data.isBlank() || data.contains(";")) {
            throw new IllegalArgumentException(
                "A data do pagamento é inválida."
            );
        }
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    // Não permite cadastrar um pagamento sem status
    public void setStatus(String status) {
        if (status == null || status.isBlank() || status.contains(";")) {
            throw new IllegalArgumentException(
                "O status do pagamento é inválido."
            );
        }
        this.status = status;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void exibirPagamento() {
        System.out.println("Pagamento: " + idPagamento
                + "\nValor: R$ " + valor
                + "\nData: " + data
                + "\nStatus: " + status
                + "\nAluno: " + aluno.getNome());
    }

    public String verificarStatus() {
        return status;
    }
}
