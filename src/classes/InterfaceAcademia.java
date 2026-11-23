package classes;

import java.io.IOException;
import java.util.ArrayList;

// Define um padrao de metodos para organizar as operacoes que a Academia deve implementar
public interface InterfaceAcademia {
    void inserir(Pessoa pessoa);
    void remover(Pessoa pessoa);
    void atualizar(Pessoa pessoa);
    Pessoa pesquisar(int codigo);
    ArrayList<Pessoa> pesquisar();
    void registrarPagamento(Pagamento pagamento);
    ArrayList<Pagamento> consultarPagamentos();
    void adicionarTreino(Treino treino);
    void removerTreino(Treino treino);
    void salvarDados() throws IOException;
    void carregarDados() throws IOException;
}
