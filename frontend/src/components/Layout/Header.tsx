import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../../hooks/useAuth';

const Header = () => {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <header className="bg-blue-600 text-white shadow-md">
      <div className="container mx-auto px-4 py-4 flex justify-between items-center">
        <Link to="/" className="text-xl font-bold">Torneos App</Link>
        
        <nav className="space-x-4">
          {user ? (
            <>
              {/* links para admin */}
              {user.role === 'admin' && (
                <Link to="/admin" className="hover:underline">Panel Admin</Link>
              )}
              {/* links para participante */}
              {user.role === 'participant' && (
                <Link to="/mis-inscripciones" className="hover:underline">Mis Torneos</Link>
              )}
              <button onClick={handleLogout} className="bg-red-500 px-3 py-1 rounded">
                Salir
              </button>
            </>
          ) : (
            <Link to="/login" className="hover:underline">Ingresar</Link>
          )}
        </nav>
      </div>
    </header>
  );
};

export default Header;