import React, { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import { apiService } from '../../services/api';
import { Button } from '../../components/Common/Button';
import { Card, CardBody, CardHeader } from '../../components/Common/Card';
import { Alert } from '../../components/Common/Alert';
import type { AdminTournamentDetail as AdminTournamentDetailType } from '../../types';

export const AdminTournamentDetail: React.FC = () => {
  const { tournamentId } = useParams<{ tournamentId: string }>();
  const id = parseInt(tournamentId || '0', 10);

  const [tournament, setTournament] = useState<AdminTournamentDetailType | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [publishing, setPublishing] = useState(false); // Estado para el loading de publicar

  useEffect(() => {
    loadTournament();
  }, [id]);

  const loadTournament = async () => {
    try {
      setLoading(true);
      const data = (await apiService.getAdminTournamentDetail(id)) as AdminTournamentDetailType;
      setTournament(data);
    } catch (err: any) {
      setError(err.message || 'Error al cargar torneo');
    } finally {
      setLoading(false);
    }
  };

  const handlePublish = async () => {
    if (window.confirm('¿Deseas publicar este torneo? Una vez publicado, será visible para los participantes y no podrá volver a borrador.')) {
      setPublishing(true);
      try {
        await apiService.publishTournament(id);
        await loadTournament(); // Recargamos para ver el estado actualizado
      } catch (err: any) {
        setError(err.message || 'Error al publicar torneo');
      } finally {
        setPublishing(false);
      }
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
        <Link to="/admin/tournaments">
          <Button>Volver a Torneos</Button>
        </Link>
      </div>
    );
  }

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
      <Link to="/admin/tournaments" className="inline-flex items-center gap-2 text-primary-600 hover:text-primary-700 mb-6">
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

      {/* Header con Título y Acciones */}
      <div className="flex flex-col md:flex-row justify-between items-start md:items-center mb-8 gap-4">
        <div>
          <h1 className="text-3xl font-bold text-gray-900">{tournament.name}</h1>
          <p className="mt-2 text-gray-600 max-w-2xl">{tournament.description}</p>
        </div>
        <div className="flex gap-3">
          {/* Botón EDITAR */}
          <Link to={`/admin/tournaments/${id}/edit`}>
            <Button variant="secondary">✏️ Editar</Button>
          </Link>
          
          {/* Botón PUBLICAR (solo si no está publicado) */}
          {!tournament.published && (
            <Button onClick={handlePublish} loading={publishing} variant="accent">
              Publicar Torneo
            </Button>
          )}
        </div>
      </div>

      <div className="grid md:grid-cols-3 gap-6 mb-8">
        {/* Main Info */}
        <div className="md:col-span-2">
          <Card className="h-full">
            <CardHeader title="Detalles Generales" />
            <CardBody>
              <div className="grid md:grid-cols-2 gap-6">
                <div>
                  <p className="text-sm text-gray-500 uppercase font-semibold mb-1">Fecha de Inicio</p>
                  <p className="text-lg font-medium text-gray-900">
                    {new Date(tournament.startDate).toLocaleDateString('es-AR')} 
                    <span className="text-gray-400 text-sm ml-2">
                       {new Date(tournament.startDate).toLocaleTimeString('es-AR', {hour: '2-digit', minute:'2-digit'})}
                    </span>
                  </p>
                </div>
                <div>
                  <p className="text-sm text-gray-500 uppercase font-semibold mb-1">Fecha de Fin</p>
                  <p className="text-lg font-medium text-gray-900">
                    {new Date(tournament.endDate).toLocaleDateString('es-AR')}
                    <span className="text-gray-400 text-sm ml-2">
                       {new Date(tournament.endDate).toLocaleTimeString('es-AR', {hour: '2-digit', minute:'2-digit'})}
                    </span>
                  </p>
                </div>
              </div>
            </CardBody>
          </Card>
        </div>

        {/* Stats */}
        <div className="space-y-6">
          <Card>
            <CardBody className="p-6">
              <p className="text-gray-600 text-sm">Estado</p>
              <div className={`text-lg font-bold mt-2 flex items-center gap-2 ${
                tournament.published ? 'text-green-600' : 'text-orange-600'
              }`}>
                {tournament.published ? (
                  <><span>✓</span> Publicado</>
                ) : (
                  <><span>📝</span> Borrador</>
                )}
              </div>
            </CardBody>
          </Card>

          <Card>
            <CardBody className="p-6">
              <p className="text-gray-600 text-sm">Total Inscripciones</p>
              <p className="text-3xl font-bold text-primary-600 mt-2">
                {tournament.totalRegistrations}
              </p>
            </CardBody>
          </Card>

          <Card>
            <CardBody className="p-6">
              <p className="text-gray-600 text-sm">Total Recaudado</p>
              <p className="text-3xl font-bold text-green-600 mt-2">
                ${tournament.totalRaised ? tournament.totalRaised.toLocaleString('es-AR') : '0'}
              </p>
            </CardBody>
          </Card>
        </div>
      </div>

      {/* Competitions List */}
      <div className="mb-8">
        <div className="flex justify-between items-center mb-6">
          <h2 className="text-2xl font-bold text-gray-900">
            Competencias ({tournament.competitions?.length || 0})
          </h2>
          <Link to={`/admin/tournaments/${id}/competitions`}>
            <Button size="sm">Gestionar Competencias</Button>
          </Link>
        </div>

        {!tournament.competitions || tournament.competitions.length === 0 ? (
          <Card>
            <CardBody className="p-12 text-center">
              <p className="text-gray-500 mb-4">No hay competencias creadas para este torneo.</p>
              <Link to={`/admin/tournaments/${id}/competitions`}>
                <Button variant="secondary">Crear Primera Competencia</Button>
              </Link>
            </CardBody>
          </Card>
        ) : (
          <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-6">
            {tournament.competitions.map((comp) => (
              <Link 
                key={comp.id} 
                to={`/admin/tournaments/${id}/competitions/${comp.id}`}
                className="block group"
              >
                <Card hoverable className="h-full border-l-4 border-l-primary-500">
                  <CardBody className="p-5">
                    <h3 className="font-bold text-lg text-gray-900 mb-2 group-hover:text-primary-600 transition-colors">
                      {comp.name}
                    </h3>
                    <div className="space-y-2 text-sm text-gray-600">
                      <div className="flex justify-between">
                        <span>Cupo:</span>
                        <span className="font-medium text-gray-900">{comp.quota}</span>
                      </div>
                      <div className="flex justify-between">
                        <span>Precio Base:</span>
                        <span className="font-medium text-gray-900">${comp.basePrice}</span>
                      </div>
                      <div className="flex justify-between">
                        <span>Inscritos:</span>
                        <span className={`font-medium ${comp.registrations >= comp.quota ? 'text-red-600' : 'text-green-600'}`}>
                          {comp.registrations} / {comp.quota}
                        </span>
                      </div>
                    </div>
                  </CardBody>
                </Card>
              </Link>
            ))}
          </div>
        )}
      </div>
    </div>
  );
};