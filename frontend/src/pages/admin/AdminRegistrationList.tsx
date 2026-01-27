import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import api from '../../services/api';
import Button from '../../components/Common/Button';

interface ParticipantRegistration {
  id: number;
  participantName: string;
  participantSurname: string;
  participantDni: string;
  participantEmail: string;
  registrationDate: string;
}

const AdminRegistrationList = () => {
  const { competitionId } = useParams<{ competitionId: string }>();
  const [registrations, setRegistrations] = useState<ParticipantRegistration[]>([]);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchRegistrations = async () => {
      try {
        // traemos la lista de inscriptos desde el backend
        const response = await api.get(`/admin/competitions/${competitionId}/registrations`);
        setRegistrations(response.data);
      } catch (err) {
        console.error('error al cargar inscriptos', err);
      } finally {
        setLoading(false);
      }
    };
    fetchRegistrations();
  }, [competitionId]);

  if (loading) return <p>Cargando lista de inscriptos...</p>;

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <h1 className="text-2xl font-bold">Inscriptos en la Competencia</h1>
        <Button variant="secondary" onClick={() => navigate(-1)}>Volver</Button>
      </div>

      <div className="bg-white shadow rounded-lg overflow-hidden">
        {registrations.length === 0 ? (
          <p className="p-8 text-center text-gray-500">Todavía no hay inscriptos en esta competencia.</p>
        ) : (
          <table className="w-full text-left border-collapse">
            <thead className="bg-gray-100">
              <tr>
                <th className="p-4 border-b">Apellido y Nombre</th>
                <th className="p-4 border-b">DNI</th>
                <th className="p-4 border-b">Email</th>
                <th className="p-4 border-b">Fecha Insc.</th>
              </tr>
            </thead>
            <tbody>
              {registrations.map((reg) => (
                <tr key={reg.id} className="hover:bg-gray-50">
                  <td className="p-4 border-b">{reg.participantSurname}, {reg.participantName}</td>
                  <td className="p-4 border-b">{reg.participantDni}</td>
                  <td className="p-4 border-b">{reg.participantEmail}</td>
                  <td className="p-4 border-b">{reg.registrationDate}</td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>
    </div>
  );
};

export default AdminRegistrationList;