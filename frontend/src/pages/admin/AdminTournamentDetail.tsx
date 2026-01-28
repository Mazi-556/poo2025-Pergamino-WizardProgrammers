import React, { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import { apiService } from '../../services/api';
import { Button } from '../../components/Common/Button';
import { Input, TextArea } from '../../components/Common/Input';
import { Card, CardBody, CardHeader } from '../../components/Common/Card';
import { Alert } from '../../components/Common/Alert';
import type { AdminTournamentDetail as AdminTournamentDetailType } from '../../types';

export const AdminTournamentDetail: React.FC = () => {
  const { tournamentId } = useParams<{ tournamentId: string }>();
  const id = parseInt(tournamentId || '0', 10);

  const [tournament, setTournament] = useState<AdminTournamentDetailType | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [editing, setEditing] = useState(false);
  const [saving, setSaving] = useState(false);

  const [formData, setFormData] = useState({
    name: '',
    description: '',
    startDate: '',
    endDate: '',
  });

  useEffect(() => {
    loadTournament();
  }, [id]);

  const loadTournament = async () => {
    try {
      setLoading(true);
      const data = (await apiService.getAdminTournamentDetail(id)) as AdminTournamentDetailType;
      setTournament(data);
      setFormData({
        name: data.name,
        description: data.description,
        startDate: data.startDate,
        endDate: data.endDate,
      });
    } catch (err: any) {
      setError(err.message || 'Error al cargar torneo');
    } finally {
      setLoading(false);
    }
  };

  const handleInputChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>
  ) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSave = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);
    setSaving(true);

    try {
      await apiService.updateTournament(id, formData);
      setEditing(false);
      await loadTournament();
    } catch (err: any) {
      setError(err.message || 'Error al actualizar torneo');
    } finally {
      setSaving(false);
    }
  };

  const handlePublish = async () => {
    if (window.confirm('¿Deseas publicar este torneo? No podrá ser editado después.')) {
      try {
        await apiService.publishTournament(id);
        await loadTournament();
      } catch (err: any) {
        setError(err.message || 'Error al publicar torneo');
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

      <div className="grid md:grid-cols-3 gap-6 mb-8">
        {/* Main Info */}
        <div className="md:col-span-2">
          <Card>
            <CardHeader title="Información del Torneo" />
            <CardBody>
              {editing ? (
                <form onSubmit={handleSave} className="space-y-4">
                  <Input
                    label="Nombre"
                    name="name"
                    value={formData.name}
                    onChange={handleInputChange}
                    required
                    fullWidth
                  />

                  <TextArea
                    label="Descripción"
                    name="description"
                    value={formData.description}
                    onChange={handleInputChange}
                    required
                    fullWidth
                    rows={4}
                  />

                  <div className="grid md:grid-cols-2 gap-4">
                    <Input
                      label="Fecha de Inicio"
                      type="datetime-local"
                      name="startDate"
                      value={formData.startDate}
                      onChange={handleInputChange}
                      required
                    />

                    <Input
                      label="Fecha de Fin"
                      type="datetime-local"
                      name="endDate"
                      value={formData.endDate}
                      onChange={handleInputChange}
                      required
                    />
                  </div>

                  <div className="flex gap-4 pt-4">
                    <Button type="submit" loading={saving}>
                      Guardar
                    </Button>
                    <Button
                      type="button"
                      variant="secondary"
                      onClick={() => setEditing(false)}
                    >
                      Cancelar
                    </Button>
                  </div>
                </form>
              ) : (
                <div>
                  <h2 className="text-2xl font-bold text-gray-900 mb-2">{tournament.name}</h2>
                  <p className="text-gray-600 mb-6">{tournament.description}</p>

                  <div className="grid md:grid-cols-2 gap-4 mb-6">
                    <div>
                      <p className="text-sm text-gray-600">Fecha de Inicio</p>
                      <p className="font-semibold text-gray-900">
                        {new Date(tournament.startDate).toLocaleString()}
                      </p>
                    </div>
                    <div>
                      <p className="text-sm text-gray-600">Fecha de Fin</p>
                      <p className="font-semibold text-gray-900">
                        {new Date(tournament.endDate).toLocaleString()}
                      </p>
                    </div>
                  </div>

                  <div className="flex gap-2">
                    {!tournament.published && (
                      <>
                        <Button onClick={() => setEditing(true)}>Editar</Button>
                        <Button variant="accent" onClick={handlePublish}>
                          Publicar
                        </Button>
                      </>
                    )}
                  </div>
                </div>
              )}
            </CardBody>
          </Card>
        </div>

        {/* Stats */}
        <div className="space-y-6">
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
              <p className="text-3xl font-bold text-accent-600 mt-2">
                ${tournament.totalRaised}
              </p>
            </CardBody>
          </Card>

          <Card>
            <CardBody className="p-6">
              <p className="text-gray-600 text-sm">Estado</p>
              <p className={`text-lg font-bold mt-2 ${
                tournament.published ? 'text-green-600' : 'text-orange-600'
              }`}>
                {tournament.published ? '✓ Publicado' : '📝 Borrador'}
              </p>
            </CardBody>
          </Card>
        </div>
      </div>

      {/* Competitions */}
      <Card>
        <CardHeader
          title="Competencias"
          subtitle={`Total: ${tournament?.competitions?.length || 0}`}
        />
        <CardBody>
          {!tournament?.competitions || tournament.competitions.length === 0 ? (
            <div className="text-center py-8">
              <p className="text-gray-600 mb-4">No hay competencias creadas</p>
              <Link to={`/admin/tournaments/${id}/competitions`}>
                <Button>Crear Competencia</Button>
              </Link>
            </div>
          ) : (
            <div className="grid md:grid-cols-2 gap-4">
              {tournament.competitions.map((comp) => (
                <div key={comp.id} className="p-4 border border-gray-200 rounded-lg">
                  <h4 className="font-semibold text-gray-900">{comp.name}</h4>
                  <div className="mt-2 text-sm text-gray-600 space-y-1">
                    <p>Cupo: {comp.quota}</p>
                    <p>Precio: ${comp.basePrice}</p>
                    <p>Inscritos: {comp.registrations}/{comp.quota}</p>
                  </div>
                </div>
              ))}
            </div>
          )}
        </CardBody>
      </Card>
    </div>
  );
};
