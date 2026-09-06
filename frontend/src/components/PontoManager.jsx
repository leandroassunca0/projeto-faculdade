import { useState, useEffect } from 'react';
import api from '../services/api';
import './PontoManager.css';

export default function PontoManager() {
  const [pontos, setPontos] = useState([]);
  const [pessoas, setPessoas] = useState([]);
  const [filtroFuncionario, setFiltroFuncionario] = useState('');

  const [form, setForm] = useState({
    funcionarioId: '',
    inicio: '',
    saida: '',
    justificativa: '',
  });

  const [mensagem, setMensagem] = useState('');
  const [isErro, setIsErro] = useState(false);

  const carregarDadosIniciais = async () => {
    try {
      const [resPontos, resPessoas] = await Promise.all([
        api.get('/pontos'),
        api.get('/pessoas'),
      ]);
      setPontos(resPontos.data);
      setPessoas(resPessoas.data);
    } catch (error) {
      console.error('Erro ao carregar dados:', error);
      setMensagem('Erro ao conectar com a API.');
      setIsErro(true);
    }
  };

  useEffect(() => {
    carregarDadosIniciais();
  }, []);

  const handleChange = (e) => {
    setForm({
      ...form,
      [e.target.name]: e.target.value,
    });
  };

  const handleSalvar = async (e) => {
    e.preventDefault();

    if (!form.funcionarioId) {
      setMensagem('Por favor, selecione um colaborador.');
      setIsErro(true);
      return;
    }

    try {
      const payload = {
        inicio: form.inicio,
        saida: form.saida ? form.saida : null,
        justificativa: form.justificativa,
        funcionario: {
          id: Number(form.funcionarioId),
        },
      };

      await api.post('/pontos', payload);

      setMensagem('Marcação de ponto registrada com sucesso!');
      setIsErro(false);
      setForm({
        funcionarioId: '',
        inicio: '',
        saida: '',
        justificativa: '',
      });

      atualizarListaPontos(filtroFuncionario);
    } catch (error) {
      setMensagem(error.response?.data?.message || 'Erro ao registrar ponto (verifique os horários informados).');
      setIsErro(true);
    }
  };

  const handleExcluir = async (id) => {
    if (!window.confirm('Deseja realmente remover este registro de ponto?')) return;

    try {
      await api.delete(`/pontos/${id}`);
      atualizarListaPontos(filtroFuncionario);
    } catch (error) {
      alert('Erro ao excluir registro de ponto.');
    }
  };

  const atualizarListaPontos = async (funcionarioId) => {
    try {
      if (funcionarioId) {
        const response = await api.get(`/pontos/funcionario/${funcionarioId}`);
        setPontos(response.data);
      } else {
        const response = await api.get('/pontos');
        setPontos(response.data);
      }
    } catch (error) {
      console.error('Erro ao filtrar pontos:', error);
    }
  };

  const handleFiltroChange = (e) => {
    const id = e.target.value;
    setFiltroFuncionario(id);
    atualizarListaPontos(id);
  };

  const formatarDataHora = (isoString) => {
    if (!isoString) return null;
    const data = new Date(isoString);
    return data.toLocaleString('pt-BR', {
      day: '2-digit',
      month: '2-digit',
      year: 'numeric',
      hour: '2-digit',
      minute: '2-digit',
    });
  };

  return (
    <div className="ponto-container">
      <h2 className="ponto-title">Registro de Ponto Eletrônico</h2>

      {mensagem && (
        <div className={`feedback-message ${isErro ? 'feedback-error' : 'feedback-success'}`}>
          {mensagem}
        </div>
      )}

      <form onSubmit={handleSalvar} className="ponto-form-grid">
        <div className="ponto-form-group full-width">
          <label>Colaborador</label>
          <select
            name="funcionarioId"
            value={form.funcionarioId}
            onChange={handleChange}
            required
          >
            <option value="">Selecione o colaborador...</option>
            {pessoas.map((p) => (
              <option key={p.id} value={p.id}>
                {p.nome} — CPF: {p.cpf}
              </option>
            ))}
          </select>
        </div>

        <div className="ponto-form-group">
          <label>Data/Hora de Entrada</label>
          <input
            type="datetime-local"
            name="inicio"
            required
            value={form.inicio}
            onChange={handleChange}
          />
        </div>

        <div className="ponto-form-group">
          <label>Data/Hora de Saída (Opcional)</label>
          <input
            type="datetime-local"
            name="saida"
            value={form.saida}
            onChange={handleChange}
          />
        </div>

        <div className="ponto-form-group full-width">
          <label>Justificativa / Observação</label>
          <input
            type="text"
            name="justificativa"
            placeholder="Ex: Plantão extraordinário, home office..."
            value={form.justificativa}
            onChange={handleChange}
          />
        </div>

        <button type="submit" className="btn-submit">
          Registrar Ponto
        </button>
      </form>

      <div className="filter-bar">
        <label>Filtrar registros por colaborador:</label>
        <select value={filtroFuncionario} onChange={handleFiltroChange}>
          <option value="">Todos os colaboradores</option>
          {pessoas.map((p) => (
            <option key={p.id} value={p.id}>
              {p.nome}
            </option>
          ))}
        </select>
      </div>

      <div className="table-responsive">
        <table className="pessoa-table">
          <thead>
            <tr>
              <th>Colaborador</th>
              <th>Entrada</th>
              <th>Saída</th>
              <th>Justificativa</th>
              <th className="col-actions">Ações</th>
            </tr>
          </thead>
          <tbody>
            {pontos.map((ponto) => (
              <tr key={ponto.id}>
                <td>{ponto.funcionario?.nome || 'Não identificado'}</td>
                <td>{formatarDataHora(ponto.inicio)}</td>
                <td>
                  {ponto.saida ? (
                    formatarDataHora(ponto.saida)
                  ) : (
                    <span className="badge-saida-pendente">Em aberto</span>
                  )}
                </td>
                <td>{ponto.justificativa || '—'}</td>
                <td className="col-actions">
                  <button
                    onClick={() => handleExcluir(ponto.id)}
                    className="btn-delete"
                  >
                    Excluir
                  </button>
                </td>
              </tr>
            ))}
            {pontos.length === 0 && (
              <tr>
                <td colSpan="5" className="empty-row">
                  Nenhum registro de ponto encontrado.
                </td>
              </tr>
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
}