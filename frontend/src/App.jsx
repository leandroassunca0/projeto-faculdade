import { useState } from 'react';
import PessoaTipoManager from './components/PessoaTipoManager';
import PessoaManager from './components/PessoaManager';
import './App.css';

export default function App() {
  const [abaAtiva, setAbaAtiva] = useState('pessoas');

  return (
    <div>
      <nav className="app-navbar">
        <button
          className={`nav-btn ${abaAtiva === 'pessoas' ? 'active' : ''}`}
          onClick={() => setAbaAtiva('pessoas')}
        >
          Colaboradores
        </button>
        <button
          className={`nav-btn ${abaAtiva === 'tipos' ? 'active' : ''}`}
          onClick={() => setAbaAtiva('tipos')}
        >
          Tipos de Vínculo
        </button>
      </nav>

      <main className="app-main">
        {abaAtiva === 'pessoas' && <PessoaManager />}
        {abaAtiva === 'tipos' && <PessoaTipoManager />}
      </main>
    </div>
  );
}