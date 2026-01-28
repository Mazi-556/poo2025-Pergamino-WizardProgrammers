import React from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../hooks/useAuth';
import { Button } from '../components/Common/Button';

export const HomePage: React.FC = () => {
  const { isAuthenticated, userRole } = useAuth();

  return (
    <div className="w-full">
      {}
      <section className="relative overflow-hidden bg-gradient-to-br from-primary-900 via-primary-800 to-accent-900 text-white py-20 md:py-32">
        <div className="absolute inset-0 overflow-hidden">
          <div className="absolute -top-40 -right-40 w-80 h-80 bg-accent-500 rounded-full opacity-10 blur-3xl"></div>
          <div className="absolute -bottom-40 -left-40 w-80 h-80 bg-primary-500 rounded-full opacity-10 blur-3xl"></div>
        </div>

        <div className="relative max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="grid md:grid-cols-2 gap-12 items-center">
            <div className="animate-slideUp">
              <div className="inline-block bg-accent-500/20 border border-accent-400/30 rounded-full px-4 py-2 mb-6">
                <span className="text-accent-100 text-sm font-medium">🎯 Plataforma de Torneos Académicos</span>
              </div>

              <h1 className="text-4xl md:text-6xl font-bold mb-6 leading-tight">
                Gestiona Torneos con
                <span className="block bg-gradient-to-r from-accent-300 to-primary-300 bg-clip-text text-transparent">
                  Elegancia y Facilidad
                </span>
              </h1>

              <p className="text-lg text-gray-100 mb-8 leading-relaxed">
                La plataforma completa para organizar, gestionar e inscribirse en torneos universitarios. Diseñada para estudiantes y administradores de la Universidad Nacional del Noroeste de la Provincia de Buenos Aires.
              </p>

              <div className="flex flex-col sm:flex-row gap-4">
                {!isAuthenticated ? (
                  <>
                    <Link to="/login">
                      <Button size="lg" className="w-full sm:w-auto">
                        Ingresar
                      </Button>
                    </Link>
                    <Link to="/signup">
                      <Button size="lg" variant="secondary" className="w-full sm:w-auto">
                        Crear Cuenta
                      </Button>
                    </Link>
                  </>
                ) : userRole === 'admin' ? (
                  <Link to="/admin/dashboard">
                    <Button size="lg">
                      Ir al Dashboard
                    </Button>
                  </Link>
                ) : (
                  <Link to="/tournaments">
                    <Button size="lg">
                      Ver Torneos
                    </Button>
                  </Link>
                )}
              </div>
            </div>

            {}
            <div className="hidden md:flex justify-center">
              <div className="relative w-full max-w-md">
                <div className="absolute inset-0 bg-gradient-to-r from-accent-400 to-primary-400 rounded-3xl blur-2xl opacity-30"></div>
                <div className="relative bg-gradient-to-br from-primary-700 to-accent-700 rounded-3xl p-8 border border-white/10">
                  <div className="space-y-4">
                    <div className="h-12 bg-white/10 rounded-lg"></div>
                    <div className="h-32 bg-white/10 rounded-lg"></div>
                    <div className="grid grid-cols-3 gap-2">
                      <div className="h-10 bg-white/10 rounded-lg"></div>
                      <div className="h-10 bg-white/10 rounded-lg"></div>
                      <div className="h-10 bg-accent-500/30 rounded-lg"></div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>

      {}
      <section className="py-20 md:py-32 bg-white">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center mb-16">
            <h2 className="section-title">Características Principales</h2>
            <p className="section-subtitle max-w-2xl mx-auto">
              Todo lo que necesitas para gestionar torneos académicos de forma profesional
            </p>
          </div>

          <div className="grid md:grid-cols-3 gap-8">
            {[
              {
                icon: '🎮',
                title: 'Gestión Completa',
                description: 'Crea y administra torneos con múltiples competencias de forma intuitiva',
              },
              {
                icon: '📊',
                title: 'Estadísticas en Tiempo Real',
                description: 'Visualiza inscripciones, ingresos y métricas de tus torneos al instante',
              },
              {
                icon: '👥',
                title: 'Inscripciones Fáciles',
                description: 'Los participantes pueden inscribirse en competencias con descuentos automáticos',
              },
              {
                icon: '🔐',
                title: 'Seguridad Garantizada',
                description: 'Autenticación segura con JWT y contraseñas encriptadas',
              },
              {
                icon: '💰',
                title: 'Sistema de Precios Inteligente',
                description: 'Descuentos automáticos del 50% en segunda y posteriores competencias',
              },
              {
                icon: '📱',
                title: 'Totalmente Responsivo',
                description: 'Accede desde cualquier dispositivo con una experiencia perfecta',
              },
            ].map((feature, index) => (
              <div
                key={index}
                className="card p-8 hover:scale-105 transition-transform duration-300"
              >
                <div className="text-4xl mb-4">{feature.icon}</div>
                <h3 className="text-xl font-semibold text-gray-900 mb-3">{feature.title}</h3>
                <p className="text-gray-600">{feature.description}</p>
              </div>
            ))}
          </div>
        </div>
      </section>

      {}
      <section className="py-20 md:py-32 bg-gray-50">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="grid md:grid-cols-2 gap-12 items-center">
            <div>
              <span className="inline-block bg-primary-100 text-primary-700 px-4 py-1 rounded-full text-sm font-semibold mb-4">
                Para Administradores
              </span>
              <h2 className="section-title">Control Total de tus Torneos</h2>
              <p className="section-subtitle mt-4">
                Herramientas poderosas diseñadas para administradores de torneos
              </p>

              <ul className="mt-8 space-y-4">
                {[
                  'Crear y editar torneos sin límite',
                  'Gestionar múltiples competencias por torneo',
                  'Establecer cuotas y precios',
                  'Ver lista completa de inscritos',
                  'Publicar torneos cuando estén listos',
                  'Seguimiento de recaudación',
                ].map((item, idx) => (
                  <li key={idx} className="flex items-center gap-3 text-gray-700">
                    <span className="w-6 h-6 rounded-full bg-primary-100 flex items-center justify-center text-primary-600 font-bold">
                      ✓
                    </span>
                    {item}
                  </li>
                ))}
              </ul>
            </div>

            <div className="hidden md:flex justify-center">
              <div className="bg-gradient-to-br from-primary-50 to-accent-50 rounded-2xl p-8 border border-primary-200">
                <div className="space-y-3">
                  <div className="h-10 bg-primary-200 rounded-lg w-3/4"></div>
                  <div className="h-32 bg-primary-100 rounded-lg"></div>
                  <div className="grid grid-cols-2 gap-2">
                    <div className="h-12 bg-accent-200 rounded-lg"></div>
                    <div className="h-12 bg-accent-200 rounded-lg"></div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>

      {}
      <section className="py-20 md:py-32 bg-white">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="grid md:grid-cols-2 gap-12 items-center">
            <div className="hidden md:flex justify-center order-last md:order-first">
              <div className="bg-gradient-to-br from-accent-50 to-primary-50 rounded-2xl p-8 border border-accent-200">
                <div className="space-y-3">
                  <div className="h-10 bg-accent-200 rounded-lg w-3/4"></div>
                  <div className="h-32 bg-accent-100 rounded-lg"></div>
                  <div className="grid grid-cols-3 gap-2">
                    <div className="h-10 bg-primary-200 rounded-lg"></div>
                    <div className="h-10 bg-primary-200 rounded-lg"></div>
                    <div className="h-10 bg-accent-300 rounded-lg"></div>
                  </div>
                </div>
              </div>
            </div>

            <div>
              <span className="inline-block bg-accent-100 text-accent-700 px-4 py-1 rounded-full text-sm font-semibold mb-4">
                Para Participantes
              </span>
              <h2 className="section-title">Únete a Torneos en Segundos</h2>
              <p className="section-subtitle mt-4">
                Experimenta la forma más fácil de participar en competencias
              </p>

              <ul className="mt-8 space-y-4">
                {[
                  'Explorar torneos disponibles',
                  'Ver detalles de competencias',
                  'Inscribirse con un clic',
                  'Descuentos automáticos',
                  'Gestionar tus inscripciones',
                  'Acceso desde cualquier lugar',
                ].map((item, idx) => (
                  <li key={idx} className="flex items-center gap-3 text-gray-700">
                    <span className="w-6 h-6 rounded-full bg-accent-100 flex items-center justify-center text-accent-600 font-bold">
                      ✓
                    </span>
                    {item}
                  </li>
                ))}
              </ul>
            </div>
          </div>
        </div>
      </section>

      {}
      <section className="py-20 md:py-32 bg-gradient-to-r from-primary-600 to-accent-600 text-white">
        <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 text-center">
          <h2 className="text-4xl md:text-5xl font-bold mb-6">¿Listo para Comenzar?</h2>
          <p className="text-lg text-white/80 mb-8 max-w-2xl mx-auto">
            Únete a nuestra plataforma y sé parte de la revolución de torneos universitarios
          </p>

          {!isAuthenticated && (
            <div className="flex flex-col sm:flex-row gap-4 justify-center">
              <Link to="/login">
                <Button size="lg" variant="secondary">
                  Ingresar Cuenta Existente
                </Button>
              </Link>
              <Link to="/signup">
                <Button size="lg">
                  Crear Mi Cuenta
                </Button>
              </Link>
            </div>
          )}
        </div>
      </section>

      {}
      <section className="py-16 bg-gray-900 text-gray-100 border-t border-gray-800">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="grid md:grid-cols-3 gap-8 text-center">
            <div>
              <div className="text-4xl font-bold text-accent-400 mb-2">500+</div>
              <p className="text-gray-400">Participantes activos</p>
            </div>
            <div>
              <div className="text-4xl font-bold text-accent-400 mb-2">50+</div>
              <p className="text-gray-400">Torneos organizados</p>
            </div>
            <div>
              <div className="text-4xl font-bold text-accent-400 mb-2">100%</div>
              <p className="text-gray-400">Satisfacción garantizada</p>
            </div>
          </div>
        </div>
      </section>
    </div>
  );
};
