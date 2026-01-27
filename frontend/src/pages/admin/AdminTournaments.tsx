import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../../services/api';
import Button from '../../components/Common/Button';
import { Tournament } from '../../types';

const AdminTournaments = () => {
  const [tournaments, setTournaments] = useState<Tournament[]>([]);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  const fetchTournaments = async () => {
    try {
      // traemos todos los torneos (incluso los borradores)
      const response = await api.get('/admin/tournaments');
      setTournaments(response.data);
    } catch (err) {
      console.error('error al cargar torneos de admin', err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchTournaments();
  }, []);

  if (loading) return <p>Cargando gestión de torneos...</p>;

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <h1 className="text-2xl font-bold">Gestión de Torneos</h1>
        <Button onClick={() => navigate('/admin/torneos/nuevo')}>
          + Nuevo Torneo
        </Button>
      </div>

      <div className="bg-white shadow rounded-lg overflow-hidden">
        <table className="w-full text-left border-collapse">
          <thead className="bg-gray-100">
            <tr>
              <th className="p-4 border-b">Nombre</th>
              <th className="p-4 border-b">Estado</th>
              <th className="p-4 border-b text-right">Acciones</th>
            </tr>
          </thead>
          <tbody>
            {tournaments.map((t) => (
              <tr key={t.id} className="hover:bg-gray-50">
                <td className="p-4 border-b">{t.name}</td>
                <td className="p-4 border-b">
                  {t.published ? (
                    <span className="text-green-600 font-medium">Publicado</span>
                  ) : (
                    <span className="text-yellow-600 font-medium">Borrador</span>
                  )}
                </td>
                <td className="p-4 border-b text-right">
                  <Button 
                    variant="secondary" 
                    onClick={() => navigate(`/admin/torneos/${t.id}`)}
                  >
                    Ver detalle
                  </Button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default AdminTournaments;