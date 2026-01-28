import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { apiService } from '../../services/api';
import { Button } from '../../components/Common/Button';
import { Card, CardBody } from '../../components/Common/Card';
import { Alert } from '../../components/Common/Alert';
import type { RegistrationResponse } from '../../types';

export const RegistrationsPage: React.FC = () => {
  const [registrations, setRegistrations] = useState<RegistrationResponse[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    loadRegistrations();
  }, []);

  const loadRegistrations = async () => {
    try {
      setLoading(true);
      const data = (await apiService.getMyRegistrations()) as RegistrationResponse[];
      setRegistrations(data);
    } catch (err: any) {
      setError(err.message || 'Error al cargar inscripciones');
    } finally {
      setLoading(false);
    }
  };

  const totalSpent = registrations.reduce((sum, reg) => sum + reg.finalPrice, 0);

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
      {/* Header */}
      <div className="mb-8">
        <h1 className="section-title">Mis Inscripciones</h1>
        <p className="section-subtitle">
          Gestiona todas tus registros en torneos y competencias
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

      {/* Stats */}
      {!loading && registrations.length > 0 && (
        <div className="grid md:grid-cols-3 gap-6 mb-8">
          <Card>
            <CardBody className="p-6">
              <p className="text-gray-600 text-sm">Total de Inscripciones</p>
              <p className="text-4xl font-bold text-primary-600 mt-2">
                {registrations.length}
              </p>
            </CardBody>
          </Card>

          <Card>
            <CardBody className="p-6">
              <p className="text-gray-600 text-sm">Torneos Únicos</p>
              <p className="text-4xl font-bold text-accent-600 mt-2">
                {new Set(registrations.map((r) => r.tournamentId)).size}
              </p>
            </CardBody>
          </Card>

          <Card>
            <CardBody className="p-6">
              <p className="text-gray-600 text-sm">Monto Total Gastado</p>
              <p className="text-4xl font-bold text-orange-600 mt-2">
                ${totalSpent.toFixed(2)}
              </p>
            </CardBody>
          </Card>
        </div>
      )}

      {/* Registrations List */}
      {loading ? (
        <Card>
          <CardBody className="p-8 text-center">
            <div className="inline-block animate-spin">⏳</div>
            <p className="text-gray-600 mt-2">Cargando inscripciones...</p>
          </CardBody>
        </Card>
      ) : registrations.length === 0 ? (
        <Card>
          <CardBody className="p-8 text-center">
            <p className="text-gray-600 mb-6">
              No tienes inscripciones aún. ¡Elige una competencia y participa!
            </p>
            <Link to="/tournaments">
              <Button>Ver Torneos Disponibles</Button>
            </Link>
          </CardBody>
        </Card>
      ) : (
        <div className="space-y-4">
          {/* Group by Tournament */}
          {Array.from(
            new Map(
              registrations.map((reg) => [
                reg.tournamentId,
                {
                  tournamentName: reg.tournamentName,
                  registrations: registrations.filter(
                    (r) => r.tournamentId === reg.tournamentId
                  ),
                },
              ])
            ).values()
          ).map(({ tournamentName, registrations: regs }) => (
            <div key={regs[0].tournamentId}>
              <h3 className="text-lg font-semibold text-gray-900 mb-3">
                {tournamentName}
              </h3>

              <div className="grid md:grid-cols-2 gap-4 mb-6">
                {regs.map((registration) => (
                  <Card key={registration.id} hoverable>
                    <CardBody className="p-6">
                      <div className="flex justify-between items-start mb-4">
                        <div>
                          <h4 className="font-semibold text-gray-900">
                            {registration.competitionName}
                          </h4>
                          <p className="text-sm text-gray-600 mt-1">
                            Inscrito el{' '}
                            {new Date(
                              registration.registrationDate
                            ).toLocaleDateString()}
                          </p>
                        </div>
                        <span className="text-2xl font-bold text-accent-600">
                          ${registration.finalPrice}
                        </span>
                      </div>

                      <Link to={`/registrations/${registration.id}`} className="inline-block">
                        <Button size="sm">Ver Detalles</Button>
                      </Link>
                    </CardBody>
                  </Card>
                ))}
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};
