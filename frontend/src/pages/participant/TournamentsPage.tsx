import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { apiService } from '../../services/api';
import { Button } from '../../components/Common/Button';
import { Card, CardBody } from '../../components/Common/Card';
import { Alert } from '../../components/Common/Alert';
import type { TournamentResponse } from '../../types';

export const TournamentsPage: React.FC = () => {
  const [tournaments, setTournaments] = useState<TournamentResponse[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    loadTournaments();
  }, []);

  const loadTournaments = async () => {
    try {
      setLoading(true);
      const data = (await apiService.getTournaments()) as TournamentResponse[];
      setTournaments(data);
    } catch (err: any) {
      setError(err.message || 'Error al cargar torneos');
    } finally {
      setLoading(false);
    }
  };

  const isUpcoming = (startDate: string) => {
    return new Date(startDate) > new Date();
  };

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
      {/* Header */}
      <div className="mb-12">
        <h1 className="section-title">Torneos Disponibles</h1>
        <p className="section-subtitle">
          Explora todos los torneos disponibles y participa en las competencias que te interesen
        </p>
      </div>

      {error && (
        <Alert
          type="error"
          message={error}
          onClose={() => setError(null)}
          className="mb-6"
        />
      )}

      {/* Tournaments Grid */}
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
            <p className="text-gray-600 mb-4">No hay torneos disponibles en este momento</p>
            <Link to="/tournaments">
              <Button onClick={loadTournaments}>Actualizar</Button>
            </Link>
          </CardBody>
        </Card>
      ) : (
        <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-6">
          {tournaments.map((tournament) => (
            <Card key={tournament.id} hoverable className="flex flex-col">
              <CardBody className="p-6 flex flex-col flex-grow">
                <div className="mb-4">
                  <div className="flex items-start justify-between mb-2">
                    <h3 className="text-lg font-bold text-gray-900 flex-grow">
                      {tournament.name}
                    </h3>
                    {isUpcoming(tournament.startDate) && (
                      <span className="px-2 py-1 bg-green-100 text-green-700 text-xs font-semibold rounded-full">
                        Próximo
                      </span>
                    )}
                  </div>
                  <p className="text-sm text-gray-600 line-clamp-2">
                    {tournament.description}
                  </p>
                </div>

                <div className="space-y-2 mb-6 flex-grow">
                  <div className="flex items-center gap-2 text-sm text-gray-600">
                    <span className="text-lg">📅</span>
                    <span>
                      Inicio: {new Date(tournament.startDate).toLocaleDateString()}
                    </span>
                  </div>
                  <div className="flex items-center gap-2 text-sm text-gray-600">
                    <span className="text-lg">🏁</span>
                    <span>
                      Fin: {new Date(tournament.endDate).toLocaleDateString()}
                    </span>
                  </div>
                </div>

                <Link to={`/tournaments/${tournament.id}`} className="block">
                  <Button fullWidth>Ver Competencias</Button>
                </Link>
              </CardBody>
            </Card>
          ))}
        </div>
      )}
    </div>
  );
};
