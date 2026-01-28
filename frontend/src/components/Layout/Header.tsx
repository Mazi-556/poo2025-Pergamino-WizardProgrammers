import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../../hooks/useAuth';

export const Header: React.FC = () => {
  const { isAuthenticated, userRole, logout } = useAuth();
  const [mobileMenuOpen, setMobileMenuOpen] = useState(false);
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/');
  };

  return (
    <header className="sticky top-0 z-50 bg-white shadow-sm border-b border-gray-200">
      <nav className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 h-16 flex items-center justify-between">
        {/* Logo */}
        <Link to="/" className="flex items-center gap-2">
          <div className="w-10 h-10 bg-gradient-to-br from-primary-600 to-accent-600 rounded-lg flex items-center justify-center">
            <span className="text-white font-bold text-lg">T</span>
          </div>
          <div className="hidden sm:block">
            <h1 className="text-lg font-bold text-gray-900">Torneos</h1>
            <p className="text-xs text-gray-600">UNNOBA</p>
          </div>
        </Link>

        {/* Desktop Navigation */}
        <div className="hidden md:flex items-center gap-8">
          {!isAuthenticated ? (
            <>
              <Link to="/" className="text-gray-700 hover:text-primary-600 transition">
                Inicio
              </Link>
              <Link to="/login" className="btn-primary">
                Ingresar
              </Link>
            </>
          ) : userRole === 'admin' ? (
            <>
              <Link to="/admin/dashboard" className="text-gray-700 hover:text-primary-600 transition">
                Dashboard
              </Link>
              <Link to="/admin/tournaments" className="text-gray-700 hover:text-primary-600 transition">
                Torneos
              </Link>
              <button onClick={handleLogout} className="btn-secondary">
                Cerrar sesión
              </button>
            </>
          ) : (
            <>
              <Link to="/tournaments" className="text-gray-700 hover:text-primary-600 transition">
                Torneos
              </Link>
              <Link to="/registrations" className="text-gray-700 hover:text-primary-600 transition">
                Mis Inscripciones
              </Link>
              <button onClick={handleLogout} className="btn-secondary">
                Cerrar sesión
              </button>
            </>
          )}
        </div>

        {/* Mobile Menu Button */}
        <button
          className="md:hidden p-2"
          onClick={() => setMobileMenuOpen(!mobileMenuOpen)}
        >
          <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M4 6h16M4 12h16M4 18h16" />
          </svg>
        </button>
      </nav>

      {/* Mobile Menu */}
      {mobileMenuOpen && (
        <div className="md:hidden bg-white border-t border-gray-200 p-4 space-y-2">
          {!isAuthenticated ? (
            <>
              <Link to="/" className="block px-4 py-2 text-gray-700 hover:bg-gray-100 rounded">
                Inicio
              </Link>
              <Link to="/login" className="block px-4 py-2 btn-primary text-center">
                Ingresar
              </Link>
            </>
          ) : userRole === 'admin' ? (
            <>
              <Link to="/admin/dashboard" className="block px-4 py-2 text-gray-700 hover:bg-gray-100 rounded">
                Dashboard
              </Link>
              <Link to="/admin/tournaments" className="block px-4 py-2 text-gray-700 hover:bg-gray-100 rounded">
                Torneos
              </Link>
              <button onClick={handleLogout} className="w-full text-left px-4 py-2 btn-secondary">
                Cerrar sesión
              </button>
            </>
          ) : (
            <>
              <Link to="/tournaments" className="block px-4 py-2 text-gray-700 hover:bg-gray-100 rounded">
                Torneos
              </Link>
              <Link to="/registrations" className="block px-4 py-2 text-gray-700 hover:bg-gray-100 rounded">
                Mis Inscripciones
              </Link>
              <button onClick={handleLogout} className="w-full text-left px-4 py-2 btn-secondary">
                Cerrar sesión
              </button>
            </>
          )}
        </div>
      )}
    </header>
  );
};
