import { useState, useEffect } from 'react';
import api from '../services/api';
import './PessoaTipoManager.css';

export default function PessoaTipoManager() {
  const [tipos, setTipos] = useState([]);
  const [nome, setNome] = useState('');
  const [mensagem, setMensagem] = useState('');
  const [isErro, setIsErro] = useState(false);

  const carregarTipos = async () => {
    try {
      const response = await api.get('/pessoa-tipos');
      setTipos(response.data);
    } catch (error) {
      console.error('Erro ao buscar tipos:', error);
      setMensagem('Erro ao carregar dados do servidor.');
      setIsErro(true);
    }
  };

  useEffect(() => {
    carregarTipos();
  }, []);

  const handleSalvar = async (e) => {
    e.preventDefault();
    if (!nome.trim()) return;

    try {
      await api.post('/pessoa-tipos', { nome });
      setNome('');
      setMensagem('Tipo cadastrado com sucesso!');
      setIsErro(false);
      carregarTipos();
    } catch (error) {
      setMensagem(error.response?.data?.message || 'Erro ao cadastrar tipo.');
      setIsErro(true);
    }
  };

  const handleExcluir = async (id) => {
    if (!window.confirm('Deseja realmente remover este registro?')) return;

    try {
      await api.delete(`/pessoa-tipos/${id}`);
      carregarTipos();
    } catch (error) {
      alert('Não foi possível excluir (verifique se existem pessoas vinculadas a este tipo).');
    }
  };

  return (
    <div className="card-container">
      <h2 className="card-title">Gerenciar Tipos de Colaborador</h2>

      {mensagem && (
        <div className={`feedback-message ${isErro ? 'feedback-error' : 'feedback-success'}`}>
          {mensagem}
        </div>
      )}

      <form onSubmit={handleSalvar} className="input-form">
        <input
          type="text"
          className="text-input"
          placeholder="Ex: Terceirizado, CLT..."
          value={nome}
          onChange={(e) => setNome(e.target.value)}
        />
        <button type="submit" className="btn-primary">
          Salvar
        </button>
      </form>

      <table className="data-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>Nome</th>
            <th className="col-actions">Ações</th>
          </tr>
        </thead>
        <tbody>
          {tipos.map((tipo) => (
            <tr key={tipo.id}>
              <td>{tipo.id}</td>
              <td>{tipo.nome}</td>
              <td className="col-actions">
                <button
                  onClick={() => handleExcluir(tipo.id)}
                  className="btn-delete"
                >
                  Excluir
                </button>
              </td>
            </tr>
          ))}
          {tipos.length === 0 && (
            <tr>
              <td colSpan="3" className="empty-row">
                Nenhum tipo cadastrado.
              </td>
            </tr>
          )}
        </tbody>
      </table>
    </div>
  );
}