import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { apiService } from '../../services/api';
import { Button } from '../../components/Common/Button';
import { Card, CardBody, CardHeader } from '../../components/Common/Card';
import { Alert } from '../../components/Common/Alert';
import type { AdminTournamentSummary } from '../../types';

export const AdminDashboard: React.FC = () => {
  const [tournaments, setTournaments] = useState<AdminTournamentSummary[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    loadTournaments();
  }, []);

  const loadTournaments = async () => {
    try {
      setLoading(true);
      const data = (await apiService.getAdminTournaments()) as AdminTournamentSummary[];
      setTournaments(data);
    } catch (err: any) {
      setError(err.message || 'Error al cargar torneos');
    } finally {
      setLoading(false);
    }
  };

  const publishedCount = tournaments.filter((t) => t.published).length;
  const unpublishedCount = tournaments.filter((t) => !t.published).length;

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
      {/* Header */}
      <div className="mb-8">
        <h1 className="section-title">Dashboard Administrativo</h1>
        <p className="section-subtitle">Resumen y gestión de tus torneos</p>
      </div>

      {/* Stats */}
      <div className="grid md:grid-cols-3 gap-6 mb-8">
        <Card>
          <CardBody className="p-6">
            <div className="flex justify-between items-start">
              <div>
                <p className="text-gray-600 text-sm">Total de Torneos</p>
                <p className="text-4xl font-bold text-primary-600 mt-2">{tournaments.length}</p>
              </div>
              <span className="text-3xl">🎯</span>
            </div>
          </CardBody>
        </Card>

        <Card>
          <CardBody className="p-6">
            <div className="flex justify-between items-start">
              <div>
                <p className="text-gray-600 text-sm">Publicados</p>
                <p className="text-4xl font-bold text-accent-600 mt-2">{publishedCount}</p>
              </div>
              <span className="text-3xl">✓</span>
            </div>
          </CardBody>
        </Card>

        <Card>
          <CardBody className="p-6">
            <div className="flex justify-between items-start">
              <div>
                <p className="text-gray-600 text-sm">Borradores</p>
                <p className="text-4xl font-bold text-orange-600 mt-2">{unpublishedCount}</p>
              </div>
              <span className="text-3xl">📝</span>
            </div>
          </CardBody>
        </Card>
      </div>

      {/* Actions */}
      <div className="mb-8 flex flex-col sm:flex-row gap-4">
        <Link to="/admin/tournaments" className="flex-1 sm:flex-none">
          <Button fullWidth size="lg">
            Ver Todos los Torneos
          </Button>
        </Link>
        <Link to="/admin/tournaments" className="flex-1 sm:flex-none">
          <Button variant="accent" size="lg" fullWidth>
            + Crear Nuevo Torneo
          </Button>
        </Link>
      </div>

      {/* Error */}
      {error && (
        <Alert
          type="error"
          message={error}
          onClose={() => setError(null)}
          className="mb-6"
        />
      )}

      {/* Tournaments List */}
      {loading ? (
        <Card>
          <CardBody className="p-8 text-center">
            <div className="inline-block animate-spin">⏳</div>
            <p className="text-gray-600 mt-2">Cargando torneos...</p>
          </CardBody>
        </Card>
      ) : tournaments.length === 0 ? (
        <Card>
          <CardBody className="p-8 text-center">
            <p className="text-gray-600 mb-4">No tienes torneos creados aún</p>
            <Link to="/admin/tournaments">
              <Button>Crear tu primer torneo</Button>
            </Link>
          </CardBody>
        </Card>
      ) : (
        <Card>
          <CardHeader title="Últimos Torneos" />
          <CardBody>
            <div className="space-y-4">
              {tournaments.slice(0, 5).map((tournament) => (
                <div
                  key={tournament.id}
                  className="flex flex-col sm:flex-row sm:items-center sm:justify-between p-4 border border-gray-100 rounded-lg hover:bg-gray-50 transition"
                >
                  <div className="flex-grow">
                    <h3 className="font-semibold text-gray-900">{tournament.name}</h3>
                    <p className="text-sm text-gray-600 mt-1">{tournament.description}</p>
                    <div className="flex gap-4 mt-2 text-xs text-gray-500">
                      <span>📅 {new Date(tournament.startDate).toLocaleDateString()}</span>
                      <span>{tournament.published ? '✓ Publicado' : '📝 Borrador'}</span>
                    </div>
                  </div>
                  <Link
                    to={`/admin/tournaments/${tournament.id}`}
                    className="mt-3 sm:mt-0"
                  >
                    <Button size="sm" variant="secondary">
                      Ver Detalles
                    </Button>
                  </Link>
                </div>
              ))}
            </div>
          </CardBody>
        </Card>
      )}
    </div>
  );
};
