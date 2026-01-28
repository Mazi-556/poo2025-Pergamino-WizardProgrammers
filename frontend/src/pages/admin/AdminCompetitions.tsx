import React, { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import { apiService } from '../../services/api';
import { Button } from '../../components/Common/Button';
import { Input } from '../../components/Common/Input';
import { Card, CardBody, CardHeader } from '../../components/Common/Card';
import { Alert } from '../../components/Common/Alert';
import type { CompetitionSummary, AdminCompetitionCreateUpdate } from '../../types';

export const AdminCompetitions: React.FC = () => {
  const { tournamentId } = useParams<{ tournamentId: string }>();
  const id = parseInt(tournamentId || '0', 10);

  const [competitions, setCompetitions] = useState<CompetitionSummary[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [showForm, setShowForm] = useState(false);
  const [formLoading, setFormLoading] = useState(false);

  const [formData, setFormData] = useState<AdminCompetitionCreateUpdate>({
    name: '',
    quota: 0,
    basePrice: 0,
  });

  useEffect(() => {
    loadCompetitions();
  }, [id]);

  const loadCompetitions = async () => {
    try {
      setLoading(true);
      const data = (await apiService.getAdminCompetitions(id)) as CompetitionSummary[];
      setCompetitions(data);
    } catch (err: any) {
      setError(err.message || 'Error al cargar competencias');
    } finally {
      setLoading(false);
    }
  };

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: name === 'name' ? value : parseFloat(value) || 0,
    }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);
    setFormLoading(true);

    try {
      await apiService.createCompetition(id, formData);
      setFormData({ name: '', quota: 0, basePrice: 0 });
      setShowForm(false);
      await loadCompetitions();
    } catch (err: any) {
      setError(err.message || 'Error al crear competencia');
    } finally {
      setFormLoading(false);
    }
  };

  const handleDelete = async (competitionId: number) => {
    if (window.confirm('¿Estás seguro de que deseas eliminar esta competencia?')) {
      try {
        await apiService.deleteCompetition(id, competitionId);
        await loadCompetitions();
      } catch (err: any) {
        setError(err.message || 'Error al eliminar competencia');
      }
    }
  };

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
      <Link to="/admin/tournaments" className="inline-flex items-center gap-2 text-primary-600 hover:text-primary-700 mb-6">
        ← Volver a Torneos
      </Link>

      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between mb-8">
        <div>
          <h1 className="section-title">Gestión de Competencias</h1>
          <p className="section-subtitle">Crea y administra competencias del torneo</p>
        </div>
        <Button
          onClick={() => setShowForm(!showForm)}
          variant="accent"
          size="lg"
          className="mt-4 sm:mt-0"
        >
          + Nueva Competencia
        </Button>
      </div>

      {error && (
        <Alert
          type="error"
          message={error}
          onClose={() => setError(null)}
          className="mb-6"
        />
      )}

      {/* Create Form */}
      {showForm && (
        <Card className="mb-8">
          <CardHeader title="Crear Nueva Competencia" />
          <CardBody>
            <form onSubmit={handleSubmit} className="space-y-4">
              <Input
                label="Nombre de la Competencia"
                placeholder="Ej: Fútbol"
                name="name"
                value={formData.name}
                onChange={handleInputChange}
                required
                fullWidth
              />

              <div className="grid md:grid-cols-2 gap-4">
                <Input
                  label="Cupo (máximo participantes)"
                  type="number"
                  placeholder="20"
                  name="quota"
                  value={formData.quota}
                  onChange={handleInputChange}
                  required
                  min="1"
                />

                <Input
                  label="Precio Base ($)"
                  type="number"
                  placeholder="500"
                  name="basePrice"
                  value={formData.basePrice}
                  onChange={handleInputChange}
                  required
                  min="0"
                  step="0.01"
                />
              </div>

              <div className="flex gap-4 pt-4">
                <Button type="submit" loading={formLoading}>
                  Crear Competencia
                </Button>
                <Button
                  type="button"
                  variant="secondary"
                  onClick={() => setShowForm(false)}
                >
                  Cancelar
                </Button>
              </div>
            </form>
          </CardBody>
        </Card>
      )}

      {/* Competitions List */}
      {loading ? (
        <Card>
          <CardBody className="p-8 text-center">
            <div className="inline-block animate-spin">⏳</div>
            <p className="text-gray-600 mt-2">Cargando competencias...</p>
          </CardBody>
        </Card>
      ) : competitions.length === 0 ? (
        <Card>
          <CardBody className="p-8 text-center">
            <p className="text-gray-600 mb-4">No hay competencias creadas</p>
            <Button onClick={() => setShowForm(true)}>Crear tu primera competencia</Button>
          </CardBody>
        </Card>
      ) : (
        <div className="grid gap-6">
          {competitions.map((competition) => (
            <Card key={competition.id} hoverable>
              <CardBody className="p-6">
                <div className="flex flex-col sm:flex-row sm:items-start sm:justify-between">
                  <div className="flex-grow">
                    <h3 className="text-xl font-semibold text-gray-900">
                      {competition.name}
                    </h3>

                    <div className="grid grid-cols-2 md:grid-cols-4 gap-4 mt-4">
                      <div>
                        <p className="text-xs text-gray-600 uppercase">Cupo</p>
                        <p className="text-lg font-semibold text-gray-900">
                          {competition.quota}
                        </p>
                      </div>
                      <div>
                        <p className="text-xs text-gray-600 uppercase">Precio</p>
                        <p className="text-lg font-semibold text-gray-900">
                          ${competition.basePrice}
                        </p>
                      </div>
                      <div>
                        <p className="text-xs text-gray-600 uppercase">Inscritos</p>
                        <p className="text-lg font-semibold text-primary-600">
                          {competition.registrations}
                        </p>
                      </div>
                      <div>
                        <p className="text-xs text-gray-600 uppercase">Disponibles</p>
                        <p className="text-lg font-semibold text-accent-600">
                          {competition.quota - competition.registrations}
                        </p>
                      </div>
                    </div>
                  </div>

                  <div className="flex flex-col gap-2 mt-4 sm:mt-0">
                    <Link to={`/admin/tournaments/${id}/competitions/${competition.id}`}>
                      <Button size="sm" fullWidth>
                        Ver Detalles
                      </Button>
                    </Link>
                    <Button
                      size="sm"
                      variant="secondary"
                      onClick={() => handleDelete(competition.id)}
                      fullWidth
                    >
                      Eliminar
                    </Button>
                  </div>
                </div>
              </CardBody>
            </Card>
          ))}
        </div>
      )}
    </div>
  );
};
