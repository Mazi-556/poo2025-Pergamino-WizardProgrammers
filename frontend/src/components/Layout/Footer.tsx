import React from 'react';

export const Footer: React.FC = () => {
  const currentYear = new Date().getFullYear();

  return (
    <footer className="bg-gray-900 text-gray-100 mt-16 border-t border-gray-800">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
        <div className="grid grid-cols-1 md:grid-cols-4 gap-8 mb-8">
          {/* Logo Section */}
          <div>
            <div className="flex items-center gap-2 mb-4">
              <div className="w-10 h-10 bg-gradient-to-br from-primary-500 to-accent-500 rounded-lg flex items-center justify-center">
                <span className="text-white font-bold text-lg">T</span>
              </div>
              <div>
                <h3 className="font-bold">Torneos</h3>
                <p className="text-xs text-gray-400">UNNOBA</p>
              </div>
            </div>
            <p className="text-sm text-gray-400">
              Plataforma de gestión de torneos académicos
            </p>
          </div>

          {/* For Participants */}
          <div>
            <h4 className="font-semibold mb-4 text-white">Para Participantes</h4>
            <ul className="space-y-2 text-sm text-gray-400">
              <li><a href="#" className="hover:text-primary-400 transition">Ver Torneos</a></li>
              <li><a href="#" className="hover:text-primary-400 transition">Mis Inscripciones</a></li>
              <li><a href="#" className="hover:text-primary-400 transition">Crear Cuenta</a></li>
            </ul>
          </div>

          {/* For Admins */}
          <div>
            <h4 className="font-semibold mb-4 text-white">Para Administradores</h4>
            <ul className="space-y-2 text-sm text-gray-400">
              <li><a href="#" className="hover:text-primary-400 transition">Dashboard</a></li>
              <li><a href="#" className="hover:text-primary-400 transition">Gestionar Torneos</a></li>
              <li><a href="#" className="hover:text-primary-400 transition">Reportes</a></li>
            </ul>
          </div>

          {/* Contact */}
          <div>
            <h4 className="font-semibold mb-4 text-white">Contacto</h4>
            <ul className="space-y-2 text-sm text-gray-400">
              <li><a href="mailto:torneos@unnoba.edu.ar" className="hover:text-primary-400 transition">torneos@unnoba.edu.ar</a></li>
              <li><a href="tel:+542314123456" className="hover:text-primary-400 transition">+54 (23) 14-123456</a></li>
              <li>Pergamino, Buenos Aires, AR</li>
            </ul>
          </div>
        </div>

        <div className="border-t border-gray-800 pt-8">
          <div className="flex flex-col md:flex-row justify-between items-center">
            <p className="text-sm text-gray-400">
              &copy; {currentYear} Wizard Programmers. Todos los derechos reservados.
            </p>
            <div className="flex gap-6 mt-4 md:mt-0 text-sm text-gray-400">
              <a href="#" className="hover:text-primary-400 transition">Privacidad</a>
              <a href="#" className="hover:text-primary-400 transition">Términos</a>
              <a href="#" className="hover:text-primary-400 transition">Cookies</a>
            </div>
          </div>
        </div>
      </div>
    </footer>
  );
};
