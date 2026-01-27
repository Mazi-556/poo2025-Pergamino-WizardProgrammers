import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import api from '../../services/api';
import { Competition } from '../../types';

const CompetitionDetailPage = () => {
  const { tournamentId, competitionId } = useParams<{ tournamentId: string; competitionId: string }>();
  const [competition, setCompetition] = useState<Competition | null>(null);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchCompetition = async () => {
      try {
        // traemos la data de la competencia especifica
        const response = await api.get(`/tournaments/${tournamentId}/competitions/${competitionId}`);
        setCompetition(response.data);
      } catch (err) {
        console.error('error al cargar competencia', err);
      } finally {
        setLoading(false);
      }
    };
    fetchCompetition();
  }, [tournamentId, competitionId]);

  const handleRegistration = async () => {
    try {
      // le pegamos al endpoint para anotar al participante logueado
      await api.post(`/tournaments/${tournamentId}/competitions/${competitionId}/registrations`);
      alert('¡Inscripción exitosa!');
      navigate('/mis-inscripciones');
    } catch (err) {
      alert('No se pudo realizar la inscripción. Revisa si hay cupos.');
    }
  };

  if (loading) return <p>Cargando datos...</p>;
  if (!competition) return <p>No se encontró la competencia.</p>;

  return (
    <div className="max-w-2xl mx-auto bg-white p-8 rounded shadow">
      <h2 className="text-3xl font-bold mb-4">{competition.name}</h2>
      <div className="space-y-4 text-lg">
        <p><strong>Precio base:</strong> ${competition.basePrice}</p>
        <p><strong>Cupos disponibles:</strong> {competition.quota}</p>
      </div>
      
      <div className="mt-8 flex gap-4">
        <button 
          onClick={handleRegistration}
          className="bg-green-600 text-white px-6 py-2 rounded font-bold hover:bg-green-700"
        >
          Confirmar Inscripción
        </button>
        <button 
          onClick={() => navigate(-1)}
          className="bg-gray-200 px-6 py-2 rounded"
        >
          Volver
        </button>
      </div>
    </div>
  );
};

export default CompetitionDetailPage;