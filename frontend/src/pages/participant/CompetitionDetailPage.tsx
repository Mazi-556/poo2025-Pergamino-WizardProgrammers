import React, { useEffect, useState } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import { apiService } from '../../services/api';
import { Button } from '../../components/Common/Button';
import { Card, CardBody, CardHeader } from '../../components/Common/Card';
import { Alert } from '../../components/Common/Alert';
import type { CompetitionSummary } from '../../types';

export const CompetitionDetailPage: React.FC = () => {
  const { tournamentId, competitionId } = useParams<{
    tournamentId: string;
    competitionId: string;
  }>();
  const navigate = useNavigate();

  const tId = parseInt(tournamentId || '0', 10);
  const cId = parseInt(competitionId || '0', 10);

  const [competition, setCompetition] = useState<CompetitionSummary | null>(null);
  const [loading, setLoading] = useState(true);
  const [registering, setRegistering] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [success, setSuccess] = useState(false);

  useEffect(() => {
    loadCompetition();
  }, [tId, cId]);

  const loadCompetition = async () => {
    try {
      setLoading(true);
      const data = (await apiService.getCompetitionDetail(tId, cId)) as CompetitionSummary;
      setCompetition(data);
    } catch (err: any) {
      setError(err.message || 'Error al cargar competencia');
    } finally {
      setLoading(false);
    }
  };

  const handleRegister = async () => {
    setError(null);
    setSuccess(false);
    setRegistering(true);

    try {
      await apiService.createRegistration(tId, cId);
      setSuccess(true);
      setTimeout(() => {
        navigate('/registrations', { replace: true });
      }, 1500);
    } catch (err: any) {
      setError(err.message || 'Error al inscribirse');
    } finally {
      setRegistering(false);
    }
  };

  if (loading) {
    return (
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
        <Card>
          <CardBody className="p-8 text-center">
            <div className="inline-block animate-spin">⏳</div>
            <p className="text-gray-600 mt-2">Cargando competencia...</p>
          </CardBody>
        </Card>
      </div>
    );
  }

  if (!competition) {
    return (
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
        <Alert
          type="error"
          message="Competencia no encontrada"
          className="mb-6"
        />
        <Link to={`/tournaments/${tId}`}>
          <Button>Volver</Button>
        </Link>
      </div>
    );
  }

  const availableSpots = competition.quota - competition.registrations;
  const isFull = availableSpots <= 0;
  const percentFull = (competition.registrations / competition.quota) * 100;

  return (
    <div className="max-w-3xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
      <Link
        to={`/tournaments/${tId}`}
        className="inline-flex items-center gap-2 text-primary-600 hover:text-primary-700 mb-6"
      >
        ← Volver
      </Link>

      {error && (
        <Alert
          type="error"
          message={error}
          onClose={() => setError(null)}
          className="mb-6"
        />
      )}

      {success && (
        <Alert
          type="success"
          message="¡Inscripción exitosa! Redirigiendo a mis inscripciones..."
          className="mb-6"
        />
      )}

      <Card className="mb-8">
        <CardHeader title={competition.name} />
        <CardBody className="p-8">
          <div className="space-y-6">
            {/* Price */}
            <div>
              <p className="text-sm text-gray-600 uppercase font-semibold mb-2">
                Precio de la Competencia
              </p>
              <p className="text-4xl font-bold text-accent-600">
                ${competition.basePrice}
              </p>
              <p className="text-sm text-gray-600 mt-2">
                ℹ️ Si ya estás inscrito en otra competencia de este torneo, obtendrás 50% de descuento
              </p>
            </div>

            {/* Availability */}
            <div>
              <p className="text-sm text-gray-600 uppercase font-semibold mb-2">
                Disponibilidad
              </p>
              <div className="space-y-2">
                <div className="w-full bg-gray-200 rounded-full h-3">
                  <div
                    className="bg-gradient-to-r from-primary-500 to-primary-600 h-3 rounded-full transition-all"
                    style={{ width: `${percentFull}%` }}
                  ></div>
                </div>
                <div className="flex justify-between text-sm">
                  <span className="font-semibold text-gray-900">
                    {competition.registrations} de {competition.quota} lugares ocupados
                  </span>
                  <span
                    className={`font-semibold ${
                      isFull ? 'text-red-600' : 'text-green-600'
                    }`}
                  >
                    {availableSpots === 0
                      ? 'LLENO'
                      : `${availableSpots} lugar${
                          availableSpots !== 1 ? 'es' : ''
                        } disponible${availableSpots !== 1 ? 's' : ''}`}
                  </span>
                </div>
              </div>
            </div>

            {/* CTA */}
            <div className="pt-6 border-t border-gray-200">
              <Button
                onClick={handleRegister}
                loading={registering}
                disabled={isFull}
                fullWidth
                size="lg"
                variant={isFull ? 'secondary' : 'accent'}
              >
                {isFull ? '❌ Competencia Llena' : '✓ Inscribirme Ahora'}
              </Button>
            </div>
          </div>
        </CardBody>
      </Card>

      {/* Info Box */}
      <Card>
        <CardBody className="p-6">
          <h3 className="font-semibold text-gray-900 mb-3">Información Importante</h3>
          <ul className="space-y-2 text-sm text-gray-600">
            <li>✓ La inscripción es inmediata tras confirmar</li>
            <li>✓ Recibirás confirmación por correo electrónico</li>
            <li>✓ Puedes ver tus inscripciones en "Mis Inscripciones"</li>
            <li>✓ El descuento del 50% se aplica automáticamente si participas en varias competencias</li>
          </ul>
        </CardBody>
      </Card>
    </div>
  );
};
