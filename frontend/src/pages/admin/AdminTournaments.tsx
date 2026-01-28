import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { apiService } from '../../services/api';
import { Button } from '../../components/Common/Button';
import { Input, TextArea } from '../../components/Common/Input';
import { Card, CardBody, CardHeader } from '../../components/Common/Card';
import { Alert } from '../../components/Common/Alert';
import type { AdminTournamentSummary, TournamentCreateUpdate } from '../../types';

export const AdminTournaments: React.FC = () => {
  const [tournaments, setTournaments] = useState<AdminTournamentSummary[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [showForm, setShowForm] = useState(false);
  const [formLoading, setFormLoading] = useState(false);

  const [formData, setFormData] = useState<TournamentCreateUpdate>({
    name: '',
    description: '',
    startDate: '',
    endDate: '',
  });

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

  const handleInputChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>
  ) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);
    setFormLoading(true);

    try {
      await apiService.createTournament(formData);
      setFormData({ name: '', description: '', startDate: '', endDate: '' });
      setShowForm(false);
      await loadTournaments();
    } catch (err: any) {
      setError(err.message || 'Error al crear torneo');
    } finally {
      setFormLoading(false);
    }
  };

  const handleDelete = async (tournamentId: number) => {
    if (window.confirm('¿Estás seguro de que deseas eliminar este torneo?')) {
      try {
        await apiService.deleteTournament(tournamentId);
        await loadTournaments();
      } catch (err: any) {
        setError(err.message || 'Error al eliminar torneo');
      }
    }
  };

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
      {/* Header */}
      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between mb-8">
        <div>
          <h1 className="section-title">Gestión de Torneos</h1>
          <p className="section-subtitle">Crea y administra tus torneos</p>
        </div>
        <Button
          onClick={() => setShowForm(!showForm)}
          variant="accent"
          size="lg"
          className="mt-4 sm:mt-0"
        >
          + Nuevo Torneo
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
          <CardHeader title="Crear Nuevo Torneo" />
          <CardBody>
            <form onSubmit={handleSubmit} className="space-y-4">
              <Input
                label="Nombre del Torneo"
                placeholder="Ej: Torneo de Fútbol 2025"
                name="name"
                value={formData.name}
                onChange={handleInputChange}
                required
                fullWidth
              />

              <TextArea
                label="Descripción"
                placeholder="Describe tu torneo..."
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
                <Button type="submit" loading={formLoading}>
                  Crear Torneo
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
            <Button onClick={() => setShowForm(true)}>Crear tu primer torneo</Button>
          </CardBody>
        </Card>
      ) : (
        <div className="grid gap-6">
          {tournaments.map((tournament) => (
            <Card key={tournament.id} hoverable>
              <CardBody className="p-6">
                <div className="flex flex-col sm:flex-row sm:items-start sm:justify-between">
                  <div className="flex-grow">
                    <div className="flex items-center gap-3">
                      <h3 className="text-xl font-semibold text-gray-900">
                        {tournament.name}
                      </h3>
                      <span
                        className={`px-3 py-1 rounded-full text-xs font-medium ${
                          tournament.published
                            ? 'bg-green-100 text-green-700'
                            : 'bg-orange-100 text-orange-700'
                        }`}
                      >
                        {tournament.published ? '✓ Publicado' : '📝 Borrador'}
                      </span>
                    </div>
                    <p className="text-gray-600 mt-2">{tournament.description}</p>
                    <div className="flex flex-col sm:flex-row gap-4 mt-4 text-sm text-gray-500">
                      <span>📅 Inicio: {new Date(tournament.startDate).toLocaleDateString()}</span>
                      <span>📅 Fin: {new Date(tournament.endDate).toLocaleDateString()}</span>
                    </div>
                  </div>

                  <div className="flex flex-col gap-2 mt-4 sm:mt-0">
                    <Link to={`/admin/tournaments/${tournament.id}`}>
                      <Button size="sm" fullWidth>
                        Ver Detalles
                      </Button>
                    </Link>
                    <Link to={`/admin/tournaments/${tournament.id}/competitions`}>
                      <Button size="sm" variant="secondary" fullWidth>
                        Competencias
                      </Button>
                    </Link>
                    <Button
                      size="sm"
                      variant="secondary"
                      onClick={() => handleDelete(tournament.id)}
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
