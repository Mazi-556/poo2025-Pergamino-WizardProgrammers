import React, { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import { apiService } from '../../services/api';
import { Button } from '../../components/Common/Button';
import { Input } from '../../components/Common/Input';
import { Card, CardBody, CardHeader } from '../../components/Common/Card';
import { Alert } from '../../components/Common/Alert';
import type { AdminCompetitionDetail as AdminCompetitionDetailType, AdminCompetitionRegistrationDTO } from '../../types';

export const AdminCompetitionDetail: React.FC = () => {
  const { tournamentId, competitionId } = useParams<{
    tournamentId: string;
    competitionId: string;
  }>();

  const tId = parseInt(tournamentId || '0', 10);
  const cId = parseInt(competitionId || '0', 10);

  const [competition, setCompetition] = useState<AdminCompetitionDetailType | null>(null);
  const [registrations, setRegistrations] = useState<AdminCompetitionRegistrationDTO[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [editing, setEditing] = useState(false);
  const [saving, setSaving] = useState(false);

  const [formData, setFormData] = useState({
    name: '',
    quota: 0,
    basePrice: 0,
  });

  useEffect(() => {
    loadCompetitionDetail();
  }, [tId, cId]);

  const loadCompetitionDetail = async () => {
    try {
      setLoading(true);
      const [compData, regData] = await Promise.all([
        apiService.getAdminCompetitionDetail(tId, cId),
        apiService.getCompetitionRegistrations(tId, cId),
      ]);
      const comp = compData as AdminCompetitionDetailType;
      const regs = regData as AdminCompetitionRegistrationDTO[];
      setCompetition(comp);
      setRegistrations(regs);
      setFormData({
        name: comp.name,
        quota: comp.quota,
        basePrice: comp.basePrice,
      });
    } catch (err: any) {
      setError(err.message || 'Error al cargar competencia');
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

  const handleSave = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);
    setSaving(true);

    try {
      await apiService.updateCompetition(tId, cId, formData);
      setEditing(false);
      await loadCompetitionDetail();
    } catch (err: any) {
      setError(err.message || 'Error al actualizar competencia');
    } finally {
      setSaving(false);
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
        <Link to={`/admin/tournaments/${tId}/competitions`}>
          <Button>Volver</Button>
        </Link>
      </div>
    );
  }

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
      <Link
        to={`/admin/tournaments/${tId}/competitions`}
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

      <div className="grid md:grid-cols-3 gap-6 mb-8">
        {/* Main Info */}
        <div className="md:col-span-2">
          <Card>
            <CardHeader title="Información de la Competencia" />
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

                  <div className="grid md:grid-cols-2 gap-4">
                    <Input
                      label="Cupo"
                      type="number"
                      name="quota"
                      value={formData.quota}
                      onChange={handleInputChange}
                      required
                      min="1"
                    />

                    <Input
                      label="Precio Base"
                      type="number"
                      name="basePrice"
                      value={formData.basePrice}
                      onChange={handleInputChange}
                      required
                      min="0"
                      step="0.01"
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
                  <h2 className="text-2xl font-bold text-gray-900 mb-4">
                    {competition.name}
                  </h2>

                  <div className="grid md:grid-cols-2 gap-4 mb-6">
                    <div>
                      <p className="text-sm text-gray-600">Cupo</p>
                      <p className="text-lg font-semibold text-gray-900">
                        {competition.quota}
                      </p>
                    </div>
                    <div>
                      <p className="text-sm text-gray-600">Precio Base</p>
                      <p className="text-lg font-semibold text-gray-900">
                        ${competition.basePrice}
                      </p>
                    </div>
                  </div>

                  <Button onClick={() => setEditing(true)}>Editar</Button>
                </div>
              )}
            </CardBody>
          </Card>
        </div>

        {/* Stats */}
        <div className="space-y-6">
          <Card>
            <CardBody className="p-6">
              <p className="text-gray-600 text-sm">Inscritos</p>
              <p className="text-3xl font-bold text-primary-600 mt-2">
                {competition.totalRegistrations}
              </p>
            </CardBody>
          </Card>

          <Card>
            <CardBody className="p-6">
              <p className="text-gray-600 text-sm">Total Recaudado</p>
              <p className="text-3xl font-bold text-accent-600 mt-2">
                ${competition.totalRaised}
              </p>
            </CardBody>
          </Card>

          <Card>
            <CardBody className="p-6">
              <p className="text-gray-600 text-sm">Disponibles</p>
              <p className="text-3xl font-bold text-orange-600 mt-2">
                {competition.quota - competition.totalRegistrations}
              </p>
            </CardBody>
          </Card>
        </div>
      </div>

      {/* Registrations */}
      <Card>
        <CardHeader title="Participantes Inscritos" />
        <CardBody>
          {registrations.length === 0 ? (
            <div className="text-center py-8 text-gray-600">
              No hay participantes inscritos aún
            </div>
          ) : (
            <div className="overflow-x-auto">
              <table className="w-full">
                <thead>
                  <tr className="border-b border-gray-200">
                    {/* SEPARADO: Columna Nombre */}
                    <th className="text-left py-3 px-4 font-semibold text-gray-900">
                      Nombre
                    </th>
                    {/* SEPARADO: Columna Apellido (Nueva) */}
                    <th className="text-left py-3 px-4 font-semibold text-gray-900">
                      Apellido
                    </th>
                    <th className="text-left py-3 px-4 font-semibold text-gray-900">
                      Email
                    </th>
                    <th className="text-left py-3 px-4 font-semibold text-gray-900">
                      DNI
                    </th>
                    <th className="text-left py-3 px-4 font-semibold text-gray-900">
                      Fecha
                    </th>
                    <th className="text-right py-3 px-4 font-semibold text-gray-900">
                      Precio
                    </th>
                  </tr>
                </thead>
                <tbody>
                  {registrations.map((reg, idx) => (
                    <tr key={idx} className="border-b border-gray-100 hover:bg-gray-50">
                      {/* SEPARADO: Celda Nombre */}
                      <td className="py-3 px-4 text-gray-900">
                        {reg.participantFirstName}
                      </td>
                      {/* SEPARADO: Celda Apellido */}
                      <td className="py-3 px-4 text-gray-900">
                        {reg.participantLastName}
                      </td>
                      <td className="py-3 px-4 text-gray-600">{reg.participantEmail}</td>
                      <td className="py-3 px-4 text-gray-600">{reg.participantDni}</td>
                      <td className="py-3 px-4 text-gray-600">
                        {new Date(reg.registrationDate).toLocaleDateString('es-AR')}
                      </td>
                      <td className="py-3 px-4 text-right font-semibold text-gray-900">
                        ${reg.finalPrice}
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </CardBody>
      </Card>
    </div>
  );
};
