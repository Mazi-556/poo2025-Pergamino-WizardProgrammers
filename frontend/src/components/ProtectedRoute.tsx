import { Navigate } from 'react-router-dom';
import { useAuth } from '../hooks/useAuth';
import { JSX } from 'react/jsx-runtime';

interface Props {
  children: JSX.Element;
  role?: 'admin' | 'participant'; // el rol que necesitamos para entrar
}

const ProtectedRoute = ({ children, role }: Props) => {
  const { user, loading } = useAuth();

  // mientras carga la sesion no mostramos nada
  if (loading) return <div>Cargando...</div>;

  // si no esta logueado, mandamos al login
  if (!user) {
    return <Navigate to="/login" />;
  }

  // si el rol no coincide (ej: participante queriendo entrar a admin), al home
  if (role && user.role !== role) {
    return <Navigate to="/" />;
  }

  return children;
};

export default ProtectedRoute;