import React, { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import api from '../../services/api';
import { Tournament, Competition } from '../../types';

const TournamentDetailPage = () => {
  const { id } = useParams<{ id: string }>();
  const [tournament, setTournament] = useState<Tournament | null>(null);
  const [competitions, setCompetitions] = useState<Competition[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchDetails = async () => {
      try {
        // obtenemos la info del torneo y sus competencias
        const [tRes, cRes] = await Promise.all([
          api.get(`/tournaments/${id}`),
          api.get(`/tournaments/${id}/competitions`)
        ]);
        setTournament(tRes.data);
        setCompetitions(cRes.data);
      } catch (err) {
        console.error('error al traer detalles', err);
      } finally {
        setLoading(false);
      }
    };
    fetchDetails();
  }, [id]);

  if (loading) return <p>cargando detalles...</p>;
  if (!tournament) return <p>no se encontro el torneo</p>;

  return (
    <div className="max-w-4xl mx-auto space-y-8">
      <div className="bg-white p-6 rounded shadow">
        <h1 className="text-3xl font-bold">{tournament.name}</h1>
        <p className="text-gray-600 mt-4">{tournament.description}</p>
        <div className="mt-4 text-sm text-gray-500">
          <p>Inicio: {tournament.startDate}</p>
          <p>Fin: {tournament.endDate}</p>
        </div>
      </div>

      <h3 className="text-2xl font-semibold">Competencias disponibles</h3>
      <div className="grid gap-4">
        {competitions.map((c) => (
          <div key={c.id} className="p-4 bg-white border rounded flex justify-between items-center shadow-sm">
            <div>
              <h4 className="font-bold">{c.name}</h4>
              <p className="text-sm text-gray-600">Cupo: {c.quota} | Precio: ${c.basePrice}</p>
            </div>
            <Link 
              to={`/torneos/${id}/competencia/${c.id}`}
              className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700"
            >
              Ver detalle
            </Link>
          </div>
        ))}
      </div>
    </div>
  );
};

export default TournamentDetailPage;