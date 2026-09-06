import { useState, useEffect } from 'react';
import api from '../services/api';
import './PessoaManager.css';

export default function PessoaManager() {
  const [pessoas, setPessoas] = useState([]);
  const [tipos, setTipos] = useState([]);
  
  // Estado único para controlar os campos do formulário
  const [form, setForm] = useState({
    nome: '',
    cpf: '',
    nascimento: '',
    telefone: '',
    pessoaTipoId: '',
  });

  const [mensagem, setMensagem] = useState('');
  const [isErro, setIsErro] = useState(false);

  // Busca as pessoas e os tipos cadastrados
  const carregarDados = async () => {
    try {
      const [resPessoas, resTipos] = await Promise.all([
        api.get('/pessoas'),
        api.get('/pessoa-tipos'),
      ]);
      setPessoas(resPessoas.data);
      setTipos(resTipos.data);
    } catch (error) {
      console.error('Erro ao carregar dados:', error);
      setMensagem('Erro ao carregar dados do servidor.');
      setIsErro(true);
    }
  };

  useEffect(() => {
    carregarDados();
  }, []);

  const handleChange = (e) => {
    setForm({
      ...form,
      [e.target.name]: e.target.value,
    });
  };

  const handleSalvar = async (e) => {
    e.preventDefault();

    if (!form.pessoaTipoId) {
      setMensagem('Por favor, selecione um tipo de colaborador.');
      setIsErro(true);
      return;
    }

    try {
      // Monta o payload esperado pelo backend
      const payload = {
        nome: form.nome,
        cpf: form.cpf,
        nascimento: form.nascimento,
        telefone: form.telefone,
        pessoaTipo: {
          id: Number(form.pessoaTipoId),
        },
      };

      await api.post('/pessoas', payload);

      setMensagem('Colaborador cadastrado com sucesso!');
      setIsErro(false);
      setForm({
        nome: '',
        cpf: '',
        nascimento: '',
        telefone: '',
        pessoaTipoId: '',
      });

      carregarDados();
    } catch (error) {
      setMensagem(error.response?.data?.message || 'Erro ao cadastrar colaborador (verifique se o CPF é duplicado).');
      setIsErro(true);
    }
  };

  const handleExcluir = async (id) => {
    if (!window.confirm('Deseja realmente excluir este colaborador?')) return;

    try {
      await api.delete(`/pessoas/${id}`);
      carregarDados();
    } catch (error) {
      alert('Não foi possível excluir (verifique se existem pontos vinculados a este colaborador).');
    }
  };

  return (
    <div className="pessoa-container">
      <h2 className="pessoa-title">Cadastro de Colaboradores</h2>

      {mensagem && (
        <div className={`feedback-message ${isErro ? 'feedback-error' : 'feedback-success'}`}>
          {mensagem}
        </div>
      )}

      <form onSubmit={handleSalvar} className="form-grid">
        <div className="form-group">
          <label>Nome Completo</label>
          <input
            type="text"
            name="nome"
            required
            placeholder="Ex: Ana Silva"
            value={form.nome}
            onChange={handleChange}
          />
        </div>

        <div className="form-group">
          <label>CPF</label>
          <input
            type="text"
            name="cpf"
            required
            placeholder="000.000.000-00"
            value={form.cpf}
            onChange={handleChange}
          />
        </div>

        <div className="form-group">
          <label>Data de Nascimento</label>
          <input
            type="date"
            name="nascimento"
            required
            value={form.nascimento}
            onChange={handleChange}
          />
        </div>

        <div className="form-group">
          <label>Telefone</label>
          <input
            type="tel"
            name="telefone"
            required
            placeholder="(11) 99999-9999"
            value={form.telefone}
            onChange={handleChange}
          />
        </div>

        <div className="form-group" style={{ gridColumn: '1 / -1' }}>
          <label>Tipo de Vínculo</label>
          <select
            name="pessoaTipoId"
            value={form.pessoaTipoId}
            onChange={handleChange}
            required
          >
            <option value="">Selecione um tipo...</option>
            {tipos.map((tipo) => (
              <option key={tipo.id} value={tipo.id}>
                {tipo.nome}
              </option>
            ))}
          </select>
        </div>

        <button type="submit" className="btn-submit">
          Cadastrar Colaborador
        </button>
      </form>

      <div className="table-responsive">
        <table className="pessoa-table">
          <thead>
            <tr>
              <th>Nome</th>
              <th>CPF</th>
              <th>Telefone</th>
              <th>Tipo</th>
              <th className="col-actions">Ações</th>
            </tr>
          </thead>
          <tbody>
            {pessoas.map((pessoa) => (
              <tr key={pessoa.id}>
                <td>{pessoa.nome}</td>
                <td>{pessoa.cpf}</td>
                <td>{pessoa.telefone}</td>
                <td>
                  <span className="badge-tipo">
                    {pessoa.pessoaTipo?.nome || 'Não definido'}
                  </span>
                </td>
                <td className="col-actions">
                  <button
                    onClick={() => handleExcluir(pessoa.id)}
                    className="btn-delete"
                  >
                    Excluir
                  </button>
                </td>
              </tr>
            ))}
            {pessoas.length === 0 && (
              <tr>
                <td colSpan="5" className="empty-row">
                  Nenhum colaborador cadastrado.
                </td>
              </tr>
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
}