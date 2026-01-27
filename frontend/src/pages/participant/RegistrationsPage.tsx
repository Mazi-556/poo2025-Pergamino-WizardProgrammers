import React, { useEffect, useState } from 'react';
import api from '../../services/api';
import { Registration } from '../../types';

const RegistrationsPage = () => {
  const [registrations, setRegistrations] = useState<Registration[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchRegistrations = async () => {
      try {
        // traemos las inscripciones del participante actual
        const response = await api.get('/registrations');
        setRegistrations(response.data);
      } catch (err) {
        console.error('error al obtener inscripciones', err);
      } finally {
        setLoading(false);
      }
    };
    fetchRegistrations();
  }, []);

  if (loading) return <p>Cargando tus inscripciones...</p>;

  return (
    <div className="max-w-4xl mx-auto">
      <h1 className="text-3xl font-bold mb-6">Mis Inscripciones</h1>
      {registrations.length === 0 ? (
        <p className="text-gray-600">Todavía no te anotaste en ninguna competencia.</p>
      ) : (
        <div className="grid gap-4">
          {registrations.map((reg) => (
            <div key={reg.id} className="bg-white p-4 rounded shadow border-l-4 border-green-500">
              <div className="flex justify-between items-center">
                <div>
                  <h3 className="font-bold text-lg">{reg.competitionName || 'Competencia'}</h3>
                  <p className="text-sm text-gray-600">Torneo: {reg.tournamentName || 'N/A'}</p>
                </div>
                <div className="text-right">
                  <p className="font-semibold text-blue-600">${reg.price}</p>
                  <p className="text-xs text-gray-400">{reg.date}</p>
                </div>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default RegistrationsPage;