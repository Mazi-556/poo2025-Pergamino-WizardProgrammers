import React, { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import { apiService } from '../../services/api';
import { Button } from '../../components/Common/Button';
import { Card, CardBody, CardHeader } from '../../components/Common/Card';
import { Alert } from '../../components/Common/Alert';
import type { RegistrationResponse } from '../../types';

export const RegistrationDetailPage: React.FC = () => {
  const { registrationId } = useParams<{ registrationId: string }>();
  const id = parseInt(registrationId || '0', 10);

  const [registration, setRegistration] = useState<RegistrationResponse | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    loadRegistration();
  }, [id]);

  const loadRegistration = async () => {
    try {
      setLoading(true);
      const data = (await apiService.getRegistrationDetail(id)) as RegistrationResponse;
      setRegistration(data);
    } catch (err: any) {
      setError(err.message || 'Error al cargar inscripción');
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return (
      <div className="max-w-3xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
        <Card>
          <CardBody className="p-8 text-center">
            <div className="inline-block animate-spin">⏳</div>
            <p className="text-gray-600 mt-2">Cargando inscripción...</p>
          </CardBody>
        </Card>
      </div>
    );
  }

  if (!registration) {
    return (
      <div className="max-w-3xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
        <Alert
          type="error"
          message="Inscripción no encontrada"
          className="mb-6"
        />
        <Link to="/registrations">
          <Button>Volver a Mis Inscripciones</Button>
        </Link>
      </div>
    );
  }

  const registrationDate = new Date(registration.registrationDate);

  return (
    <div className="max-w-3xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
      <Link to="/registrations" className="inline-flex items-center gap-2 text-primary-600 hover:text-primary-700 mb-6">
        ← Volver a Mis Inscripciones
      </Link>

      {error && (
        <Alert
          type="error"
          message={error}
          onClose={() => setError(null)}
          className="mb-6"
        />
      )}

      {}
      <Card className="mb-8">
        <CardHeader title="Detalles de la Inscripción" />
        <CardBody className="p-8">
          <div className="space-y-8">
            {}
            <div>
              <p className="text-sm text-gray-600 uppercase font-semibold mb-2">
                Torneo
              </p>
              <h2 className="text-3xl font-bold text-gray-900">
                {registration.tournamentName}
              </h2>
            </div>

            {}
            <div>
              <p className="text-sm text-gray-600 uppercase font-semibold mb-2">
                Competencia
              </p>
              <h3 className="text-2xl font-bold text-gray-900">
                {registration.competitionName}
              </h3>
            </div>

            {}
            <div className="grid md:grid-cols-3 gap-6 py-6 border-y border-gray-200">
              <div>
                <p className="text-sm text-gray-600 uppercase font-semibold mb-2">
                  Fecha de Inscripción
                </p>
                <p className="text-lg font-semibold text-gray-900">
                  {registrationDate.toLocaleDateString()}
                </p>
                <p className="text-sm text-gray-600">
                  {registrationDate.toLocaleTimeString()}
                </p>
              </div>

              <div>
                <p className="text-sm text-gray-600 uppercase font-semibold mb-2">
                  Precio Final
                </p>
                <p className="text-3xl font-bold text-accent-600">
                  ${registration.finalPrice}
                </p>
              </div>

              <div>
                <p className="text-sm text-gray-600 uppercase font-semibold mb-2">
                  Estado
                </p>
                <div className="inline-block px-4 py-2 bg-green-100 text-green-700 rounded-full font-semibold">
                  ✓ Confirmada
                </div>
              </div>
            </div>

            {}
            <div className="bg-blue-50 border border-blue-200 rounded-lg p-6">
              <h4 className="font-semibold text-blue-900 mb-3">
                ℹ️ Información Importante
              </h4>
              <ul className="space-y-2 text-sm text-blue-800">
                <li>
                  ✓ Tu inscripción está confirmada y registrada en el sistema
                </li>
                <li>
                  ✓ Se ha enviado un correo de confirmación a tu email
                </li>
                <li>
                  ✓ Guarda esta información para la competencia
                </li>
                <li>
                  ✓ Si participas en varias competencias, automáticamente se aplicó 50% de descuento
                </li>
              </ul>
            </div>
          </div>
        </CardBody>
      </Card>

      {}
      <Card>
        <CardBody className="p-6">
          <p className="text-sm text-gray-600 mb-2">ID de Inscripción</p>
          <p className="font-mono text-gray-900 text-sm">{registration.id}</p>
        </CardBody>
      </Card>
    </div>
  );
};
