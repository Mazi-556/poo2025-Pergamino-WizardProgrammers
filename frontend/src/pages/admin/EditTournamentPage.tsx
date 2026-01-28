import React, { useEffect, useState } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import { apiService } from '../../services/api';
import { Button } from '../../components/Common/Button';
import { Card, CardBody, CardHeader } from '../../components/Common/Card';
import { Input } from '../../components/Common/Input';
import { Alert } from '../../components/Common/Alert';

export const EditTournamentPage: React.FC = () => {
  const { tournamentId } = useParams<{ tournamentId: string }>();
  const navigate = useNavigate();
  const id = parseInt(tournamentId || '0', 10);

  const [formData, setFormData] = useState({
    name: '',
    description: '',
    startDate: '',
    endDate: '',
  });
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    loadTournament();
  }, [id]);

  const loadTournament = async () => {
    try {
      setLoading(true);
      const data: any = await apiService.getAdminTournamentDetail(id);
      setFormData({
        name: data.name,
        description: data.description,
        startDate: data.startDate,
        endDate: data.endDate,
      });
    } catch (err: any) {
      setError(err.message || 'Error al cargar el torneo');
    } finally {
      setLoading(false);
    }
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setSaving(true);
    setError(null);

    try {
      await apiService.updateTournament(id, formData);
      navigate(`/admin/tournaments/${id}`); // Volver al detalle
    } catch (err: any) {
      setError(err.message || 'Error al guardar los cambios');
      setSaving(false);
    }
  };

  if (loading) return <div className="p-8 text-center">Cargando...</div>;

  return (
    <div className="max-w-3xl mx-auto px-4 py-12">
      <Link to={`/admin/tournaments/${id}`} className="text-primary-600 hover:underline mb-6 block">
        ← Volver al Detalle
      </Link>

      <Card>
        <CardHeader title="Editar Torneo" />
        <CardBody className="p-6">
          {error && <Alert type="error" message={error} className="mb-6" onClose={() => setError(null)} />}

          <form onSubmit={handleSubmit} className="space-y-6">
            <Input
              label="Nombre del Torneo"
              name="name"
              value={formData.name}
              onChange={handleChange}
              required
            />

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Descripción</label>
              <textarea
                name="description"
                value={formData.description}
                onChange={handleChange}
                rows={4}
                className="w-full rounded-md border-gray-300 shadow-sm focus:border-primary-500 focus:ring-primary-500 border p-2"
                required
              />
            </div>

            <div className="grid md:grid-cols-2 gap-4">
              <Input
                label="Fecha de Inicio"
                name="startDate"
                type="datetime-local"
                value={formData.startDate ? formData.startDate.slice(0, 16) : ''} // Cortamos segundos si vienen
                onChange={handleChange}
                required
              />
              <Input
                label="Fecha de Fin"
                name="endDate"
                type="datetime-local" 
                value={formData.endDate ? formData.endDate.slice(0, 16) : ''} // Cortamos segundos si vienen
                onChange={handleChange}
                required
              />
            </div>

            <div className="flex justify-end gap-3 pt-4">
              <Link to={`/admin/tournaments/${id}`}>
                <Button variant="secondary" type="button">Cancelar</Button>
              </Link>
              <Button type="submit" loading={saving}>Guardar Cambios</Button>
            </div>
          </form>
        </CardBody>
      </Card>
    </div>
  );
};