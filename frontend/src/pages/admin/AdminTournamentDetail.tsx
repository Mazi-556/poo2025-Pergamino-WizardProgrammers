import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import api from '../../services/api';
import Button from '../../components/Common/Button';
import { Tournament, Competition } from '../../types';

const AdminTournamentDetail = () => {
  const { id } = useParams<{ id: string }>();
  const [tournament, setTournament] = useState<Tournament | null>(null);
  const [competitions, setCompetitions] = useState<Competition[]>([]);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchDetails = async () => {
      try {
        const [tRes, cRes] = await Promise.all([
          api.get(`/admin/tournaments/${id}`),
          api.get(`/admin/tournaments/${id}/competitions`)
        ]);
        setTournament(tRes.data);
        setCompetitions(cRes.data);
      } catch (err) {
        console.error('error al traer datos del torneo', err);
      }
    };
    fetchDetails();
  }, [id]);

  const handlePublish = async () => {
    try {
      await api.patch(`/admin/tournaments/${id}/publish`);
      alert('torneo publicado con exito');
      navigate('/admin/torneos');
    } catch (err) {
      alert('error al publicar, revisa si tiene competencias');
    }
  };

  if (!tournament) return <p>Cargando...</p>;

  return (
    <div className="space-y-6">
      <div className="bg-white p-6 rounded shadow flex justify-between items-center">
        <div>
          <h1 className="text-3xl font-bold">{tournament.name}</h1>
          <p className="text-gray-500">Estado: {tournament.published ? 'Publicado' : 'Borrador'}</p>
        </div>
        <div className="space-x-2">
          {!tournament.published && (
            <Button onClick={handlePublish}>Publicar Torneo</Button>
          )}
          <Button variant="secondary" onClick={() => navigate(-1)}>Volver</Button>
        </div>
      </div>

      <div className="flex justify-between items-center">
        <h2 className="text-xl font-bold">Competencias</h2>
        <Button onClick={() => navigate(`/admin/torneos/${id}/competencia/nueva`)}>
          + Agregar Competencia
        </Button>
      </div>

      <div className="grid gap-4">
        {competitions.map((comp) => (
          <div key={comp.id} className="bg-white p-4 rounded shadow flex justify-between items-center">
            <div>
              <p className="font-bold">{comp.name}</p>
              <p className="text-sm text-gray-600">Cupo: {comp.quota} | Precio: ${comp.basePrice}</p>
            </div>
            <Button variant="secondary" onClick={() => navigate(`/admin/competencias/${comp.id}/inscriptos`)}>
              Ver Inscriptos
            </Button>
          </div>
        ))}
      </div>
    </div>
  );
};

export default AdminTournamentDetail;