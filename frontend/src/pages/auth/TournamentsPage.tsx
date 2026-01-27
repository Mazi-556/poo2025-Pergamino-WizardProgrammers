import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import api from '../../services/api';
import { Tournament } from '../../types';

const TournamentsPage = () => {
  const [tournaments, setTournaments] = useState<Tournament[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchTournaments = async () => {
      try {
        // pedimos los torneos publicados al backend
        const response = await api.get('/tournaments');
        setTournaments(response.data);
      } catch (err) {
        console.error('error al traer torneos', err);
      } finally {
        setLoading(false);
      }
    };
    fetchTournaments();
  }, []);

  if (loading) return <p>cargando torneos...</p>;

  return (
    <div className="space-y-6">
      <h1 className="text-3xl font-bold text-gray-800">Torneos Disponibles</h1>
      <div className="grid gap-6 md:grid-cols-2 lg:grid-cols-3">
        {tournaments.map((t) => (
          <div key={t.id} className="p-4 bg-white rounded shadow border-l-4 border-blue-500">
            <h2 className="text-xl font-semibold">{t.name}</h2>
            <p className="text-gray-600 mt-2 line-clamp-2">{t.description}</p>
            <div className="mt-4 flex justify-between items-center">
              <span className="text-sm text-gray-500">Fin: {t.endDate}</span>
              <Link 
                to={`/torneos/${t.id}`} 
                className="text-blue-600 font-medium hover:underline"
              >
                Ver mas
              </Link>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default TournamentsPage;