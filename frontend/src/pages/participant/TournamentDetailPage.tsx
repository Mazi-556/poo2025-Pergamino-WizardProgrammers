import React, { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import { apiService } from '../../services/api';
import { Button } from '../../components/Common/Button';
import { Card, CardBody } from '../../components/Common/Card';
import { Alert } from '../../components/Common/Alert';
import type { TournamentResponse, CompetitionSummary } from '../../types';

export const TournamentDetailPage: React.FC = () => {
  const { tournamentId } = useParams<{ tournamentId: string }>();
  const id = parseInt(tournamentId || '0', 10);

  const [tournament, setTournament] = useState<TournamentResponse | null>(null);
  const [competitions, setCompetitions] = useState<CompetitionSummary[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    loadTournamentDetail();
  }, [id]);

  const loadTournamentDetail = async () => {
    try {
      setLoading(true);
      const [tourData, compData] = await Promise.all([
        apiService.getTournaments().then((tours: any) =>
          (tours as TournamentResponse[]).find((t: TournamentResponse) => t.id === id)
        ),
        apiService.getTournamentCompetitions(id),
      ]);
      setTournament(tourData || null);
      setCompetitions((compData as any) as CompetitionSummary[]);
    } catch (err: any) {
      setError(err.message || 'Error al cargar torneo');
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return (
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
        <Card>
          <CardBody className="p-8 text-center">
            <div className="inline-block animate-spin">⏳</div>
            <p className="text-gray-600 mt-2">Cargando torneo...</p>
          </CardBody>
        </Card>
      </div>
    );
  }

  if (!tournament) {
    return (
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
        <Alert
          type="error"
          message="Torneo no encontrado"
          className="mb-6"
        />
        <Link to="/tournaments">
          <Button>Volver a Torneos</Button>
        </Link>
      </div>
    );
  }

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
      <Link to="/tournaments" className="inline-flex items-center gap-2 text-primary-600 hover:text-primary-700 mb-6">
        ← Volver a Torneos
      </Link>

      {error && (
        <Alert
          type="error"
          message={error}
          onClose={() => setError(null)}
          className="mb-6"
        />
      )}

      {/* Tournament Info */}
      <Card className="mb-8">
        <CardBody className="p-8">
          <h1 className="text-3xl font-bold text-gray-900 mb-4">{tournament.name}</h1>
          <p className="text-lg text-gray-600 mb-6">{tournament.description}</p>

          <div className="grid md:grid-cols-2 gap-6">
            <div>
              <p className="text-sm text-gray-600 uppercase font-semibold mb-2">
                Fecha de Inicio
              </p>
              <p className="text-lg font-semibold text-gray-900">
                {new Date(tournament.startDate).toLocaleString()}
              </p>
            </div>
            <div>
              <p className="text-sm text-gray-600 uppercase font-semibold mb-2">
                Fecha de Fin
              </p>
              <p className="text-lg font-semibold text-gray-900">
                {new Date(tournament.endDate).toLocaleString()}
              </p>
            </div>
          </div>
        </CardBody>
      </Card>

      {/* Competitions */}
      <div>
        <h2 className="text-2xl font-bold text-gray-900 mb-6">
          Competencias ({competitions.length})
        </h2>

        {competitions.length === 0 ? (
          <Card>
            <CardBody className="p-8 text-center">
              <p className="text-gray-600">No hay competencias disponibles en este torneo</p>
            </CardBody>
          </Card>
        ) : (
          <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-6">
            {competitions.map((competition) => {
              const availableSpots = competition.quota - competition.registrations;
              const isFull = availableSpots <= 0;

              return (
                <Card
                  key={competition.id}
                  hoverable
                  className={isFull ? 'opacity-60' : ''}
                >
                  <CardBody className="p-6 flex flex-col">
                    <h3 className="text-lg font-bold text-gray-900 mb-4">
                      {competition.name}
                    </h3>

                    <div className="space-y-3 mb-6 flex-grow">
                      <div>
                        <p className="text-xs text-gray-600 uppercase font-semibold">
                          Precio
                        </p>
                        <p className="text-2xl font-bold text-accent-600">
                          ${competition.basePrice}
                        </p>
                      </div>

                      <div>
                        <p className="text-xs text-gray-600 uppercase font-semibold mb-1">
                          Disponibilidad
                        </p>
                        <div className="w-full bg-gray-200 rounded-full h-2">
                          <div
                            className="bg-primary-600 h-2 rounded-full transition-all"
                            style={{
                              width: `${
                                ((competition.quota - availableSpots) /
                                  competition.quota) *
                                100
                              }%`,
                            }}
                          ></div>
                        </div>
                        <p className="text-sm text-gray-600 mt-1">
                          {availableSpots}/{competition.quota} lugares disponibles
                        </p>
                      </div>
                    </div>

                    <Link
                      to={`/tournaments/${tournament.id}/competitions/${competition.id}`}
                      className="block"
                    >
                      <Button fullWidth disabled={isFull}>
                        {isFull ? 'Lleno' : 'Participar'}
                      </Button>
                    </Link>
                  </CardBody>
                </Card>
              );
            })}
          </div>
        )}
      </div>
    </div>
  );
};
